package com.pherodev.datingapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pherodev.datingapp.R;
import com.pherodev.datingapp.activities.ChatActivity;
import com.pherodev.datingapp.models.Conversation;
import com.pherodev.datingapp.models.DateMessage;

import java.util.ArrayList;

public class ConversationsAdapter extends RecyclerView.Adapter<ConversationsAdapter.ConversationViewHolder> {

    public final static String CONVERSATION_KEY = "conversation";

    private ArrayList<Conversation> conversations;
    private Context context;

    public ConversationsAdapter(ArrayList<Conversation> conversations, Context context) {
        this.conversations = conversations;
        this.context = context;
    }

    @Override
    public ConversationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.conversation_item, parent, false);
        ConversationViewHolder cvh = new ConversationViewHolder(v);
        return cvh;
    }

    @Override
    public void onBindViewHolder(ConversationViewHolder holder, final int position) {
        holder.correspondentTextView.setText(conversations.get(position).converserNames.get(1));
        ArrayList<DateMessage> messages = conversations.get(position).messages;
        holder.dateTextView.setText(messages.get(messages.size()-1).sent.toString());
        holder.previewTextView.setText(messages.get(messages.size()-1).text);
        // TODO: Implement profile icons
//        Picasso.get().load(conversations.get(position).converserIds.get(0).profilePictureURL.toString())
//                .error(R.drawable.ic_launcher_background)
//                .resize(45, 45)
//                .into(holder.correspondentImageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle conversationBundle = new Bundle();
                conversationBundle.putParcelable(CONVERSATION_KEY, conversations.get(position));
                Intent startChatsActivity = new Intent(context, ChatActivity.class);
                startChatsActivity.putExtra(CONVERSATION_KEY, conversationBundle);
                context.startActivity(startChatsActivity);
            }
        });
    }

    @Override
    public int getItemCount() {
        return conversations.size();
    }

    public static class ConversationViewHolder extends RecyclerView.ViewHolder {
        public ImageView correspondentImageView;
        public TextView correspondentTextView;
        public TextView dateTextView;
        public TextView previewTextView;

        public ConversationViewHolder(final View itemView) {
            super(itemView);
            correspondentTextView = (TextView) itemView.findViewById(R.id.text_view_conversation_item_correspondent);
            previewTextView       = (TextView) itemView.findViewById(R.id.text_view_conversation_item_preview);
            dateTextView          = (TextView) itemView.findViewById(R.id.text_view_conversation_item_date);
            correspondentImageView = (ImageView) itemView.findViewById(R.id.image_view_conversation_item_correspondent);
        }
    }


}
