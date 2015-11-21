package com.berniesanders.canvass.repositories;

import com.berniesanders.canvass.config.UrlConfig;
import com.berniesanders.canvass.models.CreateUserRequest;
import com.berniesanders.canvass.models.LoginEmailRequest;
import com.berniesanders.canvass.models.Token;
import com.berniesanders.canvass.models.User;
import com.berniesanders.canvass.models.UserAttributes;
import com.berniesanders.canvass.repositories.specs.TokenSpec;
import com.berniesanders.canvass.repositories.specs.UserSpec;
import com.f2prateek.rx.preferences.Preference;
import com.f2prateek.rx.preferences.RxSharedPreferences;
import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;


import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Data repository for loading, creating users
 */
@Singleton
public class UserRepo {

    final Gson gson;
    private final TokenRepo tokenRepo;
    private final RxSharedPreferences rxPrefs;
    private final OkHttpClient client = new OkHttpClient();


    @Inject
    public UserRepo(Gson gson, TokenRepo tokenRepo, RxSharedPreferences rxPrefs) {
        this.gson = gson;
        this.tokenRepo = tokenRepo;
        this.rxPrefs = rxPrefs;
    }

    /**
     */
    public Observable<User> create(final UserSpec spec) {

        return create(spec.getCreateUserRequest()).map(new Func1<User, User>() {
            @Override
            public User call(User user) {

                Timber.v("Func1 call()");

                UserAttributes userAttributes = spec
                        .getCreateUserRequest()
                        .getData()
                        .getAttributes();

                if (!userAttributes.isFacebookUser()) {
                    LoginEmailRequest loginEmailRequest = new LoginEmailRequest()
                            .username(userAttributes.getEmail())
                            .password(userAttributes.getPassword());
                    tokenRepo
                            .loginEmail(new TokenSpec().email(loginEmailRequest))
                            .subscribeOn(Schedulers.io())
                            .subscribe();
                }

                Preference<String> userPref = rxPrefs.getString(User.PREF_NAME);
                userPref.set(gson.toJson(user));

                return user;
            }
        });
    }



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
