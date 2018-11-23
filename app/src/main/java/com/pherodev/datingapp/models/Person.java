package com.pherodev.datingapp.models;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;

public class Person implements Parcelable {

    public String userId;
    public String name;
    public String email;
    public URL profilePictureURL;
    public ArrayList<Conversation> conversations;
    public Location location;

    // Bundle keys that are just the names of member variables
    private static String ID_KEY = "userId";
    private static String NAME_KEY = "name";
    private static String EMAIL_KEY = "email";
    private static String PPURL_KEY = "profilePictureURL";
    private static String CONVERSATIONS_KEY = "conversations";
    private static String LOCATION_KEY = "location";

//    private String bio;
//    private ArrayList<Issue> issues;
//    private ArrayList<Report> reports;

    // For Firebase
    public Person() { }

    public Person(String userId, String name, String email)
    {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.conversations = new ArrayList<>();
    }

    // For Parcelable
    private Person(Parcel in) {
        Bundle personBundle = in.readBundle(getClass().getClassLoader());
        this.userId = personBundle.getString(ID_KEY);
        this.name = personBundle.getString(NAME_KEY);
        this.email = personBundle.getString(EMAIL_KEY);
        this.conversations = personBundle.getParcelableArrayList(CONVERSATIONS_KEY);
    }

    public static final Parcelable.Creator<Person> CREATOR
            = new Parcelable.Creator<Person>() {
        public Person createFromParcel(Parcel in) {
            return new Person(in);
        }
        public Person[] newArray(int size) {
            return new Person[size];
        }
    };

    // Parcelable implementation methods

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Bundle personBundle = new Bundle();
        personBundle.putString(ID_KEY, userId);
        personBundle.putString(NAME_KEY, name);
        personBundle.putString(EMAIL_KEY, email);
        personBundle.putSerializable(PPURL_KEY, profilePictureURL);
        personBundle.putParcelableArrayList(CONVERSATIONS_KEY, conversations);
        personBundle.putParcelable(LOCATION_KEY, location);
        dest.writeBundle(personBundle);
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
