package com.example.services;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    Button admin, servicepro;
    Spinner serviceSpinner;
    ListView serviceListView;
    ArrayList<String> serviceDetailsList; // List for ListView data
    ArrayAdapter<String> listViewAdapter;

    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        admin = findViewById(R.id.btnAdmin);
        servicepro = findViewById(R.id.btnServiceProvider);
        serviceSpinner = findViewById(R.id.spinnerServices);
        serviceListView = findViewById(R.id.listView);


        serviceDetailsList = new ArrayList<>();


        listViewAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, serviceDetailsList);
        serviceListView.setAdapter(listViewAdapter);


        reference = FirebaseDatabase.getInstance().getReference("Service");


        fetchServiceNames();

        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Admin.class);
                startActivity(intent);
            }
        });

        servicepro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Serviceprovider.class);
                startActivity(intent);
            }
        });


        serviceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedService = parent.getItemAtPosition(position).toString();
                fetchDataForSelectedService(selectedService);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        serviceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String selectedItem = serviceDetailsList.get(position);
                Intent dialIntent = new Intent(Intent.ACTION_DIAL);
                dialIntent.setData(Uri.parse("tel:" + selectedItem));
                startActivity(dialIntent);
            }
        });
    }
    // Method to fetch service
    private void fetchServiceNames() {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> serviceNames = new ArrayList<>();
                for (DataSnapshot serviceSnapshot : snapshot.getChildren()) {
                    serviceNames.add(serviceSnapshot.getKey());
                }

                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, serviceNames);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                serviceSpinner.setAdapter(spinnerAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Failed to load services", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to fetch data based on selected service
    private void fetchDataForSelectedService(String serviceName) {

        serviceDetailsList.clear();

        reference.child(serviceName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    for (DataSnapshot detailSnapshot : snapshot.getChildren()) {

                        Object detail = detailSnapshot.getValue();

                        if (detail instanceof String) {
                            serviceDetailsList.add((String) detail);

                        } else if (detail instanceof HashMap) {
                            HashMap<String, Object> detailMap = (HashMap<String, Object>) detail;
                            StringBuilder formattedDetail = new StringBuilder();

                            for (Object value : detailMap.values()) {
                                formattedDetail.append(value.toString()).append("\n");
                            }
                            serviceDetailsList.add(formattedDetail.toString().trim());

                        } else {
                            serviceDetailsList.add(detail.toString());
                        }
                    }
                } else {
                    serviceDetailsList.add("No data available for this service");
                }

                // Notify the adapter that the data has changed
                listViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Failed to fetch service data", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
