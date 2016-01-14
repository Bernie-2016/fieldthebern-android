package com.berniesanders.fieldthebern.repositories;

import android.content.Context;
import android.util.Base64;
import com.berniesanders.fieldthebern.config.Config;
import com.berniesanders.fieldthebern.exceptions.NetworkUnavailableException;
import com.berniesanders.fieldthebern.models.LoginEmailRequest;
import com.berniesanders.fieldthebern.models.LoginFacebookRequest;
import com.berniesanders.fieldthebern.models.Token;
import com.berniesanders.fieldthebern.network.NetChecker;
import com.berniesanders.fieldthebern.repositories.interceptors.UserAgentInterceptor;
import com.berniesanders.fieldthebern.repositories.specs.TokenSpec;
import com.f2prateek.rx.preferences.Preference;
import com.f2prateek.rx.preferences.RxSharedPreferences;
import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import timber.log.Timber;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.UnsupportedEncodingException;

/**
 * Data repository for oauth2
 */
@Singleton
public class TokenRepo {


    final Gson gson;
    private final OkHttpClient client = new OkHttpClient();
    private final RxSharedPreferences rxPrefs;
    private final Config config;
    private final Context context;
    final Retrofit retrofit;
    final TokenSpec.TokenEndpoint endpoint;


    @Inject
    public TokenRepo(Gson gson, RxSharedPreferences rxPrefs, Config config, Context context) {
        this.gson = gson;
        this.rxPrefs = rxPrefs;
        this.config = config;
        this.context = context;
        client.interceptors().add(new UserAgentInterceptor(config.getUserAgent()));

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        client.interceptors().add(interceptor);

        retrofit = new Retrofit.Builder()
                .baseUrl(this.config.getCanvassUrl())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build();

        endpoint = retrofit.create(TokenSpec.TokenEndpoint.class);
    }

    /**
     */
    public Token get() {
        Timber.v("get()");

        //TODO: re-auth based on the type of user?
        Preference<String> tokenPref = rxPrefs.getString(Token.PREF_NAME);

        if (tokenPref.get() == null) { return null; }
        return gson.fromJson(tokenPref.get(), Token.class);
    }

    /**
     */
    public Observable<Token> loginEmail(final TokenSpec spec) {
        Timber.v("loginEmail()");
        return Observable.create(new Observable.OnSubscribe<Token>() {
            @Override
            public void call(Subscriber<? super Token> subscriber) {
                if (!NetChecker.connected(context)) {
                    subscriber.onError(new NetworkUnavailableException("No internet available"));
                }
            }
        })
        .flatMap(new Func1<Token, Observable<Token>>() {
            @Override
            public Observable<Token> call(Token token) {
                return loginEmail(spec.getEmail()).map(new Func1<Token, Token>() {
                    @Override
                    public Token call(Token token) {
                        Timber.v("loginEmail() saving token");
                        Preference<String> tokenPref = rxPrefs.getString(Token.PREF_NAME);
                        tokenPref.set(gson.toJson(token));
                        return token;
                    }
                });
            }
        });

    }

    /**
     */
    public Observable<Token> loginFacebook(final TokenSpec spec) {
        Timber.v("loginFacebook()");
        return Observable.create(new Observable.OnSubscribe<Token>() {
            @Override
            public void call(Subscriber<? super Token> subscriber) {
                if (!NetChecker.connected(context)) {
                    subscriber.onError(new NetworkUnavailableException("No internet available"));
                }
            }
        })
        .flatMap(new Func1<Token, Observable<Token>>() {
            @Override
            public Observable<Token> call(Token token) {
                return loginFacebook(spec.getFacebook()).map(new Func1<Token, Token>() {
                    @Override
                    public Token call(Token token) {
                        Timber.v("loginFacebook() saving token");
                        Preference<String> tokenPref = rxPrefs.getString(Token.PREF_NAME);
                        tokenPref.set(gson.toJson(token));
                        return token;
                    }
                });
            }
        });

    }

    /**
     * Might be best to pass the spec through to this method...?
     */
    private Observable<Token> loginFacebook(final LoginFacebookRequest loginFacebookRequest) {
        Timber.v("logging in facebook....");


        return endpoint.loginFacebook(
                getAuthString(),
                loginFacebookRequest.getGrantType(),
                loginFacebookRequest.username(),
                loginFacebookRequest.password());
    }

    /**
     * Might be best to pass the spec through to this method...?
     */
    private Observable<Token> loginEmail(final LoginEmailRequest loginEmailRequest) {
        Timber.v("logging in Email....");


        return endpoint.loginEmail(
                getAuthString(),
                loginEmailRequest.getGrantType(),
                loginEmailRequest.username(),
                loginEmailRequest.password());
    }

    /**
     * Might be best to pass the spec through to this method...?
     */
    public Observable<Token> refresh() {
        Timber.v("token refresh....");

        String refreshToken = null;
        try {
            refreshToken = get().refreshToken();
        } catch (NullPointerException npe) {
            Timber.w(npe, "attempted to refresh the token but it wasn't there");
            return null;
        }


        return endpoint.refresh(
                Token.GRANT_REFRESH,
                config.getClientId(),
                config.getClientSecret(),
                refreshToken)
                       .map(new Func1<Token, Token>() {
            @Override
            public Token call(Token token) {

                Preference<String> tokenPref = rxPrefs.getString(Token.PREF_NAME);
                tokenPref.set(gson.toJson(token));
                return token;
            }
        });
    }


    private String getAuthString() {
        String cred = this.config.getClientId() + ":" + this.config.getClientSecret();
        byte[] data = new byte[0];
        try {
            data = cred.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            Timber.e(e, "error in getAuthString()");
        }
        String base64ClientString = Base64.encodeToString(data, Base64.NO_WRAP);
        Timber.v("base64ClientString:"+base64ClientString);
        return "Basic "+base64ClientString;
    }

}
