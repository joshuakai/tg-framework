package com.tg.framework.web.util;

import com.tg.framework.beans.http.ProxyRequestBean;
import com.tg.framework.beans.http.ProxyResponseBean;
import com.tg.framework.commons.lang.MapOptional;
import com.tg.framework.commons.exception.InvalidParamException;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class RestTemplateUtils {

  private static final Set<String> SUPPORTED_HTTP_METHODS = Stream
      .of(HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT, HttpMethod.PATCH, HttpMethod.DELETE)
      .map(HttpMethod::name).collect(Collectors.toSet());

  private RestTemplateUtils() {
  }

  public static <T extends RestTemplate> T replaceMappingJackson2HttpMessageConverter(
      T restTemplate, MappingJackson2HttpMessageConverter messageConverter) {
    List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
    for (int i = 0; i < messageConverters.size(); i++) {
      Class clazz = messageConverters.get(i).getClass();
      if (MappingJackson2HttpMessageConverter.class == clazz) {
        messageConverters.set(i, messageConverter);
      }
    }
    return restTemplate;
  }

  public static ClientHttpRequestFactory buildDefaultClientHttpRequestFactory() {
    HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
    httpRequestFactory.setConnectionRequestTimeout(2000);
    httpRequestFactory.setConnectTimeout(3000);
    httpRequestFactory.setReadTimeout(30000);
    return httpRequestFactory;
  }

  public static <T extends RestTemplate> T buildDefaultRestTemplate(T restTemplate,
      MappingJackson2HttpMessageConverter messageConverter) {
    restTemplate.setRequestFactory(buildDefaultClientHttpRequestFactory());
    return replaceMappingJackson2HttpMessageConverter(restTemplate, messageConverter);
  }

  public static RestTemplate buildDefaultRestTemplate(
      MappingJackson2HttpMessageConverter messageConverter) {
    return buildDefaultRestTemplate(new RestTemplate(), messageConverter);
  }

  public static <T> T proxyGet(RestTemplate restTemplate, String url,
      Map<String, String[]> parameters, Class<T> responseType) {
    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
    MapOptional.ofNullable(parameters).ifPresent(map -> map.forEach(builder::queryParam));
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
      return ProxyResponseBean.fail(e.getMessage());
    }
  }

  private static HttpMethod determineHttpMethod(String method) {
    if (StringUtils.isBlank(method) || !SUPPORTED_HTTP_METHODS.contains(method.toUpperCase())) {
      throw new InvalidParamException("method", method);
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
