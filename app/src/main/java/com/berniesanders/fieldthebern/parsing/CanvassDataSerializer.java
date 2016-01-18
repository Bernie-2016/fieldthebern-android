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

import com.berniesanders.fieldthebern.models.ApiAddress;
import com.berniesanders.fieldthebern.models.CanvassData;
import com.berniesanders.fieldthebern.models.Page;
import com.berniesanders.fieldthebern.models.Person;
import com.berniesanders.fieldthebern.models.UserData;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * Custom Gson deserializer for 'ApiItem' with a mix of 'pages' and 'collections' as children
 */
public class CanvassDataSerializer implements JsonSerializer<CanvassData>, JsonDeserializer<CanvassData> {



    @Override
    public JsonElement serialize(CanvassData src, Type typeOfSrc, JsonSerializationContext context) {

        if (ApiAddress.TYPE.equals(src.type())) {
            return context.serialize(src, ApiAddress.class);
        } else  if (Person.TYPE.equals(src.type())) {
            return context.serialize(src, Person.class);
        } else  if (UserData.TYPE.equals(src.type())) {
            return context.serialize(src, UserData.class);
        } else {
            return null;
        }
    }

    @Override
    public CanvassData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        CanvassData typedContent = null;
        switch (json.getAsJsonObject().get("type").getAsString()) {
            case ApiAddress.TYPE:
                typedContent = context.deserialize(json, ApiAddress.class);
                break;
            case Person.TYPE:
                typedContent = context.deserialize(json, Person.class);
                break;
            case UserData.TYPE:
                typedContent = context.deserialize(json, UserData.class);
                break;
            default:
                throw new JsonParseException("unknown type:"+json.getAsJsonObject().get("type").getAsString());
        }

        return typedContent;
    }
}
