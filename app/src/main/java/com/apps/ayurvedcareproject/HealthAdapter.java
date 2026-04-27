package com.apps.ayurvedcareproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HealthAdapter extends RecyclerView.Adapter<HealthAdapter.ViewHolder> {

    List<HealthModel> list;

    public HealthAdapter(List<HealthModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HealthModel model = list.get(position);
        holder.problem.setText(model.problem);
        holder.solution.setText(model.solution);
        holder.timestamp.setText(model.timestamp);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView problem, solution, timestamp;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            problem = itemView.findViewById(R.id.problemTxt);
            solution = itemView.findViewById(R.id.solutionTxt);
            timestamp = itemView.findViewById(R.id.timestampTxt);
        }
    }
}
