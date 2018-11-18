package com.pherodev.datingapp.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.pherodev.datingapp.R;
import com.pherodev.datingapp.adapters.ConversationsAdapter;
import com.pherodev.datingapp.models.Conversation;
import com.pherodev.datingapp.models.DateMessage;
import com.pherodev.datingapp.models.Person;
import com.pherodev.datingapp.models.TextMessage;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

public class ConversationsActivity extends AppCompatActivity {

    private RecyclerView conversationsRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ConversationsAdapter conversationsAdapter;

    private Person p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversations);
        try {
            p = fakeInit();
            conversationsRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_conversations);
            conversationsRecyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(this);
            conversationsRecyclerView.setLayoutManager(layoutManager);
            conversationsAdapter = new ConversationsAdapter(p.conversations);
            conversationsRecyclerView.setAdapter(conversationsAdapter);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public Person fakeInit() throws MalformedURLException {
        Person p1 = new Person();
        p1.name = "Daniel";
        p1.profilePictureURL = new URL("https://via.placeholder.com/50");

        Person p2 = new Person();
        p2.name = "Adam";
        p2.profilePictureURL = new URL("https://via.placeholder.com/50");

        Conversation c1 = new Conversation();
        c1.conversers.add(p1);
        c1.conversers.add(p2);

        TextMessage t1 = new TextMessage();
        t1.author = p1;
        t1.sent = new Date();
        t1.text = "Hello";

        TextMessage t2 = new TextMessage();
        t2.author = p2;
        t2.sent = new Date();
        t2.text = "What's up?";

        c1.messages.add(t1);
        c1.messages.add(t2);

        Conversation c2 = new Conversation();
        c2.conversers.add(p1);
        c2.conversers.add(p2);

        TextMessage t3 = new TextMessage();
        t3.author = p1;
        t3.sent = new Date();
        t3.text = "Hello2";

        TextMessage t4 = new TextMessage();
        t4.author = p2;
        t4.sent = new Date();
        t4.text = "What's up?2";

        c2.messages.add(t1);
        c2.messages.add(t2);

        p1.conversations.add(c1);
        p1.conversations.add(c2);

        p2.conversations.add(c1);
        p2.conversations.add(c2);

        return p1;
    }
}
