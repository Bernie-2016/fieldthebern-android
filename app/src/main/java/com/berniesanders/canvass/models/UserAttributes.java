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
