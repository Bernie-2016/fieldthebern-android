package com.berniesanders.fieldthebern.models;

/*
 * Made with love by volunteers
 * Copyright 2015 BernieSanders.com, FeelTheBern.org,
 * Coderly, LostPacketSoftware and the volunteers
 * License: GNU AGPLv3 - https://gnu.org/licenses/agpl.html 
 */


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/*
 * https://github.com/Bernie-2016/fieldthebern-api/wiki/API-Visits
 *
 * // response format
{
   data: {
      attributes: {
           duration_sec: <duration_in_seconds>,
           total_points: <total_points>,
           created_at: <date_and_time_of_creation>
      },
      relationships: {
         // links the visit to the user who created it
         user: {
             data: {
               id: <user_id>,
               type: 'users'
           }
         }
         // tracks when and in what way the address was updated by the visit
         address_update: {
             data: {
               id: <address_update_id>,
               type: 'address_updates'
             }
         },
         // tracks the address that was updated
         address: {
             data: {
               id: <address_id>,
               type: 'addresses'
             }
         }
         // tracks when, in what way and which people were updated by the visit
         person_updates: {
             data: [
                 // once for each updated or created person
                 {
                   id: <person_update_id>,
                   type: 'person_updates'
                 }
             ]
         }
         // tracks people updated by the visit
         people: {
             data: [
                 // once for each updated or created person
                 {
                   id: <person_id>,
                   type: 'people'
                 }
             ],
         //tracks the associated score
         score: {
             data: {
               id: <score_id>,
               type: 'scores'
             }
           }
         }
      },
      included: [
         // Data for the associated score object.
         {
             id: <score_id>
             type: 'scores',
             attributes: {
                 points_for_updates: <points_for_updates>,
                 points_for_knock: <points_for_knock>
             },
             relationships: {
                 visit: {
                     data: { id: <visit_id>, type: 'visits' } }
                 }
         }
      ]
   }
}
 */

/**
 * Results model object for a visit.  This class only has getters.
 * See json in comment above.
 * https://github.com/Bernie-2016/fieldthebern-api/wiki/API-Visits
 */
public class VisitResult {

    Data data = new Data();

    public Data data() {
        return data;
    }

    public static class Data {

        Relationships relationships = new Relationships();
        Attributes attributes = new Attributes();
        List<Score> included = new ArrayList<>(); // Data for the associated score object.

        public Relationships relationships() {
            return this.relationships;
        }

        public Attributes attributes() {
            return this.attributes;
        }

        public List<Score> included() {
            return this.included;
        }


        public static class Attributes {
            //<duration_in_seconds>,
            @SerializedName("duration_sec")
            int durationSeconds;

            @SerializedName("total_points")
            int totalPoints;

            //<date_and_time_of_creation>
            //TODO what format is this?
            @SerializedName("created_at")
            String createdAt;

            public int durationSeconds() {
                return this.durationSeconds;
            }

            public int totalPoints() {
                return this.totalPoints;
            }

            public String createdAt() {
                return this.createdAt;
            }
        }
        

        public static class Relationships {
            // links the visit to the user who created it
            User user;

            // tracks when and in what way the address was updated by the visit
            @SerializedName("address_update")
            ApiAddress addressUpdate;

            // tracks the address that was updated
            ApiAddress address;

            // tracks when, in what way and which people were updated by the visit
            @SerializedName("person_updates")
            List<Person> personUpdates;

            // tracks people updated by the visit
            List<Person> people;

            //tracks the associated score
            Score score;

            public User user() {
                return this.user;
            }

            public ApiAddress addressUpdate() {
                return this.addressUpdate;
            }

            public ApiAddress address() {
                return this.address;
            }

            public List<Person> personUpdates() {
                return this.personUpdates;
            }

            public List<Person> people() {
                return this.people;
            }

            public Score score() {
                return this.score;
            }
        }

    }

}
