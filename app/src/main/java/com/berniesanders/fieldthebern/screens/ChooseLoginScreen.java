package com.berniesanders.fieldthebern.screens;

import android.os.Bundle;

import com.berniesanders.fieldthebern.R;
import com.berniesanders.fieldthebern.annotations.Layout;
import com.berniesanders.fieldthebern.dagger.FtbScreenScope;
import com.berniesanders.fieldthebern.controllers.ActionBarController;
import com.berniesanders.fieldthebern.controllers.ActionBarService;
import com.berniesanders.fieldthebern.models.User;
import com.berniesanders.fieldthebern.mortar.FlowPathBase;
import com.berniesanders.fieldthebern.views.ChooseLoginView;

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
@Layout(R.layout.screen_choose_login)
public class ChooseLoginScreen extends FlowPathBase {

    /**
     */
    public ChooseLoginScreen() {
    }

    /**
     */
    @Override
    public Object createComponent() {
        return DaggerChooseLoginScreen_Component
                .builder()
                .build();
    }

    /**
     */
    @Override
    public String getScopeName() {
        return ChooseLoginScreen.class.getName();
    }


    @dagger.Module
    class Module {
    }

    /**
     */
    @FtbScreenScope
    @dagger.Component
    public interface Component {
        void inject(ChooseLoginView t);
    }

    @FtbScreenScope
    static public class Presenter extends ViewPresenter<ChooseLoginView> {

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
        public void dropView(ChooseLoginView view) {
            super.dropView(view);
            ButterKnife.unbind(this);
        }

        @OnClick(R.id.login_email)
        void loginEmail() {
            //TODO see if there is already a saved user?
            Flow.get(getView().getContext()).set(new LoginScreen(new User()));
        }

        //TODO: pass a pre-crafted user?
        @OnClick(R.id.login_facebook)
        void loginFacebook() {
            Flow.get(getView().getContext()).set(new LoginScreen(null));
        }

        @OnClick(R.id.no_account)
        void noAccount() {
            Flow.get(getView().getContext()).set(new ChooseSignupScreen());
        }

    }


}
