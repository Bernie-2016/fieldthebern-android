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
import com.berniesanders.fieldthebern.config.Config;
import com.berniesanders.fieldthebern.exceptions.NetworkUnavailableException;
import com.berniesanders.fieldthebern.models.Rankings;
import com.berniesanders.fieldthebern.network.NetChecker;
import com.berniesanders.fieldthebern.repositories.auth.ApiAuthenticator;
import com.berniesanders.fieldthebern.repositories.interceptors.AddTokenInterceptor;
import com.berniesanders.fieldthebern.repositories.interceptors.UserAgentInterceptor;
import com.berniesanders.fieldthebern.repositories.specs.RankingSpec;
import com.f2prateek.rx.preferences.RxSharedPreferences;
import com.google.gson.Gson;
import javax.inject.Inject;
import javax.inject.Singleton;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.RxJavaCallAdapterFactory;
import rx.Observable;
import timber.log.Timber;

/**
 * Data repository for loading, creating users
 */
@Singleton public class RankingsRepo {

  final Gson gson;
  private final TokenRepo tokenRepo;
  private final RxSharedPreferences rxPrefs;
  private OkHttpClient client;
  private final Config config;
  private final Context context;

  @Inject
  public RankingsRepo(Gson gson, TokenRepo tokenRepo, RxSharedPreferences rxPrefs, Config config,
      Context context) {
    this.gson = gson;
    this.tokenRepo = tokenRepo;
    this.rxPrefs = rxPrefs;
    this.config = config;
    this.context = context;

    HttpLoggingInterceptor.Logger logger = new HttpLoggingInterceptor.Logger() {
      @Override public void log(String message) {
        Timber.v(message);
      }
    };
    HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(logger);
    loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

    client =
        new OkHttpClient.Builder().addInterceptor(new UserAgentInterceptor(config.getUserAgent()))
            .addInterceptor(new AddTokenInterceptor(tokenRepo))
            .addInterceptor(loggingInterceptor)
            .authenticator(new ApiAuthenticator(tokenRepo))
            .build();
  }

  /**
   */
  public Observable<Rankings> get(final RankingSpec spec) {

    Timber.v("get()");

    if (!NetChecker.connected(context)) {
      return Observable.error(new NetworkUnavailableException("No internet available"));
    }

    Retrofit retrofit = new Retrofit.Builder().baseUrl(config.getCanvassUrl())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .client(client)
        .build();

    RankingSpec.RankEndpoint endpoint = retrofit.create(RankingSpec.RankEndpoint.class);

    return endpoint.get(spec.type());
  }
}
