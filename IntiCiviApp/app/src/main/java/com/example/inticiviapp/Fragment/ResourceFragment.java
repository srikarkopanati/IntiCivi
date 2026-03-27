package com.example.inticiviapp.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.inticiviapp.Adapter.ResourceAdapter;
import com.example.inticiviapp.Model.Resource;
import com.example.inticiviapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class ResourceFragment extends Fragment {
    View resourceView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        resourceView= inflater.inflate(R.layout.fragment_resource, container, false);

        RecyclerView recyclerView = resourceView.findViewById(R.id.recyclerAll);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<Resource> list = new ArrayList<>();

        // FULL LIST
        list.add(new Resource("Citizen Complaint User Manual",
                "PDF • 2.4 MB • Updated Mar 2026", "PDF"));

        list.add(new Resource("Complaint SLA Guidelines 2026",
                "DOC • 1.1 MB • Admin Circular", "DOC"));

        list.add(new Resource("Monthly Grievance Report – Feb 2026",
                "XLS • 3.8 MB • Public Record", "XLS"));

        list.add(new Resource("RTI Application Form (Form A)",
                "PDF • 180 KB • Govt. of AP", "PDF"));

        list.add(new Resource("API Documentation for Developers",
                "Link • Open Data Portal", "LINK"));

        ResourceAdapter adapter = new ResourceAdapter(list);
        recyclerView.setAdapter(adapter);

        return resourceView;
    }
}