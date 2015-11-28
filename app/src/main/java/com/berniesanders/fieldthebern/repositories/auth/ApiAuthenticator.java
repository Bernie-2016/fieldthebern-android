package com.berniesanders.fieldthebern.repositories.auth;
/*
 * Made with love by volunteers
 * Copyright 2015 FeelTheBern.org, BernieSanders.com, Coderly, 
 * and the volunteers that wrote this code
 * License: GNU AGPLv3 - https://gnu.org/licenses/agpl.html 
 */

import com.berniesanders.fieldthebern.models.Token;
import com.berniesanders.fieldthebern.repositories.TokenRepo;
import com.squareup.okhttp.Authenticator;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.Proxy;

import timber.log.Timber;

public class ApiAuthenticator implements Authenticator {

    private final TokenRepo tokenRepo;


    public ApiAuthenticator(TokenRepo tokenRepo) {
        this.tokenRepo = tokenRepo;
    }


    /**
     * Returns a request that includes a credential to satisfy an authentication
     * challenge in {@code response}. Returns null if the challenge cannot be
     * satisfied. This method is called in response to an HTTP 401 unauthorized
     * status code sent by the origin server.
     * <p>
     * <p>Typical implementations will look up a credential and create a request
     * derived from the initial request by setting the "Authorization" header.
     * <pre>   {@code
     * <p>
     *    String credential = Credentials.basic(...)
     *    return response.request().newBuilder()
     *        .header("Authorization", credential)
     *        .build();
     * }</pre>
     *
     */
    @Override
    public Request authenticate(Proxy proxy, Response response) throws IOException {
//      System.out.println("Authenticating for response: " + response);
//      System.out.println("Challenges: " + response.challenges());

        // Refresh access token using a synchronous api request
        // TODO this is not the right code to do a refresh
        // TODO implement refresh
        Token token = tokenRepo.get();
        return response.request().newBuilder()
                .header("Authorization", "Bearer " + token.accessToken())
                .build();
    }

    /**
     * Returns a request that includes a credential to satisfy an authentication
     * challenge made by {@code response}. Returns null if the challenge cannot be
     * satisfied. This method is called in response to an HTTP 407 unauthorized
     * status code sent by the proxy server.
     * <p>
     * <p>Typical implementations will look up a credential and create a request
     * derived from the initial request by setting the "Proxy-Authorization"
     * header. <pre>   {@code
     * <p>
     *    String credential = Credentials.basic(...)
     *    return response.request().newBuilder()
     *        .header("Proxy-Authorization", credential)
     *        .build();
     * }</pre>
     */
    @Override
    public Request authenticateProxy(Proxy proxy, Response response) throws IOException {
        Timber.w("authenticateProxy?");
        return null;
    }
}
