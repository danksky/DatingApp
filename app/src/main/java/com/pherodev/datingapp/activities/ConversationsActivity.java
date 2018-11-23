package com.pherodev.datingapp.activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.pherodev.datingapp.R;
import com.pherodev.datingapp.adapters.ConversationsAdapter;
import com.pherodev.datingapp.models.Conversation;
import com.pherodev.datingapp.models.DateMessage;
import com.pherodev.datingapp.models.Person;
import com.pherodev.datingapp.models.TextMessage;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

public class ConversationsActivity extends AppCompatActivity {

    private final static String TAG = "Conversations";

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    private RecyclerView conversationsRecyclerView;
    private RecyclerView.LayoutManager conversationsLayoutManager;
    private ConversationsAdapter conversationsAdapter;

    private ArrayList<Conversation> conversations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversations);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        conversations = new ArrayList<>();
        populateConversations();

        conversationsRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_conversations);
        conversationsRecyclerView.setHasFixedSize(true);
        conversationsLayoutManager = new LinearLayoutManager(this);
        conversationsAdapter = new ConversationsAdapter(conversations);
        conversationsRecyclerView.setLayoutManager(conversationsLayoutManager);
        conversationsRecyclerView.setAdapter(conversationsAdapter);
    }

    private void populateConversations() {
        // TODO: Replace aaronId with firebaseAuth.getCurrentUser.getUid()
        String aaronId = "fc85b6d3-5b55-46d2-8578-31f2bdba29f0";
        // TODO: Implement conversations collection, turning Person's conversations into an array of String conversationIds
        firebaseFirestore.collection("users").document(aaronId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "fetchConversations:success");
                            ArrayList<Conversation> convoList = task.getResult().toObject(Person.class).conversations;
                            conversations.clear();
                            conversations.addAll(convoList);
                            conversationsAdapter.notifyDataSetChanged();
                        } else {
                            Log.e(TAG, "fetchConversations:"+task.getException().getMessage());
                        }
                    }
                });

        // TODO: Implement local cache of messages
    }

}
