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

import android.os.Parcelable;
import android.util.SparseArray;
import android.view.View;

public abstract class Screen extends FlowPathBase {
  private SparseArray<Parcelable> viewState;

  @Override
  public boolean equals(Object o) {
    return o != null && o instanceof Screen && this.getName().equals(((Screen) o).getName());
  }

  @Override
  public int hashCode() {
    return getName().hashCode();
  }

  public String getName() {
    return getClass().getName();
  }

  protected SparseArray<Parcelable> getViewState() {
    return viewState;
  }

  public void setViewState(SparseArray<Parcelable> viewState) {
    this.viewState = viewState;
  }

  public void restoreHierarchyState(View view) {
    if (getViewState() != null) {
      view.restoreHierarchyState(getViewState());
    }
  }
}
