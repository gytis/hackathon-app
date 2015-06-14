package hackathon.app.dao;

/**
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
public class User {

    private final long id;

    private final String facebookId;

    private final String name;

    private final boolean registered;

    private final String facebookAvatar;

    public User(final long id, final String facebookId, final String name, final boolean registered, final String facebookAvatar) {
        this.id = id;
        this.facebookId = facebookId;
        this.name = name;
        this.registered = registered;
        this.facebookAvatar = facebookAvatar;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
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
                ", name='" + name + '\'' +
                ", registered=" + registered +
                ", facebookAvatar='" + facebookAvatar + '\'' +
                '}';
    }
}
