package com.berniesanders.fieldthebern.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/**
 *
 */
public class User implements Parcelable {

    public static final String PREF_NAME = "User.Pref";
    public static final String PREF_SEEN_APP_INTRO = "Use.SeenAppIntro";

    Data data = new Data();

    @NonNull
    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data implements Parcelable {
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


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeValue(this.id);
            dest.writeString(this.type);
            dest.writeParcelable(this.attributes, flags);
            dest.writeParcelable(this.relationships, flags);
        }

        public Data() {
        }

        protected Data(Parcel in) {
            this.id = (Integer) in.readValue(Integer.class.getClassLoader());
            this.type = in.readString();
            this.attributes = in.readParcelable(UserAttributes.class.getClassLoader());
            this.relationships = in.readParcelable(UserRelationships.class.getClassLoader());
        }

        public static final Creator<Data> CREATOR = new Creator<Data>() {
            public Data createFromParcel(Parcel source) {
                return new Data(source);
            }

            public Data[] newArray(int size) {
                return new Data[size];
            }
        };
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.data, flags);
    }

    public User() {
    }

    protected User(Parcel in) {
        this.data = in.readParcelable(Data.class.getClassLoader());
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
