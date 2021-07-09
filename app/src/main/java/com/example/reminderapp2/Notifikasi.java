package com.example.reminderapp2;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
//berfungsi untuk melakukan broadcast pesan dari aplikasi lain atau dari sistem
public class Notifikasi extends BroadcastReceiver {
    //deklarasi penggunaan notification id
    public static String NOTIFICATION_ID = "notification-id";

    // deklarasi string notification berisi data dari mainActivity
    public static String NOTIFICATION = "notification";

    //digunakan untuk menerima pesan broadcast yang masuk dalam hal ini dari class MainActivity
    public void onReceive(Context context, Intent intent) {
        //digunakan untuk meminta ijin sistem untuk melewatkan informasi
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        //penyajian informasi yang telah didapatkan dari MainActivity diubah menjadi notifikasi
        Notification notification = intent.getParcelableExtra(NOTIFICATION);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            //merupakan tingkat kepentingan dari notifikasi tersebut
            int importance = NotificationManager.IMPORTANCE_HIGH;
            //inisiasi notification channel yang baru
            NotificationChannel notificationChannel = new NotificationChannel(MainActivity.NOTIFICATION_CHANNEL_ID,
                    "Pengingat", importance);
            //assert digunakan untuk menetukan kondisi bernilai true atau benar
            assert notificationManager != null;
            //membuat notifikasi berdasar channel yang ada
            notificationManager.createNotificationChannel(notificationChannel);
        }
        //mendapatkan data dari obyek yang telah dibuat
        int id = intent.getIntExtra(NOTIFICATION_ID, 0);
        //jika notifikasi manager tidak kosong atau terdapat tugas maka jalankan notifikasi
        if (notificationManager != null) {
            notificationManager.notify(id, notification);
        }
    }
}

