package com.fullmugu.nanumeal.oauth.info;

import java.util.Map;

public interface OAuth2UserInfo {
    Map<String, Object> getAttributes();

    String getProviderId();

    String getProvider();

    String getEmail();

    String getName();
}
