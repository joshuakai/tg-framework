package com.tg.framework.cloud.boot.oauth2;

import java.io.Serializable;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;

public class IdentityClient<T extends Serializable> extends BaseClientDetails {

  private static final long serialVersionUID = -4671826147517497070L;

  private T id;

  public IdentityClient(T id) {
    this.id = id;
  }

  public IdentityClient(ClientDetails prototype, T id) {
    super(prototype);
    this.id = id;
  }

  public IdentityClient(String clientId, String resourceIds, String scopes,
      String grantTypes, String authorities, T id) {
    super(clientId, resourceIds, scopes, grantTypes, authorities);
    this.id = id;
  }

  public IdentityClient(String clientId, String resourceIds, String scopes,
      String grantTypes, String authorities, String redirectUris, T id) {
    super(clientId, resourceIds, scopes, grantTypes, authorities, redirectUris);
    this.id = id;
  }

  public T getId() {
    return id;
  }

  public void setId(T id) {
    this.id = id;
  }
}
