package com.example.careerconnect;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    TextView welcomeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        View windowDecorView = getWindow().getDecorView();
        windowDecorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        ImageView appIconNav = findViewById(R.id.app_icon_nav);
        welcomeText = findViewById(R.id.welcome_text);

        appIconNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });


        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, null, R.string.job_list_welcome_text, R.string.chat_welcome_text);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new JobListFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_job_list);
            welcomeText.setText(R.string.job_list_welcome_text);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.nav_job_list) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new JobListFragment()).commit();
            welcomeText.setText(R.string.job_list_welcome_text);
        } else if (itemId == R.id.nav_job_search) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new JobSearchFragment()).commit();
            welcomeText.setText(R.string.job_search_welcome_text);
        } else if (itemId == R.id.nav_chat) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ChatFragment()).commit();
            welcomeText.setText(R.string.chat_welcome_text);
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