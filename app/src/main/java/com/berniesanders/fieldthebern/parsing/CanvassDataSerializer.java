package com.berniesanders.fieldthebern.parsing;

import com.berniesanders.fieldthebern.models.ApiAddress;
import com.berniesanders.fieldthebern.models.ApiItem;
import com.berniesanders.fieldthebern.models.CanvasData;
import com.berniesanders.fieldthebern.models.Collection;
import com.berniesanders.fieldthebern.models.Page;
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
public class CanvassDataSerializer implements JsonSerializer<CanvasData> {



    @Override
    public JsonElement serialize(CanvasData src, Type typeOfSrc, JsonSerializationContext context) {

        if ("address".equals(src.type())) {
            return context.serialize(src, ApiAddress.class);
        } else {  //("person".equals(type))
            return context.serialize(src, Page.class);
        }
    }
}
