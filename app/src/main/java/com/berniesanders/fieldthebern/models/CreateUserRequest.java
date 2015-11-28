package com.berniesanders.fieldthebern.models;

/**
 *
 */
public class CreateUserRequest {

    private final Data data;

    public CreateUserRequest() {
        this.data = new Data();
    }


    public Data getData() {
        return data;
    }

    public CreateUserRequest withAttributes(UserAttributes attributes) {
        this.data.setAttributes(attributes);
        return this;
    }

    public static class Data {

        private UserAttributes attributes;

        void setAttributes(UserAttributes attributes) {
            this.attributes = attributes;
        }

        public UserAttributes getAttributes() {
            return attributes;
        }
    }
}
