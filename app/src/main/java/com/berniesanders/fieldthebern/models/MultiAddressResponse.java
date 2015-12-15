package com.berniesanders.fieldthebern.models;

/*
 * Made with love by volunteers
 * Copyright 2015 BernieSanders.com, FeelTheBern.org, 
 * Coderly, LostPacketSoftware and the volunteers
 * License: GNU AGPLv3 - https://gnu.org/licenses/agpl.html 
 */

import com.google.gson.annotations.SerializedName;

import java.util.List;

/*
{
  "data":[
    {ApiAddress obj},
    {ApiAddress obj},
    {ApiAddress obj},
    {ApiAddress obj}
  ],
  "included":[
    {user}
  ]
}
 */
public class MultiAddressResponse {

    @SerializedName("data")
    List<ApiAddress> addresses;

    //I guess this is a list in case multiple users hav visited the house
    @SerializedName("included")
    List<User> users;

    public List<ApiAddress> addresses() {
        return this.addresses;
    }

    public List<User> users() {
        return this.users;
    }

    public MultiAddressResponse addresses(final List<ApiAddress> addresses) {
        this.addresses = addresses;
        return this;
    }

    public MultiAddressResponse users(final List<User> users) {
        this.users = users;
        return this;
    }


}
