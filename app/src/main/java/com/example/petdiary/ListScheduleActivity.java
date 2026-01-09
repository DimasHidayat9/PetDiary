package com.example.petdiary;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;

public class ListScheduleActivity extends AppCompatActivity {
    private RecyclerView rv;
    private PetAdapter adapter;
    private ArrayList<PetSchedule> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Menggunakan layout yang sudah di-set background gradasi biru-hijau lembut
        setContentView(R.layout.activity_list_schedule);

        Toolbar toolbar = findViewById(R.id.toolbarList);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Daftar Hewan");
        }

        rv = findViewById(R.id.rvPetSchedule);
        rv.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton fab = findViewById(R.id.fabAdd);
        fab.setOnClickListener(v -> startActivity(new Intent(this, MainActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        SharedPreferences prefs = getSharedPreferences("PetDiaryPrefs", MODE_PRIVATE);
        String json = prefs.getString("pet_list", "[]");
        list = new Gson().fromJson(json, new TypeToken<ArrayList<PetSchedule>>(){}.getType());

        if (list == null) list = new ArrayList<>();

        adapter = new PetAdapter(list, new PetAdapter.OnItemClickListener() {
            @Override
            public void onDeleteClick(int pos) {
                AlarmHelper.cancelAlarm(ListScheduleActivity.this, list.get(pos));
                list.remove(pos);
                prefs.edit().putString("pet_list", new Gson().toJson(list)).apply();
                adapter.notifyItemRemoved(pos);
            }

            @Override
            public void onSwitchChange(int pos, boolean isChecked) {
                list.get(pos).setNotifyActive(isChecked);
                prefs.edit().putString("pet_list", new Gson().toJson(list)).apply();
                if (isChecked) {
                    AlarmHelper.setAlarm(ListScheduleActivity.this, list.get(pos));
                } else {
                    AlarmHelper.cancelAlarm(ListScheduleActivity.this, list.get(pos));
                }
            }
        });

        rv.setAdapter(adapter);

        // Animasi kemunculan kartu dari atas ke bawah
        LayoutAnimationController anim = AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animation_fall_down);
        rv.setLayoutAnimation(anim);
        rv.scheduleLayoutAnimation();
    }
}