package com.example.inticiviapp.Adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.inticiviapp.Model.Resource;
import com.example.inticiviapp.R;

import java.util.List;

public class ResourceAdapter extends RecyclerView.Adapter<ResourceAdapter.ViewHolder> {

    List<Resource> list;

    public ResourceAdapter(List<Resource> list) {
        this.list = list;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, subtitle, type;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            subtitle = itemView.findViewById(R.id.subtitle);
            type = itemView.findViewById(R.id.type);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_resource, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder h, int position) {
        Resource r = list.get(position);

        h.title.setText(r.title);
        h.subtitle.setText(r.subtitle);
        h.type.setText(r.type);

        // 🔥 Dynamic Color
        if (r.type.equals("PDF")) {
            h.type.setBackgroundColor(Color.parseColor("#FFCDD2"));
        } else if (r.type.equals("DOC")) {
            h.type.setBackgroundColor(Color.parseColor("#D1C4E9"));
        } else if (r.type.equals("XLS")) {
            h.type.setBackgroundColor(Color.parseColor("#C8E6C9"));
        } else if (r.type.equals("LINK")) {
            h.type.setBackgroundColor(Color.parseColor("#BBDEFB"));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
