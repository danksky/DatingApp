package com.pherodev.datingapp.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pherodev.datingapp.R;
import com.pherodev.datingapp.models.DateMessage;

import java.util.ArrayList;

public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.ChatViewHolder> {

    private ArrayList<DateMessage> chats;

    public ChatsAdapter(ArrayList<DateMessage> chats) {
        this.chats = chats;
    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_item, parent, false);
        ChatViewHolder cvh = new ChatViewHolder(v);
        return cvh;
    }

    @Override
    public void onBindViewHolder(ChatViewHolder holder, int position) {
        holder.authorNameTextView.setText(chats.get(position).authorName);
        holder.contentTextView.setText(chats.get(position).text);
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        public TextView authorNameTextView;
        public TextView contentTextView;

        public ChatViewHolder(final View itemView) {
            super(itemView);
            authorNameTextView = (TextView) itemView.findViewById(R.id.text_view_chat_item_author);
            contentTextView  = (TextView) itemView.findViewById(R.id.text_view_chat_item_content);
        }
    }


}
