package com.berniesanders.canvass.repositories;

import android.content.Context;
import android.util.Base64;

import com.berniesanders.canvass.R;
import com.berniesanders.canvass.config.UrlConfig;
import com.berniesanders.canvass.models.LoginEmailRequest;
import com.berniesanders.canvass.models.Token;
import com.berniesanders.canvass.repositories.specs.TokenSpec;
import com.f2prateek.rx.preferences.Preference;
import com.f2prateek.rx.preferences.RxSharedPreferences;
import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import java.io.UnsupportedEncodingException;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.functions.Func1;
import timber.log.Timber;

/**
 * Data repository for oauth2
 */
@Singleton
public class TokenRepo {

    private static final String CLIENT_ID = "cbe732e440995e8ae73dfb093de4b880f3a68346e4b1bf933e626599a090bdec";
    private static final String CLIENT_SECRET = "30d732185171be840adff6e11aae9157614bb7be2da2d27100bcb7bc938ac369";

    final Gson gson;
    private final Context context;
    private final OkHttpClient client = new OkHttpClient();
    private final RxSharedPreferences rxPrefs;


    @Inject
    public TokenRepo(Gson gson, Context context, RxSharedPreferences rxPrefs) {
        this.gson = gson;
        this.context = context;
        this.rxPrefs = rxPrefs;
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
     * Might be best to pass the spec through to this method...?
     */
    private Observable<Token> loginEmail(final LoginEmailRequest loginEmailRequest) {
        Timber.v("logging in Email....");

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        client.interceptors().add(interceptor);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(context.getString(R.string.baseUrl))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build();

        TokenSpec.TokenEndpoint endpoint =
                retrofit.create(TokenSpec.TokenEndpoint.class);

        return endpoint.loginEmail(
                getAuthString(),
                "password",
                loginEmailRequest.username(),
                loginEmailRequest.password());
    }


    private String getAuthString() {
        String cred = CLIENT_ID + ":" + CLIENT_SECRET;
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
