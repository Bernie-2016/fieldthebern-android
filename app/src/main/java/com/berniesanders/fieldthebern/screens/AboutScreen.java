package com.berniesanders.fieldthebern.screens;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

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

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;
import mortar.ViewPresenter;
import timber.log.Timber;


@Layout(R.layout.screen_about)
public class AboutScreen extends FlowPathBase {


    public AboutScreen() {
    }

    @Override
    public Object createComponent() {
        return DaggerAboutScreen_Component
                .builder()
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

        @BindString(R.string.about) String screenTitleString;
        @BindString(R.string.about_text) String versionUnformatted;

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
            ActionBarService
                    .get(getView())
                    .closeAppbar()
                    .setMainImage(null)
                    .setConfig(new ActionBarController.Config(screenTitleString, null));
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
