package com.tg.framework.web.boot.http;

import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import javax.annotation.Resource;
import javax.net.ssl.SSLException;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.RequestUserAgent;
import org.apache.http.ssl.SSLContexts;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

@Configuration
@ConditionalOnClass(HttpClient.class)
@ConditionalOnProperty(prefix = "tg.web.http-client", value = "enabled")
@EnableConfigurationProperties(HttpClientProperties.class)
public class HttpClientAutoConfigure {

  @Resource
  private HttpClientProperties httpClientProperties;

  @Bean("defaultHttpClient")
  @ConditionalOnMissingBean(name = "defaultHttpClient")
  public HttpClient httpClient() {

    HttpRequestRetryHandler retryHandler = (exception, executionCount, context) -> {
      if (executionCount >= httpClientProperties.getMaxRetryCount()) {
        // Do not retry if over max retry count
        return false;
      }
      if (exception instanceof ConnectTimeoutException) {
        // Connection refused
        return false;
      }
      if (exception instanceof InterruptedIOException) {
        // Timeout
        return false;
      }
      if (exception instanceof UnknownHostException) {
        // Unknown host
        return false;
      }
      if (exception instanceof SSLException) {
        // SSL handshake exception
        return false;
      }
      HttpClientContext clientContext = HttpClientContext.adapt(context);
      HttpRequest request = clientContext.getRequest();
      boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
      if (idempotent) {
        // Retry if the request is considered idempotent
        return true;
      }
      return false;
    };

    return HttpClients.custom()
        .setSSLSocketFactory(sslSocketFactory())
        .setConnectionManager(connectionManager())
        .setDefaultRequestConfig(
            RequestConfig.custom()
                .setConnectionRequestTimeout(httpClientProperties.getConnectionRequestTimeout())
                .setConnectTimeout(httpClientProperties.getConnectTimeout())
                .setSocketTimeout(httpClientProperties.getSocketTimeout())
                .build()
        )
        .setRetryHandler(retryHandler)
//          .disableAutomaticRetries()
        .setRedirectStrategy(new ConfigurableRedirectStrategy(
            httpClientProperties.getDisableRedirectionHeaderName()))
        .setKeepAliveStrategy(new ConfigurableConnectionKeepAliveStrategy(
            httpClientProperties.getDefaultKeepAliveDuration()))
        .setDefaultCookieStore(new BasicCookieStore())
        .addInterceptorLast(new RequestUserAgent(httpClientProperties.getDefaultUserAgent()))
        .build();
  }


  public SSLConnectionSocketFactory sslSocketFactory() {
    try {
      return new SSLConnectionSocketFactory(
          SSLContexts.custom().loadTrustMaterial(null, (cert, authType) -> true).build(),
          NoopHostnameVerifier.INSTANCE);
    } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
      throw new RuntimeException(e);
    }
  }

  public PoolingHttpClientConnectionManager connectionManager() {
    PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
    connectionManager.setMaxTotal(httpClientProperties.getMaxTotal());
    connectionManager.setDefaultMaxPerRoute(httpClientProperties.getDefaultMaxPerRoute());
    connectionManager.setValidateAfterInactivity(httpClientProperties.getValidateAfterInactivity());
    return connectionManager;
  }

  static class ConfigurableRedirectStrategy extends LaxRedirectStrategy {

    private String disabledHeaderName;

    public ConfigurableRedirectStrategy(String disabledHeaderName) {
      Assert.isTrue(StringUtils.isNotBlank(disabledHeaderName),
          "disabledHeaderName must not be empty.");
      this.disabledHeaderName = disabledHeaderName;
    }

    @Override
    public boolean isRedirected(HttpRequest request, HttpResponse response, HttpContext context)
        throws ProtocolException {
      if (request.getFirstHeader(disabledHeaderName) != null) {
        return false;
      }
      return super.isRedirected(request, response, context);
    }
  }

  static class ConfigurableConnectionKeepAliveStrategy extends
      DefaultConnectionKeepAliveStrategy {

    private static final long NO_SUGGESTED_DURATION = -1L;

    private long defaultDuration = NO_SUGGESTED_DURATION;

    public ConfigurableConnectionKeepAliveStrategy() {
    }

    public ConfigurableConnectionKeepAliveStrategy(long defaultDuration) {
      this.defaultDuration = defaultDuration;
    }

    @Override
    public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
      long duration = super.getKeepAliveDuration(response, context);
      return duration == NO_SUGGESTED_DURATION ? defaultDuration : duration;
    }

    public long getDefaultDuration() {
      return defaultDuration;
    }

    public void setDefaultDuration(long defaultDuration) {
      this.defaultDuration = defaultDuration;
    }

  }

}
