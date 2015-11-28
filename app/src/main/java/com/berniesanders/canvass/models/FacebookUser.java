package com.berniesanders.canvass.models;

import com.facebook.AccessToken;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 {
     "id":"123456",
     "first_name":"Bernie",
     "last_name":"Sanders",
     "picture":{
        "data":{
            "is_silhouette":false,
            "url":"https://facebook.com/123456"
        }
     },
     "email":"bernie@example.com",
     "friends":{
        "data":[
             {
                 "name": "Cool Guy",
                 "id": "00000"
             },
             {
                 "name": "Cool Gal",
                 "id": "00000"
             }
        ],
        "paging": {
            "next": "https://graph.facebook.com/123456"
        },
        "summary":{
            "total_count":84
        }
     }
 }
 */
public class FacebookUser {

    String id;
    @SerializedName("first_name")
    String firstName;
    @SerializedName("last_name")
    String lastName;
    String email;
    Friends friends;
    Picture picture;

    public static class Picture {
        Data data;
        public static class Data {
            boolean is_silhouette;
            String url;
        }
    }

    public static class Friends {
        @SerializedName("data")
        List<FacebookFriend> facebookFriends;
        Paging paging;
        Summary summary;

        public static class Paging {
            @SerializedName("next")
            String nextPageUrl;
        }
        public static class Summary {
            @SerializedName("total_count")
            int totalCount;
        }
    }

    public UserAttributes convertToApiUser() {
        return new UserAttributes()
                .firstName(firstName)
                .lastName(lastName)
                .facebookId(id)
                .facebookAccessToken(AccessToken.getCurrentAccessToken().getToken())
                .email(email)
                .photoLargeUrl(picture.data.url) //TODO need to base64 encode
                .setAsFacebookUser(id);
    }
}
