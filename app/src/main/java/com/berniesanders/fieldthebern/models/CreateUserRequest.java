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

/**
 *
 */
public class CreateUserRequest {

    private final Data data;

    public CreateUserRequest() {
        this.data = new Data();
    }


    public Data getData() {
        return data;
    }

    public CreateUserRequest withAttributes(UserAttributes attributes) {
        this.data.setAttributes(attributes);
        return this;
    }

    public static class Data {

        private UserAttributes attributes;

        void setAttributes(UserAttributes attributes) {
            this.attributes = attributes;
        }

        public UserAttributes getAttributes() {
            return attributes;
        }
    }
}
