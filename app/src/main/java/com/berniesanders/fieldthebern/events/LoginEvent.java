package com.berniesanders.fieldthebern.events;

import android.support.annotation.StringDef;

import com.berniesanders.fieldthebern.models.User;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class LoginEvent {

    public static final String LOGIN = "LoginEvent.LOGIN";
    public static final String LOGOUT = "LoginEvent.LOGOUT";

    private final String eventType;
    private final User user;

    @StringDef({LOGIN, LOGOUT})

    @Retention(RetentionPolicy.SOURCE)
    public @interface EventType{}

    public LoginEvent(@EventType String eventType, User user) {
        this.eventType = eventType;

        this.user = user;
    }

    @EventType
    public String getEventType() {
        return eventType;
    }

    public User getUser() {
        return user;
    }
}
