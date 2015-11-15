package com.berniesanders.canvass.repositories;

import android.content.Context;

import com.berniesanders.canvass.config.UrlConfig;
import com.berniesanders.canvass.models.CreateUserRequest;
import com.berniesanders.canvass.models.LoginEmailRequest;
import com.berniesanders.canvass.models.LoginEmailResponse;
import com.berniesanders.canvass.models.User;
import com.berniesanders.canvass.repositories.specs.TokenSpec;
import com.berniesanders.canvass.repositories.specs.UserSpec;
import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.Observable;
import timber.log.Timber;

/**
 * Data repository for oauth2
 */
@Singleton
public class TokenRepo {

    final Gson gson;
    private final Context context;
    private final OkHttpClient client = new OkHttpClient();


    @Inject
    public TokenRepo(Gson gson, Context context) {
        this.gson = gson;
        this.context = context;
    }

    /**
     */
    public Observable<LoginEmailResponse> loginEmail(final TokenSpec spec) {
        Timber.v("loginEmail()");
        return loginEmail(spec.getEmail());
    }



    /**
     * Might be best to pass the spec through to this method...?
     */
    private Observable<LoginEmailResponse> loginEmail(final LoginEmailRequest loginEmailRequest) {
        Timber.v("loging in Email....");

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

        TokenSpec.TokenEndpoint endpoint =
                retrofit.create(TokenSpec.TokenEndpoint.class);

        return endpoint.loginEmail(loginEmailRequest);
    }

}
