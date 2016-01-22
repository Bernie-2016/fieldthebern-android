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

package com.berniesanders.fieldthebern.views;

import android.graphics.Bitmap;
import android.support.v7.graphics.Palette;
import com.squareup.picasso.Transformation;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * util for getting a pallet from a bitmap loaded by Picasso
 */
public final class PaletteTransformation implements Transformation {
  private static final PaletteTransformation INSTANCE = new PaletteTransformation();
  private static final Map<Bitmap, Palette> CACHE = new WeakHashMap<>();

  public static PaletteTransformation instance() {
    return INSTANCE;
  }

  public static Palette getPalette(Bitmap bitmap) {
    return CACHE.get(bitmap);
  }

  private PaletteTransformation() {
  }

  @Override
  public Bitmap transform(Bitmap source) {
    Palette palette = Palette.generate(source);
    CACHE.put(source, palette);
    return source;
  }

  @Override
  public String key() {
    return ""; // Stable key for all requests. An unfortunate requirement.
  }
}
