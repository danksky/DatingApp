package com.pherodev.datingapp.models;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class DateMessage implements Parcelable {

    public String authorName;
    public Date sent;
    public String text;

    private static String AUTHOR_KEY = "authorName";
    private static String SENT_KEY = "time_sent";
    private static String TEXT_KEY = "text";

    // For Firebase

    public DateMessage() {}

    private DateMessage(Parcel in) {
        Bundle dateMessageBundle = in.readBundle(getClass().getClassLoader());
        authorName = dateMessageBundle.getString(AUTHOR_KEY);
        sent = (Date) dateMessageBundle.getSerializable(SENT_KEY);
        text = dateMessageBundle.getString(TEXT_KEY);
    }

    public static final Parcelable.Creator<DateMessage> CREATOR
            = new Parcelable.Creator<DateMessage>() {
        public DateMessage createFromParcel(Parcel in) {
            return new DateMessage(in);
        }

        public DateMessage[] newArray(int size) {
            return new DateMessage[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Bundle dateMessageBundle = new Bundle();
        dateMessageBundle.putString(AUTHOR_KEY, authorName);
        dateMessageBundle.putSerializable(SENT_KEY, sent);
        dateMessageBundle.putString(TEXT_KEY, text);

        dest.writeBundle(dateMessageBundle);
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public Date getSent() {
        return sent;
    }

    public void setSent(Date sent) {
        this.sent = sent;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
