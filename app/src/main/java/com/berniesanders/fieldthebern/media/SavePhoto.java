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

package com.berniesanders.fieldthebern.media;

import android.content.Context;
import android.graphics.Bitmap;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import timber.log.Timber;

public class SavePhoto {

  private static File createFile(Context context) throws IOException {
    // Create an image file name
    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    String imageFileName = "avatar_" + timeStamp + "_";
    File storageDir = context.getFilesDir();
    File image = File.createTempFile(imageFileName,  /* prefix */
        ".png",         /* suffix */
        storageDir      /* directory */);

    // Save a file: path for use with ACTION_VIEW intents
    // mCurrentPhotoPath = "file:" + image.getAbsolutePath();
    return image;
  }

  public static File saveBitmap(Bitmap bitmap, Context context) {

    FileOutputStream out = null;
    File file = null;
    try {
      file = createFile(context);
      out = new FileOutputStream(file);
      bitmap.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
    } catch (Exception e) {
      Timber.e(e, "error saving bitmap to disk");
    } finally {
      try {
        if (out != null) {
          out.close();
        }
      } catch (IOException e) {
        Timber.e(e, "error saving bitmap to disk");
      }
    }

    return file;
  }
}
