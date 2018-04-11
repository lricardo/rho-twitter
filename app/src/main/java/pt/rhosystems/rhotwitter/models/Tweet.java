package pt.rhosystems.rhotwitter.models;

public class Tweet {
    private String created_at;
    private String id_str;
    private String text;
    private User user;

    public Tweet(String created_at, String id_str, String text, User user) {
        this.created_at = created_at;
        this.id_str = id_str;
        this.text = text;
        this.user = user;
    }

    public String getCreatedAt() {
        return created_at;
    }

    public String getIdStr() {
        return id_str;
    }

    public String getText() {
        return text;
    }

    public User getUser() {
        return user;
    }
}

