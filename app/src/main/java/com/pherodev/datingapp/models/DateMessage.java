package com.pherodev.datingapp.models;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Date;

public class DateMessage implements Parcelable {

    public String author;
    public Date sent;

    private static String AUTHOR_KEY = "author";
    private static String SENT_KEY = "time_sent";

    // For Firebase

    public DateMessage() {}

    private DateMessage(Parcel in) {
        Bundle dateMessageBundle = in.readBundle(getClass().getClassLoader());
        author = dateMessageBundle.getString(AUTHOR_KEY);
        sent = (Date) dateMessageBundle.getSerializable(SENT_KEY);
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
        dateMessageBundle.putString(AUTHOR_KEY, author);
        dateMessageBundle.putSerializable(SENT_KEY, sent);
        dest.writeBundle(dateMessageBundle);
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getSent() {
        return sent;
    }

    public void setSent(Date sent) {
        this.sent = sent;
    }

}
