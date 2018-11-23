package com.pherodev.datingapp.models;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Conversation implements Parcelable {

    public String conversationId;

    public ArrayList<String> converserIds;
    public ArrayList<String> converserNames;
    public ArrayList<DateMessage> messages;

    private static String CONVERSER_IDS_KEY = "converserIds";
    private static String CONVERSER_NAMES_KEY = "converserNames";
    private static String MESSAGES_KEY = "messages";

    public Conversation() {
        this.converserIds = new ArrayList<>();
        this.converserNames = new ArrayList<>();
        this.messages = new ArrayList<>();
    }

    private Conversation(Parcel in) {
        Bundle conversationBundle = in.readBundle(getClass().getClassLoader());
        this.converserIds = conversationBundle.getStringArrayList(CONVERSER_IDS_KEY);
        this.converserNames = conversationBundle.getStringArrayList(CONVERSER_NAMES_KEY);
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
        conversationBundle.putStringArrayList(CONVERSER_IDS_KEY, converserIds);
        conversationBundle.putStringArrayList(CONVERSER_NAMES_KEY, converserNames);
        conversationBundle.putParcelableArrayList(MESSAGES_KEY, messages);
        dest.writeBundle(conversationBundle);
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public ArrayList<String> getConverserIds() {
        return converserIds;
    }

    public void setConverserIds(ArrayList<String> converserIds) {
        this.converserIds = converserIds;
    }

    public ArrayList<String> getConverserNames() {
        return converserNames;
    }

    public void setConverserNames(ArrayList<String> converserNames) {
        this.converserNames = converserNames;
    }

    public ArrayList<DateMessage> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<DateMessage> messages) {
        this.messages = messages;
    }

}
