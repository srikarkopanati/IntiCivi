package com.example.inticiviapp.Fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.inticiviapp.R;

public class HelpFragment extends Fragment {
    View HelpView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        HelpView= inflater.inflate(R.layout.fragment_help, container, false);
        LinearLayout call = HelpView.findViewById(R.id.btn_call);
        LinearLayout email = HelpView.findViewById(R.id.btn_email);

//  CALL
        call.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:+919502673836"));
            startActivity(intent);
        });

// EMAIL
        email.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:support@inticivi.gov.in"));
            intent.putExtra(Intent.EXTRA_SUBJECT, "Support Request");
            startActivity(intent);
        });
        TextView q1 = HelpView.findViewById(R.id.q1);
        TextView a1 = HelpView.findViewById(R.id.a1);

        q1.setOnClickListener(v -> {
            a1.setVisibility(a1.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
        });

        TextView q2 = HelpView.findViewById(R.id.q2);
        TextView a2 = HelpView.findViewById(R.id.a2);

        q2.setOnClickListener(v -> {
            a2.setVisibility(a2.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
        });

        return HelpView;
    }
}