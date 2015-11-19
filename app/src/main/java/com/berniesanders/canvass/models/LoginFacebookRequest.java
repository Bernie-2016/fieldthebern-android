package com.berniesanders.canvass.models;

/**
 * See <a href="https://github.com/Bernie-2016/ground-game-api/wiki/API-Tokens">https://github.com/Bernie-2016/ground-game-api/wiki/API-Tokens</a>
 */
public class LoginFacebookRequest {

    private final String username = "facebook";
    //<facebook_access_token>
    private final String password;

    public LoginFacebookRequest(String token) {
        this.password = token;
    }
}
