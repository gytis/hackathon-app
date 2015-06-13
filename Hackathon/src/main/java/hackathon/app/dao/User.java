package hackathon.app.dao;

/**
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
public class User {

    private final int id;

    private final String facebookId;

    private final boolean registered;

    private final String facebookAvatar;

    public User(final int id, final String facebookId, final boolean registered, final String facebookAvatar) {
        this.id = id;
        this.facebookId = facebookId;
        this.registered = registered;
        this.facebookAvatar = facebookAvatar;
    }

    public int getId() {
        return id;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public boolean isRegistered() {
        return registered;
    }

    public String getFacebookAvatar() {
        return facebookAvatar;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", facebookId='" + facebookId + '\'' +
                ", registered=" + registered +
                ", facebookAvatar='" + facebookAvatar + '\'' +
                '}';
    }
}
