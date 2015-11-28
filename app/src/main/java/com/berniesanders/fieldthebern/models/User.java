package com.berniesanders.fieldthebern.models;

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
        Integer id;
        String type;
        UserAttributes attributes = new UserAttributes();
        UserRelationships relationships;

        public Integer id() {
            return this.id;
        }

        @NonNull
        public UserAttributes attributes() {
            return attributes;
        }

        public UserRelationships relationships() {
            return this.relationships;
        }

        public Data id(final Integer id) {
            this.id = id;
            return this;
        }

        public Data type(final String type) {
            this.type = type;
            return this;
        }

        public Data attributes(final UserAttributes attributes) {
            this.attributes = attributes;
            return this;
        }

        public Data relationships(final UserRelationships relationships) {
            this.relationships = relationships;
            return this;
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
