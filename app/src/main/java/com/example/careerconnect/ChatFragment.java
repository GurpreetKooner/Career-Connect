package com.example.careerconnect;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {

    private RecyclerView chatRecyclerView;
    private EditText messageInput;
    private ImageButton sendButton;
    private ChatAdapter chatAdapter;
    private List<ChatAdapter.Message> messages;
    private TextView userNameTextView, userEmailTextView, emptyPlaceholder;

    private static final String ARG_USER_NAME = "userName";
    private static final String ARG_USER_EMAIL = "userEmail";

    private String userName;
    private String userEmail;

    public ChatFragment() {
        // Required empty public constructor
    }

    public static ChatFragment newInstance(String userName, String userEmail) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USER_NAME, userName);
        args.putString(ARG_USER_EMAIL, userEmail);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize the messages list
        messages = new ArrayList<>();

        if (getArguments() != null) {
            userName = getArguments().getString(ARG_USER_NAME);
            userEmail = getArguments().getString(ARG_USER_EMAIL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chatbox, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Find views
        userNameTextView = view.findViewById(R.id.userName);
        userEmailTextView = view.findViewById(R.id.userEmail);
        emptyPlaceholder = view.findViewById(R.id.emptyPlaceholder);
        chatRecyclerView = view.findViewById(R.id.chatRecyclerView);
        messageInput = view.findViewById(R.id.messageInput);
        sendButton = view.findViewById(R.id.sendButton);

        // Set user details
        if (userName != null) {
            userNameTextView.setText(userName);
        }
        if (userEmail != null) {
            userEmailTextView.setText(userEmail);
        }

        // Set up RecyclerView
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        chatAdapter = new ChatAdapter(messages);
        chatRecyclerView.setAdapter(chatAdapter);

        // Show placeholder message if chat is empty
        updateEmptyState();

        // Set up send button functionality
        sendButton.setOnClickListener(v -> {
            String messageText = messageInput.getText().toString().trim();
            if (!messageText.isEmpty()) {
                // Add the new message to the list and update the RecyclerView
                messages.add(new ChatAdapter.Message(messageText, true)); // true for sent messages
                chatAdapter.notifyItemInserted(messages.size() - 1);
                chatRecyclerView.scrollToPosition(messages.size() - 1);

                // Clear the input field
                messageInput.setText("");

                // Remove empty state if a message is sent
                updateEmptyState();
            }
        });
    }

    private void updateEmptyState() {
        if (messages.isEmpty()) {
            chatRecyclerView.setVisibility(View.GONE);
            emptyPlaceholder.setVisibility(View.VISIBLE);
        } else {
            chatRecyclerView.setVisibility(View.VISIBLE);
            emptyPlaceholder.setVisibility(View.GONE);
        }
    }
}
