package com.example.inticiviapp.Fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.inticiviapp.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ReportFragment extends Fragment {

    View ReportView;

    //Location
    FusedLocationProviderClient fusedLocationClient;

    // UI fields
    MaterialAutoCompleteTextView dropdownState;
    TextInputEditText etCity, etPin, etAddress, etDescription;
    private static final int PICK_MEDIA = 101;

    MaterialButton btnUpload;
    LinearLayout layoutFileAck;
    TextView tvFileName;
    ChipGroup chipGroup;
    Button btnSubmit;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ReportView = inflater.inflate(R.layout.fragment_report, container, false);
        // ================= INPUT FIELDS =================
        chipGroup = ReportView.findViewById(R.id.chipGroup);
        etDescription = ReportView.findViewById(R.id.etDescription);
        etCity = ReportView.findViewById(R.id.etCity);
        etPin = ReportView.findViewById(R.id.etPin);
        etAddress = ReportView.findViewById(R.id.etAddress);
        dropdownState = ReportView.findViewById(R.id.dropdownState);
        layoutFileAck = ReportView.findViewById(R.id.layoutFileAck);
        btnSubmit = ReportView.findViewById(R.id.btnSubmit);
        btnUpload = ReportView.findViewById(R.id.btnUpload);


        // ================= TIME DROPDOWN =================
        MaterialAutoCompleteTextView dropdownTime = ReportView.findViewById(R.id.dropdownTime);

        String[] timeOptions = {
                "Any time",
                "Morning (9 AM - 12 PM)",
                "Afternoon (12 PM - 3 PM)",
                "Evening (3 PM - 6 PM)",
                "Night (6 PM - 9 PM)"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                timeOptions
        );

        dropdownTime.setAdapter(adapter);


        // ================= STATE DROPDOWN =================
        dropdownState = ReportView.findViewById(R.id.dropdownState);

        String[] states = {
                "Andhra Pradesh",
                "Telangana",
                "Tamil Nadu",
                "Karnataka",
                "Maharashtra"
        };

        ArrayAdapter<String> stateAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                states
        );

        dropdownState.setAdapter(stateAdapter);


        // ================= LOCATION =================
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());

        MaterialButton btnLocation = ReportView.findViewById(R.id.btnLocation);

        btnLocation.setOnClickListener(v -> getCurrentLocation());

        //=======================for upload evidence===================================
        tvFileName = ReportView.findViewById(R.id.tvFileName);

        // Button click
        btnUpload.setOnClickListener(v -> openMediaPicker());

        //============================Submition button Validation=======================
        btnSubmit.setOnClickListener(v -> validateForm());

        return ReportView;
    }

    private void validateForm() {

        // CATEGORY
        if (chipGroup.getCheckedChipId() == View.NO_ID) {
            Toast.makeText(getContext(), "Please select a category", Toast.LENGTH_SHORT).show();
            return;
        }

        // DESCRIPTION
        String description = etDescription.getText().toString().trim();
        if (description.isEmpty()) {
            Toast.makeText(getContext(), "Fill all required", Toast.LENGTH_SHORT).show();
            etDescription.setError("Description is required");
            return;
        }

        // FILE UPLOAD (check visibility of acknowledgement)
        if (layoutFileAck.getVisibility() != View.VISIBLE) {
            Toast.makeText(getContext(), "Please upload evidence", Toast.LENGTH_SHORT).show();
            return;
        }

        // STATE
        String state = dropdownState.getText().toString().trim();
        if (state.isEmpty()) {
            Toast.makeText(getContext(), "Fill all required", Toast.LENGTH_SHORT).show();
            dropdownState.setError("Select state");
            return;
        }

        // CITY
        String city = etCity.getText().toString().trim();
        if (city.isEmpty()) {
            Toast.makeText(getContext(), "Fill all required", Toast.LENGTH_SHORT).show();
            etCity.setError("City required");
            return;
        }

        // PIN
        String pin = etPin.getText().toString().trim();
        if (pin.length() != 6) {
            Toast.makeText(getContext(), "Fill all required", Toast.LENGTH_SHORT).show();
            etPin.setError("Enter valid 6-digit PIN");
            return;
        }

        // ADDRESS
        String address = etAddress.getText().toString().trim();
        if (address.isEmpty()) {
            Toast.makeText(getContext(), "Fill all required", Toast.LENGTH_SHORT).show();
            etAddress.setError("Address required");
            return;
        }

        // ALL VALID
        Toast.makeText(getContext(), "Form Submitted Successfully ✅", Toast.LENGTH_LONG).show();

        // 👉 You can send data to backend here

    }

    private void openMediaPicker() {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");

        // Allow only image + video
        String[] mimeTypes = {"image/*", "video/*"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);

        startActivityForResult(Intent.createChooser(intent, "Select Image or Video"), PICK_MEDIA);
    }


    // ================= GET LOCATION =================
    private void getCurrentLocation() {

        if (ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {

                double lat = location.getLatitude();
                double lon = location.getLongitude();

                Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());

                try {
                    List<Address> addresses = geocoder.getFromLocation(lat, lon, 1);

                    if (addresses != null && !addresses.isEmpty()) {

                        Address addr = addresses.get(0);

                        String state = addr.getAdminArea();
                        String city = addr.getLocality();
                        String pin = addr.getPostalCode();
                        String fullAddress = addr.getAddressLine(0);

                        // AUTO FILL
                        dropdownState.setText(state, false);
                        etCity.setText(city);
                        etPin.setText(pin);
                        etAddress.setText(fullAddress);

                        Toast.makeText(getContext(), "Location Auto-filled", Toast.LENGTH_SHORT).show();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(getContext(), "Unable to get location", Toast.LENGTH_SHORT).show();
            }
        });
    }


    // ================= PERMISSION RESULT =================
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1 && grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            getCurrentLocation();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_MEDIA && resultCode == getActivity().RESULT_OK && data != null) {

            Uri fileUri = data.getData();

            if (fileUri != null) {

                String fileType = requireContext().getContentResolver().getType(fileUri);

                //Allow only image/video
                if (fileType != null &&
                        (fileType.startsWith("image/") || fileType.startsWith("video/"))) {

                    //Show acknowledgement
                    layoutFileAck.setVisibility(View.VISIBLE);

                    // Extract simple file name
                    String fileName = fileUri.getLastPathSegment();

                    tvFileName.setText("Uploaded: " + fileName);

                    Toast.makeText(getContext(), "File uploaded successfully", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getContext(), "Only image or video allowed", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}