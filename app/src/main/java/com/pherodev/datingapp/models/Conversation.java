package com.pherodev.datingapp.models;

import java.util.ArrayList;

public class Conversation {

    public ArrayList<Person> conversers;
    public ArrayList<DateMessage> messages;

    public Conversation() {
        conversers = new ArrayList<>();
        messages = new ArrayList<>();
    }

}
