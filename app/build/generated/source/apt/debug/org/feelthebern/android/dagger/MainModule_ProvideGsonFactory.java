package org.feelthebern.android.dagger;

import com.google.gson.Gson;
import dagger.internal.Factory;
import javax.annotation.Generated;

@Generated("dagger.internal.codegen.ComponentProcessor")
public final class MainModule_ProvideGsonFactory implements Factory<Gson> {
  private final MainModule module;

  public MainModule_ProvideGsonFactory(MainModule module) {  
    assert module != null;
    this.module = module;
  }

  @Override
  public Gson get() {  
    Gson provided = module.provideGson();
    if (provided == null) {
      throw new NullPointerException("Cannot return null from a non-@Nullable @Provides method");
    }
    return provided;
  }

  public static Factory<Gson> create(MainModule module) {  
    return new MainModule_ProvideGsonFactory(module);
  }
}

