package com.tg.framework.web.util;

import com.tg.framework.commons.util.OptionalUtils;
import eu.bitwalker.useragentutils.DeviceType;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import java.lang.reflect.Array;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class HttpUtils {

  public static final int HTTP_PORT = 80;
  public static final int HTTPS_PORT = 443;
  public static final String HTTP_SCHEME = "http";
  public static final String HTTPS_SCHEME = "https";
  public static final String HTTP_URL_PREFIX = HTTP_SCHEME + "://";
  public static final String HTTPS_URL_PREFIX = HTTPS_SCHEME + "://";

  public static final String LOCALHOST_IPV4 = "127.0.0.1";
  public static final String LOCALHOST_IPV6 = "0:0:0:0:0:0:0:1";

  public static final String CONTENT_TYPE_HEADER = "Content-Type";
  public static final String USER_AGENT_HEADER = "user-agent";
  public static final String X_REQUESTED_WITH_HEADER = "x-requested-with";
  public static final String XML_HTTP_REQUEST = "XMLHttpRequest";

  public static final String X_FORWARDED_FOR_HEADER = "X-Forwarded-For";
  public static final String X_FORWARDED_HOST_HEADER = "X-Forwarded-Host";
  public static final String X_FORWARDED_PORT_HEADER = "X-Forwarded-Port";
  public static final String X_FORWARDED_PROTO_HEADER = "X-Forwarded-Proto";
  public static final String X_FORWARDED_PREFIX_HEADER = "X-Forwarded-Prefix";
  public static final String X_FORWARDED_SEPARATOR = ",";
  public static final String URL_SEPARATOR = "/";
  public static final String URL_HASH_SEPARATOR = "#";
  public static final String QUERY_STRING_SEPARATOR = "?";

  public static final String X_REAL_IP_HEADER = "X-Real-IP";
  public static final String PROXY_CLIENT_IP_HEADER = "Proxy-RequestClient-IP";
  public static final String WL_PROXY_CLIENT_IP_HEADER = "WL-Proxy-RequestClient-IP";

  private static final String FORMATTER_HTTP_HOST_PORT = "%s:%d";
  private static final String FORMATTER_HTTP_PROTOCOL_HOST = "%s://%s";

  private static final String UNKNOWN = "unknown";

  private static final String WILDCARD = "*";
  private static final String PATTERN_WILDCARD = "\\*";
  private static final String WILDCARD_REPLACER = "[0-9\\.]*";
  private static final String IP_PATTERN_TEMPLATE = "^%s$";


  private static final String DNS_8888 = "8.8.8.8";
  private static final int DNS_8888_PORT = 10002;

  private HttpUtils() {
  }

  public static boolean isLocalhost(String ip) {
    return LOCALHOST_IPV4.equals(ip) || LOCALHOST_IPV6.equals(ip);
  }

  public static String getLocalHostAddressPreferOutbound() {
    try (final DatagramSocket socket = new DatagramSocket()) {
      socket.connect(InetAddress.getByName(DNS_8888), DNS_8888_PORT);
      return socket.getLocalAddress().getHostAddress();
    } catch (Exception e) {
      return getLocalHostAddress();
    }
  }

  public static String getLocalHostAddress() {
    try {
      return InetAddress.getLocalHost().getHostAddress();
    } catch (UnknownHostException e) {
      return LOCALHOST_IPV4;
    }
  }

  public static String convertLocalhost(String ip, boolean preferOutbound) {
    return OptionalUtils.notEmpty(ip)
        .filter(HttpUtils::isLocalhost)
        .map(s -> preferOutbound ? getLocalHostAddressPreferOutbound() : getLocalHostAddress())
        .orElse(ip);
  }

  public static String convertLocalhost2Ipv4(String ip) {
    return OptionalUtils.notEmpty(ip)
        .filter(HttpUtils::isLocalhost)
        .map(s -> LOCALHOST_IPV4)
        .orElse(ip);
  }

  public static String convertLocalhost(String ip) {
    return convertLocalhost(ip, true);
  }

  public static String getRemoteAddr(ServletRequest request) {
    return Optional.ofNullable(request).map(ServletRequest::getRemoteAddr)
        .map(HttpUtils::convertLocalhost)
        .orElse(null);
  }

  public static String getMethod(HttpServletRequest request) {
    return Optional.ofNullable(request).map(HttpServletRequest::getMethod).map(String::toUpperCase)
        .orElse(StringUtils.EMPTY);
  }

  public static String getHeader(HttpServletRequest request, String name) {
    return Optional.ofNullable(request)
        .flatMap(req -> OptionalUtils.notEmpty(name).map(req::getHeader))
        .map(StringUtils::trim)
        .filter(StringUtils::isNotBlank)
        .orElse(null);
  }


  public static String getContentType(HttpServletRequest request) {
    return getHeader(request, CONTENT_TYPE_HEADER);
  }

  public static Map<String, String> getHeaderMap(HttpServletRequest request) {
    return Optional.ofNullable(request).map(req -> {
      Enumeration<String> headerNames = req.getHeaderNames();
      Map<String, String> map = new HashMap<>();
      while (headerNames.hasMoreElements()) {
        String headerName = headerNames.nextElement();
        map.put(headerName, req.getHeader(headerName));
      }
      return map;
    }).orElse(new HashMap<>());
  }

  public static String getXRealIp(HttpServletRequest request) {
    return convertLocalhost(getHeader(request, X_REAL_IP_HEADER));
  }

  public static String getXForwardedFor(HttpServletRequest request) {
    return getHeader(request, X_FORWARDED_FOR_HEADER);
  }

  public static String getXForwardedOriginal(String value) {
    return OptionalUtils.notEmpty(value)
        .map(v -> !v.contains(X_FORWARDED_SEPARATOR) ? v : v.split(X_FORWARDED_SEPARATOR)[0])
        .map(String::trim)
        .filter(StringUtils::isNotEmpty)
        .orElse(null);
  }

  public static String getXForwardedForAsRemoteAddr(HttpServletRequest request) {
    return OptionalUtils.notEmpty(getXForwardedFor(request))
        .map(HttpUtils::getXForwardedOriginal)
        .map(HttpUtils::convertLocalhost)
        .orElse(null);
  }

  public static String getProxyClientIp(HttpServletRequest request) {
    return convertLocalhost(getHeader(request, PROXY_CLIENT_IP_HEADER));
  }

  public static String getWlProxyClientIp(HttpServletRequest request) {
    return convertLocalhost(getHeader(request, WL_PROXY_CLIENT_IP_HEADER));
  }

  public static String getRemoteAddrPreferXRealIp(HttpServletRequest request) {
    return OptionalUtils.notEmpty(getXRealIp(request))
        .filter(ip -> !UNKNOWN.equalsIgnoreCase(ip))
        .orElseGet(() -> getRemoteAddr(request));
  }

  public static String getRemoteAddrPreferXForwardedFor(HttpServletRequest request) {
    return OptionalUtils.notEmpty(getXForwardedForAsRemoteAddr(request))
        .filter(ip -> !UNKNOWN.equalsIgnoreCase(ip))
        .orElseGet(() -> getRemoteAddr(request));
  }

  public static String getProtocol(HttpServletRequest request) {
    return Optional.ofNullable(request)
        .map(HttpServletRequest::getScheme)
        .orElse(StringUtils.EMPTY);
  }

  public static String getXForwardedProto(HttpServletRequest request) {
    return getHeader(request, X_FORWARDED_PROTO_HEADER);
  }

  public static String getXForwardedProtoAsProtocol(HttpServletRequest request) {
    return OptionalUtils.notEmpty(getXForwardedProto(request))
        .map(HttpUtils::getXForwardedOriginal)
        .orElse(null);
  }

  public static String getProtocolPreferXForwardedProto(HttpServletRequest request) {
    return OptionalUtils.notEmpty(getXForwardedProtoAsProtocol(request))
        .orElseGet(() -> getProtocol(request));
  }

  public static String getHost(HttpServletRequest request) {
    return Optional.ofNullable(request)
        .map(req -> isDefaultPort(req.getScheme(), req.getServerPort()) ? req.getServerName()
            : String.format(FORMATTER_HTTP_HOST_PORT, req.getServerName(), req.getServerPort()))
        .orElse(StringUtils.EMPTY);
  }

  public static String getXForwardedHost(HttpServletRequest request) {
    return getHeader(request, X_FORWARDED_HOST_HEADER);
  }

  public static String getXForwardedHostAsHost(HttpServletRequest request) {
    return OptionalUtils.notEmpty(getXForwardedHost(request))
        .map(HttpUtils::getXForwardedOriginal)
        .orElse(null);
  }

  public static String getHostPreferXForwardedHost(HttpServletRequest request) {
    return OptionalUtils.notEmpty(getXForwardedHostAsHost(request))
        .orElseGet(() -> getHost(request));
  }

  public static int getPort(HttpServletRequest request) {
    return Optional.ofNullable(request)
        .map(HttpServletRequest::getServerPort)
        .filter(p -> p >= 0)
        .orElseGet(() -> getDefaultPort(getProtocol(request)));
  }

  public static int getDefaultPort(String protocol) {
    return HTTPS_SCHEME.equals(protocol) ? HTTPS_PORT : HTTP_PORT;
  }

  public static boolean isDefaultPort(String protocol, int port) {
    return port < 0 || (port == HTTP_PORT && HTTP_SCHEME.equals(protocol)) || (port == HTTPS_PORT
        && HTTPS_SCHEME.equals(protocol));
  }

  public static String getXForwardedPort(HttpServletRequest request) {
    return getHeader(request, X_FORWARDED_PORT_HEADER);
  }

  public static Integer getXForwardedPortAsPort(HttpServletRequest request) {
    return OptionalUtils.notEmpty(getXForwardedPort(request))
        .map(HttpUtils::getXForwardedOriginal)
        .map(Integer::valueOf)
        .orElse(null);
  }

  public static int getPortPreferXForwardedPort(HttpServletRequest request) {
    return Optional.ofNullable(getXForwardedPortAsPort(request))
        .filter(p -> p >= 0)
        .orElseGet(() -> getPort(request));
  }

  public static String getRequestURI(HttpServletRequest request) {
    return Optional.ofNullable(request)
        .map(HttpServletRequest::getRequestURI)
        .orElse(StringUtils.EMPTY);
  }

  public static String getXForwardedPrefix(HttpServletRequest request) {
    return getHeader(request, X_FORWARDED_PREFIX_HEADER);
  }

  public static String getXForwardedPrefixAsPrefix(HttpServletRequest request) {
    return OptionalUtils.notEmpty(getXForwardedPrefix(request))
        .map(HttpUtils::getXForwardedOriginal)
        .orElse(null);
  }

  public static String getRequestURIPreferXForwardedPrefix(HttpServletRequest request) {
    return OptionalUtils.notEmpty(getXForwardedPrefixAsPrefix(request))
        .map(p -> p + getRequestURI(request))
        .orElseGet(() -> getRequestURI(request));
  }

  public static String getUrl(HttpServletRequest request) {
    return Optional.ofNullable(request).map(req -> {
      String protocol = req.getScheme();
      int port = req.getServerPort();
      StringBuilder sb = new StringBuilder();
      if (isDefaultPort(protocol, port)) {
        sb.append(String.format(FORMATTER_HTTP_PROTOCOL_HOST, protocol, req.getServerName()))
            .append(req.getRequestURI());
        return String.format(FORMATTER_HTTP_PROTOCOL_HOST, protocol, req.getServerName()) + req
            .getRequestURI();
      } else {
        sb.append(req.getRequestURL());
      }
      OptionalUtils.notEmpty(req.getQueryString())
          .ifPresent(qs -> sb.append(QUERY_STRING_SEPARATOR).append(qs));
      return sb.toString();
    }).orElse(StringUtils.EMPTY);
  }

  public static String getUrlPreferXForwarded(HttpServletRequest request) {
    return Optional.ofNullable(request).map(req -> {
      String protocol = getProtocolPreferXForwardedProto(req);
      String host = getHostPreferXForwardedHost(req);
      String uri = getRequestURIPreferXForwardedPrefix(req);
      StringBuilder sb = new StringBuilder(
          String.format(FORMATTER_HTTP_PROTOCOL_HOST, protocol, host) + uri);
      OptionalUtils.notEmpty(req.getQueryString())
          .ifPresent(qs -> sb.append(QUERY_STRING_SEPARATOR).append(qs));
      return sb.toString();
    }).orElse(StringUtils.EMPTY);
  }

  public static String getHostFromUrl(String url) {
    return OptionalUtils.notEmpty(url)
        .map(u -> StringUtils.substringBefore(u.replace(HTTP_URL_PREFIX, StringUtils.EMPTY)
            .replace(HTTPS_URL_PREFIX, StringUtils.EMPTY), URL_SEPARATOR))
        .map(u -> StringUtils.substringBefore(u, URL_HASH_SEPARATOR))
        .filter(StringUtils::isNotBlank)
        .orElse(StringUtils.EMPTY);
  }

  public static String getUserAgentString(HttpServletRequest request) {
    return Optional.ofNullable(request).map(req -> req.getHeader(USER_AGENT_HEADER))
        .orElse(StringUtils.EMPTY);
  }

  public static UserAgent getUserAgent(HttpServletRequest request) {
    return Optional.ofNullable(request).map(req -> req.getHeader(USER_AGENT_HEADER))
        .filter(StringUtils::isNoneBlank).map(UserAgent::parseUserAgentString).orElse(null);
  }

  public static boolean isXMLHttpRequest(HttpServletRequest request) {
    return Optional.ofNullable(request).filter(req -> StringUtils
        .equalsIgnoreCase(XML_HTTP_REQUEST, req.getHeader(X_REQUESTED_WITH_HEADER))).isPresent();
  }

  public static boolean isMobileOrTablet(HttpServletRequest request) {
    return Optional.ofNullable(getUserAgent(request)).map(UserAgent::getOperatingSystem)
        .map(OperatingSystem::getDeviceType)
        .filter(dt -> dt == DeviceType.MOBILE || dt == DeviceType.TABLET).isPresent();
  }

  public static HttpServletRequest getCurrentRequest() {
    return Optional
        .ofNullable((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
        .map(ServletRequestAttributes::getRequest).orElse(null);
  }

  public static boolean matchesIp(String ip, String pattern, boolean acceptWildcard) {
    if (StringUtils.isBlank(ip) || StringUtils.isBlank(pattern)) {
      return false;
    }
    if (acceptWildcard && pattern.contains(WILDCARD)) {
      return ip.matches(String
          .format(IP_PATTERN_TEMPLATE, pattern.replaceAll(PATTERN_WILDCARD, WILDCARD_REPLACER)));
    }
    return StringUtils.equals(ip, pattern);
  }

  public static boolean matchesIp(String ip, String pattern) {
    return matchesIp(ip, pattern, true);
  }

  public static boolean isIpInWhitelist(String ip, Set<String> ipWhitelist,
      boolean acceptWildcard) {
    return CollectionUtils.isEmpty(ipWhitelist) || (acceptWildcard ? ipWhitelist.stream()
        .map(StringUtils::trim).filter(StringUtils::isNotEmpty)
        .anyMatch(p -> matchesIp(ip, p, acceptWildcard)) : ipWhitelist.contains(ip));
  }

  public static boolean isIpInWhitelist(String ip, Set<String> ipWhitelist) {
    return isIpInWhitelist(ip, ipWhitelist, true);
  }

  public static boolean isIpNotInBlacklist(String ip, Set<String> ipBlacklist,
      boolean acceptWildcard) {
    return CollectionUtils.isEmpty(ipBlacklist) || (acceptWildcard ? ipBlacklist.stream()
        .map(StringUtils::trim).filter(StringUtils::isNotEmpty)
        .noneMatch(p -> matchesIp(ip, p, acceptWildcard)) : !ipBlacklist.contains(ip));
  }

  public static boolean isIpNotInBlacklist(String ip, Set<String> ipBlacklist) {
    return isIpNotInBlacklist(ip, ipBlacklist, true);
  }

  public static boolean isIpAcceptable(String ip, Set<String> ipWhitelist, Set<String> ipBlacklist,
      boolean acceptWildcardWithWhitelist, boolean acceptWildcardWithBlacklist) {
    return isIpNotInBlacklist(ip, ipBlacklist, acceptWildcardWithBlacklist) && isIpInWhitelist(ip,
        ipWhitelist, acceptWildcardWithWhitelist);
  }

  public static boolean isIpAcceptable(String ip, Set<String> ipWhitelist,
      Set<String> ipBlacklist) {
    return isIpAcceptable(ip, ipWhitelist, ipBlacklist, true, true);
  }

  public static String toURLEncodedString(Map<String, ?> parameters, String charset) {
    List<NameValuePair> nameValuePairs = new ArrayList<>();
    OptionalUtils.notEmpty(parameters)
        .ifPresent(params -> params.forEach((k, v) -> {
          if (v == null) {
            nameValuePairs.add(new BasicNameValuePair(k, null));
          } else if (v instanceof Collection) {
            //noinspection unchecked
            ((Collection) v).forEach(sb -> nameValuePairs.add(new BasicNameValuePair(k,
                Optional.ofNullable(sb).map(Object::toString).orElse(null))));
          } else if (v.getClass().isArray()) {
            int length = Array.getLength(v);
            for (int i = 0; i < length; i++) {
              nameValuePairs.add(new BasicNameValuePair(k,
                  Optional.ofNullable(Array.get(v, i)).map(Object::toString).orElse(null)));
            }
          } else {
            nameValuePairs.add(new BasicNameValuePair(k, v.toString()));
          }
        }));
    return URLEncodedUtils.format(nameValuePairs, charset);
  }

  public static String toURLEncodedString(Map<String, ?> parameters) {
    return toURLEncodedString(parameters, StandardCharsets.UTF_8.name());
  }

}
