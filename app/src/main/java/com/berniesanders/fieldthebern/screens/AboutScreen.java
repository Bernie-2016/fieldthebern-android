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

package com.berniesanders.fieldthebern.screens;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.berniesanders.fieldthebern.BuildConfig;
import com.berniesanders.fieldthebern.FTBApplication;
import com.berniesanders.fieldthebern.R;
import com.berniesanders.fieldthebern.annotations.Layout;
import com.berniesanders.fieldthebern.controllers.ActionBarController;
import com.berniesanders.fieldthebern.controllers.ActionBarService;
import com.berniesanders.fieldthebern.dagger.FtbScreenScope;
import com.berniesanders.fieldthebern.dagger.MainComponent;
import com.berniesanders.fieldthebern.mortar.FlowPathBase;
import com.berniesanders.fieldthebern.views.AboutView;
import javax.inject.Inject;
import mortar.ViewPresenter;
import timber.log.Timber;

@Layout(R.layout.screen_about)
public class AboutScreen extends FlowPathBase {

  public AboutScreen() {
  }

  @Override
  public Object createComponent() {
    return DaggerAboutScreen_Component.builder()
        .mainComponent(FTBApplication.getComponent())
        .aboutModule(new AboutModule())
        .build();
  }

  @Override
  public String getScopeName() {
    return AboutScreen.class.getName();
  }

  @dagger.Module
  class AboutModule {

    public AboutModule() {
    }
  }

  @FtbScreenScope
  @dagger.Component(modules = AboutModule.class, dependencies = MainComponent.class)
  public interface Component {
    void inject(AboutView t);
  }

  @FtbScreenScope
  static public class Presenter extends ViewPresenter<AboutView> {

    @BindString(R.string.about)
    String screenTitleString;
    @BindString(R.string.about_text)
    String versionUnformatted;
    @BindString(R.string.email_support)
    String supportEmail;
    @BindString(R.string.gitHash)
    String gitHash;
    @BindString(R.string.buildDate)
    String buildDate;

    @Bind(R.id.email)
    TextView emailText;
    @Bind(R.id.textView)
    TextView versionText;

    @Inject
    Presenter() {
    }

    @Override
    protected void onLoad(Bundle savedInstanceState) {
      Timber.v("onLoad");
      ButterKnife.bind(this, getView());
      setActionBar();

      SpannableStringBuilder spanBuilder = new SpannableStringBuilder(supportEmail);
      spanBuilder.setSpan(new UnderlineSpan(), 0, spanBuilder.length(), 0);
      emailText.setText(spanBuilder);

      String pkName = getView().getContext().getPackageName();
      String version = null;
      try {
        version = getView().getContext().getPackageManager().getPackageInfo(pkName, 0).versionName;
        versionText.setText(String.format(versionUnformatted, version));
      } catch (PackageManager.NameNotFoundException e) {
        Timber.e(e, "error loading app version string from package manager");
      }
    }

    void setActionBar() {
      ActionBarService.get(getView())
          .closeAppbar()
          .setMainImage(null)
          .setConfig(new ActionBarController.Config(screenTitleString, null));
    }

    @OnClick(R.id.email)
    void emailSupport(final View v) {
      String body = "Version: " + BuildConfig.VERSION_NAME + "\n" +
          "\n" +
          "-- Device --" + "\n" +
          "Make: " + Build.MANUFACTURER + "\n" +
          "Model: " + Build.MODEL + "\n" +
          "Release: " + Build.VERSION.RELEASE + "\n" +
          "API: " + Build.VERSION.SDK_INT + "\n" +
          "Build date: " + buildDate + "\n" +
          "Git hash: " + gitHash + "\n" +
          "\n" +
          "-- Description --" + "\n";

      Intent intent =
          new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:")).putExtra(Intent.EXTRA_EMAIL,
              new String[] { supportEmail }).putExtra(Intent.EXTRA_TEXT, body);

      Context context = v.getContext();
      if (intent.resolveActivity(context.getPackageManager()) != null) {
        context.startActivity(intent);
      } else {
        Timber.w("No app available to handle email intent");
      }
    }

    @Override
    protected void onSave(Bundle outState) {
    }

    @Override
    public void dropView(AboutView view) {
      super.dropView(view);
      ButterKnife.unbind(this);
    }
  }
}
