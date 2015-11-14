package com.berniesanders.canvass.models;

/**
 *
 */
public class CreateUserRequest {

    private final Data data;

    public CreateUserRequest() {
        this.data = new Data();
    }


    public CreateUserRequest withAttributes(CreateUserAttributes attributes) {
        this.data.setAttributes(attributes);
        return this;
    }

    static class Data {

        private CreateUserAttributes attributes;

        void setAttributes(CreateUserAttributes attributes) {
            this.attributes = attributes;
        }
    }
}
