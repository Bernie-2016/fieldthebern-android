package com.berniesanders.canvass.screens;

import android.os.Bundle;

import com.berniesanders.canvass.R;
import com.berniesanders.canvass.annotations.Layout;
import com.berniesanders.canvass.dagger.FtbScreenScope;
import com.berniesanders.canvass.mortar.ActionBarController;
import com.berniesanders.canvass.mortar.ActionBarService;
import com.berniesanders.canvass.mortar.FlowPathBase;
import com.berniesanders.canvass.views.AddAddressView;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.ButterKnife;
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


    public AddAddressScreen() {
    }

    @Override
    public Object createComponent() {
        return DaggerAddAddressScreen_Component
                .builder()
                .build();
    }

    @Override
    public String getScopeName() {
        return AddAddressScreen.class.getName();// TODO temp scope name?
    }


//    @Module
//    class Module {
//
//    }

    @FtbScreenScope
    @dagger.Component
    public interface Component {
        void inject(AddAddressView t);
    }

    @FtbScreenScope
    static public class Presenter extends ViewPresenter<AddAddressView> {

        @BindString(android.R.string.cancel) String cancel;

        @BindString(R.string.add_address) String addAddress;


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
                    .setConfig(new ActionBarController.Config(addAddress, menu));
        }

        @Override
        protected void onSave(Bundle outState) {
        }

        @Override
        public void dropView(AddAddressView view) {
            super.dropView(view);
        }

    }
}
