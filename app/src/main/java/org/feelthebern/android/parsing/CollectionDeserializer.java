package org.feelthebern.android.parsing;

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
 * Custom Gson deserializer for 'collections' with a mix of 'pages' and 'collections' as children
 *
 * This deserializer adapter treats the top level object as a "collection"
 * It then grabs the "items" json array named jsonElements
 * Then, looping through this array, it checks the type of each item
 * Each item is deserialized into a page or collection, based on its type.
 *
 * The final array is a mix of pages and collection objects,
 * this is set as a list of ApiItems on the Collection object we're deserializing.
 *
 * TODO: this could use some unit tests
 * TODO: We'll need to do something similar with page.content json arrays of 'h1' 'h2' 'p' etc
 * TODO: this may not be very efficient as we deserialize the collection object twice
 * TODO: we could probably do some sort of "peek"
 */
public class CollectionDeserializer implements JsonDeserializer<Collection> {


    @Override
    public Collection deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        // new gson that doesn't have the CollectionTypeAdapter, so it deserializes normally
        // This makes sense because we're going to manually set the ApiItems array
        // in the Collection object we're creating
        Gson gson = new Gson();
        Collection collection = gson.fromJson(json, Collection.class);

        // here it doesn't totally matter what we use as this is a temp object
        // that is just used to get access to the items array
        JsonObject jObj = gson.fromJson(json, JsonObject.class);
        JsonArray jsonElements = (JsonArray) jObj.get("items");
        List<ApiItem> items = new ArrayList<>(jsonElements.size());

//        Timber.v("BEGIN deserialize parent collection: %s | %s ", collection.getTitle(), collection.getName());

        for (int i = 0; i < jsonElements.size(); i++) {
            JsonObject jsonObject = (JsonObject) jsonElements.get(i);
            String type = jsonObject.get("type").getAsString();
            if ("collection".equals(type)) {
                // here we want to use the 'context' gson which has the type adapter
                // this will cause it to recursively use the custom deserialize function
                Collection childCollection = context.deserialize(jsonObject, Collection.class);
                items.add(childCollection);
//                Timber.v("deserialize childCollection --------- %s | %s %d",
//                        childCollection.getTitle(),
//                        childCollection.getName(),
//                        i);
            } else {
                Page page = context.deserialize(jsonObject, Page.class);
                items.add(page);
//                Timber.v("deserialize child page --------- %s %d",
//                        page.getTitle(),
//                        i);
            }
        }

//        Timber.v("END deserialize parent collection: %s | %s ", collection.getTitle(), collection.getName());

        collection.setApiItems(items);
        return collection;
    }
}
