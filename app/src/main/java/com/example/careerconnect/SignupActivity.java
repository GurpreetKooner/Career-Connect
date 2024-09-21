package com.example.careerconnect;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);
        EditText emailEditText = findViewById(R.id.signup_email);
        EditText passwordEditText = findViewById(R.id.signup_password);
        EditText companyEditText = findViewById(R.id.signup_company);
        EditText nameEditText = findViewById(R.id.signup_name);
        androidx.appcompat.widget.AppCompatButton signupButton = findViewById(R.id.signup_button);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (emailEditText.getText().toString().isEmpty() || passwordEditText.getText().toString().isEmpty()|| nameEditText.getText().toString().isEmpty() || companyEditText.getText().toString().isEmpty()) {
                    Toast.makeText(SignupActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(SignupActivity.this, JobListActivity.class);
                    startActivity(intent);
                }
            }
        });
        TextView refererTextView = findViewById(R.id.signup_referer);

        LinearLayout refererDialogLayout = findViewById(R.id.referer_dialog_layout);
        TextView closeButton = findViewById(R.id.referer_close);
        refererTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refererDialogLayout.setVisibility(View.VISIBLE);
            }
        });
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refererDialogLayout.setVisibility(View.GONE);
            }
        });
//        refererDialogLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                refererDialogLayout.setVisibility(View.GONE);
//            }
//        });

    }
}