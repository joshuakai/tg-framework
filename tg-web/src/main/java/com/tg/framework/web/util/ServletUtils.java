package com.tg.framework.web.util;

import com.tg.framework.commons.http.RequestDetails;
import com.tg.framework.commons.lang.StringOptional;
import eu.bitwalker.useragentutils.DeviceType;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class ServletUtils {

  private static final String UNKNOWN = "unknown";
  private static final String HEADER_X_REAL_IP = "X-Real-IP";
  private static final String HEADER_X_FORWARDED_FOR = "x-forwarded-for";
  private static final String SEPARATOR_REVERSE_PROXY_IP = ",";
  private static final String HEADER_PROXY_CLIENT_IP = "Proxy-RequestDetails-IP";
  private static final String HEADER_WL_PROXY_CLIENT_IP = "WL-Proxy-RequestDetails-IP";
  private static final String HEADER_USER_AGENT = "user-agent";
  private static final String HEADER_X_REQUESTED_WITH = "x-requested-with";
  private static final String HEADER_ACCEPT = "Accept";

  private static final int HTTP_PORT = 80;
  private static final String FORMATTER_SERVER_PATH_WITH_HTTP_PORT = "%s://%s";
  private static final String FORMATTER_SERVER_PATH = "%s://%s:%s";
  private static final String XML_HTTP_REQUEST = "XMLHttpRequest";
  private static final String LOCALHOST_REMOTE_ADDRESS = "0:0:0:0:0:0:0:1";
  private static final String APPLICATION_JSON = "application/json";

  private ServletUtils() {
  }

  public static String getSessionId(HttpServletRequest request, boolean create) {
    return Optional.ofNullable(request).map(req -> req.getSession(create)).map(HttpSession::getId)
        .orElse(null);
  }

  public static String getRemoteAddr(HttpServletRequest request) {
    String remoteAddr = Optional.ofNullable(request).map(HttpServletRequest::getRemoteAddr)
        .filter(ip -> StringUtils.isNotBlank(ip) && !LOCALHOST_REMOTE_ADDRESS.equals(ip))
        .orElse(null);
    return StringOptional.ofNullable(remoteAddr).orElseGet(() -> {
      try {
        return StringOptional.ofNullable(InetAddress.getLocalHost().getHostAddress()).orElse(null);
      } catch (UnknownHostException e) {
        return remoteAddr;
      }
    });
  }

  public static String getXRealIp(HttpServletRequest request) {
    return Optional.ofNullable(request).map(req -> req.getHeader(HEADER_X_REAL_IP))
        .filter(StringUtils::isNotBlank).orElse(null);
  }

  public static String getXForwardedFor(HttpServletRequest request) {
    return Optional.ofNullable(request).map(req -> req.getHeader(HEADER_X_FORWARDED_FOR))
        .filter(StringUtils::isNotBlank).map(ip -> ip.indexOf(SEPARATOR_REVERSE_PROXY_IP) == -1 ? ip
            : ip.split(SEPARATOR_REVERSE_PROXY_IP)[0]).orElse(null);
  }

  public static String getProxyClientIp(HttpServletRequest request) {
    return Optional.ofNullable(request).map(req -> req.getHeader(HEADER_PROXY_CLIENT_IP))
        .filter(StringUtils::isNotBlank).orElse(null);
  }

  public static String getWlProxyClientIp(HttpServletRequest request) {
    return Optional.ofNullable(request).map(req -> req.getHeader(HEADER_WL_PROXY_CLIENT_IP))
        .filter(StringUtils::isNotBlank).orElse(null);
  }

  public static String getRemoteAddrPreferXRealIp(HttpServletRequest request) {
    Optional<String> optional = Optional.ofNullable(getXRealIp(request))
        .filter(ip -> !UNKNOWN.equalsIgnoreCase(ip));
    if (optional.isPresent()) {
      return optional.get();
    }
    optional = Optional.ofNullable(getXForwardedFor(request))
        .filter(ip -> !UNKNOWN.equalsIgnoreCase(ip));
    if (optional.isPresent()) {
      return optional.get();
    }
    return getRemoteAddr(request);
  }

  public static String getRemoteAddrPreferXForwardedFor(HttpServletRequest request) {
    Optional<String> optional = Optional.ofNullable(getXForwardedFor(request))
        .filter(ip -> !UNKNOWN.equalsIgnoreCase(ip));
    if (optional.isPresent()) {
      return optional.get();
    }
    optional = Optional.ofNullable(getXRealIp(request)).filter(ip -> !UNKNOWN.equalsIgnoreCase(ip));
    if (optional.isPresent()) {
      return optional.get();
    }
    return getRemoteAddr(request);
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

  public static boolean isAcceptApplicationJSON(HttpServletRequest request) {
    return Optional.ofNullable(request).map(req -> {
      Enumeration<String> accepts = req.getHeaders(HEADER_ACCEPT);
      while (accepts.hasMoreElements()) {
        if (StringUtils.equalsIgnoreCase(APPLICATION_JSON, accepts.nextElement())) {
          return true;
        }
      }
      return false;
    }).orElse(false);
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

  public static HttpSession getCurrentSession() {
    return Optional
        .ofNullable((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
        .map(ServletRequestAttributes::getRequest).map(HttpServletRequest::getSession).orElse(null);
  }

  public static RequestDetails getRequestDetails(HttpServletRequest request) {
    return Optional.ofNullable(request).map(
        req -> new RequestDetails(getSessionId(request, false), getUrl(req), getRemoteAddr(req),
            getRemoteAddrPreferXForwardedFor(req), getRemoteAddrPreferXRealIp(req),
            UserAgent.parseUserAgentString(req.getHeader(HttpHeaders.USER_AGENT)))).orElse(null);
  }

}
