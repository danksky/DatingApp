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
import com.pherodev.datingapp.R;
import com.pherodev.datingapp.adapters.ConversationsAdapter;
import com.pherodev.datingapp.models.Conversation;
import com.pherodev.datingapp.models.Person;

import java.util.ArrayList;

public class ConversationsActivity extends AppCompatActivity {

    private final static String TAG = "Conversations";

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    private RecyclerView conversationsRecyclerView;
    private RecyclerView.LayoutManager conversationsLayoutManager;
    private ConversationsAdapter conversationsAdapter;

    private ArrayList<String> conversationsIds;
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
        conversationsAdapter = new ConversationsAdapter(conversations, this);
        conversationsRecyclerView.setLayoutManager(conversationsLayoutManager);
        conversationsRecyclerView.setAdapter(conversationsAdapter);
    }

    /**
    private void populateConversationIds() {
        if (getIntent() != null || getIntent().getExtras() != null) {
            Bundle personBundle = getIntent().getBundleExtra(USER_PERSON_KEY);
            conversationsIds = ((Person) personBundle.getParcelable(USER_PERSON_KEY)).conversationIds;
        } else {
            String error = "Intent (" + (getIntent() == null ? "null" : "non-null") + ")" +
                    "\t" +
                    "Extras (" + (getIntent() == null || getIntent().getExtras() == null ? "null" : "non-null") + ")";
            Log.e(TAG, error);
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
        }
    }
    */

    private void populateConversations() {
        // TODO: Replace aaronId with firebaseAuth.getCurrentUser.getUid() or userPersonBundle's id (see comment above)
        String aaronId = "31be09d3-5bed-488d-9004-99925f4f1776";
        firebaseFirestore.collection("users").document(aaronId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "fetchConversations:success");
                            // TODO: Add null checker
                            conversationsIds = task.getResult().toObject(Person.class).conversationIds;
                            conversations.clear();
                            for (int i = 0; i < conversationsIds.size(); i++)
                                conversations.add(null);
                            for (int i = 0; i < conversationsIds.size(); i++)
                                populateConversation(i, conversationsIds.get(i));
                        } else {
                            Log.e(TAG, "fetchConversations:"+task.getException().getMessage());
                        }
                    }
                });

        // TODO: Implement local cache of messages
    }

    private void populateConversation(final int index, String conversationId) {
        firebaseFirestore.collection("conversations").document(conversationId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "populateConversation:success");
                            Conversation conversation = task.getResult().toObject(Conversation.class);
                            conversations.set(index, conversation);
                            conversationsAdapter.notifyDataSetChanged();
                        } else {
                            Log.e(TAG, "populateConversation:" + task.getException().getMessage());
                        }
                    }
                });
    }

}
