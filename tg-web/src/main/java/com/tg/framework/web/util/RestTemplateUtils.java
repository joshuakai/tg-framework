package com.tg.framework.web.util;

import com.tg.framework.beans.http.SimpleRequestEntity;
import com.tg.framework.beans.http.SimpleResponseEntity;
import com.tg.framework.beans.http.SimpleResponseEntityBuilder;
import com.tg.framework.beans.http.SimpleResponseResult;
import com.tg.framework.beans.http.SimpleResponseResultBuilder;
import com.tg.framework.commons.util.OptionalUtils;
import java.util.Optional;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public class RestTemplateUtils {

  private RestTemplateUtils() {
  }

  private static ResponseEntity<String> innerRequest(RestTemplate restTemplate,
      SimpleRequestEntity request) {
    String url = request.getUrl();

    HttpMethod method = OptionalUtils.notEmpty(request.getMethod()).map(HttpMethod::resolve)
        .orElseThrow(() -> new IllegalArgumentException(request.getMethod()));

    HttpHeaders headers = new HttpHeaders();
    Optional.ofNullable(request.getHeaders()).ifPresent(map -> map.forEach(headers::addAll));
    OptionalUtils.notEmpty(request.getContentType()).map(MediaType::parseMediaType)
        .ifPresent(headers::setContentType);

    HttpEntity<String> entity = new HttpEntity<>(request.getBody(), headers);
    return restTemplate.exchange(url, method, entity, String.class);
  }

  public static SimpleResponseEntity request(RestTemplate restTemplate,
      SimpleRequestEntity request) {
    ResponseEntity<String> response = innerRequest(restTemplate, request);

    SimpleResponseEntityBuilder builder = SimpleResponseEntityBuilder.builder()
        .statusCode(response.getStatusCode().value());
    response.getHeaders().forEach(builder::addHeader);
    return builder.body(response.getBody()).build();
  }

  public static SimpleResponseResult requestForResult(RestTemplate restTemplate,
      SimpleRequestEntity request) {
    ResponseEntity<String> response;
    try {
      response = innerRequest(restTemplate, request);
    } catch (RestClientException e) {
      return SimpleResponseResultBuilder.failed(e.getMessage());
    }
    if (response.getStatusCode() == HttpStatus.OK) {
      return SimpleResponseResultBuilder.succeed(response.getBody());
    }
    return SimpleResponseResultBuilder.failed(response.getBody());
  }

}
