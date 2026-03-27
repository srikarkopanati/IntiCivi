package com.example.inticiviapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.inticiviapp.Model.Notice;
import com.example.inticiviapp.R;

import java.util.List;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.ViewHolder> {

    List<Notice> list;

    public NoticeAdapter(List<Notice> list) {
        this.list = list;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView day, month, title, subtitle, tag;

        public ViewHolder(View itemView) {
            super(itemView);
            day = itemView.findViewById(R.id.dateDay);
            month = itemView.findViewById(R.id.dateMonth);
            title = itemView.findViewById(R.id.title);
            subtitle = itemView.findViewById(R.id.subtitle);
            tag = itemView.findViewById(R.id.tag);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notice, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder h, int position) {
        Notice n = list.get(position);


        h.day.setText(n.day);
        h.month.setText(n.month);
        h.title.setText(n.title);
        h.subtitle.setText(n.subtitle);
        h.tag.setText(n.tag);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
