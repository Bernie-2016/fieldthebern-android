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
                visitPeople.add((Person) canvassData);
            }
        }

        addressPeople.removeAll(visitPeople);
        visitPeople.removeAll(previousPeople);

        return !addressPeople.isEmpty() || !visitPeople.isEmpty() ||
                ((ApiAddress)current.included().get(0)).attributes().bestCanvassResponse() != null;
    }
}
