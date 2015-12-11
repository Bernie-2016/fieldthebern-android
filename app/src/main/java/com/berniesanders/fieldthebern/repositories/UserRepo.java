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
    private final Config config;


    @Inject
    public UserRepo(Gson gson, TokenRepo tokenRepo, RxSharedPreferences rxPrefs, Config config) {
        this.gson = gson;
        this.tokenRepo = tokenRepo;
        this.rxPrefs = rxPrefs;
        this.config = config;

//        HttpLoggingInterceptor.Logger logger = new HttpLoggingInterceptor.Logger() {
//            @Override
//            public void log(String message) {
//                Timber.v(message);
//            }
//        };
//        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(logger);
//        loggingInterceptor.setLevel(Level.BODY);
//        client.interceptors().add(loggingInterceptor);

        client.interceptors().add(new UserAgentInterceptor(config.getUserAgent()));
        client.interceptors().add(new AddTokenInterceptor(tokenRepo));
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

    public Observable<User> update(final UserSpec spec) {
        Timber.v("Calling update");
        Observable<User> me = getMe()
                .flatMap(new Func1<User, Observable<User>>() {
                    @Override
                    public Observable<User> call(User user) {
                        Timber.v("getMe flatmap");
                        String firstName = spec.user().getData().attributes().getFirstName();
                        String lastName = spec.user().getData().attributes().getLastName();
                        user.getData().attributes()
                                .firstName(firstName)
                                .lastName(lastName);
                        CreateUserRequest request = spec.getCreateUserRequest()
                                .withAttributes(user.getData().attributes());
                        return update(request);
                    }
                });

        Timber.v("Calling update finished");
        return me;
    }


    /**
     * Might be best to pass the spec through to this method...?
     */
    private Observable<User> create(final CreateUserRequest user) {
        Timber.v("create()");

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

    private Observable<User> update(final CreateUserRequest user) {
        Timber.v("update(CreateUserRequest)");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(config.getCanvassUrl())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build();

        UserSpec.UserEndpoint endpoint =
                retrofit.create(UserSpec.UserEndpoint.class);

        return endpoint.update(user);
    }

    private Observable<User> getMe() {
        Timber.v("getMe()");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(config.getCanvassUrl())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build();

        UserSpec.UserEndpoint endpoint =
                retrofit.create(UserSpec.UserEndpoint.class);
        return endpoint.getMe();
    }
}
