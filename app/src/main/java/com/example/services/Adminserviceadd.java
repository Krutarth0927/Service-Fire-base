package com.example.services;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Adminserviceadd extends AppCompatActivity {

    EditText e1;

    Button b1;

    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminserviceadd);

        reference = FirebaseDatabase.getInstance().getReference("Service");

        e1 = findViewById(R.id.editname);
        b1 = findViewById(R.id.btnAddSer);


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName = e1.getText().toString();

                if (!newName.isEmpty()) {
                  //  String key = reference.push().getKey();
                    reference.child(newName).setValue(newName).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(Adminserviceadd.this, " Service Name added successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Adminserviceadd.this, "Failed to add Service name", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {

                    Toast.makeText(Adminserviceadd.this, "Please enter a  service Name", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}