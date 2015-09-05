package org.feelthebern.android.api;

import com.google.gson.Gson;
import dagger.internal.Factory;
import javax.annotation.Generated;
import javax.inject.Provider;

@Generated("dagger.internal.codegen.ComponentProcessor")
public final class Api_Factory implements Factory<Api> {
  private final Provider<Gson> gsonProvider;

  public Api_Factory(Provider<Gson> gsonProvider) {  
    assert gsonProvider != null;
    this.gsonProvider = gsonProvider;
  }

  @Override
  public Api get() {  
    return new Api(gsonProvider.get());
  }

  public static Factory<Api> create(Provider<Gson> gsonProvider) {  
    return new Api_Factory(gsonProvider);
  }
}

