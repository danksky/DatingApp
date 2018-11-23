package com.pherodev.datingapp.models;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public class TextMessage extends DateMessage {

    public String text;

    private static String TEXT_KEY = "text";

    public TextMessage() {}

    public TextMessage(String text) {
        this.text = text;
    }

    private TextMessage(Parcel in) {
        Bundle textMessageBundle = in.readBundle(getClass().getClassLoader());
        text  = textMessageBundle.getString(TEXT_KEY);
    }

    public static final Parcelable.Creator<TextMessage> CREATOR
            = new Parcelable.Creator<TextMessage>() {
        public TextMessage createFromParcel(Parcel in) {
            return new TextMessage(in);
        }

        public TextMessage[] newArray(int size) {
            return new TextMessage[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        Bundle textMessageBundle = new Bundle();
        textMessageBundle.putString(TEXT_KEY, text);
        dest.writeBundle(textMessageBundle);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


}
