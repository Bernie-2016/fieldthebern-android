package com.berniesanders.fieldthebern.screens;

import android.location.Address;
import android.os.Bundle;

import com.berniesanders.fieldthebern.FTBApplication;
import com.berniesanders.fieldthebern.R;
import com.berniesanders.fieldthebern.annotations.Layout;
import com.berniesanders.fieldthebern.controllers.ActionBarController;
import com.berniesanders.fieldthebern.controllers.ActionBarService;
import com.berniesanders.fieldthebern.controllers.DialogController;
import com.berniesanders.fieldthebern.controllers.DialogController.DialogAction;
import com.berniesanders.fieldthebern.controllers.DialogController.DialogConfig;
import com.berniesanders.fieldthebern.controllers.DialogService;
import com.berniesanders.fieldthebern.dagger.FtbScreenScope;
import com.berniesanders.fieldthebern.dagger.MainComponent;
import com.berniesanders.fieldthebern.mortar.FlowPathBase;
import com.berniesanders.fieldthebern.views.AddAddressView;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.Provides;
import flow.Flow;
import flow.History;
import mortar.ViewPresenter;
import rx.functions.Action0;
import timber.log.Timber;

/**
 *
 */
@Layout(R.layout.screen_add_address)
public class AddAddressScreen extends FlowPathBase {


    private final Address address;

    public AddAddressScreen(Address address) {
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

        private final Address address;

        Module(Address address) {
            this.address = address;
        }

        @Provides
        @FtbScreenScope
        public Address provideAddress() {
            return address;
        }
    }

    @FtbScreenScope
    @dagger.Component(dependencies = MainComponent.class, modules = Module.class)
    public interface Component {
        void inject(AddAddressView t);
        Address address();
    }

    @FtbScreenScope
    static public class Presenter extends ViewPresenter<AddAddressView> {

        private Address address;
        @BindString(android.R.string.cancel) String cancel;

        @BindString(R.string.add_address) String screenTitle;
        @BindString(R.string.are_you_sure_address_correct) String confirmTitle;
        @BindString(R.string.are_you_sure_address_body) String confirmBody;


        @Inject
        Presenter(Address address) {
            this.address = address;
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
                                        Flow.get(getView()).setHistory(History.single(new Main()), Flow.Direction.BACKWARD);
                                    }
                                }
                            });
            ActionBarService
                    .getActionbarController(getView())
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

            String formattedAddress = String.format(
                    confirmBody,
                    address.getAddressLine(0),
                    address.getAddressLine(1));

            DialogAction confirmAction = new DialogAction()
                    .label(android.R.string.ok)
                    .action(new Action0() {
                        @Override
                        public void call() {
                            Timber.d("ok button click");
                            Flow.get(getView()).set(new NewVisitScreen());
                        }
                    });

            DialogAction cancelAction = new DialogAction()
                    .label(android.R.string.cancel)
                    .setAsDismiss()
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
    }
}
