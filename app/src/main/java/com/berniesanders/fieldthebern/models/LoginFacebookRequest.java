package com.berniesanders.fieldthebern.models;

/**
 * See <a href="https://github.com/Bernie-2016/ground-game-api/wiki/API-Tokens">https://github.com/Bernie-2016/ground-game-api/wiki/API-Tokens</a>
 */
public class LoginFacebookRequest {

    private final String grant_type = "password";

    private String username;

    private String password;

    public LoginFacebookRequest() {
    }

    public String getGrantType() {
        return grant_type;
    }

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

    public LoginFacebookRequest username(String email) {
        this.username = email;
        return this;
    }
}
