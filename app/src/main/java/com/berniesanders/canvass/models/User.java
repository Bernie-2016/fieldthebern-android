package com.berniesanders.canvass.models;

/**
 *
 */
public class User {

    Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    static class Data {
        int id;
        String type;
        UserAttributes attributes;
        UserRelationships relationships;

        public int getId() {
            return id;
        }

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
