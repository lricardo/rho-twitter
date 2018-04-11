package pt.rhosystems.rhotwitter.models;

import com.google.gson.annotations.SerializedName;

public class User {
    private static final long serialVersionUID = 1L;

    private String name;
    private String screenName;

    public User(String name, String screenName) {
        this.name = name;
        this.screenName = screenName;
    }

    public String getName() {
        return name;
    }

    public String getScreenName() {
        return screenName;
    }
}
