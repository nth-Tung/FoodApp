package com.tuantung.oufood.Activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.tuantung.oufood.Adapter.AddressSuggetionAdapter;
import com.tuantung.oufood.R;

import java.io.IOException;
import java.util.List;

public class SelectAddressActivity extends AppCompatActivity {
    private final int FINE_PERMISSION_CODE = 1;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location currentLocation;

    private Toolbar toolbar;
    private RecyclerView recyclerViewAddressSuggestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_select_address);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerViewAddressSuggestion= findViewById(R.id.recyclerView_addressSuggestion);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();
    }

    private void getLastLocation(){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION )!= PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(SelectAddressActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_PERMISSION_CODE);
            return;
        }
        Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();
        locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                Geocoder geocoder = new Geocoder(SelectAddressActivity.this);
                List<Address> addresses;
                try {
                    addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 15);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                AddressSuggetionAdapter adapter = new AddressSuggetionAdapter(addresses);
                recyclerViewAddressSuggestion.setLayoutManager(new LinearLayoutManager(SelectAddressActivity.this,LinearLayoutManager.VERTICAL,false));
                recyclerViewAddressSuggestion.setAdapter(adapter);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}