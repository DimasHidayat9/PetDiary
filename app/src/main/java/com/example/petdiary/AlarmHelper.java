package com.example.petdiary;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.widget.Toast;
import java.util.Calendar;

public class AlarmHelper {

    public static void setAlarm(Context context, PetSchedule pet) {
        if (!pet.isNotifyActive()) return;

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // CEK IZIN ANDROID 12+ (PENTING: Agar tidak gagal simpan/crash)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarmManager != null && !alarmManager.canScheduleExactAlarms()) {
                Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                context.startActivity(intent);
                Toast.makeText(context, "Aktifkan izin alarm untuk PetDiary agar jadwal berfungsi", Toast.LENGTH_LONG).show();
                return;
            }
        }

        try {
            schedule(context, pet.getName(), pet.getMorningTime(), pet.getId().hashCode() + 1);
            schedule(context, pet.getName(), pet.getEveningTime(), pet.getId().hashCode() + 2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void schedule(Context context, String name, String time, int reqCode) {
        String[] t = time.split(":");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(t[0]));
        cal.set(Calendar.MINUTE, Integer.parseInt(t[1]));
        cal.set(Calendar.SECOND, 0);

        if (cal.before(Calendar.getInstance())) cal.add(Calendar.DATE, 1);

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, NotificationReceiver.class).putExtra("petName", name);

        PendingIntent pi = PendingIntent.getBroadcast(context, reqCode, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        if (am != null) {
            am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi);
        }
    }

    public static void cancelAlarm(Context context, PetSchedule pet) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, NotificationReceiver.class);
        PendingIntent p1 = PendingIntent.getBroadcast(context, pet.getId().hashCode() + 1, intent, PendingIntent.FLAG_IMMUTABLE);
        PendingIntent p2 = PendingIntent.getBroadcast(context, pet.getId().hashCode() + 2, intent, PendingIntent.FLAG_IMMUTABLE);
        if (am != null) { am.cancel(p1); am.cancel(p2); }
    }
}