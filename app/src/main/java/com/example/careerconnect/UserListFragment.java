package com.example.careerconnect;

import android.os.Bundle;
import android.text.TextUtils;
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

        // Populate user list
        userList = getUserList();
        filteredUserList = new ArrayList<>(userList); // Initialize with all users

        // Set up adapter with filteredUserList
        userAdapter = new UserAdapter(filteredUserList, user -> {
            // Open ChatFragment for the selected user
            ChatFragment chatFragment = ChatFragment.newInstance(user.getName(), user.getEmail());
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, chatFragment)
                    .addToBackStack(null)
                    .commit();
        });
        userRecyclerView.setAdapter(userAdapter);

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
        // Example user data
        List<User> users = new ArrayList<>();
        users.add(new User("John Doe", "john.doe@example.com"));
        users.add(new User("Jane Smith", "jane.smith@example.com"));
        users.add(new User("Alice Johnson", "alice.johnson@example.com"));
        users.add(new User("Bob Brown", "bob.brown@example.com"));
        return users;
    }
}
