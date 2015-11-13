package com.berniesanders.canvass.screens;

import android.os.Bundle;

import com.berniesanders.canvass.R;
import com.berniesanders.canvass.annotations.Layout;
import com.berniesanders.canvass.dagger.FtbScreenScope;
import com.berniesanders.canvass.controllers.ActionBarController;
import com.berniesanders.canvass.controllers.ActionBarService;
import com.berniesanders.canvass.mortar.FlowPathBase;
import com.berniesanders.canvass.views.ChooseSignupView;

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
@Layout(R.layout.screen_choose_signup)
public class ChooseSignupScreen extends FlowPathBase {

    /**
     */
    public ChooseSignupScreen() {
    }

    /**
     */
    @Override
    public Object createComponent() {
        return DaggerChooseSignupScreen_Component
                .builder()
                .build();
    }

    /**
     */
    @Override
    public String getScopeName() {
        return ChooseSignupScreen.class.getName();
    }


    @dagger.Module
    class Module {
    }

    /**
     */
    @FtbScreenScope
    @dagger.Component
    public interface Component {
        void inject(ChooseSignupView t);
    }

    @FtbScreenScope
    static public class Presenter extends ViewPresenter<ChooseSignupView> {

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
                    .lockDrawer()
                    .closeAppbar()
                    .setMainImage(null)
                    .setConfig(new ActionBarController.Config(screenTitleString, null));
        }

        @Override
        protected void onSave(Bundle outState) {
        }

        @Override
        public void dropView(ChooseSignupView view) {
            super.dropView(view);
            ButterKnife.unbind(this);
        }

        @OnClick(R.id.sign_up_email)
        void signUpEmail() {
            Flow.get(getView().getContext()).set(new SignupScreen());
        }

        @OnClick(R.id.sign_up_facebook)
        void signUpFacebook() {
            Flow.get(getView().getContext()).set(new SignupScreen());
        }

        @OnClick(R.id.have_an_account)
        void haveAccount() {
            Flow.get(getView().getContext()).set(new ChooseLoginScreen());
        }
    }
}
