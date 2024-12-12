//package com.example.careerconnect;
//
//import static android.content.Context.MODE_PRIVATE;
//
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.text.method.PasswordTransformationMethod;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.EditText;
//import android.widget.ImageButton;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.android.gms.tasks.Task;
//import com.google.android.material.materialswitch.MaterialSwitch;
//import com.google.firebase.auth.EmailAuthProvider;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.auth.UserProfileChangeRequest;
//import com.google.firebase.firestore.FirebaseFirestore;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class UserProfileFragment extends Fragment {
//    private EditText nameEditText;
//    private EditText emailEditText;
//    private EditText passwordEditText;
//    private EditText confirmPasswordEditText;
//
//    private EditText companyEditText;
//    private ImageButton editButton;
//    private ImageButton saveButton;
//    private ImageButton passwordVisibilityToggle;
//    private ImageButton confirmPasswordVisibilityToggle;
//
//    private FirebaseAuth auth;
//    private FirebaseFirestore firestore;
//
//    private MaterialSwitch refererSwitch;
//
//    private boolean isEditMode = false;
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater,
//                             @Nullable ViewGroup container,
//                             @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
//
//        // Initialize Firebase
//        auth = FirebaseAuth.getInstance();
//        firestore = FirebaseFirestore.getInstance();
//
//        // Initialize Views
//        nameEditText = view.findViewById(R.id.nameEditText);
//        emailEditText = view.findViewById(R.id.emailEditText);
//        passwordEditText = view.findViewById(R.id.passwordEditText);
//        confirmPasswordEditText = view.findViewById(R.id.confirmPasswordEditText);
//        companyEditText = view.findViewById(R.id.signup_company);
//        refererSwitch = view.findViewById(R.id.opt_in_switch);
//        editButton = view.findViewById(R.id.editButton);
//        saveButton = view.findViewById(R.id.saveButton);
//
//        // Initially set fields as non-editable
//        setFieldsEditable(false);
//
//        // Load current user details
//        loadUserDetails();
//
//        // Edit button click listener
//        editButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                toggleEditMode();
//            }
//        });
//
//        // Save button click listener
//        saveButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                updateUserProfile();
//            }
//        });
//
//        return view;
//    }
//
//    private void loadUserDetails() {
//        FirebaseUser currentUser = auth.getCurrentUser();
//        if (currentUser != null) {
//            nameEditText.setText(currentUser.getDisplayName() != null ?
//                    currentUser.getDisplayName() : "");
//            emailEditText.setText(currentUser.getEmail() != null ?
//                    currentUser.getEmail() : "");
//        }
//    }
//
//    private void toggleEditMode() {
//        isEditMode = !isEditMode;
//        setFieldsEditable(isEditMode);
//
//        // Update button visibilities
//        editButton.setVisibility(isEditMode ? View.GONE : View.VISIBLE);
//        saveButton.setVisibility(isEditMode ? View.VISIBLE : View.GONE);
//
//        // Reset password fields when not in edit mode
//        if (!isEditMode) {
//            passwordEditText.setText("");
//            confirmPasswordEditText.setText("");
//        }
//    }
//
//    private void setFieldsEditable(boolean editable) {
//        nameEditText.setEnabled(editable);
//        emailEditText.setEnabled(editable);
//        passwordEditText.setEnabled(editable);
//        confirmPasswordEditText.setEnabled(editable);
//    }
//
//    private void togglePasswordVisibility(EditText editText) {
//        // Toggle password visibility
//        if (editText.getTransformationMethod() == null) {
//            editText.setTransformationMethod(
//                    PasswordTransformationMethod.getInstance()
//            );
//        } else {
//            editText.setTransformationMethod(null);
//        }
//    }
//
//    private void updateUserProfile() {
//        FirebaseUser currentUser = auth.getCurrentUser();
//        if (currentUser == null) return;
//
//        String name = nameEditText.getText().toString().trim();
//        String email = emailEditText.getText().toString().trim();
//        String password = passwordEditText.getText().toString().trim();
//        String confirmPassword = confirmPasswordEditText.getText().toString().trim();
//
//        // Validate inputs
//        if (name.isEmpty() || email.isEmpty()) {
//            Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        // Check password match if password is being updated
//        if (!password.isEmpty()) {
//            if (!password.equals(confirmPassword)) {
//                Toast.makeText(requireContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            // Ensure the password length is at least 6 characters
//            if (password.length() < 6) {
//                Toast.makeText(requireContext(), "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
//                return;
//            }
//        }
//
//        // Re-authenticate before updating sensitive information
//        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE);
//        String savedPassword = sharedPreferences.getString("password", ""); // Default value is empty string if not found
//
//        currentUser.reauthenticate(EmailAuthProvider.getCredential(currentUser.getEmail(), savedPassword))
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()) {
//                            // Update profile details
//                            if (!currentUser.getDisplayName().equals(name)) {
//                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
//                                        .setDisplayName(name)
//                                        .build();
//
//                                currentUser.updateProfile(profileUpdates)
//                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                            @Override
//                                            public void onComplete(@NonNull Task<Void> task) {
//                                                if (!task.isSuccessful()) {
//                                                    Toast.makeText(requireContext(), "Failed to update profile name", Toast.LENGTH_SHORT).show();
//                                                }
//                                            }
//                                        });
//                            }
//
//                            // Update email if changed
//                            if (!currentUser.getEmail().equals(email)) {
//                                currentUser.updateEmail(email)
//                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                            @Override
//                                            public void onComplete(@NonNull Task<Void> task) {
//                                                if (!task.isSuccessful()) {
//                                                    Toast.makeText(requireContext(), "Failed to update email", Toast.LENGTH_SHORT).show();
//                                                }
//                                            }
//                                        });
//                            }
//
//                            // Update password if provided
//                            if (!password.isEmpty()) {
//                                currentUser.updatePassword(password)
//                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                            @Override
//                                            public void onComplete(@NonNull Task<Void> task) {
//                                                if (!task.isSuccessful()) {
//                                                    Toast.makeText(requireContext(), "Failed to update password", Toast.LENGTH_SHORT).show();
//                                                } else {
//                                                    Toast.makeText(requireContext(), "Password updated successfully", Toast.LENGTH_SHORT).show();
//                                                }
//                                            }
//                                        });
//                            }
//
//                            // Update Firestore document if needed
//                            Map<String, Object> userUpdates = new HashMap<>();
//                            firestore.collection("users").document(currentUser.getUid())
//                                    .update(userUpdates)
//                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                        @Override
//                                        public void onSuccess(Void aVoid) {
//                                            Toast.makeText(requireContext(), "Profile Updated", Toast.LENGTH_SHORT).show();
//                                            toggleEditMode(); // Exit edit mode
//                                        }
//                                    })
//                                    .addOnFailureListener(new OnFailureListener() {
//                                        @Override
//                                        public void onFailure(@NonNull Exception e) {
//                                            Toast.makeText(requireContext(), "Update Failed: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
//                                        }
//                                    });
//                        } else {
//                            Toast.makeText(requireContext(), "Re-authentication failed. Please try again.", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//    }
//
//
//}

package com.example.careerconnect;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.android.material.materialswitch.MaterialSwitch;

import java.util.HashMap;
import java.util.Map;

public class UserProfileFragment extends Fragment {
    private EditText nameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private EditText companyEditText;
    private ImageButton editButton;
    private ImageButton saveButton;
    private ImageButton passwordVisibilityToggle;
    private ImageButton confirmPasswordVisibilityToggle;

    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

    private MaterialSwitch refererSwitch;

    private boolean isEditMode = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        // Initialize Firebase
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        // Initialize Views
        nameEditText = view.findViewById(R.id.nameEditText);
        emailEditText = view.findViewById(R.id.emailEditText);
        passwordEditText = view.findViewById(R.id.passwordEditText);
        confirmPasswordEditText = view.findViewById(R.id.confirmPasswordEditText);
        companyEditText = view.findViewById(R.id.signup_company);
        refererSwitch = view.findViewById(R.id.opt_in_switch);
        editButton = view.findViewById(R.id.editButton);
        saveButton = view.findViewById(R.id.saveButton);

        // Initially set fields as non-editable
        setFieldsEditable(false);

        // Load current user details
        loadUserDetails();

        // Edit button click listener
        editButton.setOnClickListener(v -> toggleEditMode());

        // Save button click listener
        saveButton.setOnClickListener(v -> updateUserProfile());

        return view;
    }

    private void loadUserDetails() {
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            nameEditText.setText(currentUser.getDisplayName() != null ? currentUser.getDisplayName() : "");
            emailEditText.setText(currentUser.getEmail() != null ? currentUser.getEmail() : "");
            companyEditText.setText(getCompanyFromFirestore(currentUser.getUid())); // Add logic to get company from Firestore
        }
    }

    private String getCompanyFromFirestore(String userId) {
        // Implement the logic to fetch company from Firestore
        // You can use Firestore query to get the company name associated with the user
        return ""; // return the company name from Firestore
    }

    private void toggleEditMode() {
        isEditMode = !isEditMode;
        setFieldsEditable(isEditMode);

        // Update button visibilities
        editButton.setVisibility(isEditMode ? View.GONE : View.VISIBLE);
        saveButton.setVisibility(isEditMode ? View.VISIBLE : View.GONE);

        // Reset password fields when not in edit mode
        if (!isEditMode) {
            passwordEditText.setText("");
            confirmPasswordEditText.setText("");
        }
    }

    private void setFieldsEditable(boolean editable) {
        nameEditText.setEnabled(editable);
        emailEditText.setEnabled(editable);
        passwordEditText.setEnabled(editable);
        confirmPasswordEditText.setEnabled(editable);
        companyEditText.setEnabled(editable);
        refererSwitch.setEnabled(editable);
    }

    private void togglePasswordVisibility(EditText editText) {
        // Toggle password visibility
        if (editText.getTransformationMethod() == null) {
            editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        } else {
            editText.setTransformationMethod(null);
        }
    }

    private void updateUserProfile() {
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser == null) return;

        String name = nameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();
        String company = companyEditText.getText().toString().trim();
        boolean isReferer = refererSwitch.isChecked();

        // Validate inputs
        if (name.isEmpty() || email.isEmpty() || company.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check password match if password is being updated
        if (!password.isEmpty()) {
            if (!password.equals(confirmPassword)) {
                Toast.makeText(requireContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            // Ensure the password length is at least 6 characters
            if (password.length() < 6) {
                Toast.makeText(requireContext(), "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // Re-authenticate before updating sensitive information
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String savedPassword = sharedPreferences.getString("password", ""); // Default value is empty string if not found

        currentUser.reauthenticate(EmailAuthProvider.getCredential(currentUser.getEmail(), savedPassword))
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Update profile details
                        if (!currentUser.getDisplayName().equals(name)) {
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(name)
                                    .build();

                            currentUser.updateProfile(profileUpdates)
                                    .addOnCompleteListener(task1 -> {
                                        if (!task1.isSuccessful()) {
                                            Toast.makeText(requireContext(), "Failed to update profile name", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }

                        // Update email if changed
                        if (!currentUser.getEmail().equals(email)) {
                            currentUser.updateEmail(email)
                                    .addOnCompleteListener(task12 -> {
                                        if (!task12.isSuccessful()) {
                                            Toast.makeText(requireContext(), "Failed to update email", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }

                        // Update password if provided
                        if (!password.isEmpty()) {
                            currentUser.updatePassword(password)
                                    .addOnCompleteListener(task13 -> {
                                        if (!task13.isSuccessful()) {
                                            Toast.makeText(requireContext(), "Failed to update password", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(requireContext(), "Password updated successfully", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }

                        // Update Firestore document with company and referer status
                        Map<String, Object> userUpdates = new HashMap<>();
                        userUpdates.put("name", name);
                        userUpdates.put("company", company);
                        userUpdates.put("email", email);
                        userUpdates.put("isReferer", isReferer);

                        firestore.collection("users").document(currentUser.getUid())
                                .update(userUpdates)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(requireContext(), "Profile Updated", Toast.LENGTH_SHORT).show();
                                    toggleEditMode(); // Exit edit mode
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(requireContext(), "Update Failed: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                });
                    } else {
                        Toast.makeText(requireContext(), "Re-authentication failed. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
