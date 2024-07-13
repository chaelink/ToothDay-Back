package com.Backend.ToothDay.jwt.config.oauth;

import java.util.Map;

public class GoogleUser implements OAuthUserInfo{

    private Map<String, Object> attribute; //attribute를 받는다.

    public GoogleUser(Map<String, Object> attribute) {
        this.attribute = attribute;
    }

    @Override
    public String getProviderId() {
        return (String)attribute.get("id");
    }

    @Override
    public String getProvider() {
        return "google";
    }

    @Override
    public String getEmail() {
        return (String)attribute.get("email");
    }

    @Override
    public String getName() {
        return (String)attribute.get("name");
    }

    @Override
    public String getProfileImageUrl() {
        return (String)attribute.get("picture");
    }

}