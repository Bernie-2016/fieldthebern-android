package com.berniesanders.canvass.models;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import static org.apache.commons.lang3.StringUtils.isAnyBlank;

import static com.berniesanders.canvass.validation.StringValidator.findNullOrBlank;

/**
 *
 */
public class CreateUserAttributes {

    private final String email;
    private final String password;
    @SerializedName("first_name")
    private final String firstName;
    @SerializedName("last_name")
    private final String lastName;
    @SerializedName("state_code")
    private final String stateCode;
    private final double lat;
    private final double lng;

    private CreateUserAttributes(Builder builder) {
        email = builder.email;
        password = builder.password;
        firstName = builder.firstName;
        lastName = builder.lastName;
        stateCode = builder.stateCode;
        lat = builder.lat;
        lng = builder.lng;
    }

    @NonNull
    public String getEmail() {
        return email;
    }
    @NonNull
    public String getPassword() {
        return password;
    }
    @NonNull
    public String getFirstName() {
        return firstName;
    }
    @NonNull
    public String getLastName() {
        return lastName;
    }
    @NonNull
    public String getStateCode() {
        return stateCode;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public static final class Builder {
        private String email;
        private String password;
        private String firstName;
        private String lastName;
        private String stateCode;
        private double lat = 0.0;
        private double lng = 0.0;

        public Builder() {
        }

        @NonNull
        public Builder email(@NonNull String val) {
            email = val;
            return this;
        }

        @NonNull
        public Builder password(@NonNull String val) {
            password = val;
            return this;
        }

        @NonNull
        public Builder firstName(@NonNull String val) {
            firstName = val;
            return this;
        }

        @NonNull
        public Builder lastName(@NonNull String val) {
            lastName = val;
            return this;
        }

        @NonNull
        public Builder stateCode(@NonNull String val) {
            stateCode = val;
            return this;
        }

        @NonNull
        public Builder lat(double val) {
            lat = val;
            return this;
        }

        @NonNull
        public Builder lng(double val) {
            lng = val;
            return this;
        }

        @NonNull
        public CreateUserAttributes build() {

            if(isAnyBlank(email, password, firstName, lastName, stateCode)) {

                String[] values = {email, password, firstName, lastName, stateCode};
                String[] names = {"email", "password", "firstName", "lastName", "stateCode"};

                throw new IllegalArgumentException("Cannot build a CreateUserAttributes "
                        + findNullOrBlank(values, names)
                        + " cannot be blank");
            }

            if (lat == 0.0 && lng == 0.0) {
                // no canvassing allowed from the Atlantic Ocean
                throw new IllegalArgumentException("Cannot build a CreateUserAttributes"
                        + " lat, lng"
                        + " cannot both be 0, you're not that good a swimmer");
            }

            return new CreateUserAttributes(this);
        }
    }
}
