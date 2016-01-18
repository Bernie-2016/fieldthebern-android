/*
 * Copyright (c) 2016 - Bernie 2016, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
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

    transient boolean spokenTo = false;

    public boolean spokenTo() {
        return this.spokenTo;
    }

    public Person spokenTo(final boolean spokenTo) {
        this.spokenTo = spokenTo;
        return this;
    }


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

    public String fullName() {

        String first = (attributes.firstName == null) ? "" : attributes.firstName;
        String last = (attributes.lastName == null) ? "" : attributes.lastName;

        return first + " " + last;
    }

    public static Person copy(Person src) {
        Person copy = new Person();
        copy.id(src.id());
        copy.type(src.type());
        copy.spokenTo(src.spokenTo());

        Attributes srcAttributes = src.attributes();

        copy.attributes().previouslyParticipated(srcAttributes.previouslyParticipated());
        copy.attributes().firstName(srcAttributes.firstName());
        copy.attributes().lastName(srcAttributes.lastName());
        copy.attributes().email(srcAttributes.email());
        copy.attributes().phone(srcAttributes.phone());
        copy.attributes().party(srcAttributes.party());
        copy.attributes().party(srcAttributes.party());
        copy.attributes().canvassResponse(srcAttributes.canvassResponse());
        copy.attributes().preferredContact(srcAttributes.preferredContact());

        return copy;
    }

    public void update(Person src) {
        this.id(src.id());
        this.type(src.type());
        this.spokenTo(src.spokenTo());

        Attributes srcAttributes = src.attributes();

        this.attributes().previouslyParticipated(srcAttributes.previouslyParticipated());
        this.attributes().firstName(srcAttributes.firstName());
        this.attributes().lastName(srcAttributes.lastName());
        this.attributes().email(srcAttributes.email());
        this.attributes().phone(srcAttributes.phone());
        this.attributes().party(srcAttributes.party());
        this.attributes().canvassResponse(srcAttributes.canvassResponse());
        this.attributes().preferredContact(srcAttributes.preferredContact());
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

        @Contact.Method
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
        super.writeToParcel(dest, flags);
        dest.writeValue(this.id);
        dest.writeString(this.type);
        dest.writeParcelable(this.attributes, flags);
    }

    public Person() {
        type = TYPE;
    }

    protected Person(Parcel in) {
        super(in);
        type = TYPE;
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
