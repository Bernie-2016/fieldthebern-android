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

package com.berniesanders.fieldthebern.models;


import java.util.List;



/**
 * ErrorResponse
 * <p/>
 * JSON Format
 * {
 * "errors": [
 * {
 * "id": "DASHERIZED_CAPITALIZED_ERROR_NAME",
 * "title": "User friendly error name",
 * "detail": "Value of Error.message",
 * "status": "HTTP_CODE_IN_INTEGER_FORMAT"
 * }
 * ]
 * }
 */
public class ErrorResponse {
    public List<Error> errors;

    public static class Error {
        public String id;
        public String title;
        public String detail;
        public int status;

        @Override
        public String toString() {
            return title + " " + detail;
        }
    }


    public String getAllDetails() {
        try {
            StringBuilder sb = new StringBuilder();
            for (Error error : errors) {
                sb.append(error.detail);
                sb.append("\n");
            }
            //remove the extra blank line
            sb.delete(sb.lastIndexOf("\n"), sb.length());
            return sb.toString();
        } catch (Exception e) {

        }

        return "Unknown Error";
    }
}
