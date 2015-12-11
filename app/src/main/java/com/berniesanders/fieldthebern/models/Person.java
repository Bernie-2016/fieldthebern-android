/*
 * Made with love by volunteers
 * Copyright 2015 BernieSanders.com, FeelTheBern.org,
 * Coderly, LostPacketSoftware and the volunteers
 * License: GNU AGPLv3 - https://gnu.org/licenses/agpl.html
 */
package com.berniesanders.fieldthebern.models;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Person is someone who a canvasser spoke with
 *
 * {
 *     id: <person_id>,
 *     type: 'people',
 *     attributes: {
 *        first_name: <first_name>,
 *        last_name: <last_name>,
 *        email: <email>,
 *        phone: <phone>,
 *        preferred_contact_method: <'email'|'phone'>,
 *        previously_participated_in_caucus_or_primary: <true|false>,
 *        party_affiliation: <'Unknown'|'Unaffiliated'|'Undeclared'|'Democrat'|'Republican'|'Independent'>,
 *        canvass_response: <'Unknown'|'Asked to leave'|'Strongly for'|'Leaning for'|'Undecided'|'Leaning against'|'Strongly against'>
 *     }
 * }
 */
public class Person extends CanvassData implements Parcelable {

    public static final String TYPE = "people";

    Integer id; //null if we're creating a new person in the db
    Attributes attributes = new Attributes();

    public Integer id() {
        return this.id;
    }

    @Override
    public String type() {
        return TYPE;
    }

    public Attributes attributes() {
        return this.attributes;
    }

    public Person id(final Integer id) {
        this.id = id;
        return this;
    }
    
    @Override
    public Person type(final String type) {
        this.type = type;
        return this;
    }

    public Person attributes(final Attributes attributes) {
        this.attributes = attributes;
        return this;
    }


    public static class Attributes implements Parcelable {

        @SerializedName("first_name")
        String firstName;
        @SerializedName("last_name")
        String lastName;
        String email;
        String phone;
        @SerializedName("preferred_contact_method")
        String preferredContact;
        @SerializedName("previously_participated_in_caucus_or_primary")
        boolean previouslyParticipated;
        @SerializedName("party_affiliation")
        String party;
        @SerializedName("canvass_response")
        String canvassResponse;

        public String firstName() {
            return this.firstName;
        }

        public String lastName() {
            return this.lastName;
        }

        public String email() {
            return this.email;
        }

        public String phone() {
            return this.phone;
        }

        public String preferredContact() {
            return this.preferredContact;
        }

        public boolean previouslyParticipated() {
            return this.previouslyParticipated;
        }

        @Party.Affiliation
        public String party() {
            return this.party;
        }

        @CanvassResponse.Response
        public String canvassResponse() {
            return this.canvassResponse;
        }

        public Attributes firstName(final String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Attributes lastName(final String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Attributes email(final String email) {
            this.email = email;
            return this;
        }

        public Attributes phone(final String phone) {
            this.phone = phone;
            return this;
        }

        public Attributes preferredContact(@Contact.Method final String preferredContact) {
            this.preferredContact = preferredContact;
            return this;
        }

        public Attributes previouslyParticipated(final boolean previouslyParticipated) {
            this.previouslyParticipated = previouslyParticipated;
            return this;
        }

        public Attributes party(@Party.Affiliation final String party) {
            this.party = party;
            return this;
        }

        public Attributes canvassResponse(@CanvassResponse.Response final String canvassResponse) {
            this.canvassResponse = canvassResponse;
            return this;
        }

        @Override
        public String toString() {
            return "Attributes{" +
                    "firstName='" + firstName + '\'' +
                    ", lastName='" + lastName + '\'' +
                    ", email='" + email + '\'' +
                    ", phone='" + phone + '\'' +
                    ", preferredContact='" + preferredContact + '\'' +
                    ", previouslyParticipated=" + previouslyParticipated +
                    ", party='" + party + '\'' +
                    ", canvassResponse='" + canvassResponse + '\'' +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Attributes that = (Attributes) o;

            if (previouslyParticipated != that.previouslyParticipated) return false;
            if (firstName != null ? !firstName.equals(that.firstName) : that.firstName != null) {
                return false;
            }
            if (lastName != null ? !lastName.equals(that.lastName) : that.lastName != null)
                return false;
            if (email != null ? !email.equals(that.email) : that.email != null) return false;
            if (phone != null ? !phone.equals(that.phone) : that.phone != null) return false;
            if (preferredContact != null ? !preferredContact.equals(that.preferredContact) : that.preferredContact != null) {
                return false;
            }
            if (party != null ? !party.equals(that.party) : that.party != null) return false;
            return !(canvassResponse != null ? !canvassResponse.equals(that.canvassResponse) : that.canvassResponse != null);

        }

        @Override
        public int hashCode() {
            int result = firstName != null ? firstName.hashCode() : 0;
            result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
            result = 31 * result + (email != null ? email.hashCode() : 0);
            result = 31 * result + (phone != null ? phone.hashCode() : 0);
            result = 31 * result + (preferredContact != null ? preferredContact.hashCode() : 0);
            result = 31 * result + (previouslyParticipated ? 1 : 0);
            result = 31 * result + (party != null ? party.hashCode() : 0);
            result = 31 * result + (canvassResponse != null ? canvassResponse.hashCode() : 0);
            return result;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.firstName);
            dest.writeString(this.lastName);
            dest.writeString(this.email);
            dest.writeString(this.phone);
            dest.writeString(this.preferredContact);
            dest.writeByte(previouslyParticipated ? (byte) 1 : (byte) 0);
            dest.writeString(this.party);
            dest.writeString(this.canvassResponse);
        }

        public Attributes() {
        }

        protected Attributes(Parcel in) {
            this.firstName = in.readString();
            this.lastName = in.readString();
            this.email = in.readString();
            this.phone = in.readString();
            this.preferredContact = in.readString();
            this.previouslyParticipated = in.readByte() != 0;
            this.party = in.readString();
            this.canvassResponse = in.readString();
        }

        public static final Creator<Attributes> CREATOR = new Creator<Attributes>() {
            public Attributes createFromParcel(Parcel source) {
                return new Attributes(source);
            }

            public Attributes[] newArray(int size) {
                return new Attributes[size];
            }
        };
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", attributes=" + attributes +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Person person = (Person) o;

        if (id != null ? !id.equals(person.id) : person.id != null) return false;
        if (type != null ? !type.equals(person.type) : person.type != null) return false;
        return !(attributes != null ? !attributes.equals(person.attributes) : person.attributes != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (attributes != null ? attributes.hashCode() : 0);
        return result;
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
    }

    public Person() {
    }

    protected Person(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.type = in.readString();
        this.attributes = in.readParcelable(Attributes.class.getClassLoader());
    }

    public static final Parcelable.Creator<Person> CREATOR = new Parcelable.Creator<Person>() {
        public Person createFromParcel(Parcel source) {
            return new Person(source);
        }

        public Person[] newArray(int size) {
            return new Person[size];
        }
    };
}
