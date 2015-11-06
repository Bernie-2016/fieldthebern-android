package com.berniesanders.canvass.screens;

import android.os.Bundle;

import com.berniesanders.canvass.FTBApplication;
import com.berniesanders.canvass.R;
import com.berniesanders.canvass.annotations.Layout;
import com.berniesanders.canvass.dagger.FtbScreenScope;
import com.berniesanders.canvass.events.ChangePageEvent;
import com.berniesanders.canvass.events.ShowToolbarEvent;
import com.berniesanders.canvass.models.Img;
import com.berniesanders.canvass.mortar.FlowPathBase;
import com.berniesanders.canvass.views.AddAddressView;
import com.berniesanders.canvass.views.PhotoScreenView;
import com.berniesanders.canvass.views.TemplateView;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import dagger.Module;
import dagger.Provides;
import mortar.MortarScope;
import mortar.ViewPresenter;
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


        @Inject
        Presenter() {
        }

        @Override
        protected void onLoad(Bundle savedInstanceState) {
            Timber.v("onLoad");
            new ChangePageEvent()
                    .with(FTBApplication.getEventBus())
                    .close(true)
                    .hideToolbar(false)
                    .title("Add Address")
                    .remain(true)
                    .dispatch();

            new ShowToolbarEvent()
                    .with(FTBApplication.getEventBus())
                    .showToolbar(true)
                    .dispatch();


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
