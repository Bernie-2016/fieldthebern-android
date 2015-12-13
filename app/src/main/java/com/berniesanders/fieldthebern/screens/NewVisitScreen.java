package com.berniesanders.fieldthebern.screens;

import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.widget.CompoundButton;

import com.berniesanders.fieldthebern.FTBApplication;
import com.berniesanders.fieldthebern.R;
import com.berniesanders.fieldthebern.annotations.Layout;
import com.berniesanders.fieldthebern.controllers.ProgressDialogService;
import com.berniesanders.fieldthebern.controllers.ToastService;
import com.berniesanders.fieldthebern.dagger.FtbScreenScope;
import com.berniesanders.fieldthebern.controllers.ActionBarController;
import com.berniesanders.fieldthebern.controllers.ActionBarService;
import com.berniesanders.fieldthebern.dagger.MainComponent;
import com.berniesanders.fieldthebern.exceptions.AuthFailRedirect;
import com.berniesanders.fieldthebern.models.ApiAddress;
import com.berniesanders.fieldthebern.models.CanvassResponse;
import com.berniesanders.fieldthebern.models.ErrorResponse;
import com.berniesanders.fieldthebern.models.VisitResult;
import com.berniesanders.fieldthebern.mortar.FlowPathBase;
import com.berniesanders.fieldthebern.parsing.ErrorResponseParser;
import com.berniesanders.fieldthebern.repositories.VisitRepo;
import com.berniesanders.fieldthebern.views.NewVisitView;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.Provides;
import flow.Flow;
import flow.History;
import mortar.ViewPresenter;
import retrofit.HttpException;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Example for creating new Mortar Screen that helps explain how it all works
 *
 * Set the @Layout annotation to the resource id of the layout for the screen
 */
@Layout(R.layout.screen_new_visit)
public class NewVisitScreen extends FlowPathBase {


    private final ApiAddress apiAddress;

    /**
     */
    public NewVisitScreen(ApiAddress apiAddress) {
        this.apiAddress = apiAddress;
    }

    /**
     * Create the component defined as an inner class below.
     * This component will inject the presenter on the view, and dependencies/module on the presenter.
     * You can pass data (someData) from the Screen to its Presenter through this component.
     * Remember you must run the gradle 'build' class for Dagger to generate to component code
     */
    @Override
    public Object createComponent() {
        return DaggerNewVisitScreen_Component
                .builder()
                .mainComponent(FTBApplication.getComponent())
                .module(new Module(apiAddress))
                .build();
    }

    /**
     * Important: make this unique to the data you want cages or displayed.
     * If you show "someData" then add "someData.hashCode()" to the end of this method
     * This will define the uniqueness that mortar and flow will see this screen for comparisons
     */
    @Override
    public String getScopeName() {
        return NewVisitScreen.class.getName();
    }


    @dagger.Module
    class Module {
        private final ApiAddress apiAddress;

        Module(ApiAddress apiAddress) {
            this.apiAddress = apiAddress;
        }

        @Provides
        @FtbScreenScope
        public ApiAddress provideApiAddress() {
            return apiAddress;
        }
    }

    /**
     */
    @FtbScreenScope
    @dagger.Component(dependencies = MainComponent.class, modules = Module.class)
    public interface Component {
        void inject(NewVisitView t);
        ApiAddress apiAddress();
        VisitRepo visitRepo();
        ErrorResponseParser errorResponseParser();
    }

    @FtbScreenScope
    static public class Presenter extends ViewPresenter<NewVisitView> {

        private final ApiAddress apiAddress;
        private final VisitRepo visitRepo;
        private final ErrorResponseParser errorResponseParser;
        Subscription visitSubscription;

        @BindString(android.R.string.cancel) String cancel;
        @BindString(R.string.new_visit) String newVisit;

        boolean noOneHome = false;
        boolean askedToLeave = false;

        @Bind(R.id.no_one_home)
        SwitchCompat noOneHomeSwitch;

        @Bind(R.id.asked_to_leave)
        SwitchCompat askedToLeaveSwitch;

        @Inject
        Presenter(ApiAddress apiAddress, VisitRepo visitRepo, ErrorResponseParser errorResponseParser) {
            this.apiAddress = apiAddress;
            this.visitRepo = visitRepo;
            this.errorResponseParser = errorResponseParser;

            if (!visitRepo.inProgress()) {
                visitRepo.start(apiAddress);
            }

        }

        @Override
        protected void onLoad(Bundle savedInstanceState) {
            Timber.v("onLoad");
            ButterKnife.bind(this, getView());
            setActionBar();
            initSwitches();
            setSwitchListeners();
            getView().showPeople(visitRepo.get());
        }

        /**
         * If user rotated the device, be sure the switches match our boolean values
         */
        private void initSwitches() {
            noOneHomeSwitch.setChecked(noOneHome);
            askedToLeaveSwitch.setChecked(askedToLeave);
        }

        private void setSwitchListeners() {
            noOneHomeSwitch.setOnCheckedChangeListener(noOneHomeListener);
            askedToLeaveSwitch.setOnCheckedChangeListener(askedToLeaveListener);
        }

        private void clearSwitchListeners() {
            noOneHomeSwitch.setOnCheckedChangeListener(null);
            askedToLeaveSwitch.setOnCheckedChangeListener(null);
        }

        CompoundButton.OnCheckedChangeListener noOneHomeListener =
                new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                noOneHome = isChecked;

                //if no one was home, can't have been asked to leave
                if (isChecked) {
                    clearSwitchListeners();
                    askedToLeaveSwitch.setChecked(false);
                    askedToLeave = false;
                    setSwitchListeners();
                }
            }
        };

        CompoundButton.OnCheckedChangeListener askedToLeaveListener =
                new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                askedToLeave = isChecked;

                //if asked to leave, someone must have been home
                if (isChecked) {
                    clearSwitchListeners();
                    noOneHomeSwitch.setChecked(false);
                    noOneHome = false;
                    setSwitchListeners();
                }
            }
        };

        void setActionBar() {
            ActionBarController.MenuAction menu =
                    new ActionBarController.MenuAction()
                            .label(cancel)
                            .action(new Action0() {
                                @Override
                                public void call() {
                                    Flow.get(getView()).setHistory(History.single(new Main()), Flow.Direction.BACKWARD);
                                    visitRepo.clear();
                                }
                            });
            ActionBarService
                    .get(getView())
                    .showToolbar()
                    .closeAppbar()
                    .setMainImage(null)
                    .setConfig(new ActionBarController.Config(apiAddress.attributes().street1(), menu));
        }

        @Override
        protected void onSave(Bundle outState) {
        }

        @Override
        public void dropView(NewVisitView view) {
            super.dropView(view);
            clearSwitchListeners();
            ButterKnife.unbind(this);
        }

        @OnClick(R.id.add_person)
        public void addPerson() {
            Flow.get(getView()).set(new AddPersonScreen(null));
        }

        @OnClick(R.id.submit)
        public void score() {
            ProgressDialogService.get(getView()).show(R.string.please_wait);
            if(noOneHome) {
                //the first item in the included() array is the address
                ((ApiAddress) visitRepo.get().included().get(0))
                        .attributes()
                        .bestCanvassResponse(CanvassResponse.NO_ONE_HOME);
            } else if(askedToLeave) {
                //the first item in the included() array is the address
                ((ApiAddress) visitRepo.get().included().get(0))
                        .attributes()
                        .bestCanvassResponse(CanvassResponse.ASKED_TO_LEAVE);
            }

            visitSubscription = visitRepo.submit()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(visitResultObserver);
        }

        Observer<VisitResult> visitResultObserver = new Observer<VisitResult>() {
            @Override
            public void onCompleted() {
                Timber.v("visitResultObserver.onCompleted");
                ProgressDialogService.get(getView()).dismiss();
            }

            @Override
            public void onError(Throwable e) {
                Timber.e(e, "error submitting visit");
                ProgressDialogService.get(getView()).dismiss();
                if (AuthFailRedirect.redirectOnFailure(e, getView())) {
                    return;
                }

                if (e instanceof HttpException) {
                    ErrorResponse errorResponse = errorResponseParser.parse((HttpException) e);
                    ToastService.get(getView()).bern(errorResponse.getAllDetails());
                }
            }

            @Override
            public void onNext(VisitResult visitResult) {
                Flow.get(getView()).set(new ScoreScreen(visitResult, visitRepo.get()));
                visitRepo.clear();
            }
        };
    }
}
