package com.example.careerconnect;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
// Import the RecyclerView import statement
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FieldPath;

import java.util.ArrayList;
import java.util.List;

public class UserListFragment extends Fragment {

    private RecyclerView userRecyclerView;
    private UserAdapter userAdapter;
    private List<User> userList;
    private List<User> filteredUserList; // New list to hold filtered users
    private EditText searchUser;
    private ImageButton searchButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_list, container, false);

        // Initialize views
        userRecyclerView = view.findViewById(R.id.userRecyclerView);
        searchUser = view.findViewById(R.id.searchUser);
        searchButton = view.findViewById(R.id.searchButton);

        userRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        // Initialize lists
        userList = new ArrayList<>(); // Initialize as an empty list
        filteredUserList = new ArrayList<>();

        // Set up adapter with filteredUserList
        userAdapter = new UserAdapter(filteredUserList, user -> {
            // Open ChatFragment for the selected user
            ChatFragment chatFragment = ChatFragment.newInstance(user.getId(), user.getName(), user.getEmail());
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, chatFragment)
                    .addToBackStack(null)
                    .commit();
        });
        userRecyclerView.setAdapter(userAdapter);

        // Fetch the user list from Firestore
        userList = getUserList();

        // Set up search button click listener
        searchButton.setOnClickListener(v -> performSearch());

        return view;
    }

    private void performSearch() {
        String query = searchUser.getText().toString().trim();

        if (TextUtils.isEmpty(query)) {
            // If the search query is empty, show all users
            filteredUserList.clear();
            filteredUserList.addAll(userList);
            userAdapter.notifyDataSetChanged();
            Toast.makeText(getContext(), "Showing all users", Toast.LENGTH_SHORT).show();
            return;
        }

        // Filter the userList based on the search query
        List<User> tempList = new ArrayList<>();
        for (User user : userList) {
            if (user.getName().toLowerCase().contains(query.toLowerCase())) {
                tempList.add(user);
            }
        }

        // Update the filteredUserList and notify the adapter
        filteredUserList.clear();
        filteredUserList.addAll(tempList);
        userAdapter.notifyDataSetChanged();

        if (filteredUserList.isEmpty()) {
            Toast.makeText(getContext(), "No users found", Toast.LENGTH_SHORT).show();
        }
    }

    private List<User> getUserList() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid(); // Get current user's ID

        // Fetch the 'friends' list from the current user's document
        db.collection("users").document(currentUserId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        List<String> friendIds = (List<String>) documentSnapshot.get("friends"); // Retrieve 'friends' list

                        Log.d("UserList", "Friend IDs: " + friendIds);

                        if (friendIds != null && !friendIds.isEmpty()) {
                            // Fetch users by their IDs
                            fetchUsersByIds(friendIds);
                        } else {
                            Log.d("UserList", "No friends found.");
                            userList.clear();
                            filteredUserList.clear();
                            userAdapter.notifyDataSetChanged();
                            Toast.makeText(getContext(), "You have no friends in your list", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.e("UserList", "User document does not exist.");
                        Toast.makeText(getContext(), "Failed to load user data.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("UserList", "Failed to fetch friends: " + e.getMessage());
                    Toast.makeText(getContext(), "Failed to load friend list: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });

        return userList;
    }

    private void fetchUsersByIds(List<String> friendIds) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users")
                .whereIn(FieldPath.documentId(), friendIds) // Match document IDs
                .get()
                .addOnSuccessListener(userSnapshot -> {
                    userList.clear(); // Clear existing list
                    for (DocumentSnapshot document : userSnapshot.getDocuments()) {
                        User user = document.toObject(User.class);
                        if (user != null) {
                            user.setId(document.getId()); // Set the document ID manually
                            userList.add(user);
                        }
                    }

                    Log.d("UserList", "Fetched Friends: " + userList);

                    // Update filteredUserList and notify adapter
                    filteredUserList.clear();
                    filteredUserList.addAll(userList);
                    userAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Log.e("UserList", "Failed to fetch user details: " + e.getMessage());
                    Toast.makeText(getContext(), "Failed to load friend details: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
