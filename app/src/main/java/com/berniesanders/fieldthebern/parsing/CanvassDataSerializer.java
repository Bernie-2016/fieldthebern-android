package com.berniesanders.fieldthebern.parsing;

import com.berniesanders.fieldthebern.models.ApiAddress;
import com.berniesanders.fieldthebern.models.CanvassData;
import com.berniesanders.fieldthebern.models.Page;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * Custom Gson deserializer for 'ApiItem' with a mix of 'pages' and 'collections' as children
 */
public class CanvassDataSerializer implements JsonSerializer<CanvassData> {



    @Override
    public JsonElement serialize(CanvassData src, Type typeOfSrc, JsonSerializationContext context) {

        if (ApiAddress.TYPE.equals(src.type())) {
            return context.serialize(src, ApiAddress.class);
        } else {  //("person".equals(type))
            return context.serialize(src, Page.class);
        }
    }
}
