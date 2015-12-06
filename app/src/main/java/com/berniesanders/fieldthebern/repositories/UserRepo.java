package com.berniesanders.fieldthebern.repositories;

import com.berniesanders.fieldthebern.config.Config;
import com.berniesanders.fieldthebern.models.CreateUserRequest;
import com.berniesanders.fieldthebern.models.LoginEmailRequest;
import com.berniesanders.fieldthebern.models.LoginFacebookRequest;
import com.berniesanders.fieldthebern.models.User;
import com.berniesanders.fieldthebern.models.UserAttributes;
import com.berniesanders.fieldthebern.repositories.auth.ApiAuthenticator;
import com.berniesanders.fieldthebern.repositories.interceptors.AddTokenInterceptor;
import com.berniesanders.fieldthebern.repositories.interceptors.UserAgentInterceptor;
import com.berniesanders.fieldthebern.repositories.specs.TokenSpec;
import com.berniesanders.fieldthebern.repositories.specs.UserSpec;
import com.f2prateek.rx.preferences.Preference;
import com.f2prateek.rx.preferences.RxSharedPreferences;
import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Data repository for loading, creating users
 */
@Singleton
public class UserRepo {

    final Gson gson;
    private final TokenRepo tokenRepo;
    private final RxSharedPreferences rxPrefs;
    private final OkHttpClient client = new OkHttpClient();
    private final Config config;


    @Inject
    public UserRepo(Gson gson, TokenRepo tokenRepo, RxSharedPreferences rxPrefs, Config config) {
        this.gson = gson;
        this.tokenRepo = tokenRepo;
        this.rxPrefs = rxPrefs;
        this.config = config;

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        client.interceptors().add(new UserAgentInterceptor(config.getUserAgent()));
        client.interceptors().add(new AddTokenInterceptor(tokenRepo));
        client.interceptors().add(loggingInterceptor);
        client.setAuthenticator(new ApiAuthenticator(tokenRepo));
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
                } else {
                    LoginFacebookRequest loginFacebookRequest = new LoginFacebookRequest()
                            .username(userAttributes.getEmail())
                            .password(userAttributes.getPassword());
                    tokenRepo
                            .loginFacebook(new TokenSpec().facebook(loginFacebookRequest))
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
                .baseUrl(config.getCanvassUrl())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build();

        UserSpec.UserEndpoint endpoint =
                retrofit.create(UserSpec.UserEndpoint.class);


        return endpoint.create(user);
    }

}
