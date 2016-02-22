package com.berniesanders.fieldthebern.mortar;

import android.os.Parcelable;
import flow.StateParceler;

public class BernParceler implements StateParceler {
  @Override
  public Parcelable wrap(Object instance) {
    return (Parcelable) instance;
  }

  @Override public Object
  unwrap(Parcelable parcelable) {
    return parcelable;
  }
}
