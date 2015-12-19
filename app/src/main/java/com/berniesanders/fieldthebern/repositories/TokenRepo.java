package com.berniesanders.fieldthebern.repositories;

import android.util.Base64;
import com.berniesanders.fieldthebern.config.Config;
import com.berniesanders.fieldthebern.models.LoginEmailRequest;
import com.berniesanders.fieldthebern.models.LoginFacebookRequest;
import com.berniesanders.fieldthebern.models.Token;
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
    final Retrofit retrofit;
    final TokenSpec.TokenEndpoint endpoint;


    @Inject
    public TokenRepo(Gson gson, RxSharedPreferences rxPrefs, Config config) {
        this.gson = gson;
        this.rxPrefs = rxPrefs;
        this.config = config;
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

        return loginEmail(spec.getEmail()).map(new Func1<Token, Token>() {
            @Override
            public Token call(Token token) {

                Preference<String> tokenPref = rxPrefs.getString(Token.PREF_NAME);
                tokenPref.set(gson.toJson(token));
                return token;
            }
        });
    }

    /**
     */
    public Observable<Token> loginFacebook(final TokenSpec spec) {
        Timber.v("loginFacebook()");

        return loginFacebook(spec.getFacebook()).map(new Func1<Token, Token>() {
            @Override
            public Token call(Token token) {

                Preference<String> tokenPref = rxPrefs.getString(Token.PREF_NAME);
                tokenPref.set(gson.toJson(token));
                return token;
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

        String refreshToken = get().refreshToken();

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
