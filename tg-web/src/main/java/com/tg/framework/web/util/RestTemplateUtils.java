package com.tg.framework.web.util;

import com.tg.framework.beans.http.ProxyRequestBean;
import com.tg.framework.beans.http.ProxyResponseBean;
import com.tg.framework.commons.exception.ParamInvalidException;
import com.tg.framework.commons.util.OptionalUtils;
import java.net.URI;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class RestTemplateUtils {

  private static final Logger LOGGER = LoggerFactory.getLogger(RestTemplateUtils.class);

  private static final Set<String> SUPPORTED_HTTP_METHODS = Stream
      .of(HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT, HttpMethod.PATCH, HttpMethod.DELETE)
      .map(HttpMethod::name).collect(Collectors.toSet());

  private RestTemplateUtils() {
  }

  public static <T> T proxyGet(RestTemplate restTemplate, String url,
      Map<String, String[]> parameters, Class<T> responseType) {
    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
    OptionalUtils.notEmpty(parameters).ifPresent(map -> map.forEach(builder::queryParam));
    return restTemplate.getForObject(builder.build().toUri(), responseType);
  }

  public static <T> T proxyPost(RestTemplate restTemplate, String url, Object requestBody,
      Class<T> responseType) {
    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
    return restTemplate.postForObject(builder.build().toUri(), requestBody, responseType);
  }

  public static void proxyPut(RestTemplate restTemplate, String url, Object requestBody) {
    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
    restTemplate.put(builder.build().toUri(), requestBody);
  }

  public static <T> T proxyPatch(RestTemplate restTemplate, String url, Object requestBody,
      Class<T> responseType) {
    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
    return restTemplate.patchForObject(builder.build().toUri(), requestBody, responseType);
  }

  public static void proxyDelete(RestTemplate restTemplate, String url,
      Map<String, String[]> parameters) {
    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
    OptionalUtils.notEmpty(parameters).ifPresent(map -> map.forEach(builder::queryParam));
    restTemplate.delete(builder.build().toUri());
  }

  public static ProxyResponseBean proxyRequest(RestTemplate restTemplate,
      ProxyRequestBean proxyRequest) {
    String url = proxyRequest.getUrl();
    HttpMethod method = determineHttpMethod(proxyRequest.getMethod());
    boolean isUsingPayload = proxyRequest.isUsingPayload();
    Map<String, ?> parameters = proxyRequest.getParameters();
    Object payload = proxyRequest.getPayload();
    Map<String, Object> uriVariables = proxyRequest.getUriVariables();
    return wrapProxyRequest(restTemplate, url, method, isUsingPayload, parameters, payload,
        uriVariables);
  }

  private static ProxyResponseBean wrapProxyRequest(RestTemplate restTemplate, String url,
      HttpMethod method, boolean isUsingPayload, Map<String, ?> parameters, Object payload,
      Map<String, Object> uriVariables) {
    boolean isGetOrDelete = method == HttpMethod.GET || method == HttpMethod.DELETE;
    URI uri =
        isGetOrDelete ? buildURI(url, parameters, uriVariables) : buildURI(url, null, uriVariables);
    HttpEntity httpEntity =
        !isGetOrDelete ? getHttpEntity(isUsingPayload, parameters, payload) : null;
    try {
      return ProxyResponseBean
          .success(restTemplate.exchange(uri, method, httpEntity, String.class).getBody());
    } catch (Exception e) {
      if (LOGGER.isDebugEnabled()) {
        LOGGER.error("Failed to execute proxy request.", e);
      }
      return ProxyResponseBean.fail(e.getMessage());
    }
  }

  private static HttpMethod determineHttpMethod(String method) {
    if (StringUtils.isBlank(method) || !SUPPORTED_HTTP_METHODS.contains(method.toUpperCase())) {
      throw new ParamInvalidException("method", method);
    }
    return HttpMethod.valueOf(method.toUpperCase());
  }

  private static URI buildURI(String url, Map<String, ?> parameters,
      Map<String, Object> uriVariables) {
    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
    if (MapUtils.isNotEmpty(parameters)) {
      parameters.forEach((key, value) -> {
        if (value != null && !Objects.equals(StringUtils.EMPTY, value)) {
          builder.queryParam(key, value);
        }
      });
    }
    if (uriVariables == null) {
      return builder.build().toUri();
    }
    return builder.build(uriVariables);
  }

  private static HttpEntity getHttpEntity(boolean isUsingPayload, Map<String, ?> parameters,
      Object payload) {
    if (isUsingPayload) {
      return new HttpEntity<>(payload);
    } else {
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
      MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();
      parameters.forEach(formData::add);
      return new HttpEntity<>(formData, headers);
    }
  }
}
