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

package com.berniesanders.fieldthebern.parsing;

import android.util.Patterns;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class FormValidator {

  public static boolean isNullOrBlank(EditText editText) {
    return (editText.getText() == null || isBlank(editText.getText().toString()));
  }

  public static boolean isNullOrBlank(AutoCompleteTextView autoCompleteTextView) {
    return (autoCompleteTextView.getText() == null || isBlank(
        autoCompleteTextView.getText().toString()));
  }

  public static boolean isEmailValid(CharSequence email) {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
  }

  public static boolean isPhoneValid(CharSequence phone) {
    if (Patterns.PHONE.matcher(phone).matches()) {
      return passesPhoneRequirements(phone.toString());
    } else {
      return false;
    }
  }

  private static boolean passesPhoneRequirements(String number) {
    number = number.replaceAll("[^\\d]", "");
    if (number.length() < 10) {
      return false;
    } else if (number.startsWith("0")) {
      return false;
    }
    return true;
  }
}
