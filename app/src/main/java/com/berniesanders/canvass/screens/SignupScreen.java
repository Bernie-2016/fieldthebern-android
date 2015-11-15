package com.berniesanders.canvass.screens;

import android.os.Bundle;

import com.berniesanders.canvass.R;
import com.berniesanders.canvass.annotations.Layout;
import com.berniesanders.canvass.dagger.FtbScreenScope;
import com.berniesanders.canvass.controllers.ActionBarController;
import com.berniesanders.canvass.controllers.ActionBarService;
import com.berniesanders.canvass.mortar.FlowPathBase;
import com.berniesanders.canvass.views.SignupView;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.ButterKnife;
import mortar.ViewPresenter;
import timber.log.Timber;

/**
 * Example for creating new Mortar Screen that helps explain how it all works
 *
 * Set the @Layout annotation to the resource id of the layout for the screen
 */
@Layout(R.layout.screen_signup)
public class SignupScreen extends FlowPathBase {

    /**
     */
    public SignupScreen() {
    }

    /**
     */
    @Override
    public Object createComponent() {
        return DaggerSignupScreen_Component
                .builder()
                .build();
    }

    /**
     */
    @Override
    public String getScopeName() {
        return SignupScreen.class.getName();
    }


    @dagger.Module
    class Module {
    }

    /**
     */
    @FtbScreenScope
    @dagger.Component
    public interface Component {
        void inject(SignupView t);
    }

    @FtbScreenScope
    static public class Presenter extends ViewPresenter<SignupView> {

        @BindString(R.string.signup_title) String screenTitleString;

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
        public void dropView(SignupView view) {
            super.dropView(view);
        }

    }
}
