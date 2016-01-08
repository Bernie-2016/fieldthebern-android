package com.berniesanders.fieldthebern.parsing;

import android.content.Context;

import com.berniesanders.fieldthebern.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Parse states.json
 * Created by Vishal on 1/7/16.
 */
public class StatesDataParser {

    private String json;

    public StatesDataParser(Context context) {
        String json = null;
        try {
            InputStream is = context.getResources().openRawResource(R.raw.states);
            int size = is.available();
            byte[] buffer = new byte[size];
            int read = is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            this.json = null;
        }
        this.json = json;
    }

    public String getJson() {
        return json;
    }

    public String getStateNameFromCode(String code) {
        String stateName = null;

        try {
            JSONArray jsonArray = new JSONArray(json);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (code.equalsIgnoreCase(jsonObject.getString("code"))) {
                    stateName = jsonObject.getString("state");
                    break;
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return stateName;
    }

    public Map<String, String> getStateInfoFromCode(String code) {
        Map<String, String> dictionary = new HashMap<>();

        try {
            JSONArray jsonArray = new JSONArray(json);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (code.equalsIgnoreCase(jsonObject.getString("code"))) {

                    dictionary.put("state", jsonObject.getString("state"));
                    dictionary.put("type", jsonObject.getString("type"));
                    dictionary.put("date", jsonObject.getString("date"));
                    dictionary.put("deadline", jsonObject.getString("deadline"));
                    dictionary.put("details", jsonObject.getString("details"));
                    break;
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return dictionary;
    }
}
