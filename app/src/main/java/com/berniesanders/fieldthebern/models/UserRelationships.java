package com.berniesanders.fieldthebern.models;

/**
 *
 */
public class UserRelationships {

    UserVisit visits;
    UserFollowers followers;
    UsersFollowing following;

    @Override
    public String toString() {
        return "UserRelationships{" +
                "visits=" + visits +
                ", followers=" + followers +
                ", following=" + following +
                '}';
    }
}
