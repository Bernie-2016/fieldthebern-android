package org.feelthebern.android.dagger;

import com.google.gson.Gson;
import dagger.MembersInjector;
import dagger.internal.MembersInjectors;
import javax.annotation.Generated;
import javax.inject.Provider;
import org.feelthebern.android.MainActivity;
import org.feelthebern.android.MainActivity_MembersInjector;
import org.feelthebern.android.api.Api;
import org.feelthebern.android.api.Api_Factory;

@Generated("dagger.internal.codegen.ComponentProcessor")
public final class DaggerMainComponent implements MainComponent {
  private Provider<Gson> provideGsonProvider;
  private Provider<Api> apiProvider;
  private MembersInjector<MainActivity> mainActivityMembersInjector;

  private DaggerMainComponent(Builder builder) {  
    assert builder != null;
    initialize(builder);
  }

  public static Builder builder() {  
    return new Builder();
  }

  private void initialize(final Builder builder) {  
    this.provideGsonProvider = MainModule_ProvideGsonFactory.create(builder.mainModule);
    this.apiProvider = Api_Factory.create(provideGsonProvider);
    this.mainActivityMembersInjector = MainActivity_MembersInjector.create((MembersInjector) MembersInjectors.noOp(), apiProvider, provideGsonProvider);
  }

  @Override
  public void inject(MainActivity mainActivity) {  
    mainActivityMembersInjector.injectMembers(mainActivity);
  }

  public static final class Builder {
    private MainModule mainModule;
  
    private Builder() {  
    }
  
    public MainComponent build() {  
      if (mainModule == null) {
        throw new IllegalStateException("mainModule must be set");
      }
      return new DaggerMainComponent(this);
    }
  
    public Builder mainModule(MainModule mainModule) {  
      if (mainModule == null) {
        throw new NullPointerException("mainModule");
      }
      this.mainModule = mainModule;
      return this;
    }
  }
}

