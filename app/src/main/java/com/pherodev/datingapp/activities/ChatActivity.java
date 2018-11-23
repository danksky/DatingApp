package com.pherodev.datingapp.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.pherodev.datingapp.R;
import com.pherodev.datingapp.adapters.ChatsAdapter;
import com.pherodev.datingapp.models.Conversation;
import com.pherodev.datingapp.models.DateMessage;

import java.util.ArrayList;

import static com.pherodev.datingapp.adapters.ConversationsAdapter.CONVERSATION_KEY;

public class ChatActivity extends AppCompatActivity {

    private static final String TAG = "Chat";

    private RecyclerView chatsRecyclerView;
    private RecyclerView.LayoutManager chatsLayoutManager;
    private ChatsAdapter chatsAdapter;

    private ArrayList<DateMessage> messages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        populateMessages();

        // TODO: Replace with Exception handling.
        if (messages == null)
            return;

        chatsRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_messages);
        chatsRecyclerView.setHasFixedSize(true);
        chatsLayoutManager = new LinearLayoutManager(this);
        chatsAdapter = new ChatsAdapter(messages);
        chatsRecyclerView.setLayoutManager(chatsLayoutManager);
        chatsRecyclerView.setAdapter(chatsAdapter);
    }

    private void populateMessages() {
        if (getIntent() != null || getIntent().getExtras() != null) {
            Bundle personBundle = getIntent().getBundleExtra(CONVERSATION_KEY);
            messages = ((Conversation) personBundle.getParcelable(CONVERSATION_KEY)).messages;
        } else {
            String error = "Intent (" + (getIntent() == null ? "null" : "non-null") + ")" +
                    "\t" +
                    "Extras (" + (getIntent() == null || getIntent().getExtras() == null ? "null" : "non-null") + ")";
            Log.e(TAG, error);
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
        }
    }
}
