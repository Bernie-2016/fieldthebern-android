package com.berniesanders.canvass.models;

/**
 * See <a href="https://github.com/Bernie-2016/ground-game-api/wiki/API-Tokens">https://github.com/Bernie-2016/ground-game-api/wiki/API-Tokens</a>
 */
public final class LoginEmailRequest {

    private final String grant_type = "password";

    /**
     * email
     */
    private String username;
    private String password;

    public LoginEmailRequest() {
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

    /**
     * email
     */
    public LoginEmailRequest username(String username) {
        this.username = username;
        return this;
    }

    public LoginEmailRequest password(String password) {
        this.password = password;
        return this;
    }
}
