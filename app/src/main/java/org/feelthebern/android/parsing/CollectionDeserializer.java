package org.feelthebern.android.parsing;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import org.feelthebern.android.models.ApiItem;
import org.feelthebern.android.models.Collection;
import org.feelthebern.android.models.Page;

import java.lang.reflect.Type;

/**
 * Custom Gson deserializer for 'ApiItem' with a mix of 'pages' and 'collections' as children
 */
public class CollectionDeserializer implements JsonDeserializer<ApiItem> {


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
}
