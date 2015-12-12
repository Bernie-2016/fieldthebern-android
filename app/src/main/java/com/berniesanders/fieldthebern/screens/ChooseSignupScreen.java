package com.berniesanders.fieldthebern.screens;

import android.os.Bundle;

import com.berniesanders.fieldthebern.FTBApplication;
import com.berniesanders.fieldthebern.R;
import com.berniesanders.fieldthebern.annotations.Layout;
import com.berniesanders.fieldthebern.controllers.ActionBarController;
import com.berniesanders.fieldthebern.controllers.ActionBarService;
import com.berniesanders.fieldthebern.controllers.FacebookService;
import com.berniesanders.fieldthebern.dagger.FtbScreenScope;
import com.berniesanders.fieldthebern.dagger.MainComponent;
import com.berniesanders.fieldthebern.models.FacebookUser;
import com.berniesanders.fieldthebern.models.User;
import com.berniesanders.fieldthebern.models.UserAttributes;
import com.berniesanders.fieldthebern.mortar.FlowPathBase;
import com.berniesanders.fieldthebern.views.ChooseSignupView;
import com.f2prateek.rx.preferences.Preference;
import com.f2prateek.rx.preferences.RxSharedPreferences;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.google.gson.Gson;

import org.json.JSONObject;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.OnClick;
import flow.Flow;
import mortar.ViewPresenter;
import rx.functions.Action0;
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
                .mainComponent(FTBApplication.getComponent())
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
    @dagger.Component(dependencies = MainComponent.class)
    public interface Component {
        void inject(ChooseSignupView t);
        Gson gson();
        RxSharedPreferences rxPrefs();
    }

    @FtbScreenScope
    static public class Presenter extends ViewPresenter<ChooseSignupView> {

        private final Gson gson;
        private final RxSharedPreferences rxPrefs;
        @BindString(R.string.signup_title) String screenTitleString;

        @Inject
        Presenter(Gson gson, RxSharedPreferences rxPrefs) {
            this.gson = gson;
            this.rxPrefs = rxPrefs;
        }

        @Override
        protected void onLoad(Bundle savedInstanceState) {
            Timber.v("onLoad");
            ButterKnife.bind(this, getView());
            setActionBar();
        }


        void setActionBar() {
            ActionBarService
                    .get(getView())
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
            Flow.get(getView().getContext())
                    .set(new SignupScreen(new UserAttributes()));
        }

        @OnClick(R.id.sign_up_facebook)
        void signUpFacebook() {

            FacebookService
                    .get(getView())
                    .loginWithFacebook(new Action0() {
                        @Override
                        public void call() {
                            Timber.v("Action0.call()");

                            Bundle parameters = new Bundle();
                            parameters.putString("fields", "id,first_name,last_name,picture,email,friends");
                            GraphRequest graphRequest = GraphRequest.newMeRequest(
                                    AccessToken.getCurrentAccessToken(),
                                    new GraphRequest.GraphJSONObjectCallback() {
                                        @Override
                                        public void onCompleted(JSONObject object, GraphResponse response) {
                                            Timber.v("GraphRequest onCompleted response:%s",
                                                    response.getJSONObject().toString());
                                            FacebookUser facebookUser = gson
                                                    .fromJson(
                                                            response.getJSONObject().toString(),
                                                            FacebookUser.class);

                                            Flow.get(getView().getContext())
                                                    .set(new SignupScreen(facebookUser.convertToApiUser()));
                                        }
                                    }
                            );
                            graphRequest.setParameters(parameters);
                            graphRequest.executeAsync();
                        }
                    });


        }

        @OnClick(R.id.have_an_account)
        void haveAccount() {

            Preference<String> userPref = rxPrefs.getString(User.PREF_NAME);
            String userString = userPref.get();

            //if we have any store user info we can start with that
            User user = (userString == null) ? new User() : gson.fromJson(userString, User.class);

            Flow.get(getView().getContext()).set(new LoginScreen(user));
        }

    }
}
