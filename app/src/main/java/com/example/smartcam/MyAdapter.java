package com.example.smartcam;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private List<NotificationData> dataList;

    public MyAdapter(List<NotificationData> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.noti_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        NotificationData data = dataList.get(position);
        holder.tv_notititle.setText(data.getTitle());
        holder.tv_notiday.setText(data.getDate());
        holder.tv_notiTime.setText(data.getTime());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_notititle, tv_notiday, tv_notiTime;
        Button button2;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_notititle = itemView.findViewById(R.id.tv_title);
            tv_notiday = itemView.findViewById(R.id.tv_date);
            tv_notiTime = itemView.findViewById(R.id.tv_time);
            button2 = itemView.findViewById(R.id.button2);
        }
    }
}
