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
 * Looks like this type adapter treats the top level object as a "collection"
 * It then grabs the "items" json array named 'jsonElements'
 * Then looping through this array is check the type of each item
 * Each item is deserialized into a page or collection, based on its type.
 *
 * The final array is a mix of pages and collection objects,
 * this is set as a list of ApiItems on the Collection object we're deserializing.
 *
 * TODO: this could use some unit tests
 * TODO: We'll need to do something similar with page.content json arrays of 'h1' 'h2' 'p' etc
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
