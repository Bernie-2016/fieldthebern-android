package com.berniesanders.canvass.models;

/**
 * See <a href="https://github.com/Bernie-2016/ground-game-api/wiki/API-Tokens">https://github.com/Bernie-2016/ground-game-api/wiki/API-Tokens</a>
 */
public final class LoginEmailResponse {


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

    /**
     * time_of_creation_as_integer
     */
    private String created_at;
}
