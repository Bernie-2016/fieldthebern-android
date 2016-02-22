/*
 * Copyright (c) 2016 - Bernie 2016, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.berniesanders.fieldthebern.mortar;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseArray;
import java.lang.reflect.Array;

/**
 * Extends Screen for persistence.  Basically just takes care of persisting the view
 * state, and has template methods so that subclasses can persist anything else they
 * might need (generally parameters).
 */
public abstract class ParcelableScreen extends Screen implements Parcelable {
  @Override
  public final int describeContents() {
    return 0;
  }

  @Override
  public final void writeToParcel(Parcel parcel, int i) {
    //noinspection unchecked
    SparseArray<Object> sparseArray = (SparseArray) getViewState();
    parcel.writeSparseArray(sparseArray);
    doWriteToParcel(parcel, i);
  }

  protected void doWriteToParcel(Parcel parcel, int flags) {
  }

  public static <T extends ParcelableScreen> Creator<T> zeroArgsScreenCreator(
      final Class<T> screenClass) {
    return new ScreenCreator<T>() {
      @Override
      protected T doCreateFromParcel(Parcel source) {
        try {
          return screenClass.newInstance();
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }

      @Override
      public T[] newArray(int size) {
        //noinspection unchecked
        return (T[]) Array.newInstance(screenClass, size);
      }
    };
  }

  /** Handy Creator implementation for use/extension by ParcelableScreen subclasses. */
  public static abstract class ScreenCreator<T extends ParcelableScreen> implements Creator<T> {

    @Override
    public final T createFromParcel(Parcel source) {
      ClassLoader classLoader = getClassLoader();
      //noinspection unchecked
      SparseArray<Parcelable> sparseArray = source.readSparseArray(classLoader);
      T created = doCreateFromParcel(source);
      created.setViewState(sparseArray);
      return created;
    }

    protected abstract T doCreateFromParcel(Parcel source);

    protected ClassLoader getClassLoader() {
      return ParcelableScreen.class.getClassLoader();
    }
  }
}
