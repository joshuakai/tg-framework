package com.tg.framework.core.security;

import java.io.Serializable;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class IdentityUser<T extends Serializable> extends User {

  private static final long serialVersionUID = -2626548510227823144L;

  private T id;

  public IdentityUser(String username, String password,
      Collection<? extends GrantedAuthority> authorities,
      T id) {
    super(username, password, authorities);
    this.id = id;
  }

  public IdentityUser(String username, String password, boolean enabled, boolean accountNonExpired,
      boolean credentialsNonExpired, boolean accountNonLocked,
      Collection<? extends GrantedAuthority> authorities,
      T id) {
    super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked,
        authorities);
    this.id = id;
  }

  public T getId() {
    return id;
  }

  public void setId(T id) {
    this.id = id;
  }
}
