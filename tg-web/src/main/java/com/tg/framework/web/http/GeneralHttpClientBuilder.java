package com.tg.framework.web.http;

import com.tg.framework.web.util.HttpUtils;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import javax.net.ssl.SSLException;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.RequestUserAgent;
import org.apache.http.ssl.SSLContexts;
import org.springframework.util.Assert;

public class GeneralHttpClientBuilder {

  private GeneralHttpClientBuilder() {
  }

  public static HttpClientConnectionManagerProperties generalConnectionManagerProperties() {
    HttpClientConnectionManagerProperties properties = new HttpClientConnectionManagerProperties();
    properties.setMaxTotal(200);
    properties.setDefaultMaxPerRoute(50);
    properties.setValidateAfterInactivity(5 * 1000);
    return properties;
  }

  public static HttpClientProperties generalHttpClientProperties() {
    return generalHttpClientProperties(generalConnectionManagerProperties());
  }

  public static HttpClientProperties generalHttpClientProperties(HttpClientConnectionManagerProperties connectionManagerProperties) {
    HttpClientProperties properties = new HttpClientProperties();
    properties.setConnectionManagerProperties(connectionManagerProperties);
    properties.setConnectionRequestTimeout(1000);
    properties.setConnectTimeout(10 * 1000);
    properties.setSocketTimeout(60 * 1000);
    properties.setMaxRetryCount(3);
    properties.setDisableRedirectionHeaderName("x-disabled-redirect");
    properties.setDefaultUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.88 Safari/537.36");
    properties.setDefaultKeepAliveDuration(60 * 1000L);
    return properties;
  }

  public static CloseableHttpClient generalHttpClient() {
    return generalHttpClient(generalHttpClientProperties());
  }

  public static CloseableHttpClient generalHttpClient(HttpClientProperties httpClientProperties) {
    return generalHttpClient(httpClientProperties, new BasicCookieStore());
  }

  public static CloseableHttpClient generalHttpClient(CookieStore cookieStore) {
    return generalHttpClient(generalHttpClientProperties(), cookieStore);
  }

  public static CloseableHttpClient generalHttpClient(HttpClientProperties httpClientProperties, CookieStore cookieStore) {
    HttpRequestRetryHandler retryHandler = new DefaultHttpRequestRetryHandler(httpClientProperties.getMaxRetryCount());
    SSLConnectionSocketFactory sslSocketFactory = trustAllSslSocketFactory();
    PoolingHttpClientConnectionManager connectionManager = Optional.ofNullable(httpClientProperties.getConnectionManagerProperties())
        .map(properties -> generalConnectionManager(properties, sslSocketFactory))
        .orElse(null);
    return HttpClients.custom()
        .setSSLSocketFactory(sslSocketFactory)
        .setConnectionManager(connectionManager)
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
        .setDefaultCookieStore(cookieStore)
        .addInterceptorLast(new RequestUserAgent(httpClientProperties.getDefaultUserAgent()))
        .build();
  }

  public static SSLConnectionSocketFactory trustAllSslSocketFactory() {
    try {
      return new SSLConnectionSocketFactory(
          SSLContexts.custom().loadTrustMaterial(null, (cert, authType) -> true).build(),
          NoopHostnameVerifier.INSTANCE);
    } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
      throw new RuntimeException(e);
    }
  }

  public static PoolingHttpClientConnectionManager generalConnectionManager() {
    return generalConnectionManager(generalConnectionManagerProperties(), trustAllSslSocketFactory());
  }

  public static PoolingHttpClientConnectionManager generalConnectionManager(HttpClientConnectionManagerProperties connectionManagerProperties) {
    return generalConnectionManager(connectionManagerProperties, trustAllSslSocketFactory());
  }

  public static PoolingHttpClientConnectionManager generalConnectionManager(SSLConnectionSocketFactory sslSocketFactory) {
    return generalConnectionManager(generalConnectionManagerProperties(), sslSocketFactory);
  }

  public static PoolingHttpClientConnectionManager generalConnectionManager(HttpClientConnectionManagerProperties connectionManagerProperties, SSLConnectionSocketFactory sslSocketFactory) {
    Registry<ConnectionSocketFactory> sslSocketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
        .register(HttpUtils.HTTP_SCHEME, PlainConnectionSocketFactory.getSocketFactory())
        .register(HttpUtils.HTTPS_SCHEME, sslSocketFactory)
        .build();
    PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(sslSocketFactoryRegistry);
    connectionManager.setMaxTotal(connectionManagerProperties.getMaxTotal());
    connectionManager.setDefaultMaxPerRoute(connectionManagerProperties.getDefaultMaxPerRoute());
    connectionManager.setValidateAfterInactivity(connectionManagerProperties.getValidateAfterInactivity());
    return connectionManager;
  }

  static class ConfigurableRedirectStrategy extends LaxRedirectStrategy {

    private String disabledHeaderName;

    public ConfigurableRedirectStrategy(String disabledHeaderName) {
      Assert.hasText(disabledHeaderName, "Disabled header name must not be null or empty");
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

  static class DefaultHttpRequestRetryHandler implements HttpRequestRetryHandler {

    private int maxRetryCount;

    public DefaultHttpRequestRetryHandler(int maxRetryCount) {
      Assert.state(maxRetryCount >= 0, "Max retry count must be greater than 0");
      this.maxRetryCount = maxRetryCount;
    }

    @Override
    public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
      if (executionCount >= maxRetryCount) {
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
