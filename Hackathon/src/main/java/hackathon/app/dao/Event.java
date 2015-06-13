package hackathon.app.dao;

/**
 * Created by don on 6/13/15.
 */
public class Event {

    Long id;
    String name;
    String description;
    String postcode;
    String houseName;
    String street;
    String town;
    Boolean disabledAccess;
    String email;
    String info;

    public Event(Long id, String name, String description, String postcode, String houseName, String street, String town, Boolean disabledAccess, String email, String info) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.postcode = postcode;
        this.houseName = houseName;
        this.street = street;
        this.town = town;
        this.disabledAccess = disabledAccess;
        this.email = email;
        this.info = info;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPostcode() {
        return postcode;
    }

    public String getHouseName() {
        return houseName;
    }

    public String getStreet() {
        return street;
    }

    public String getTown() {
        return town;
    }

    public Boolean getDisabledAccess() {
        return disabledAccess;
    }

    public String getEmail() {
        return email;
    }

    public String getInfo() {
        return info;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", postcode='" + postcode + '\'' +
                ", houseName='" + houseName + '\'' +
                ", street='" + street + '\'' +
                ", town='" + town + '\'' +
                ", disabledAccess=" + disabledAccess +
                ", email='" + email + '\'' +
                ", info='" + info + '\'' +
                '}';
    }
}
