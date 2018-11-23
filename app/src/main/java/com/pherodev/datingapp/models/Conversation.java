package com.pherodev.datingapp.models;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Conversation implements Parcelable {

    public String conversationId;

    public ArrayList<String> conversers;
    public ArrayList<DateMessage> messages;

    private static String CONVERSERS_KEY = "conversers";
    private static String MESSAGES_KEY = "messages";

    public Conversation() {
        this.conversers = new ArrayList<>();
        this.messages = new ArrayList<>();
    }

    private Conversation(Parcel in) {
        Bundle conversationBundle = in.readBundle(getClass().getClassLoader());
        this.conversers = conversationBundle.getStringArrayList(CONVERSERS_KEY);
        this.messages = conversationBundle.getParcelableArrayList(MESSAGES_KEY);
    }

    public static final Parcelable.Creator<Conversation> CREATOR
            = new Parcelable.Creator<Conversation>() {
        public Conversation createFromParcel(Parcel in) {
            return new Conversation(in);
        }

        public Conversation[] newArray(int size) {
            return new Conversation[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Bundle conversationBundle = new Bundle();
        conversationBundle.putStringArrayList(CONVERSERS_KEY, conversers);
        conversationBundle.putParcelableArrayList(MESSAGES_KEY, messages);
        dest.writeBundle(conversationBundle);
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public ArrayList<String> getConversers() {
        return conversers;
    }

    public void setConversers(ArrayList<String> conversers) {
        this.conversers = conversers;
    }

    public ArrayList<DateMessage> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<DateMessage> messages) {
        this.messages = messages;
    }

}
