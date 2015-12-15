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
public class SingleAddressResponse {

    @SerializedName("data")
    List<ApiAddress> addresses;

    //I guess this is a list in case multiple users hav visited the house
    @SerializedName("included")
    List<CanvassData> included;

    public List<ApiAddress> addresses() {
        return this.addresses;
    }

    public List<CanvassData> included() {
        return this.included;
    }

    public SingleAddressResponse addresses(final List<ApiAddress> addresses) {
        this.addresses = addresses;
        return this;
    }

    public SingleAddressResponse included(final List<CanvassData> included) {
        this.included = included;
        return this;
    }


}
