package com.example.petdiary;

import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private EditText etNama, etPagi, etSore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbarInput);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Tambah Jadwal");
            // Mengubah Tombol Back menjadi Hitam
            final Drawable upArrow = ContextCompat.getDrawable(this, androidx.appcompat.R.drawable.abc_ic_ab_back_material);
            if (upArrow != null) {
                upArrow.setColorFilter(ContextCompat.getColor(this, android.R.color.black), PorterDuff.Mode.SRC_ATOP);
                getSupportActionBar().setHomeAsUpIndicator(upArrow);
            }
        }

        etNama = findViewById(R.id.etNamaHewan);
        etPagi = findViewById(R.id.etMakanPagi);
        etSore = findViewById(R.id.etMakanSore);
        Button btnSimpan = findViewById(R.id.btnBuatJadwal);

        etPagi.setOnClickListener(v -> showTimePicker(etPagi));
        etSore.setOnClickListener(v -> showTimePicker(etSore));
        btnSimpan.setOnClickListener(v -> saveSchedule());
    }

    private void showTimePicker(EditText target) {
        Calendar c = Calendar.getInstance();
        new TimePickerDialog(this, (view, h, m) ->
                target.setText(String.format("%02d:%02d", h, m)),
                c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show();
    }

    private void saveSchedule() {
        String nama = etNama.getText().toString().trim();
        String pagi = etPagi.getText().toString().trim();
        String sore = etSore.getText().toString().trim();

        if (nama.isEmpty() || pagi.isEmpty() || sore.isEmpty()) {
            Toast.makeText(this, "Lengkapi semua data!", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences prefs = getSharedPreferences("PetDiaryPrefs", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString("pet_list", "[]");
        ArrayList<PetSchedule> list = gson.fromJson(json, new TypeToken<ArrayList<PetSchedule>>(){}.getType());

        PetSchedule newPet = new PetSchedule(String.valueOf(System.currentTimeMillis()), nama, pagi, sore, true);
        list.add(newPet);

        prefs.edit().putString("pet_list", gson.toJson(list)).apply();

        // PENTING: Tetap panggil logika alarm Anda
        AlarmHelper.setAlarm(this, newPet);

        Toast.makeText(this, "Berhasil disimpan!", Toast.LENGTH_SHORT).show();
        finish(); // Kembali ke halaman daftar
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}