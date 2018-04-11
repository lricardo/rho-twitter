package pt.rhosystems.rhotwitter.models;

public class User {
    private String name;
    private String screen_name;

    public User(String name, String screen_name) {
        this.name = name;
        this.screen_name = screen_name;
    }

    public String getName() {
        return name;
    }

    public String getScreenName() {
        return screen_name;
    }
}
