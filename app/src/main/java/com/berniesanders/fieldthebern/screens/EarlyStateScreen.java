package com.berniesanders.fieldthebern.screens;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.TextView;

import com.berniesanders.fieldthebern.FTBApplication;
import com.berniesanders.fieldthebern.R;
import com.berniesanders.fieldthebern.annotations.Layout;
import com.berniesanders.fieldthebern.controllers.ActionBarService;
import com.berniesanders.fieldthebern.dagger.FtbScreenScope;
import com.berniesanders.fieldthebern.dagger.MainComponent;
import com.berniesanders.fieldthebern.location.EarlyState;
import com.berniesanders.fieldthebern.models.FieldOffice;
import com.berniesanders.fieldthebern.mortar.FlowPathBase;
import com.berniesanders.fieldthebern.repositories.FieldOfficeRepo;
import com.berniesanders.fieldthebern.views.EarlyStateView;
import com.berniesanders.fieldthebern.views.ExampleView;
import com.f2prateek.rx.preferences.RxSharedPreferences;

import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import dagger.Provides;
import flow.Flow;
import flow.History;
import mortar.ViewPresenter;
import timber.log.Timber;

/**
 */
@Layout(R.layout.screen_early_state)
public class EarlyStateScreen extends FlowPathBase {


    private final EarlyState earlyState;

    public EarlyStateScreen(EarlyState earlyState) {
        this.earlyState = earlyState;
    }

    @Override
    public Object createComponent() {
        return DaggerEarlyStateScreen_Component
                .builder()
                .mainComponent(FTBApplication.getComponent())
                .module(new Module(earlyState))
                .build();
    }

    @Override
    public String getScopeName() {
        return EarlyStateScreen.class.getName();
    }


    @dagger.Module
    class Module {

        private final EarlyState earlyState;

        public Module(EarlyState earlyState) {
            this.earlyState = earlyState;
        }

        @Provides
        @FtbScreenScope
        EarlyState provideEarlyState() {
            return earlyState;
        }
    }

    @FtbScreenScope
    @dagger.Component(dependencies = MainComponent.class, modules = Module.class)
    public interface Component {
        void inject(EarlyStateView t);
    }

    @FtbScreenScope
    static public class Presenter extends ViewPresenter<EarlyStateView> {


        private final EarlyState earlyState;
        private final RxSharedPreferences rxPrefs;

        @Bind(R.id.field_office_container)
        View fieldOfficeContainer;

        @Bind(R.id.phonebank_container)
        View phonebankContainer;

        @Bind(R.id.field_office_address)
        TextView address;

        @Bind(R.id.call)
        AppCompatButton callButton;

        @Bind(R.id.map)
        AppCompatButton mapButton;

        @Inject
        Presenter(EarlyState earlyState, RxSharedPreferences rxPrefs) {
            this.earlyState = earlyState;
            this.rxPrefs = rxPrefs;
        }

        @Override
        protected void onLoad(Bundle savedInstanceState) {
            Timber.v("onLoad");
            ButterKnife.bind(this, getView());
            setActionBar();

            if (earlyState.type().equals(EarlyState.PHONEBANK)) {
                phonebankContainer.setVisibility(View.VISIBLE);
                fieldOfficeContainer.setVisibility(View.GONE);
            } else if (earlyState.type().equals(EarlyState.FIELD_OFFICE)) {
                phonebankContainer.setVisibility(View.GONE);
                fieldOfficeContainer.setVisibility(View.VISIBLE);

                address.setText(earlyState.fieldOffice().fullAddress());

                if (StringUtils.isBlank(earlyState.fieldOffice().phone())) {
                    callButton.setVisibility(View.GONE);
                }
            }
        }

        void setActionBar() {
            ActionBarService
                    .get(getView())
                    .hideToolbar();
        }


        @Override
        public void dropView(EarlyStateView view) {
            super.dropView(view);
            ButterKnife.unbind(this);
        }

        @OnClick(R.id.continueButton)
        void onContinueClick() {
            Flow.get(getView()).setHistory(History.single(new HomeScreen()), Flow.Direction.FORWARD);
        }

        @OnClick(R.id.call)
        void onCallClick() {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:"+earlyState.fieldOffice().phone()));
            getView().getContext().startActivity(intent);
        }

        @OnClick(R.id.map)
        void onMapClick() {
            Uri gmmIntentUri = Uri.parse("geo:"
                    + earlyState.fieldOffice().lat()
                    + ","
                    + earlyState.fieldOffice().lng()
                    + "?q="
                    + Uri.encode(earlyState.fieldOffice().flatAddress()));
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            if (mapIntent.resolveActivity(getView().getContext().getPackageManager()) != null) {
                getView().getContext().startActivity(mapIntent);
            }

        }

        @OnCheckedChanged(R.id.dont_show_again)
        void onDontShowAgainChecked(boolean checked) {

            if (earlyState.type().equals(EarlyState.FIELD_OFFICE)) {
                rxPrefs.getBoolean(EarlyState.PREF_SHOW_FIELD_OFFICE, true).set(!checked);
            } else {
                rxPrefs.getBoolean(EarlyState.PREF_SHOW_PHONEBANK, true).set(!checked);
            }
        }

    }
}
