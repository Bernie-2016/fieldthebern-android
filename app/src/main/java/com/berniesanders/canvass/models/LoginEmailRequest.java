package com.berniesanders.canvass.models;

/**
 * See <a href="https://github.com/Bernie-2016/ground-game-api/wiki/API-Tokens">https://github.com/Bernie-2016/ground-game-api/wiki/API-Tokens</a>
 */
public final class LoginEmailRequest {

    private final String grant_type = "password";

    private final String username; //email
    private final String password;

    public LoginEmailRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
