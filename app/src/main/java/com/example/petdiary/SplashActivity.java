package com.example.petdiary;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import java.util.List;
import java.util.Locale;

public class SplashActivity extends AppCompatActivity {
    private TextView tvWelcome, tvProvinsi;
    private ImageView imgLogo;
    private String fullText = "Selamat Datang";
    private int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        tvWelcome = findViewById(R.id.tvWelcomeText);
        tvProvinsi = findViewById(R.id.tvProvinsi);
        imgLogo = findViewById(R.id.imgLogoSplash);

        detectLocation();
    }

    private void detectLocation() {
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            client.getLastLocation().addOnSuccessListener(location -> {
                if (location != null) {
                    try {
                        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        if (addresses != null && !addresses.isEmpty()) {
                            String prov = addresses.get(0).getAdminArea();
                            tvProvinsi.setText(prov);
                            setGreetingByProvince(prov);
                        }
                    } catch (Exception e) {
                        tvProvinsi.setText("Indonesia");
                        fullText = "Selamat Datang";
                    }
                }
                startTyping();
            });
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            startTyping();
        }
    }

    private void setGreetingByProvince(String prov) {
        if (prov == null) return;

        // LOGIKA SAPAAN DAERAH (Mencakup 38 Provinsi)
        if (prov.contains("Aceh")) {
            fullText = "Peue Habah";
        } else if (prov.contains("Sumatera Utara")) {
            fullText = "Horas";
        } else if (prov.contains("Sumatera Barat") || prov.contains("Riau") || prov.contains("Kepulauan Riau") || prov.contains("Jambi")) {
            fullText = "Salamat Datang";
        } else if (prov.contains("Sumatera Selatan") || prov.contains("Bangka Belitung") || prov.contains("Bengkulu") || prov.contains("Lampung")) {
            fullText = "Slamat Datang";
        } else if (prov.contains("Jawa Barat") || prov.contains("Banten")) {
            fullText = "Wilujeng Sumping";
        } else if (prov.contains("Jakarta")) {
            fullText = "Selamat Datang";
        } else if (prov.contains("Jawa Tengah") || prov.contains("Jawa Timur") || prov.contains("Yogyakarta")) {
            fullText = "Sugeng Rawuh";
        } else if (prov.contains("Bali")) {
            fullText = "Om Swastiastu";
        } else if (prov.contains("Nusa Tenggara Barat")) {
            fullText = "Selamat Datang"; // Sapaan Sasak bervariasi
        } else if (prov.contains("Nusa Tenggara Timur")) {
            fullText = "Mai Ga'e";
        } else if (prov.contains("Kalimantan")) { // Mencakup Kalbar, Kalteng, Kalsel, Kaltim, Kalut
            fullText = "Adil Ka' Talino";
        } else if (prov.contains("Sulawesi Utara") || prov.contains("Gorontalo")) {
            fullText = "Tabea";
        } else if (prov.contains("Sulawesi Selatan") || prov.contains("Sulawesi Barat") || prov.contains("Sulawesi Tenggara") || prov.contains("Sulawesi Tengah")) {
            fullText = "Kurru Sumanga";
        } else if (prov.contains("Maluku")) { // Maluku & Maluku Utara
            fullText = "Sio Amato";
        } else if (prov.contains("Papua")) { // Mencakup Papua, Papua Barat, Tengah, Selatan, Pegunungan, Barat Daya
            fullText = "Sio Amato";
        } else {
            fullText = "Selamat Datang";
        }
    }

    private void startTyping() {
        if (index <= fullText.length()) {
            tvWelcome.setText(fullText.substring(0, index++));
            new Handler().postDelayed(this::startTyping, 100);
        } else {
            new Handler().postDelayed(this::transitionToLogo, 1000);
        }
    }

    private void transitionToLogo() {
        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setDuration(600);
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                tvWelcome.setVisibility(View.GONE);
                imgLogo.setVisibility(View.VISIBLE);
                Animation fadeIn = new AlphaAnimation(0, 1);
                fadeIn.setDuration(800);
                imgLogo.startAnimation(fadeIn);
                new Handler().postDelayed(() -> {
                    startActivity(new Intent(SplashActivity.this, ListScheduleActivity.class));
                    finish();
                }, 2000);
            }
            @Override public void onAnimationStart(Animation animation) {}
            @Override public void onAnimationRepeat(Animation animation) {}
        });
        tvWelcome.startAnimation(fadeOut);
    }
}