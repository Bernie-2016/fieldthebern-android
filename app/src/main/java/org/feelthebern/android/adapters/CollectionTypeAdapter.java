package org.feelthebern.android.adapters;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import org.feelthebern.android.models.ApiItem;
import org.feelthebern.android.models.Collection;
import org.feelthebern.android.models.Page;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by AndrewOrobator on 9/4/15.
 */
public class CollectionTypeAdapter implements JsonDeserializer<Collection> {


    @Override
    public Collection deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Gson gson = new Gson();
        Collection collection = gson.fromJson(json, Collection.class);
        JsonObject jObj = gson.fromJson(json, JsonObject.class);
        JsonArray jsonElements = (JsonArray) jObj.get("items");
        List<ApiItem> items = new ArrayList<>(jsonElements.size());

        for (int i = 0; i < jsonElements.size(); i++) {
            JsonObject jsonObject = (JsonObject) jsonElements.get(i);
            String type = jsonObject.get("type").getAsString();
            if ("collection".equals(type)) {
                items.add(gson.fromJson(jsonObject, Collection.class));
            } else {
                items.add(gson.fromJson(jsonObject, Page.class));
            }
        }

        collection.setApiItems(items);
        return collection;
    }
}
