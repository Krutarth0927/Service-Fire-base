package com.example.services;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Admin extends AppCompatActivity {
    final String ADMIN_ID = "123";
    final String ADMIN_PASSWORD = "123";

    EditText editTextAdminId, editTextAdminPassword;
    Button btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        editTextAdminId = findViewById(R.id.editTextAdminId);
        editTextAdminPassword = findViewById(R.id.editTextAdminPassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredId = editTextAdminId.getText().toString().trim();
                String enteredPassword = editTextAdminPassword.getText().toString().trim();

                if (enteredId.equals(ADMIN_ID) && enteredPassword.equals(ADMIN_PASSWORD)) {

                    Intent intent = new Intent(Admin.this, Adminserviceadd.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(Admin.this, "Invalid ID or Password", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}