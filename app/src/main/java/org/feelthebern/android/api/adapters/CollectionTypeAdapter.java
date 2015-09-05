package org.feelthebern.android.api.adapters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import org.feelthebern.android.api.models.Collection;

import java.lang.reflect.Type;

/**
 * Created by AndrewOrobator on 9/4/15.
 */
public class CollectionTypeAdapter implements JsonDeserializer<Collection> {


    @Override
    public Collection deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return null;
    }
}
