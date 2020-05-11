package org.mana.utils;

import org.mana.db.entity.User;

public class CurrentUser {
    private static User INSTANCE = null;

    private CurrentUser() { }

    public static User getInstance() throws IllegalStateException{
        if (INSTANCE == null)
            throw new IllegalStateException("No instance available");

        return INSTANCE;
    }

    public static void setInstance(User user) throws IllegalArgumentException, IllegalStateException {
        if (INSTANCE != null)
            throw new IllegalStateException("Already Instantiated");
        if (user == null)
            throw new IllegalArgumentException("User cannot be null");

        INSTANCE = user;
    }

    public static void updateInstance(User user) throws IllegalArgumentException, IllegalStateException {
        if (INSTANCE == null) {
            throw new IllegalStateException("No instance available");
        }

        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        INSTANCE = user;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
