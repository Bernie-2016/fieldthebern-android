package com.berniesanders.canvass.models;

/**
 * See <a href="https://github.com/Bernie-2016/ground-game-api/wiki/API-Tokens">https://github.com/Bernie-2016/ground-game-api/wiki/API-Tokens</a>
 */
public class LoginFacebookRequest {

    private final String username = "facebook";
    //<facebook_access_token>
    private String password;

    public LoginFacebookRequest() {
    }

//    public String getGrantType() {
//        return grant_type;
//    }

    /**
     * email
     */
    public String username() {
        return username;
    }

    public String password() {
        return password;
    }


    public LoginFacebookRequest password(String token) {
        this.password = token;
        return this;
    }
}
