package com.Backend.ToothDay.jwt.config.oauth;

public interface OAuthUserInfo {
    String getProviderId();
    String getProvider();
    String getEmail();
    String getName();
}