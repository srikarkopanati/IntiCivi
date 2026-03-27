package com.example.inticiviapp.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.inticiviapp.Adapter.NoticeAdapter;
import com.example.inticiviapp.Model.Notice;
import com.example.inticiviapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class NoticeFragment extends Fragment {
    View NoticeView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        NoticeView= inflater.inflate(R.layout.fragment_notice, container, false);

        RecyclerView recyclerView = NoticeView.findViewById(R.id.recyclerAllNotice);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<Notice> list = new ArrayList<>();

        // FULL LIST (same data)
        list.add(new Notice("18","MAR","System maintenance scheduled","Issued by IT Dept","IMPORTANT"));
        list.add(new Notice("15","MAR","Urban Flooding complaints accepted","Issued by PWD","NEW"));
        list.add(new Notice("10","MAR","Complaint SLA updated","Issued by Admin","UPDATED"));
        list.add(new Notice("05","MAR","Ward re-mapping update","Issued by Revenue Dept","IMPORTANT"));
        list.add(new Notice("01","MAR","App v2.4 released","Issued by IT Dept","NEW"));

        NoticeAdapter adapter = new NoticeAdapter(list);
        recyclerView.setAdapter(adapter);

        return NoticeView;
    }
}