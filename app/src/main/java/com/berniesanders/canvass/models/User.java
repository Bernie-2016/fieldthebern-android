package com.berniesanders.canvass.models;

import android.support.annotation.NonNull;

/**
 *
 */
public class User {

    public static final String PREF_NAME = "User.Pref";

    Data data = new Data();

    @NonNull
    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {
        int id;
        String type;
        UserAttributes attributes = new UserAttributes();
        UserRelationships relationships;

        public int getId() {
            return id;
        }
        
        @NonNull
        public UserAttributes getAttributes() {
            return attributes;
        }

        public UserRelationships getRelationships() {
            return relationships;
        }
    }


    @Override
    public String toString() {
        return "User Data{" +
                "id=" + data.id +
                ", type='" + data.type + '\'' +
                ", attributes=" + data.attributes +
                ", relationships=" + data.relationships +
                '}';
    }

}
