package com.pherodev.datingapp.activities;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.pherodev.datingapp.R;
import com.pherodev.datingapp.adapters.ChatsAdapter;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView chatRecyclerView;
    private RecyclerView.LayoutManager chatsLayoutManager;
    private ChatsAdapter chatsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
    }
}
