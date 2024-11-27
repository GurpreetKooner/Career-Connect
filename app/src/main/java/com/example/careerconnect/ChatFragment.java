package com.example.careerconnect;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {

    private RecyclerView chatRecyclerView;
    private EditText messageInput;
    private ImageButton sendButton;
    private ChatAdapter chatAdapter;
    private List<ChatAdapter.Message> messages;
    private TextView userNameTextView, userEmailTextView, emptyPlaceholder;

    private FirebaseFirestore db;
    private String currentUserId;
    private String friendId;

    private static final String ARG_USER_NAME = "userName";
    private static final String ARG_USER_EMAIL = "userEmail";
    private static final String ARG_FRIEND_ID = "friendId";

    private String userName;
    private String userEmail;

    public ChatFragment() {
        // Required empty public constructor
    }

    public static ChatFragment newInstance(String friendId, String userName, String userEmail) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putString(ARG_FRIEND_ID, friendId);
        args.putString(ARG_USER_NAME, userName);
        args.putString(ARG_USER_EMAIL, userEmail);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        messages = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if (getArguments() != null) {
            friendId = getArguments().getString(ARG_FRIEND_ID);
            Log.d("ChatFragment", "Friend ID: " + ARG_FRIEND_ID + friendId + ARG_USER_NAME);
            userName = getArguments().getString(ARG_USER_NAME);
            userEmail = getArguments().getString(ARG_USER_EMAIL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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

        // Load chat messages
        if (friendId != null) {
            loadMessages();
        } else {
            Toast.makeText(getContext(), "Invalid friend ID", Toast.LENGTH_SHORT).show();
        }

        // Set up send button functionality
        sendButton.setOnClickListener(v -> sendMessage());
    }

    private void loadMessages() {
        db.collection("chats")
                .document(getChatId(currentUserId, friendId))
                .collection("messages")
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .addSnapshotListener((querySnapshot, error) -> {
                    if (error != null) {
                        Log.e("ChatFragment", "Error loading messages: " + error.getMessage());
                        return;
                    }

                    if (querySnapshot != null) {
                        messages.clear();
                        for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                            String text = document.getString("message");
                            String sender = document.getString("sender");

                            boolean isSent = currentUserId.equals(sender);
                            messages.add(new ChatAdapter.Message(text, isSent));
                        }

                        chatAdapter.notifyDataSetChanged();
                        chatRecyclerView.scrollToPosition(messages.size() - 1);
                        updateEmptyState();
                    }
                });
    }

    private void sendMessage() {
        String messageText = messageInput.getText().toString().trim();
        if (TextUtils.isEmpty(messageText)) {
            return;
        }

        Long timestamp = System.currentTimeMillis();
        String chatId = getChatId(currentUserId, friendId);

        ChatMessage chatMessage = new ChatMessage(currentUserId, friendId, messageText, timestamp);

        db.collection("chats")
                .document(chatId)
                .collection("messages")
                .add(chatMessage)
                .addOnSuccessListener(aVoid -> {
                    messageInput.setText("");
                    Log.d("ChatFragment", "Message sent successfully");
                })
                .addOnFailureListener(e -> Log.e("ChatFragment", "Error sending message: " + e.getMessage()));
    }

    private String getChatId(String userId1, String userId2) {
        // Ensure consistent chat ID generation
        return userId1.compareTo(userId2) < 0 ? userId1 + "_" + userId2 : userId2 + "_" + userId1;
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
