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

    public String name;
    public URL profilePictureURL;
    public ArrayList<Conversation> conversations;
    public Location location;

    public Person() {
        conversations = new ArrayList<>();
    }

}
