package com.example.careerconnect;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class UserListFragment extends Fragment {

    private RecyclerView userRecyclerView;
    private UserAdapter userAdapter;
    private List<User> userList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_list, container, false);

        userRecyclerView = view.findViewById(R.id.userRecyclerView);
        userRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Populate user list
        userList = getUserList();
        userAdapter = new UserAdapter(userList, user -> {
            // Open ChatFragment for the selected user
            ChatFragment chatFragment = ChatFragment.newInstance(user.getName(), user.getEmail());
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, chatFragment)
                    .addToBackStack(null)
                    .commit();
        });
        userRecyclerView.setAdapter(userAdapter);

        return view;
    }

    private List<User> getUserList() {
        // Example user data
        List<User> users = new ArrayList<>();
        users.add(new User("John Doe", "john.doe@example.com"));
        users.add(new User("Jane Smith", "jane.smith@example.com"));
        return users;
    }
}
