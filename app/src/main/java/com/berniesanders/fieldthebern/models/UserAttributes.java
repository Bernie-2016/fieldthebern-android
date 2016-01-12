package com.berniesanders.fieldthebern.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 *
 */
public class UserAttributes implements Parcelable {

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

    @SerializedName("facebook_access_token")
    String facebookAccessToken;

    @SerializedName("facebook_id")
    String facebookId;

    @SerializedName("visits_count")
    String visitsCount;

    @SerializedName("total_points")
    String totalPoints;

    public boolean isFacebookUser() {
        return (facebookId!=null);
    }
    public UserAttributes setAsFacebookUser(String facebookId) {
        this.facebookId = facebookId;
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

    public String facebookId() {
        return this.facebookId;
    }

    public String facebookAccessToken() {
        return this.facebookAccessToken;
    }

    public UserAttributes facebookId(final String facebookId) {
        this.facebookId = facebookId;
        return this;
    }

    public UserAttributes facebookAccessToken(final String facebookAccessToken) {
        this.facebookAccessToken = facebookAccessToken;
        return this;
    }

    public String totalPoints() {
        return this.totalPoints;
    }

    public String visitsCount() {
        return this.visitsCount;
    }

    public UserAttributes totalPoints(final String totalPoints) {
        this.totalPoints = totalPoints;
        return this;
    }

    public UserAttributes visitsCount(final String visitsCount) {
        this.visitsCount = visitsCount;
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
                ", has base64PhotoData? ='" + (base64PhotoData != null)  + '\'' +
                ", facebookAccessToken='" + facebookAccessToken + '\'' +
                ", facebookId='" + facebookId + '\'' +
                ", visitsCount='" + visitsCount + '\'' +
                ", totalPoints='" + totalPoints + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.email);
        dest.writeString(this.password);
        dest.writeString(this.firstName);
        dest.writeString(this.lastName);
        dest.writeString(this.stateCode);
        dest.writeDouble(this.lat);
        dest.writeDouble(this.lng);
        dest.writeString(this.photoThumbUrl);
        dest.writeString(this.photoLargeUrl);
        dest.writeString(this.base64PhotoData);
        dest.writeString(this.facebookAccessToken);
        dest.writeString(this.facebookId);
        dest.writeString(this.visitsCount);
        dest.writeString(this.totalPoints);
    }

    public UserAttributes() {
    }

    protected UserAttributes(Parcel in) {
        this.email = in.readString();
        this.password = in.readString();
        this.firstName = in.readString();
        this.lastName = in.readString();
        this.stateCode = in.readString();
        this.lat = in.readDouble();
        this.lng = in.readDouble();
        this.photoThumbUrl = in.readString();
        this.photoLargeUrl = in.readString();
        this.base64PhotoData = in.readString();
        this.facebookAccessToken = in.readString();
        this.facebookId = in.readString();
        this.visitsCount = in.readString();
        this.totalPoints = in.readString();
    }

    public static final Creator<UserAttributes> CREATOR = new Creator<UserAttributes>() {
        public UserAttributes createFromParcel(Parcel source) {
            return new UserAttributes(source);
        }

        public UserAttributes[] newArray(int size) {
            return new UserAttributes[size];
        }
    };
}
