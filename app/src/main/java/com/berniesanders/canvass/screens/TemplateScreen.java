package com.berniesanders.canvass.screens;

import android.os.Bundle;

import com.berniesanders.canvass.FTBApplication;
import com.berniesanders.canvass.R;
import com.berniesanders.canvass.annotations.Layout;
import com.berniesanders.canvass.dagger.FtbScreenScope;
import com.berniesanders.canvass.events.ChangePageEvent;
import com.berniesanders.canvass.mortar.FlowPathBase;
import com.berniesanders.canvass.views.AddAddressView;
import com.berniesanders.canvass.views.TemplateView;

import javax.inject.Inject;

import mortar.ViewPresenter;
import timber.log.Timber;

/**
 *
 */
@Layout(R.layout.screen_add_address)
public class TemplateScreen extends FlowPathBase {


    public TemplateScreen() {
    }

    @Override
    public Object createComponent() {
        return DaggerTemplateScreen_Component
                .builder()
                .build();
    }

    @Override
    public String getScopeName() {
        return TemplateScreen.class.getName();// TODO temp scope name?
    }


//    @Module
//    class Module {
//
//    }

    @FtbScreenScope
    @dagger.Component
    public interface Component {
        void inject(TemplateView t);
    }

    @FtbScreenScope
    static public class Presenter extends ViewPresenter<TemplateView> {


        @Inject
        Presenter() {
        }

        @Override
        protected void onLoad(Bundle savedInstanceState) {
            Timber.v("onLoad");
            new ChangePageEvent()
                    .with(FTBApplication.getEventBus())
                    .close(true)
                    .hideToolbar(true)
                    .dispatch();

//            new ShowToolbarEvent()
//                    .with(FTBApplication.getEventBus())
//                    .showToolbar(true)
//                    .dispatch();


        }

        @Override
        protected void onSave(Bundle outState) {
        }

        @Override
        public void dropView(TemplateView view) {
            super.dropView(view);
        }

    }
}
