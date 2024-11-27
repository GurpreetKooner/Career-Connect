package com.example.careerconnect;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    public static class Message {
        public String text;
        public boolean isSent;

        public Message(String text, boolean isSent) {
            this.text = text;
            this.isSent = isSent;
        }
    }

    private final List<Message> messages;

    public ChatAdapter(List<Message> messages) {
        this.messages = messages;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_item, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        Message message = messages.get(position);
        if (message.isSent) {
            holder.sentMessage.setVisibility(View.VISIBLE);
            holder.receivedMessage.setVisibility(View.GONE);
            holder.sentMessage.setText(message.text);
        } else {
            holder.sentMessage.setVisibility(View.GONE);
            holder.receivedMessage.setVisibility(View.VISIBLE);
            holder.receivedMessage.setText(message.text);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView sentMessage;
        TextView receivedMessage;

        ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            sentMessage = itemView.findViewById(R.id.sentMessage);
            receivedMessage = itemView.findViewById(R.id.receivedMessage);
        }
    }
}
