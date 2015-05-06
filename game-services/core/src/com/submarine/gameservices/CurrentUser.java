package com.submarine.gameservices;

/**
 * Created by mariam on 5/6/15.
 */
public class CurrentUser {

    private static CurrentUser instance = null;

    public String name;
    public String photoUrl;

    public static CurrentUser getInstance() {
        if (instance == null) {
            instance = new CurrentUser();
        }
        return instance;
    }

    public void init(String name, String photoUrl) {
        this.name = name;
        this.photoUrl = photoUrl;
    }

}
