package com.example.myapplication.model;

public class User {
    String Name, Email, UserId, ProfilePicture;

    public User() {
    }

    public User(String name, String email, String userId, String profilePicture) {
        Name = name;
        Email = email;
        UserId = userId;
        ProfilePicture = profilePicture;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getProfilePicture() {
        return ProfilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        ProfilePicture = profilePicture;
    }
}
