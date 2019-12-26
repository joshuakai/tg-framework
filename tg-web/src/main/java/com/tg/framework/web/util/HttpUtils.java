package com.tg.framework.web.util;

import com.tg.framework.commons.lang.StringOptional;
import eu.bitwalker.useragentutils.DeviceType;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class HttpUtils {

  public static final String HEADER_X_REAL_IP = "X-Real-IP";
  public static final String HEADER_X_FORWARDED_FOR = "x-forwarded-for";
  public static final String SEPARATOR_REVERSE_PROXY_IP = ",";
  public static final String HEADER_PROXY_CLIENT_IP = "Proxy-RequestClient-IP";
  public static final String HEADER_WL_PROXY_CLIENT_IP = "WL-Proxy-RequestClient-IP";
  public static final String HEADER_USER_AGENT = "user-agent";
  public static final String HEADER_X_REQUESTED_WITH = "x-requested-with";

  private static final String FORMATTER_SERVER_PATH_WITH_HTTP_PORT = "%s://%s";
  private static final String FORMATTER_SERVER_PATH = "%s://%s:%s";
  private static final String XML_HTTP_REQUEST = "XMLHttpRequest";
  private static final String LOCALHOST_REMOTE_ADDRESS = "0:0:0:0:0:0:0:1";

  private static final int HTTP_PORT = 80;
  private static final String UNKNOWN = "unknown";

  private static final String WILDCARD = "*";
  private static final String PATTERN_WILDCARD = "\\*";
  private static final String WILDCARD_REPLACER = "[0-9\\.]*";
  private static final String IP_PATTERN_TEMPLATE = "^%s$";

  private HttpUtils() {
  }

  public static String getRemoteAddr(ServletRequest request) {
    return Optional.ofNullable(request).map(ServletRequest::getRemoteAddr)
        .filter(ip -> StringUtils.isNotBlank(ip) && !LOCALHOST_REMOTE_ADDRESS.equals(ip))
        .orElse(null);
  }

  public static String getXRealIp(HttpServletRequest request) {
    return getHeader(request, HEADER_X_REAL_IP);
  }

  public static String getRawXForwardedFor(HttpServletRequest request) {
    return getHeader(request, HEADER_X_FORWARDED_FOR);
  }

  public static String getXForwardedForFromRaw(String rawXForwardedFor) {
    return StringOptional.ofNullable(rawXForwardedFor).map(
        ip -> ip.indexOf(SEPARATOR_REVERSE_PROXY_IP) == -1 ? ip
            : ip.split(SEPARATOR_REVERSE_PROXY_IP)[0]).orElse(null);
  }

  public static String getXForwardedFor(HttpServletRequest request) {
    return getXForwardedForFromRaw(getRawXForwardedFor(request));
  }

  public static String getProxyClientIp(HttpServletRequest request) {
    return getHeader(request, HEADER_PROXY_CLIENT_IP);
  }

  public static String getWlProxyClientIp(HttpServletRequest request) {
    return getHeader(request, HEADER_WL_PROXY_CLIENT_IP);
  }

  public static String getRemoteAddrPreferXRealIp(HttpServletRequest request) {
    return StringOptional.ofNullable(getXRealIp(request))
        .filter(ip -> !UNKNOWN.equalsIgnoreCase(ip) && !LOCALHOST_REMOTE_ADDRESS.equals(ip))
        .orElseGet(() -> getRemoteAddr(request));
  }

  public static String getRemoteAddrPreferXForwardedFor(HttpServletRequest request) {
    return StringOptional.ofNullable(getXForwardedFor(request))
        .filter(ip -> !UNKNOWN.equalsIgnoreCase(ip) && !LOCALHOST_REMOTE_ADDRESS.equals(ip))
        .orElseGet(() -> getRemoteAddr(request));
  }

  public static String getUserAgentString(HttpServletRequest request) {
    return Optional.ofNullable(request).map(req -> req.getHeader(HEADER_USER_AGENT))
        .orElse(StringUtils.EMPTY);
  }

  public static UserAgent getUserAgent(HttpServletRequest request) {
    return Optional.ofNullable(request).map(req -> req.getHeader(HEADER_USER_AGENT))
        .filter(StringUtils::isNoneBlank).map(UserAgent::parseUserAgentString).orElse(null);
  }

  public static String getMethod(HttpServletRequest request) {
    return Optional.ofNullable(request).map(HttpServletRequest::getMethod).map(String::toUpperCase)
        .orElse(StringUtils.EMPTY);
  }

  public static String getServerPath(HttpServletRequest request) {
    return Optional.ofNullable(request).map(req -> {
      int serverPort = req.getServerPort();
      return serverPort == HTTP_PORT ?
          String.format(FORMATTER_SERVER_PATH_WITH_HTTP_PORT, req.getScheme(), req.getServerName())
          : String.format(FORMATTER_SERVER_PATH, req.getScheme(), req.getServerName(), serverPort);
    }).orElse(StringUtils.EMPTY);
  }

  public static String getRequestURI(HttpServletRequest request) {
    return Optional.ofNullable(request).map(HttpServletRequest::getRequestURI)
        .orElse(StringUtils.EMPTY);
  }

  public static String getContextPath(HttpServletRequest request) {
    return Optional.ofNullable(request).map(HttpServletRequest::getContextPath)
        .orElse(StringUtils.EMPTY);
  }

  public static String getServletPath(HttpServletRequest request) {
    return Optional.ofNullable(request).map(HttpServletRequest::getServletPath)
        .orElse(StringUtils.EMPTY);
  }

  public static String getUrl(HttpServletRequest request) {
    return Optional.ofNullable(request).map(req -> getServerPath(req) + getRequestURI(req))
        .orElse(StringUtils.EMPTY);
  }

  public static String getHeader(HttpServletRequest request, String name) {
    return Optional.ofNullable(request).map(req -> req.getHeader(name)).orElse(null);
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

  public static boolean isXMLHttpRequest(HttpServletRequest request) {
    return Optional.ofNullable(request).filter(req -> StringUtils
        .equalsIgnoreCase(XML_HTTP_REQUEST, req.getHeader(HEADER_X_REQUESTED_WITH))).isPresent();
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

}
