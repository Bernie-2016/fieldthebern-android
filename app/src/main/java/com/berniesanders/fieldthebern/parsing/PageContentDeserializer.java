package com.berniesanders.fieldthebern.parsing;

import com.berniesanders.fieldthebern.models.List;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import com.berniesanders.fieldthebern.models.Content;
import com.berniesanders.fieldthebern.models.H1;
import com.berniesanders.fieldthebern.models.H2;
import com.berniesanders.fieldthebern.models.H3;
import com.berniesanders.fieldthebern.models.Iframe;
import com.berniesanders.fieldthebern.models.Img;
import com.berniesanders.fieldthebern.models.Nav;
import com.berniesanders.fieldthebern.models.P;
import com.berniesanders.fieldthebern.models.Quote;
import com.berniesanders.fieldthebern.models.Video;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Type;

import timber.log.Timber;

/**
 * Custom Gson deserializer for page 'content' to deserialize items into 'h1' 'p' etc
 *
 * TODO: this could use some unit tests
 * TODO: We'll need to do something similar with page.content json arrays of 'h1' 'h2' 'p' etc
 */
public class PageContentDeserializer implements JsonDeserializer<Content> {


    @Override
    public Content deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        //Gson gson = new Gson(); //use a temp gson so we don't trigger a stack overflow.
        //Content untypedContent = gson.fromJson(json, Content.class);

        Content typedContent = null;
        switch (json.getAsJsonObject().get("type").getAsString()) {
            case "h1":
                typedContent = context.deserialize(json, H1.class);
                break;
            case "h2":
                typedContent = context.deserialize(json, H2.class);
                break;
            case "h3":
                typedContent = context.deserialize(json, H3.class);
                break;
            case "p":
                typedContent = context.deserialize(json, P.class);
                break;
            case "img":
                typedContent = context.deserialize(json, Img.class);
                break;
            case "nav":
                try {
                    typedContent = context.deserialize(json, Nav.class);
                } catch (JsonSyntaxException jsonEx) {
                    Timber.e(jsonEx, "Error deserializing a JSON object");
                    //use an empty nav item, there is an error in the ftb json
                    typedContent = new Nav();
                }
                break;
            case "video":
                typedContent = context.deserialize(json, Video.class);
                break;
            case "quote":
                typedContent = context.deserialize(json, Quote.class);
                break;
            case "list":
                typedContent = context.deserialize(json, List.class);
                break;
            case "iframe":
                typedContent = context.deserialize(json, Iframe.class);
                break;
            default:
                throw new JsonParseException("unknown type:"+json.getAsJsonObject().get("type").getAsString());
        }

        return typedContent;
    }
}
