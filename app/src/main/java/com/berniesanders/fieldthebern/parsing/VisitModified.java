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

package com.berniesanders.fieldthebern.parsing;

import com.berniesanders.fieldthebern.models.ApiAddress;
import com.berniesanders.fieldthebern.models.CanvassData;
import com.berniesanders.fieldthebern.models.Person;
import com.berniesanders.fieldthebern.models.Visit;

import java.util.ArrayList;
import java.util.List;

public class VisitModified {

    public static boolean wasModified(final List<Person> previousPeople, final Visit current) {

        // because we generated equals() and hashCode() methods for the Person model objects
        // we should be able to use Collection methods to see if the user updated the visit at all
        List<Person> visitPeople = new ArrayList<>();

        //need a separate array for comparison, we don't want to modify the array we're given
        List<Person> addressPeople = new ArrayList<>(previousPeople);

        for(CanvassData canvassData : current.included()) {
            if (canvassData.type().equals(Person.TYPE)) {
                Person person = (Person) canvassData;
                visitPeople.add(person);
                if (person.spokenTo()) {
                    return true;
                }
            }
        }

        addressPeople.removeAll(visitPeople);
        visitPeople.removeAll(previousPeople);

        return !addressPeople.isEmpty() || !visitPeople.isEmpty() ||
                ((ApiAddress)current.included().get(0)).attributes().bestCanvassResponse() != null;
    }

    public static boolean personAdded(final List<Person> previousPeople, final Visit current) {
        List<Person> addressPeople = new ArrayList<>(previousPeople);

        List<Person> visitPeople = new ArrayList<>();
        for (CanvassData canvassData : current.included()) {
            if (canvassData.type().equals(Person.TYPE)) {
                visitPeople.add((Person) canvassData);
            }
        }

        return addressPeople.size() < visitPeople.size();
    }
}
