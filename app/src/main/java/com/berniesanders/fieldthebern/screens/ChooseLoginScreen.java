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

import android.os.Bundle;

import com.berniesanders.fieldthebern.FTBApplication;
import com.berniesanders.fieldthebern.R;
import com.berniesanders.fieldthebern.annotations.Layout;
import com.berniesanders.fieldthebern.controllers.FacebookService;
import com.berniesanders.fieldthebern.controllers.PermissionService;
import com.berniesanders.fieldthebern.controllers.ProgressDialogService;
import com.berniesanders.fieldthebern.controllers.ToastService;
import com.berniesanders.fieldthebern.dagger.FtbScreenScope;
import com.berniesanders.fieldthebern.controllers.ActionBarController;
import com.berniesanders.fieldthebern.controllers.ActionBarService;
import com.berniesanders.fieldthebern.dagger.MainComponent;
import com.berniesanders.fieldthebern.events.LoginEvent;
import com.berniesanders.fieldthebern.exceptions.NetworkUnavailableException;
import com.berniesanders.fieldthebern.models.FacebookUser;
import com.berniesanders.fieldthebern.models.Token;
import com.berniesanders.fieldthebern.models.User;
import com.berniesanders.fieldthebern.mortar.FlowPathBase;
import com.berniesanders.fieldthebern.repositories.TokenRepo;
import com.berniesanders.fieldthebern.repositories.UserRepo;
import com.berniesanders.fieldthebern.views.ChooseLoginView;
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
import flow.History;
import mortar.ViewPresenter;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
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
                .mainComponent(FTBApplication.getComponent())
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
    @dagger.Component(dependencies = MainComponent.class)
    public interface Component {
        void inject(ChooseLoginView t);
    }

    @FtbScreenScope
    static public class Presenter extends ViewPresenter<ChooseLoginView> {

        private final Gson gson;
        private final RxSharedPreferences rxPrefs;
        private final TokenRepo tokenRepo;
        private final UserRepo userRepo;
        @BindString(R.string.login_title) String screenTitleString;
        private boolean showPleaseWait = false;

        @Inject
        Presenter(Gson gson, RxSharedPreferences rxPrefs, TokenRepo tokenRepo, UserRepo userRepo) {
            this.gson = gson;
            this.rxPrefs = rxPrefs;
            this.tokenRepo = tokenRepo;
            this.userRepo = userRepo;
        }

        @Override
        protected void onLoad(Bundle savedInstanceState) {
            Timber.v("onLoad");
            ButterKnife.bind(this, getView());
            setActionBar();
            attemptLoginViaRefresh();

            if (showPleaseWait) {
                ProgressDialogService.get(getView()).show(R.string.please_wait);
            }
        }


        void setActionBar() {
            ActionBarService
                    .get(getView())
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
            Preference<String> userPref = rxPrefs.getString(User.PREF_NAME);
            String userString = userPref.get();

            //if we have any store user info we can start with that
            User user = (userString == null) ? new User() : gson.fromJson(userString, User.class);

            ProgressDialogService.get(getView()).dismiss();
            showPleaseWait = false;
            Flow.get(getView().getContext()).set(new LoginScreen(user));
        }

        //TODO: pass a pre-crafted user?
        @OnClick(R.id.login_facebook)
        void loginFacebook() {
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
                                            
                                            if (getView() == null) { return; }

                                            User user = new User();
                                            user.getData().attributes(facebookUser.convertToApiUser());
                                            ProgressDialogService.get(getView()).dismiss();
                                            showPleaseWait = false;
                                            Flow.get(getView().getContext())
                                                    .set(new LoginScreen(user));
                                        }
                                    }
                            );
                            graphRequest.setParameters(parameters);
                            graphRequest.executeAsync();
                        }
                    });
        }

        @OnClick(R.id.no_account)
        void noAccount() {
            Flow.get(getView().getContext()).goBack();
        }

        private void attemptLoginViaRefresh() {
            //if the permission hasn't been granted the user should just login again
            if (!PermissionService.get(getView()).isGranted()) { return; }

            Preference<String> tokenPref = rxPrefs.getString(Token.PREF_NAME);

            if (tokenPref.get()==null) { return; }

            Token token = gson.fromJson(tokenPref.get(), Token.class);

            if (token == null) { return; } // if we don't have a token, we cant refresh

            tokenRepo.refresh()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(refreshObserver);

            ProgressDialogService.get(getView()).show(R.string.please_wait);
            showPleaseWait = true;
        }


        Observer<Token> refreshObserver = new Observer<Token>() {
            @Override
            public void onCompleted() {
                Timber.d("refreshObserver done.");
                if (getView() == null) {
                    return;
                }
                ProgressDialogService.get(getView()).dismiss();
                showPleaseWait = false;
            }

            @Override
            public void onError(Throwable e) {
                if (getView() == null) {
                    Timber.e(e, "refreshObserver onError");
                    return;
                }
                ProgressDialogService.get(getView()).dismiss();
                showPleaseWait = false;

                if (e instanceof NetworkUnavailableException) {
                    ToastService.get(getView())
                            .bern(getView().getResources().getString(R.string.err_internet_not_available));
                }
            }

            @Override
            public void onNext(Token token) {
                Timber.d("refreshObserver onNext: %s", token.toString());
                userRepo.getMe()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<User>() {
                            @Override
                            public void call(User user) {
                                ProgressDialogService.get(getView()).dismiss();
                                showPleaseWait = false;
                                FTBApplication.getEventBus().post(new LoginEvent(LoginEvent.LOGIN, user));
                                Flow.get(getView()).setHistory(History.single(new HomeScreen()), Flow.Direction.FORWARD);
                            }
                        });
            }
        };

    }


}
