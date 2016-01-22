package com.berniesanders.fieldthebern.screens;

import android.location.Location;
import com.berniesanders.fieldthebern.location.EarlyState;
import com.berniesanders.fieldthebern.models.FieldOfficeList;
import com.berniesanders.fieldthebern.repositories.FieldOfficeRepo;
import com.f2prateek.rx.preferences.RxSharedPreferences;
import javax.inject.Inject;

public class MessageScreen {

  private final FieldOfficeRepo fieldOfficeRepo;
  private final RxSharedPreferences rxPrefs;

  @Inject
  public MessageScreen(FieldOfficeRepo fieldOfficeRepo, RxSharedPreferences rxPrefs) {

    this.fieldOfficeRepo = fieldOfficeRepo;
    this.rxPrefs = rxPrefs;
  }

  public Object getMessageOrHome(Location location, String stateCode) {

    //TODO this blocking will need to be fixed if the data is loaded from the web
    FieldOfficeList offices = fieldOfficeRepo.get().toBlocking().single();

    EarlyState earlyState = new EarlyState().location(location).state(stateCode).offices(offices);

    boolean showFieldOffice = rxPrefs.getBoolean(EarlyState.PREF_SHOW_FIELD_OFFICE, true).get();
    boolean showPhonebank = rxPrefs.getBoolean(EarlyState.PREF_SHOW_PHONEBANK, true).get();

    if (earlyState.isNear() && showFieldOffice) {
      earlyState.type(EarlyState.FIELD_OFFICE);
      return new EarlyStateScreen(earlyState);
    } else if (earlyState.phonebank() && showPhonebank) {
      earlyState.type(EarlyState.PHONEBANK);
      return new EarlyStateScreen(earlyState);
    } else {
      return new HomeScreen();
    }
  }
}
