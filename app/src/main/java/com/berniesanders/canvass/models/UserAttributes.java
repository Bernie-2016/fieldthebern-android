package com.berniesanders.canvass.models;

/**
 *
 */
public class UserAttributes {
    String email;
    String password;
    String first_name;
    String last_name;
    String state_code;
    double lat;
    double lng;
    String photo_thumb_url;
    String photo_large_url;
    String base_64_photo_data;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getState_code() {
        return state_code;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public String getPhoto_thumb_url() {
        return photo_thumb_url;
    }

    public String getPhoto_large_url() {
        return photo_large_url;
    }

    public String getBase_64_photo_data() {
        return base_64_photo_data;
    }

    @Override
    public String toString() {
        return "UserAttributes{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", state_code='" + state_code + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                ", photo_thumb_url='" + photo_thumb_url + '\'' +
                ", photo_large_url='" + photo_large_url + '\'' +
                ", (base_64_photo_data!=null)='" + (base_64_photo_data!=null) + '\'' +
                '}';
    }
}
