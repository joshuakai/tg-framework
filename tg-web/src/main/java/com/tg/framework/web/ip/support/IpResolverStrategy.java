package com.tg.framework.web.ip.support;

public enum IpResolverStrategy {
  AUTO, REMOTE_ADDRESS, X_FORWARDED_FOR, X_REAL_IP, CUSTOM_HEADER
}
