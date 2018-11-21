package com.pherodev.datingapp.models;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.net.URL;
import java.util.ArrayList;

public class Person {

    public String userId;
    public String name;
    public String email;
    public URL profilePictureURL;
    public ArrayList<Conversation> conversations;
    public Location location;

//    private ArrayList<Issue> issues;
//    private ArrayList<Report> reports;

    public Person() { }

    public Person(String userId, String name, String email)
    {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.conversations = new ArrayList<>();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public URL getProfilePictureURL() {
        return profilePictureURL;
    }

    public void setProfilePictureURL(URL profilePictureURL) {
        this.profilePictureURL = profilePictureURL;
    }

    public ArrayList<Conversation> getConversations() {
        return conversations;
    }

    public void setConversations(ArrayList<Conversation> conversations) {
        this.conversations = conversations;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

}
