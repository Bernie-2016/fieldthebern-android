package com.berniesanders.fieldthebern.screens;

import android.location.Address;
import android.os.Bundle;

import com.berniesanders.fieldthebern.FTBApplication;
import com.berniesanders.fieldthebern.R;
import com.berniesanders.fieldthebern.annotations.Layout;
import com.berniesanders.fieldthebern.controllers.ActionBarController;
import com.berniesanders.fieldthebern.controllers.ActionBarService;
import com.berniesanders.fieldthebern.controllers.DialogController.DialogAction;
import com.berniesanders.fieldthebern.controllers.DialogController.DialogConfig;
import com.berniesanders.fieldthebern.controllers.DialogService;
import com.berniesanders.fieldthebern.controllers.ProgressDialogService;
import com.berniesanders.fieldthebern.controllers.ToastService;
import com.berniesanders.fieldthebern.dagger.FtbScreenScope;
import com.berniesanders.fieldthebern.dagger.MainComponent;
import com.berniesanders.fieldthebern.exceptions.AuthFailRedirect;
import com.berniesanders.fieldthebern.location.StateConverter;
import com.berniesanders.fieldthebern.models.ApiAddress;
import com.berniesanders.fieldthebern.models.ErrorResponse;
import com.berniesanders.fieldthebern.parsing.ErrorResponseParser;
import com.berniesanders.fieldthebern.models.CanvassData;
import com.berniesanders.fieldthebern.models.Person;
import com.berniesanders.fieldthebern.models.RequestSingleAddress;
import com.berniesanders.fieldthebern.models.SingleAddressResponse;
import com.berniesanders.fieldthebern.mortar.FlowPathBase;
import com.berniesanders.fieldthebern.parsing.PartyEvaluator;
import com.berniesanders.fieldthebern.repositories.AddressRepo;
import com.berniesanders.fieldthebern.repositories.VisitRepo;
import com.berniesanders.fieldthebern.repositories.specs.AddressSpec;
import com.berniesanders.fieldthebern.views.AddAddressView;
import com.bugsnag.android.Bugsnag;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

import javax.inject.Inject;

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
 *
 */
@Layout(R.layout.screen_add_address)
public class AddAddressScreen extends FlowPathBase {


    private final ApiAddress address;

    public AddAddressScreen(ApiAddress address) {
        this.address = address;
    }

    @Override
    public Object createComponent() {
        return DaggerAddAddressScreen_Component
                .builder()
                .mainComponent(FTBApplication.getComponent())
                .module(new Module(address))
                .build();
    }

    @Override
    public String getScopeName() {
        return AddAddressScreen.class.getName();// TODO temp scope name?
    }


    @dagger.Module
    class Module {

        private final ApiAddress address;

        Module(ApiAddress address) {
            this.address = address;
        }

        @Provides
        @FtbScreenScope
        public ApiAddress provideAddress() {
            return address;
        }
    }

    @FtbScreenScope
    @dagger.Component(dependencies = MainComponent.class, modules = Module.class)
    public interface Component {
        void inject(AddAddressView t);
        ApiAddress address();
        AddressRepo addressRepo();
        ErrorResponseParser errorResponseParser();
        VisitRepo visitRepo();
    }

    @FtbScreenScope
    static public class Presenter extends ViewPresenter<AddAddressView> {

        private ApiAddress address;
        private final AddressRepo addressRepo;
        private final ErrorResponseParser errorResponseParser;
        private final VisitRepo visitRepo;
        @BindString(android.R.string.cancel) String cancel;

        @BindString(R.string.add_address) String screenTitle;
        @BindString(R.string.are_you_sure_address_correct) String confirmTitle;
        @BindString(R.string.are_you_sure_address_body) String confirmBody;
        @BindString(R.string.err_address_blank) String addressBlank;


        @Inject
        Presenter(ApiAddress address, AddressRepo addressRepo, ErrorResponseParser errorResponseParser, VisitRepo visitRepo) {
            this.address = address;
            this.addressRepo = addressRepo;
            this.errorResponseParser = errorResponseParser;
            this.visitRepo = visitRepo;
        }

        @Override
        protected void onLoad(Bundle savedInstanceState) {
            Timber.v("onLoad");
            ButterKnife.bind(this, getView());
            setActionBar();
            getView().setAddress(address);
        }


        void setActionBar() {
            ActionBarController.MenuAction menu =
                    new ActionBarController.MenuAction()
                            .label(cancel)
                            .action(new Action0() {
                                @Override
                                public void call() {
                                    if (getView()!=null) {
                                        visitRepo.clear();
                                        Flow.get(getView()).setHistory(History.single(new HomeScreen()), Flow.Direction.BACKWARD);
                                    }
                                }
                            });
            ActionBarService
                    .get(getView())
                    .showToolbar()
                    .closeAppbar()
                    .setMainImage(null)
                    .setConfig(new ActionBarController.Config(screenTitle, menu));
        }

        @Override
        protected void onSave(Bundle outState) {
        }

        @Override
        public void dropView(AddAddressView view) {
            super.dropView(view);
            ButterKnife.unbind(this);
            address = view.getAddress(); //grab the address in case the user is rotating
        }


        @OnClick(R.id.submit)
        public void startNewVisit() {
            address = getView().getAddress();
            if(!formIsValid()) { return; }

            String apartment = (address.attributes().street2() == null) ? "" : address.attributes().street2();

            String formattedAddress = String.format(
                    confirmBody,
                    address.attributes().street1(),
                    apartment);

            DialogAction confirmAction = new DialogAction()
                    .label(android.R.string.ok)
                    .action(new Action0() {
                        @Override
                        public void call() {
                            Timber.d("ok button click");
                            loadAddressFromApi();
                        }
                    });

            DialogAction cancelAction = new DialogAction()
                    .label(android.R.string.cancel)
                    .action(new Action0() {
                        @Override
                        public void call() {
                            Timber.d("cancel button click");
                        }
                    });

            DialogService
                    .get(getView())
                    .setDialogConfig(
                        new DialogConfig()
                                .title(confirmTitle)
                                .message(formattedAddress)
                                .withActions(confirmAction, cancelAction)
                    );
        }

        private boolean formIsValid() {
            if (StringUtils.isBlank(address.attributes().street1())) {
                ToastService.get(getView()).bern(addressBlank);
                return false;
            }
            return true;
        }

        private void loadAddressFromApi() {

            Subscription singleAddressSubscription =
                    addressRepo.getSingle(new AddressSpec().singleAddress(new RequestSingleAddress(address)))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(singleAddressObserver);

            ProgressDialogService.get(getView()).show(R.string.please_wait);
        }

        Observer<SingleAddressResponse> singleAddressObserver = new Observer<SingleAddressResponse>() {
            @Override
            public void onCompleted() {
                Timber.v("singleAddressObserver onCompleted");
                ProgressDialogService.get(getView()).dismiss();
            }


            @Override
            public void onError(Throwable e) {
                ProgressDialogService.get(getView()).dismiss();

                if (AuthFailRedirect.redirectOnFailure(e, getView())) {
                    return;
                }

                if (e instanceof HttpException) {

                    HttpException httpe = (HttpException) e;
                    ErrorResponse errorResponse = errorResponseParser.parse((HttpException) e);
                    if (httpe.code() == 404) {
                        //address was not found in db, proceed with visit
                        Flow.get(getView()).set(new NewVisitScreen(address));

                    } else if (httpe.code() == 400) {
                        //address was found in db, but not specific enough
                        //ask user for more info
                        //TODO probably better to show a dialog here than a bern
                        ToastService.get(getView()).bern(errorResponse.getAllDetails());
                    } else {
                        ToastService.get(getView()).bern(errorResponse.getAllDetails());
                        Timber.e(e, "singleAddressObserver onError");
                    }
                } else {
                    //wtf
                    ToastService.get(getView()).bern("unknown error");//TODO externalize string
                    Timber.e(e, "singleAddressObserver unknown onError");
                    Bugsnag.notify(e);
                }
            }


            @Override
            public void onNext(SingleAddressResponse response) {
                Timber.v("singleAddressObserver onNext  response.addresses().get(0) =\n%s", response.addresses().get(0) );
                address = response.addresses().get(0);
                address.included(response.included());

                //TODO this is hack because API party values are not the same when outputted as they are input
                for(CanvassData canvassData : address.included()) {
                    if (canvassData.type().equals(Person.TYPE)) {
                        Person person = (Person) canvassData;
                        if (person.attributes().party() !=null) {
                            person.attributes().party(
                                    PartyEvaluator.mapApiParty(
                                            person.attributes().party()));
                        }
                    }
                }
                Flow.get(getView()).set(new NewVisitScreen(address));
            }
        };
    }
}
