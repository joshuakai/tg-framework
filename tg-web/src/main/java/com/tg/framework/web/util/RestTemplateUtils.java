package com.tg.framework.web.util;

import com.tg.framework.beans.http.SimpleResponseResult;
import com.tg.framework.beans.http.SimpleResponseResultBuilder;
import com.tg.framework.commons.util.OptionalUtils;
import java.net.URI;
import java.util.Map;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class RestTemplateUtils {

  private RestTemplateUtils() {
  }

  public static boolean isBodyRequest(HttpMethod method) {
    return method != null && (method == HttpMethod.POST || method == HttpMethod.PUT
        || method == HttpMethod.PATCH);
  }

  public static boolean isQueryParameterRequest(HttpMethod method) {
    return method != null && (method == HttpMethod.GET || method == HttpMethod.DELETE);
  }

  public static <T> ResponseEntity<T> request(RestTemplate restTemplate, String url, String method,
      boolean isUsingPayload, Map<String, ?> parameters, Object payload,
      Map<String, Object> uriVariables, Class<T> responseType) {
    HttpMethod httpMethod = OptionalUtils.notEmpty(method).map(HttpMethod::resolve)
        .orElseThrow(() -> new IllegalArgumentException("Unknown http method " + method));
    UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);
    HttpEntity<?> httpEntity = null;
    if (isQueryParameterRequest(httpMethod)) {
      if (parameters != null && !parameters.isEmpty()) {
        parameters.forEach(uriBuilder::queryParam);
      }
    } else if (isBodyRequest(httpMethod)) {
      if (isUsingPayload && payload != null) {
        httpEntity = new HttpEntity<>(payload);
      } else if (!isUsingPayload && parameters != null && !parameters.isEmpty()) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();
        parameters.forEach(formData::add);
        httpEntity = new HttpEntity<>(formData, headers);
      }
    } else {
      throw new IllegalArgumentException("Unsupported http method " + method);
    }
    URI uri = OptionalUtils.notEmpty(uriVariables).map(uriBuilder::build)
        .orElseGet(() -> uriBuilder.build().toUri());
    return restTemplate.exchange(uri, httpMethod, httpEntity, responseType);
  }

  public static ResponseEntity<String> request(RestTemplate restTemplate, String url, String method,
      boolean isUsingPayload, Map<String, ?> parameters, Object payload,
      Map<String, Object> uriVariables) {
    return request(restTemplate, url, method, isUsingPayload, parameters, payload, uriVariables,
        String.class);
  }

  public static SimpleResponseResult succeedOrFailedRequest(RestTemplate restTemplate, String url,
      String method, boolean isUsingPayload, Map<String, ?> parameters, Object payload,
      Map<String, Object> uriVariables) {
    ResponseEntity<String> responseEntity;
    try {
      responseEntity = request(restTemplate, url, method, isUsingPayload, parameters, payload,
          uriVariables);
    } catch (HttpStatusCodeException e) {
      return SimpleResponseResultBuilder
          .failed(String.format("%s %s", e.getStatusCode(), e.getMessage()));
    } catch (RestClientException e) {
      return SimpleResponseResultBuilder.failed(e.getMessage());
    }
    if (responseEntity.getStatusCode() == HttpStatus.OK) {
      return SimpleResponseResultBuilder.succeed(responseEntity.getBody());
    }
    return SimpleResponseResultBuilder.failed(responseEntity.getBody());
  }

}
