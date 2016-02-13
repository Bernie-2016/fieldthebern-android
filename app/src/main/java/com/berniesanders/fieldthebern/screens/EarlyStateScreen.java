package com.berniesanders.fieldthebern.screens;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import com.berniesanders.fieldthebern.FTBApplication;
import com.berniesanders.fieldthebern.R;
import com.berniesanders.fieldthebern.annotations.Layout;
import com.berniesanders.fieldthebern.controllers.ActionBarService;
import com.berniesanders.fieldthebern.dagger.FtbScreenScope;
import com.berniesanders.fieldthebern.dagger.MainComponent;
import com.berniesanders.fieldthebern.location.EarlyState;
import com.berniesanders.fieldthebern.mortar.ParcelableScreen;
import com.berniesanders.fieldthebern.views.EarlyStateView;
import com.f2prateek.rx.preferences.RxSharedPreferences;
import dagger.Provides;
import flow.Flow;
import flow.History;
import javax.inject.Inject;
import mortar.ViewPresenter;
import org.apache.commons.lang3.StringUtils;
import timber.log.Timber;

/**
 */
@Layout(R.layout.screen_early_state)
public class EarlyStateScreen extends ParcelableScreen {

  private final EarlyState earlyState;

  public EarlyStateScreen(EarlyState earlyState) {
    this.earlyState = earlyState;
  }

  @Override
  public Object createComponent() {
    return DaggerEarlyStateScreen_Component.builder()
        .mainComponent(FTBApplication.getComponent())
        .module(new Module(earlyState))
        .build();
  }

  @Override
  public String getScopeName() {
    return EarlyStateScreen.class.getName();
  }

  @Override protected void doWriteToParcel(Parcel parcel, int flags) {
    parcel.writeParcelable(earlyState, 0);
  }

  public static final Parcelable.Creator<EarlyStateScreen>
      CREATOR = new ParcelableScreen.ScreenCreator<EarlyStateScreen>() {
    @Override protected EarlyStateScreen doCreateFromParcel(Parcel source) {
      EarlyState earlyState = source.readParcelable(EarlyState.class.getClassLoader());
      return new EarlyStateScreen(earlyState);
    }

    @Override public EarlyStateScreen[] newArray(int size) {
      return new EarlyStateScreen[size];
    }
  };

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
      ActionBarService.get(getView()).hideToolbar();
    }

    @Override
    public void dropView(EarlyStateView view) {
      super.dropView(view);
      ButterKnife.unbind(this);
    }

    @OnClick(R.id.continueButton)
    void onContinueClick(final View v) {
      Flow.get(v).setHistory(History.single(new HomeScreen()), Flow.Direction.FORWARD);
    }

    @OnClick(R.id.call)
    void onCallClick(final View v) {
      Intent intent = new Intent(Intent.ACTION_DIAL);
      intent.setData(Uri.parse("tel:" + earlyState.fieldOffice().phone()));
      v.getContext().startActivity(intent);
    }

    @OnClick(R.id.map)
    void onMapClick(final View v) {
      Uri gmmIntentUri = Uri.parse("geo:"
          + earlyState.fieldOffice().lat()
          + ","
          + earlyState.fieldOffice().lng()
          + "?q="
          + Uri.encode(earlyState.fieldOffice().flatAddress()));
      Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
      mapIntent.setPackage("com.google.android.apps.maps");
      if (mapIntent.resolveActivity(v.getContext().getPackageManager()) != null) {
        v.getContext().startActivity(mapIntent);
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
