package com.Backend.jwtpart.config.oauth;

public interface OAuthUserInfo {
    String getProviderId();
    String getProvider();
    String getEmail();
    String getName();
}