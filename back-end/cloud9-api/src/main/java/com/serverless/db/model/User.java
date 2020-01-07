package com.serverless.db.model;

import com.amazonaws.services.cognitoidp.model.AttributeType;
import com.amazonaws.services.cognitoidp.model.UserType;

public class User {
    private String id;
    private String login;
    private String email;
    private String name;
    private String surname;
    private String profile;

    public User(String id, String login, String email, String name, String surname, String profile) {
        this.id = id;
        this.login = login;
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.profile = profile;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public static String getSub(UserType userType) {
        AttributeType attribute = userType.getAttributes().stream()
                .filter(attributeType -> attributeType.getName().equals("sub"))
                .findAny()
                .orElse(null);
        if (attribute != null) {
            return attribute.getValue();
        }

        return null;
    }

    public static User fromUserType(UserType userType) {
        String email = "", name = "", surname = "", id = "",  profile ="";
        for (AttributeType attribute : userType.getAttributes()) {
            if (attribute.getName().equals("sub")) {
                id = attribute.getValue();
            }
            if (attribute.getName().equals("email")) {
                email = attribute.getValue();
            }
            if (attribute.getName().equals("name")) {
                name = attribute.getValue();
            }
            if (attribute.getName().equals("family_name")) {
                surname = attribute.getValue();
            }
            if (attribute.getName().equals("profile")) {
                profile = attribute.getValue();
            }
        }

        return new User(id, userType.getUsername(), email, name, surname, profile);
    }
}
