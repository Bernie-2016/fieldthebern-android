package com.berniesanders.canvass.repositories;

import com.berniesanders.canvass.config.UrlConfig;
import com.berniesanders.canvass.models.CreateUserRequest;
import com.berniesanders.canvass.models.User;
import com.berniesanders.canvass.repositories.specs.UserSpec;
import com.google.gson.Gson;
import com.squareup.okhttp.Authenticator;
import com.squareup.okhttp.Credentials;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import java.io.File;
import java.io.IOException;
import java.net.Proxy;

import javax.inject.Inject;
import javax.inject.Singleton;

import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.Observable;
import timber.log.Timber;

/**
 * Data repository for loading, creating users
 */
@Singleton
public class UserRepo {

    final Gson gson;


    @Inject
    public UserRepo(Gson gson) {
        this.gson = gson;
    }

    /**
     */
    public Observable<User> create(final UserSpec spec) {

        return create(spec.getCreateUserRequest());
    }


//    private final OkHttpClient client = new OkHttpClient();
//
//    public void run() throws Exception {
//        client.setAuthenticator(new Authenticator() {
//            @Override public Request authenticate(Proxy proxy, Response response) {
//                System.out.println("Authenticating for response: " + response);
//                System.out.println("Challenges: " + response.challenges());
//                String credential = Credentials.basic("jesse", "password1");
//                return response.request().newBuilder()
//                        .header("Authorization", credential)
//                        .build();
//            }
//
//            @Override public Request authenticateProxy(Proxy proxy, Response response) {
//                return null; // Null indicates no attempt to authenticate.
//            }
//        });
//
//        Request request = new Request.Builder()
//                .url("http://publicobject.com/secrets/hellosecret.txt")
//                .build();
//
//        Response response = client.newCall(request).execute();
//        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
//
//        System.out.println(response.body().string());
//    }

    private final OkHttpClient client = new OkHttpClient();

    /**
     * Might be best to pass the spec through to this method...?
     */
    private Observable<User> create(final CreateUserRequest user) {
        Timber.v("create()");

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        client.interceptors().add(interceptor);

//        client.setAuthenticator(new Authenticator() {
//            @Override
//            public Request authenticate(Proxy proxy, Response response) {
////                System.out.println("Authenticating for response: " + response);
////                System.out.println("Challenges: " + response.challenges());
////                //String credential = Credentials.basic()
////                return response.request().newBuilder()
////                        .header("Authorization", credential)
////                        .build();
//
//                // Refresh access token using a synchronous api request
//                newAccessToken = service.refreshToken();
//                return response.request().newBuilder()
//                        .header("Authorization", newAccessToken)
//                        .build();
//            }
//
//            @Override
//            public Request authenticateProxy(Proxy proxy, Response response) {
//                return null; // Null indicates no attempt to authenticate.
//            }
//        });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UrlConfig.CANVASS_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build();

        UserSpec.UserEndpoint endpoint =
                retrofit.create(UserSpec.UserEndpoint.class);

        return endpoint.create(user);
    }

}
