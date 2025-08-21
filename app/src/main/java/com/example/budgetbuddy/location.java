package com.example.budgetbuddy;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class location extends AppCompatActivity implements OnMapReadyCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private RecyclerView locationRecyclerView;
    private List<LocationItem> locationItems;
    private Map<String, Marker> markersMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        // Initialize UI components
        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());

        // Update page title
        TextView locationTitle = findViewById(R.id.location_title);
        locationTitle.setText("Nearest Banks & Services");

        // Setup Google Maps
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Initialize location services
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Set up location data
        setupNearbyLocationData();

        // Setup RecyclerView
        locationRecyclerView = findViewById(R.id.location_list);
        locationRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        locationRecyclerView.setAdapter(new LocationAdapter(locationItems));
    }

    private void setupNearbyLocationData() {
        locationItems = new ArrayList<>();

        // Add banks
        locationItems.add(new LocationItem("Bank of Ceylon - Main Branch", "Bank", 0.5));
        locationItems.add(new LocationItem("People's Bank - City Branch", "Bank", 0.8));
        locationItems.add(new LocationItem("Commercial Bank - Central", "Bank", 1.2));

        // Add ATMs
        locationItems.add(new LocationItem("BOC ATM - Shopping Mall", "ATM", 0.3));
        locationItems.add(new LocationItem("People's Bank ATM", "ATM", 0.7));
        locationItems.add(new LocationItem("Commercial Bank ATM", "ATM", 1.1));

        // Add branches
        locationItems.add(new LocationItem("Bank of Ceylon - North Branch", "Branch", 1.3));
        locationItems.add(new LocationItem("People's Bank - East Branch", "Branch", 1.8));

        // Add Food City locations
        locationItems.add(new LocationItem("Cargills Food City - Main", "Food City", 0.6));
        locationItems.add(new LocationItem("Cargills Food City - Express", "Food City", 1.5));

        // Sort by distance - nearest first
        Collections.sort(locationItems, Comparator.comparingDouble(item -> item.distance));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Check for location permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            enableMyLocation();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }

        // Add place markers
        addLocationMarkers();
    }

    private void enableMyLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);

        // Move camera to user's current location
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
            }
        });
    }

    private void addLocationMarkers() {
        // Sample locations for banks, ATMs, branches and Food City
        // Banks
        LatLng bocMainLocation = new LatLng(6.9271, 79.8612);
        LatLng peoplesBankLocation = new LatLng(6.9157, 79.8636);
        LatLng commercialBankLocation = new LatLng(6.9200, 79.8570);

        // ATMs
        LatLng bocAtmLocation = new LatLng(6.9300, 79.8580);
        LatLng pbAtmLocation = new LatLng(6.9220, 79.8590);
        LatLng commAtmLocation = new LatLng(6.9180, 79.8560);

        // Branches
        LatLng bocBranchLocation = new LatLng(6.9350, 79.8650);
        LatLng pbBranchLocation = new LatLng(6.9400, 79.8700);

        // Food City
        LatLng foodCityMainLocation = new LatLng(6.9210, 79.8550);
        LatLng foodCityExpressLocation = new LatLng(6.9260, 79.8590);

        // Highlight the nearest bank with a green marker
        markersMap.put("Bank of Ceylon - Main Branch", mMap.addMarker(new MarkerOptions()
                .position(bocMainLocation)
                .title("Bank of Ceylon - Main Branch")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))));

        markersMap.put("People's Bank - City Branch", mMap.addMarker(new MarkerOptions()
                .position(peoplesBankLocation)
                .title("People's Bank - City Branch")));

        markersMap.put("Commercial Bank - Central", mMap.addMarker(new MarkerOptions()
                .position(commercialBankLocation)
                .title("Commercial Bank - Central")));

        // Highlight the nearest ATM with a blue marker
        markersMap.put("BOC ATM - Shopping Mall", mMap.addMarker(new MarkerOptions()
                .position(bocAtmLocation)
                .title("BOC ATM - Shopping Mall")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))));

        markersMap.put("People's Bank ATM", mMap.addMarker(new MarkerOptions()
                .position(pbAtmLocation)
                .title("People's Bank ATM")));

        markersMap.put("Commercial Bank ATM", mMap.addMarker(new MarkerOptions()
                .position(commAtmLocation)
                .title("Commercial Bank ATM")));

        // Add branch markers
        markersMap.put("Bank of Ceylon - North Branch", mMap.addMarker(new MarkerOptions()
                .position(bocBranchLocation)
                .title("Bank of Ceylon - North Branch")));

        markersMap.put("People's Bank - East Branch", mMap.addMarker(new MarkerOptions()
                .position(pbBranchLocation)
                .title("People's Bank - East Branch")));

        // Highlight the nearest Food City with an orange marker
        markersMap.put("Cargills Food City - Main", mMap.addMarker(new MarkerOptions()
                .position(foodCityMainLocation)
                .title("Cargills Food City - Main")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))));

        markersMap.put("Cargills Food City - Express", mMap.addMarker(new MarkerOptions()
                .position(foodCityExpressLocation)
                .title("Cargills Food City - Express")));

        // Center map on nearest ATM
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bocAtmLocation, 14));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableMyLocation();
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Location item data class
    private static class LocationItem {
        String name;
        String category;
        double distance; // distance in kilometers

        LocationItem(String name, String category, double distance) {
            this.name = name;
            this.category = category;
            this.distance = distance;
        }
    }

    // RecyclerView adapter for location items
    private class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder> {
        private List<LocationItem> items;

        LocationAdapter(List<LocationItem> items) {
            this.items = items;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // Create a simple layout for each item
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(android.R.layout.simple_list_item_2, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            LocationItem item = items.get(position);
            holder.text1.setText(item.name);

            // Format distance with one decimal place
            String distanceText = String.format("%.1f km away", item.distance);

            // Apply different styling based on category
            if (item.distance < 0.7) {
                // Highlight nearest locations of each type
                switch (item.category) {
                    case "Bank":
                        holder.text2.setText(item.category + " • " + distanceText + " ⭐ NEAREST");
                        holder.text2.setTextColor(Color.parseColor("#00D09E")); // Green
                        break;
                    case "ATM":
                        holder.text2.setText(item.category + " • " + distanceText + " ⭐ NEAREST");
                        holder.text2.setTextColor(Color.parseColor("#4285F4")); // Blue
                        break;
                    case "Food City":
                        holder.text2.setText(item.category + " • " + distanceText + " ⭐ NEAREST");
                        holder.text2.setTextColor(Color.parseColor("#FF8C00")); // Orange
                        break;
                    default:
                        holder.text2.setText(item.category + " • " + distanceText);
                        holder.text2.setTextColor(Color.GRAY);
                }
            } else {
                holder.text2.setText(item.category + " • " + distanceText);
                holder.text2.setTextColor(Color.GRAY);
            }

            holder.itemView.setOnClickListener(v -> {
                // Center map on this location when clicked
                Marker marker = markersMap.get(item.name);
                if (marker != null) {
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
                    marker.showInfoWindow();
                }
                Toast.makeText(location.this, "Selected: " + item.name, Toast.LENGTH_SHORT).show();
            });
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView text1;
            TextView text2;

            ViewHolder(View view) {
                super(view);
                text1 = view.findViewById(android.R.id.text1);
                text2 = view.findViewById(android.R.id.text2);
            }
        }
    }
}