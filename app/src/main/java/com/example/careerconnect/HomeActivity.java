package com.example.careerconnect;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.core.view.WindowCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private TextView welcomeText;
    private TextView userNameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Enable edge-to-edge
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        setContentView(R.layout.activity_home);

        // Initialize your views
        ImageView appIconNav = findViewById(R.id.app_icon_nav);
        welcomeText = findViewById(R.id.welcome_text);
        userNameTextView = findViewById(R.id.user_name);
        drawerLayout = findViewById(R.id.drawer_layout);

        appIconNav.setOnClickListener(view -> drawerLayout.openDrawer(GravityCompat.START));

        drawerLayout.setScrimColor(getResources().getColor(R.color.blue_tint));

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, null,
                R.string.job_list_welcome_text, R.string.chat_welcome_text);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Get current user info from Firebase
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String username = currentUser.getDisplayName();
            Log.d("FirebaseUser", "Current user: " + currentUser);
            if (username != null) {
                userNameTextView.setText(username);
            } else {
                String email = currentUser.getEmail();
                if (email != null && email.contains("@")) {
                    username = email.substring(0, email.indexOf("@")); // Get the part before "@"
                    userNameTextView.setText(username);
                } else {
                    userNameTextView.setText(email); // Fallback in case email doesn't contain "@"
                }
            }

        } else {
            Log.d("FirebaseUser", "No user is logged in");
        }


        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new JobSearchFragment())
                    .commit();
            navigationView.setCheckedItem(R.id.nav_job_search);
            welcomeText.setText(R.string.job_search_welcome_text);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

//        if (itemId == R.id.nav_job_list) {
//            getSupportFragmentManager()
//                    .beginTransaction()
//                    .replace(R.id.fragment_container, new UserListFragment())
//                    .commit();
//        } else

            if (itemId == R.id.nav_job_search) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new JobSearchFragment())
                    .commit();
            welcomeText.setText(R.string.job_search_welcome_text);
        }else if (itemId == R.id.nav_setting) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new UserProfileFragment())
                    .commit();
            welcomeText.setText(R.string.update_profile_welcome_text);
        } else if (itemId == R.id.nav_chat) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new UserListFragment())
                    .commit();
            welcomeText.setText(R.string.chat_welcome_text);
        } else if (itemId == R.id.nav_saved) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new SavedJobsFragment())
                    .commit();
            welcomeText.setText(R.string.saved_jobs_welcome_text);
        } else if (itemId == R.id.nav_logout) {
            FirebaseAuth.getInstance().signOut(); // Sign out from Firebase
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class)); // Redirect to Login screen
            finish(); // Close current activity
        }

        drawerLayout.closeDrawers();
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
