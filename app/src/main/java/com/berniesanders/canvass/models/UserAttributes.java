package com.berniesanders.canvass.models;

import com.google.gson.annotations.SerializedName;

/**
 *
 */
public class UserAttributes {

    public static final String FACEBOOK_USERNAME = "facebook";

    private String email;
    private String password;
    @SerializedName("first_name")
    private String firstName;
    @SerializedName("last_name")
    private String lastName;
    @SerializedName("state_code")
    private String stateCode;
    private double lat;
    private double lng;

    @SerializedName("photo_thumb_url")
    String photoThumbUrl;
    @SerializedName("photo_large_url")
    String photoLargeUrl;
    @SerializedName("base_64_photo_data")
    String base64PhotoData;


    public boolean isFacebookUser() {
        return FACEBOOK_USERNAME.equals(email);
    }
    public UserAttributes setAsFacebookUser() {
        email = FACEBOOK_USERNAME;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getStateCode() {
        return stateCode;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public String getPhotoThumbUrl() {
        return photoThumbUrl;
    }

    public String getPhotoLargeUrl() {
        return photoLargeUrl;
    }

    public String getBase64PhotoData() {
        return base64PhotoData;
    }

    public UserAttributes email(String email) {
        this.email = email;
        return this;
    }

    public UserAttributes password(String password) {
        this.password = password;
        return this;
    }

    public UserAttributes firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public UserAttributes lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public UserAttributes stateCode(String stateCode) {
        this.stateCode = stateCode;
        return this;
    }

    public UserAttributes lat(double lat) {
        this.lat = lat;
        return this;
    }

    public UserAttributes lng(double lng) {
        this.lng = lng;
        return this;
    }

    public UserAttributes photoThumbUrl(String photoThumbUrl) {
        this.photoThumbUrl = photoThumbUrl;
        return this;
    }

    public UserAttributes photoLargeUrl(String photoLargeUrl) {
        this.photoLargeUrl = photoLargeUrl;
        return this;
    }

    public UserAttributes base64PhotoData(String base64PhotoData) {
        this.base64PhotoData = base64PhotoData;
        return this;
    }


    @Override
    public String toString() {
        return "UserAttributes{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", stateCode='" + stateCode + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                ", photoThumbUrl='" + photoThumbUrl + '\'' +
                ", photoLargeUrl='" + photoLargeUrl + '\'' +
                ", (base64PhotoData!=null)='" + (base64PhotoData!=null) + '\'' +
                '}';
    }
}
