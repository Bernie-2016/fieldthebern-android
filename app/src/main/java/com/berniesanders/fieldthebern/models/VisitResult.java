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

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/*
 * https://github.com/Bernie-2016/fieldthebern-api/wiki/API-Visits
 *
 * // response format
{
	"data":
	{
		"id":"116",
		"type":"visits",
		"attributes":
		{
			"total_points":15.0,
			"duration_sec":0,
			"created_at":"2015-12-03T23:39:49.177Z"
		},
		"relationships":{
		"user":{"data":{"id":"42","type":"users"}},
		"score":{"data":{"id":"79","type":"scores"}},
		"address_update":{"data":{"id":"89","type":"address_updates"}},
		"address":{"data":{"id":"56","type":"addresses"}},
		"person_updates":{"data":[{"id":"85","type":"person_updates"}]},
		"people":{"data":[{"id":"43","type":"people"}]}}
	},
	"included":[
		{
			"id":"79",
			"type":"scores",
			"attributes":
			{
				"points_for_updates":10,
				"points_for_knock":5
			},
			"relationships":
			{
				"visit":
				{
					"data":{"id":"116","type":"visits"}
				}
			}
		}
	]
}
 */

/**
 * Results model object for a visit.  This class only has getters.
 * See json in comment above.
 * https://github.com/Bernie-2016/fieldthebern-api/wiki/API-Visits
 */
public class VisitResult {

    Data data = new Data();
    List<Score> included = new ArrayList<>(); // Data for the associated score object.

    public Data data() {
        return data;
    }

    public List<Score> included() {
        return this.included;
    }

    public static class Data {

        Relationships relationships = new Relationships();
        Attributes attributes = new Attributes();


        public Relationships relationships() {
            return this.relationships;
        }

        public Attributes attributes() {
            return this.attributes;
        }



        public static class Attributes {
            //<duration_in_seconds>,
            @SerializedName("duration_sec")
            int durationSeconds;

            @SerializedName("total_points")
            int totalPoints;

            //<date_and_time_of_creation>
            //ISO-8601  2015-12-03T23:19:48.480Z
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
            PersonUpdates personUpdates;

            // tracks people updated by the visit
            PeopleUpdates people;

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

            public PersonUpdates personUpdates() {
                return this.personUpdates;
            }

            public PeopleUpdates people() {
                return this.people;
            }

            public Score score() {
                return this.score;
            }

            public static class PersonUpdates {
                List<Person> data;
            }

            public static class PeopleUpdates {
                List<Person> data;
            }
        }

    }

}
