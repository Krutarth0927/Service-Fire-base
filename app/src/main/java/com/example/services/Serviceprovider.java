package com.example.services;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class Serviceprovider extends AppCompatActivity {

    Spinner sp;
    DatabaseReference reference;
    ArrayList<String> namesList = new ArrayList<>();
    ArrayAdapter<String> adapter;
    EditText ename, epass;
    Button bsub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serviceprovider);

        sp = findViewById(R.id.spinnerService);

        ename = findViewById(R.id.editTextName);
        epass = findViewById(R.id.editTextPhone);

        bsub = findViewById(R.id.btnSubmit);


        reference = FirebaseDatabase.getInstance().getReference("Service");

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, namesList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adapter);

        fetchData();


        bsub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitData();
            }
        });

    }

    private void fetchData() {

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                namesList.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    String serviceName = snapshot1.getKey();
                    namesList.add(serviceName);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Serviceprovider.this, "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void submitData() {
        String name = ename.getText().toString().trim();
        String phone = epass.getText().toString().trim();
        String selectedService = sp.getSelectedItem().toString();

        if (name.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Please enter both name and phone", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference selectedRef = reference.child(selectedService);

        HashMap<String, String> dataMap = new HashMap<>();
        dataMap.put("name", name);
        dataMap.put("phone", phone);

        selectedRef.child(name).setValue(dataMap).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(Serviceprovider.this, "Data inserted successfully  " + selectedService, Toast.LENGTH_SHORT).show();
                ename.setText("");
                epass.setText("");
            } else {
                Toast.makeText(Serviceprovider.this, "Failed to insert data", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(Serviceprovider.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }
}