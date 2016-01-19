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

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import com.berniesanders.fieldthebern.models.ApiItem;
import com.berniesanders.fieldthebern.models.Collection;
import com.berniesanders.fieldthebern.models.Page;

import java.lang.reflect.Type;

/**
 * Custom Gson deserializer for 'ApiItem' with a mix of 'pages' and 'collections' as children
 */
public class CollectionDeserializer implements JsonDeserializer<ApiItem>, JsonSerializer<ApiItem> {


    @Override
    public ApiItem deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        ApiItem typedContent = null;
        switch (json.getAsJsonObject().get("type").getAsString()) {
            case "collection":
                typedContent = context.deserialize(json, Collection.class);
                break;
            case "page":
                typedContent = context.deserialize(json, Page.class);
                break;
            default:
                throw new JsonParseException("unknown type:"+json.getAsJsonObject().get("type").getAsString());
        }

        return typedContent;
    }

    @Override
    public JsonElement serialize(ApiItem src, Type typeOfSrc, JsonSerializationContext context) {

        if ("collection".equals(src.getType())) {
            return context.serialize(src, Collection.class);
        } else {  //("page".equals(type))
            return context.serialize(src, Page.class);
        }
    }
}
