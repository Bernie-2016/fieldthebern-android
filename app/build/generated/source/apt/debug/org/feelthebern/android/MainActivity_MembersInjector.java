package org.feelthebern.android;

import android.support.v7.app.AppCompatActivity;
import com.google.gson.Gson;
import dagger.MembersInjector;
import javax.annotation.Generated;
import javax.inject.Provider;
import org.feelthebern.android.api.Api;

@Generated("dagger.internal.codegen.ComponentProcessor")
public final class MainActivity_MembersInjector implements MembersInjector<MainActivity> {
  private final MembersInjector<AppCompatActivity> supertypeInjector;
  private final Provider<Api> mApiProvider;
  private final Provider<Gson> mGsonProvider;

  public MainActivity_MembersInjector(MembersInjector<AppCompatActivity> supertypeInjector, Provider<Api> mApiProvider, Provider<Gson> mGsonProvider) {  
    assert supertypeInjector != null;
    this.supertypeInjector = supertypeInjector;
    assert mApiProvider != null;
    this.mApiProvider = mApiProvider;
    assert mGsonProvider != null;
    this.mGsonProvider = mGsonProvider;
  }

  @Override
  public void injectMembers(MainActivity instance) {  
    if (instance == null) {
      throw new NullPointerException("Cannot inject members into a null reference");
    }
    supertypeInjector.injectMembers(instance);
    instance.mApi = mApiProvider.get();
    instance.mGson = mGsonProvider.get();
  }

  public static MembersInjector<MainActivity> create(MembersInjector<AppCompatActivity> supertypeInjector, Provider<Api> mApiProvider, Provider<Gson> mGsonProvider) {  
      return new MainActivity_MembersInjector(supertypeInjector, mApiProvider, mGsonProvider);
  }
}

