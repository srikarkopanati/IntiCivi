package com.example.inticiviapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class register extends AppCompatActivity {
    TextInputEditText etPhone, etAadhaar, etAddress;
    TextInputLayout tilAadhaar;
    Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            etPhone = findViewById(R.id.et_phone);
            etAadhaar = findViewById(R.id.et_aadhaar);
            etAddress = findViewById(R.id.et_address);
            tilAadhaar = findViewById(R.id.til_aadhaar);
            btnSubmit = findViewById(R.id.btn_submit);

            btnSubmit.setOnClickListener(v1 -> validateForm());
            
            return insets;
        });
    }

    private void validateForm() {

        String phone = etPhone.getText().toString().trim();
        String aadhaar = etAadhaar.getText().toString().trim();
        String address = etAddress.getText().toString().trim();

        // PHONE VALIDATION
        if (phone.isEmpty()) {
            etPhone.setError("Phone number required");
            return;
        }

        if (phone.length() != 10) {
            etPhone.setError("Enter valid 10-digit phone");
            return;
        }

        // AADHAAR VALIDATION
        if (aadhaar.isEmpty()) {
            tilAadhaar.setError("Aadhaar required");
            return;
        }

        if (aadhaar.length() != 12) {
            tilAadhaar.setError("Enter valid 12-digit Aadhaar");
            return;
        } else {
            tilAadhaar.setError(null);
        }

        // ADDRESS VALIDATION
        if (address.isEmpty()) {
            etAddress.setError("Address required");
            return;
        }

        // SUCCESS
        Toast.makeText(this, "Profile Completed", Toast.LENGTH_LONG).show();

        // 👉 Move to next screen
         startActivity(new Intent(this, MainActivity.class));
    }
}