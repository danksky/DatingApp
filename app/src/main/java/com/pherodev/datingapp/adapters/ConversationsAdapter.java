package com.pherodev.datingapp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pherodev.datingapp.R;
import com.pherodev.datingapp.models.Conversation;
import com.pherodev.datingapp.models.DateMessage;

import java.util.ArrayList;

public class ConversationsAdapter extends RecyclerView.Adapter<ConversationsAdapter.ConversationViewHolder> {
    private ArrayList<Conversation> conversations;

    public ConversationsAdapter(ArrayList<Conversation> conversations) {
        this.conversations = conversations;
    }

    @Override
    public ConversationsAdapter.ConversationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.conversation_item, parent, false);
        ConversationViewHolder cvh = new ConversationViewHolder(v);
        return cvh;
    }

    @Override
    public void onBindViewHolder(ConversationViewHolder holder, int position) {
        holder.correspondentTextView.setText(conversations.get(position).converserNames.get(1));
        ArrayList<DateMessage> messages = conversations.get(position).messages;
        holder.previewTextView.setText(messages.get(messages.size()-1).sent.toString());
        // TODO: Implement profile icons
//        Picasso.get().load(conversations.get(position).converserIds.get(0).profilePictureURL.toString())
//                .error(R.drawable.ic_launcher_background)
//                .resize(45, 45)
//                .into(holder.correspondentImageView);
    }

    @Override
    public int getItemCount() {
        return conversations.size();
    }

    public static class ConversationViewHolder extends RecyclerView.ViewHolder {
        public ImageView correspondentImageView;
        public TextView correspondentTextView;
        public TextView previewTextView;

        public ConversationViewHolder(final View itemView) {
            super(itemView);
            correspondentTextView = (TextView) itemView.findViewById(R.id.text_view_conversation_item_correspondent);
            previewTextView       = (TextView) itemView.findViewById(R.id.text_view_conversation_item_preview);
            correspondentImageView = (ImageView) itemView.findViewById(R.id.image_view_conversation_item_correspondent);
        }
    }


}
