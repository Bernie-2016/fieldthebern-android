/*
 * Copyright (c) 2016 - Bernie 2016, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.berniesanders.fieldthebern.repositories;

import android.content.Context;
import android.support.annotation.Nullable;

import com.berniesanders.fieldthebern.FTBApplication;
import com.berniesanders.fieldthebern.config.Config;
import com.berniesanders.fieldthebern.events.LoginEvent;
import com.berniesanders.fieldthebern.exceptions.NetworkUnavailableException;
import com.berniesanders.fieldthebern.models.CreateUserRequest;
import com.berniesanders.fieldthebern.models.LoginEmailRequest;
import com.berniesanders.fieldthebern.models.LoginFacebookRequest;
import com.berniesanders.fieldthebern.models.Token;
import com.berniesanders.fieldthebern.models.User;
import com.berniesanders.fieldthebern.models.UserAttributes;
import com.berniesanders.fieldthebern.network.NetChecker;
import com.berniesanders.fieldthebern.repositories.auth.ApiAuthenticator;
import com.berniesanders.fieldthebern.repositories.interceptors.AddTokenInterceptor;
import com.berniesanders.fieldthebern.repositories.interceptors.UserAgentInterceptor;
import com.berniesanders.fieldthebern.repositories.specs.TokenSpec;
import com.berniesanders.fieldthebern.repositories.specs.UserSpec;
import com.f2prateek.rx.preferences.Preference;
import com.f2prateek.rx.preferences.RxSharedPreferences;
import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.functions.Func1;
import timber.log.Timber;

/**
 * Data repository for loading, creating users
 */
@Singleton
public class UserRepo {

    final Gson gson;
    private final TokenRepo tokenRepo;
    private final RxSharedPreferences rxPrefs;
    private OkHttpClient client;
    private final Config config;
    private final Context context;

    User currentUser;

    @Inject
    public UserRepo(Gson gson, TokenRepo tokenRepo, RxSharedPreferences rxPrefs, Config config, Context context) {
        this.gson = gson;
        this.tokenRepo = tokenRepo;
        this.rxPrefs = rxPrefs;
        this.config = config;
        this.context = context;

        HttpLoggingInterceptor.Logger logger = new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Timber.v(message);
            }
        };
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(logger);
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        client = new OkHttpClient.Builder()
                .addInterceptor(new UserAgentInterceptor(config.getUserAgent()))
                .addInterceptor(new AddTokenInterceptor(tokenRepo))
                .addInterceptor(loggingInterceptor)
                .authenticator(new ApiAuthenticator(tokenRepo))
                .build();
        FTBApplication.getEventBus().register(this);
    }

    public void logout() {
        Preference<String> userPref = rxPrefs.getString(User.PREF_NAME);
        Preference<String> tokenPref = rxPrefs.getString(Token.PREF_NAME);
        userPref.delete();
        tokenPref.delete();
        currentUser = null;
        FTBApplication.getEventBus().post(new LoginEvent(LoginEvent.LOGOUT, null));
    }

    @Subscribe
    public void onLoginEvent(LoginEvent event) {
        switch (event.getEventType()) {
            case LoginEvent.LOGIN:
                currentUser = event.getUser();
                Preference<String> userPref = rxPrefs.getString(User.PREF_NAME);
                if (currentUser!=null) {
                    userPref.set(gson.toJson(currentUser));
                }
                break;
            case LoginEvent.LOGOUT:
                break;
            default:
                //uh
                Timber.e("onLoginEvent unknown type");
        }
    }

    /**
     * Current user could be null, this should generally only be called after receiving a
     * LoginEvent.LOGIN
     */
    @Nullable
    public User getCurrentUser() {
        if (currentUser == null) {
            Preference<String> userPref = rxPrefs.getString(User.PREF_NAME);
            String userString = userPref.get();

            if (userString != null) {
                currentUser = gson.fromJson(userPref.get(), User.class);
            }
        }
        return currentUser;
    }

    /**
     */
    public Observable<User> create(final UserSpec spec) {

        Timber.v("create()");

        if (!NetChecker.connected(context)) {
            return Observable.error(new NetworkUnavailableException("No internet available"));
        }

        //TODO not sure I have chained these in the best way...

        // This is the order that things are supposed to happen:
        //
        // 1. post/create a new user
        // 2. save a ref to the returned user
        // 3. login via tokenRepo, (which unfortunately transforms the response to Token, hence the reason for this craziness)
        // 4. read the returned user from the ref we saved in #2
        // 5. add the base64 photo data from the user in step #1 to the user we read from step #2
        // 6. upload that user with the photo data via update() (aka PATCH users/me)
        // 7. save the user returned from #6
        // 8. return the user to the original subscriber

        return create(spec.getCreateUserRequest())
                .map(new Func1<User, User>() { //save the user, this give the late .flatMap access to it
                    @Override
                    public User call(User user) {
                        Timber.v("user created, saving in memory...");
                        currentUser = user;
                        Preference<String> userPref = rxPrefs.getString(User.PREF_NAME);
                        userPref.set(gson.toJson(user));
                        return user;
                    }
                })
                .flatMap(new Func1<User, Observable<Token>>() { //log them in
                    @Override
                    public Observable<Token> call(User user) {
                        Timber.v("user created, calling tokenRepo to sign in...");

                        //get the username and pass right out of the create user request
                        //the don't exist in the user object returned from the API call in .map above
                        final UserAttributes userAttributes = spec
                                .getCreateUserRequest()
                                .getData()
                                .getAttributes();

                        if (!userAttributes.isFacebookUser()) {
                            LoginEmailRequest loginEmailRequest = new LoginEmailRequest()
                                    .username(userAttributes.getEmail())
                                    .password(userAttributes.getPassword());
                            return tokenRepo
                                    .loginEmail(new TokenSpec().email(loginEmailRequest));
                        } else {
                            LoginFacebookRequest loginFacebookRequest = new LoginFacebookRequest()
                                    .username(userAttributes.getEmail())
                                    .password(userAttributes.getPassword());
                            return tokenRepo
                                    .loginFacebook(new TokenSpec().facebook(loginFacebookRequest));
                        }
                    }
                })
                .flatMap(new Func1<Token, Observable<User>>() { //upload photo

                    @Override
                    public Observable<User> call(Token user) {
                        Timber.v("user signed in, calling update to upload the photo");

                        final UserAttributes userAttributes = spec
                                .getCreateUserRequest()
                                .getData()
                                .getAttributes();
                        currentUser.getData().attributes(userAttributes);
                        currentUser.getData().attributes().password(null); //it's all fun and games until someone loses a password
                        currentUser.getData().attributes().photoLargeUrl(null);
                        currentUser.getData().attributes().photoThumbUrl(null);
                        spec.update(currentUser);
                        return update(spec);
                    }
                })
                .map(new Func1<User, User>() {  //re-save the user to be sure they are an awesome user
                    @Override
                    public User call(User user) {
                        Timber.v("photo uploaded, saving user and returning to the subscriber");
                        currentUser = user;
                        Preference<String> userPref = rxPrefs.getString(User.PREF_NAME);
                        userPref.set(gson.toJson(user));
                        return currentUser;
                    }
                });
    }

    public Observable<User> update(final UserSpec spec) {

        Timber.v("update()");

        if (!NetChecker.connected(context)) {
            return Observable.error(new NetworkUnavailableException("No internet available"));
        }

        return getMe()
                .flatMap(new Func1<User, Observable<User>>() {
                    @Override
                    public Observable<User> call(User user) {
                        Timber.v("getMe flatmap");
                        String firstName = spec.user().getData().attributes().getFirstName();
                        String lastName = spec.user().getData().attributes().getLastName();
                        user.getData().attributes()
                                .firstName(firstName)
                                .lastName(lastName)
                                .base64PhotoData(spec.user().getData().attributes().getBase64PhotoData());
                        CreateUserRequest request = spec.getCreateUserRequest()
                                .withAttributes(user.getData().attributes());
                        Preference<String> userPref = rxPrefs.getString(User.PREF_NAME);
                        userPref.set(gson.toJson(user));
                        currentUser = user;
                        currentUser.getData().attributes().password(null);  //be safe, children
                        return update(request);
                    }
                });
    }


    /**
     * Might be best to pass the spec through to this method...?
     */
    private Observable<User> create(final CreateUserRequest userRequest) {
        Timber.v("create()");

        if (!NetChecker.connected(context)) {
            return Observable.error(new NetworkUnavailableException("No internet available"));
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(config.getCanvassUrl())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build();

        UserSpec.UserEndpoint endpoint =
                retrofit.create(UserSpec.UserEndpoint.class);


        return endpoint.create(userRequest);
    }

    private Observable<User> update(final CreateUserRequest user) {
        Timber.v("update(CreateUserRequest)");

        if (!NetChecker.connected(context)) {
            return Observable.error(new NetworkUnavailableException("No internet available"));
        }

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

    public Observable<User> getMe() {
        Timber.v("getMe()");

        if (!NetChecker.connected(context)) {
            return Observable.error(new NetworkUnavailableException("No internet available"));
        }

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
