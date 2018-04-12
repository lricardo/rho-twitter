package pt.rhosystems.rhotwitter.models;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Tweet {
    private static final long serialVersionUID = 1L;

    private String createdAt;
    private String idStr;
    private String text;
    private User user;

    public Tweet(String createdAt, String idStr, String text, User user) {
        this.createdAt = createdAt;
        this.idStr = idStr;
        this.text = text;
        this.user = user;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getIdStr() {
        return idStr;
    }

    public String getText() {
        return text;
    }

    public User getUser() {
        return user;
    }

    public static Tweet fromJsonObject (JsonObject jsonObject) {
        User user = new User(
                jsonObject.getAsJsonObject("user").get("name").getAsString(),
                jsonObject.getAsJsonObject("user").get("screen_name").getAsString()
        );
        Tweet tweet = new Tweet(
                jsonObject.get("created_at").getAsString(),
                jsonObject.get("id_str").getAsString(),
                jsonObject.get("text").getAsString(),
                user
        );

        return tweet;
    }
}

