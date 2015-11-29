/*
 * Made with love by volunteers
 * Copyright 2015 BernieSanders.com, FeelTheBern.org,
 * Coderly, LostPacketSoftware and the volunteers
 * License: GNU AGPLv3 - https://gnu.org/licenses/agpl.html
 */
package com.berniesanders.fieldthebern.models;


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
public class Person extends CanvasData{

    Integer id; //null if we're creating a new person in the db
    String type;
    Attributes attributes = new Attributes();

    public Integer id() {
        return this.id;
    }

    public String type() {
        return this.type;
    }

    public Attributes attributes() {
        return this.attributes;
    }

    public Person id(final Integer id) {
        this.id = id;
        return this;
    }

    public Person type(final String type) {
        this.type = type;
        return this;
    }

    public Person attributes(final Attributes attributes) {
        this.attributes = attributes;
        return this;
    }


    public static class Attributes {

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

        public String party() {
            return this.party;
        }

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

        public Attributes canvassResponse(@CanvasResponse.Response final String canvassResponse) {
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
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", attributes=" + attributes +
                '}';
    }
}
