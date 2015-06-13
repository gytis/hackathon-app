package hackathon.app.api;

/**
 * Created by don on 6/13/15.
 */
public class User {

    Long id;
    String facebookId;
    String firstName;
    String lastName;
    Boolean registered;
    String urlAvatar;

    public Long getId() {
        return id;
    }
    public String getFacebookId() {
        return facebookId;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public Boolean getRegistered() {
        return registered;
    }
    public String getUrlAvatar() {
        return urlAvatar;
    }
}
