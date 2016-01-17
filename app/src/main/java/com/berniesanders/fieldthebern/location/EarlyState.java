package com.berniesanders.fieldthebern.location;

import android.location.Location;
import android.support.annotation.StringDef;

import com.berniesanders.fieldthebern.models.FieldOffice;
import com.berniesanders.fieldthebern.models.FieldOfficeList;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/*
    the phonebank popup should go to people who are NOT in one of these states:
    AL, AR, AZ, CO, IA, MA, ME, MN, NH, NV, SC, VA, TX
 */
public class EarlyState {

    public static final String PHONEBANK = "phonebank";
    public static final String FIELD_OFFICE = "field_office";

    private FieldOffice fieldOffice;
    private Location location;
    private String state; //Two letter state code
    private FieldOfficeList offices;

    @ScreenType
    String type;

    @StringDef({PHONEBANK, FIELD_OFFICE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ScreenType {}


    public boolean nearFieldOffice() {

        return false;
    }


    /**
     * Returns if the "hey you should phonebank" screen should be shown to the user
     */
    public boolean phonebank() {

        switch (state) {
            case "AL":
            case "AR":
            case "AZ":
            case "CO":
            case "IA":
            case "MA":
            case "ME":
            case "MN":
            case "NH":
            case "NV":
            case "SC":
            case "VA":
            case "TX":
                return false;
            default:
                return true;
        }
    }

    public FieldOffice fieldOffice() {
        return this.fieldOffice;
    }

    public Location location() {
        return this.location;
    }

    /**
     * Two letter state code
     */
    public String state() {
        return this.state;
    }

    public FieldOfficeList offices() {
        return this.offices;
    }

    @ScreenType
    public String type() {
        return this.type;
    }

    public EarlyState fieldOffice(final FieldOffice fieldOffice) {
        this.fieldOffice = fieldOffice;
        return this;
    }

    public EarlyState location(final Location location) {
        this.location = location;
        return this;
    }

    /**
     * Two letter state code
     */
    public EarlyState state(final String state) {
        this.state = state;
        return this;
    }

    public EarlyState offices(final FieldOfficeList offices) {
        this.offices = offices;
        return this;
    }

    public EarlyState type(@ScreenType final String type) {
        this.type = type;
        return this;
    }


}
