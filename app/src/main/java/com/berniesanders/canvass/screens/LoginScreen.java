package com.berniesanders.canvass.screens;

import android.os.Bundle;

import com.berniesanders.canvass.R;
import com.berniesanders.canvass.annotations.Layout;
import com.berniesanders.canvass.dagger.FtbScreenScope;
import com.berniesanders.canvass.controllers.ActionBarController;
import com.berniesanders.canvass.controllers.ActionBarService;
import com.berniesanders.canvass.mortar.FlowPathBase;
import com.berniesanders.canvass.views.LoginView;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.OnClick;
import flow.Flow;
import mortar.ViewPresenter;
import timber.log.Timber;

/**
 * Example for creating new Mortar Screen that helps explain how it all works
 *
 * Set the @Layout annotation to the resource id of the layout for the screen
 */
@Layout(R.layout.screen_login)
public class LoginScreen extends FlowPathBase {

    /**
     */
    public LoginScreen() {
    }

    /**
     */
    @Override
    public Object createComponent() {
        return DaggerLoginScreen_Component
                .builder()
                .build();
    }

    /**
     */
    @Override
    public String getScopeName() {
        return LoginScreen.class.getName();
    }


    @dagger.Module
    class Module {
    }

    /**
     */
    @FtbScreenScope
    @dagger.Component
    public interface Component {
        void inject(LoginView t);
    }

    @FtbScreenScope
    static public class Presenter extends ViewPresenter<LoginView> {

        @BindString(R.string.login_title) String screenTitleString;

        @Inject
        Presenter() {
        }

        @Override
        protected void onLoad(Bundle savedInstanceState) {
            Timber.v("onLoad");
            ButterKnife.bind(this, getView());
            setActionBar();
        }


        void setActionBar() {
            ActionBarService
                    .getActionbarController(getView())
                    .showToolbar()
                    .closeAppbar()
                    .lockDrawer()
                    .setMainImage(null)
                    .setConfig(new ActionBarController.Config(screenTitleString, null));
        }

        @Override
        protected void onSave(Bundle outState) {
        }

        @Override
        public void dropView(LoginView view) {
            super.dropView(view);
        }

        @OnClick(R.id.login_email)
        void loginEmail() {
            Flow.get(getView().getContext()).set(new HomeScreen());
        }
    }
}
