package com.berniesanders.canvass.models;

/**
 * See <a href="https://github.com/Bernie-2016/ground-game-api/wiki/API-Tokens">https://github.com/Bernie-2016/ground-game-api/wiki/API-Tokens</a>
 */
public class LoginFacebookResponse {

    private String access_token;
    /**
     * time_in_seconds
     */
    private int expires_in;

    /**
     * should be 'bearer'
     */
    private String token_type;

    private int user_id;

}
