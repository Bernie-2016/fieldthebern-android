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

        EarlyState earlyState = new EarlyState()
                .location(location)
                .state(stateCode)
                .offices(offices);

        if (earlyState.nearFieldOffice() || earlyState.phonebank()) {
            return new EarlyStateScreen(earlyState);
        } else {
            return new HomeScreen();
        }

    }
}
