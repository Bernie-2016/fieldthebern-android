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

  public static final String PREF_SHOW_PHONEBANK = "pref_show_phonebank";
  public static final String PREF_SHOW_FIELD_OFFICE = "pref_show_field_office";

  /**
   * The distance we consider close enough that the user may way to travel to the field office
   * ~50 miles in meters
   */
  public static final int MAX_DISTANCE_TO_OFFICE = 80467;

  private FieldOffice closestOffice;
  private Location location;
  private String state; //Two letter state code
  private FieldOfficeList offices;
  private float distance;

  @ScreenType String type;

  @StringDef({ PHONEBANK, FIELD_OFFICE }) @Retention(RetentionPolicy.SOURCE)
  public @interface ScreenType {
  }

  public boolean isNear() {

    if (location == null) {
      return false;
    }

    if (closestOffice == null) {
      closestOffice = nearestOffice();
    }

    if (inState() || closestOffice.distance() < MAX_DISTANCE_TO_OFFICE) {
      type(FIELD_OFFICE);
      return true;
    }

    return false;
  }

  private boolean inState() {

    if (state == null) {
      return false;
    }

    for (FieldOffice office : offices) {
      if (state.equals(office.state())) {
        return true;
      }
    }
    return false;
  }

  private FieldOffice nearestOffice() {

    FieldOffice nearest = null;
    float shortestDistance = 0f;

    for (FieldOffice office : offices) {

      float[] results = new float[10];
      Location.distanceBetween(office.lat(), office.lng(), location.getLatitude(),
          location.getLongitude(), results);

      float distance = results[0];
      office.distance(distance);

      if (nearest == null || distance < shortestDistance) {
        nearest = office;
        shortestDistance = distance;
      }
    }

    return nearest;
  }

  /**
   * Returns if the "hey you should phonebank" screen should be shown to the user
   * Was asked to exclude these specific states from seeing the message
   */
  public boolean phonebank() {

    if (state == null) {
      return false;
    }

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

  public float distance() {

    return this.distance;
  }

  public EarlyState distance(final float distance) {
    this.distance = distance;
    return this;
  }

  public FieldOffice fieldOffice() {
    return this.closestOffice;
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

  @ScreenType public String type() {
    return this.type;
  }

  public EarlyState fieldOffice(final FieldOffice fieldOffice) {
    this.closestOffice = fieldOffice;
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
