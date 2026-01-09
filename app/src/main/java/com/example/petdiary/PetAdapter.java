package com.example.petdiary;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class PetAdapter extends RecyclerView.Adapter<PetAdapter.ViewHolder> {
    private ArrayList<PetSchedule> list;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onDeleteClick(int pos);
        void onSwitchChange(int pos, boolean isChecked);
    }

    public PetAdapter(ArrayList<PetSchedule> list, OnItemClickListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pet_schedule, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PetSchedule p = list.get(position);
        holder.tvNama.setText(p.getName());
        holder.tvWaktu.setText(p.getMorningTime() + " | " + p.getEveningTime());

        // Reset listener sebelum setChecked agar tidak trigger loop
        holder.swNotify.setOnCheckedChangeListener(null);
        holder.swNotify.setChecked(p.isNotifyActive());

        // Terapkan warna awal
        updateSwitchColor(holder.swNotify, p.isNotifyActive());

        holder.btnDelete.setOnClickListener(v -> listener.onDeleteClick(position));

        holder.swNotify.setOnCheckedChangeListener((buttonView, isChecked) -> {
            updateSwitchColor(holder.swNotify, isChecked);
            listener.onSwitchChange(position, isChecked);
        });
    }

    // --- LOGIKA WARNA SWITCH (HIJAU & MERAH) ---
    private void updateSwitchColor(SwitchCompat sw, boolean isChecked) {
        int thumbColor = isChecked ? Color.parseColor("#4CAF50") : Color.parseColor("#F44336"); // Hijau : Merah
        int trackColor = isChecked ? Color.parseColor("#A5D6A7") : Color.parseColor("#EF9A9A"); // Hijau Muda : Merah Muda

        sw.setThumbTintList(ColorStateList.valueOf(thumbColor));
        sw.setTrackTintList(ColorStateList.valueOf(trackColor));
    }

    @Override
    public int getItemCount() { return list != null ? list.size() : 0; }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNama, tvWaktu;
        ImageButton btnDelete;
        SwitchCompat swNotify;

        public ViewHolder(@NonNull View v) {
            super(v);
            tvNama = v.findViewById(R.id.tvPetName);
            tvWaktu = v.findViewById(R.id.tvFeedingTime);
            btnDelete = v.findViewById(R.id.btnDelete);
            swNotify = v.findViewById(R.id.switchNotify);
        }
    }
}