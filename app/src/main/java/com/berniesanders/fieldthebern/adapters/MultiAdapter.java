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

package com.berniesanders.fieldthebern.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * handles multiple content types and view holders, which all must extend a base view holder
 */
public abstract class MultiAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  /**
   * Created in the adapter and passed to view holders
   */
  public interface ClickListener {
    void onClick(Object model, View view);
  }
}
