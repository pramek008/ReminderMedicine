package com.example.reminderapp2;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.AbsListView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    //inisiasi channel id yang bersifat unik
    public static final String NOTIFICATION_CHANNEL_ID = "1001";
    //inisiasi default notification channel id sebagai default
    private final static String default_notification_channel_id = "default";
    //inisiasi
    private static final String TAG = "MainActivity";

    //mendeklarasikan/memanggil class java Database Helper
    private DatabaseHelper databaseHelper;

    //Deklarasi variabel jenis data listview
    private ListView itemsListView;

    //deklarasi variabel button
    private FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //method dibuat untuk memudahkan dalam mengakses database
        databaseHelper = new DatabaseHelper(this);

        //menghubungkan variabel fab dengan komponen pada layout
        fab = findViewById(R.id.fab);

        //menghubungkan variabel itemListView dengan komponen pada layout xml
        itemsListView = findViewById(R.id.itemsList);

        //memanggil method di luar class
        populateListView();
        onFabClick();
    }

    //Mengatur notifikasi
    private void scheduleNotification(Notification notification, Notification obat, long delay) {
        //memanggil activity Notifikasi.class
        Intent notificationIntent = new Intent(this, Notifikasi.class);
        //mangambilData NofitikasiId
        notificationIntent.putExtra(Notifikasi.NOTIFICATION_ID, 1);
        //Mengambil data di dalam method
        notificationIntent.putExtra(Notifikasi.NOTIFICATION, notification);

        //mengambil data yang ada untuk dikirimkan
        notificationIntent.putExtra(Notifikasi.NOTIFICATION, (Parcelable) obat);

        //intent yang dikirim akan ditampilkan menurut waktu yang sudah didaftarkan
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getLayoutInflater().getContext().getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, delay, pendingIntent);
        }
    }

    private Notification getNotification(String content) {
        //Saat notifikasi di klik di arahkan ke MainActivity
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        //pendingintent untuk memulai activity saat waktu lain atau tidak secara langsung
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        //builder digunakan untuk menambahkan konstruktor
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getLayoutInflater().getContext(), default_notification_channel_id);

        //untuk memnampilkan teks judul di kolom notifikasi
        builder.setContentTitle("Pengingat Minum Obat");

        //menuliskan text di dalam notifikasi sesuai dengan content atau input user
        builder.setContentText(content);

        //berisi pending intent dimana saat intent di klik akan diarahkan pada activity yang terdaftar
        builder.setContentIntent(pendingIntent);

        //agar notifikasi dapat di swipe dan hilang dari bar
        builder.setAutoCancel(true);

        //untuk menampilkan icon saat notifikasi muncul
        builder.setSmallIcon(android.R.drawable.ic_dialog_info);

        //untul mengatur suara default dari device saat notifikasi muncul
        builder.setDefaults(Notification.DEFAULT_SOUND);

        //agar notifikasi muncul di channel id yang sudah di tentukan
        builder.setChannelId(NOTIFICATION_CHANNEL_ID);

        //menentukan prioritas dari notifikasi yang akan muncul
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        return builder.build();
    }

    //Memasukkan data ke database
    private void insertDataToDb(String title, String title2,String date, String time) {
        //mengirim variabel yang di input user ke databaseHelper untuk dimasukkan ke dalam database
        boolean insertData = databaseHelper.insertData(title, title2, date, time);

        //percabangan kondisi saat data berhasil di input
        if (insertData) {

            //mekanisme untuk mencoba menjalankan program
            try {
                populateListView();
                toastMsg("Tugas di tambahkan");
            }
            //jika terdapat masalah maka akan ditangkap dan ditampilkan
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        //saat data tidak berhasil di input maka akan menampikan pesan berbentuk toast
        else
            toastMsg("Opps.. terjadi kesalahan saat menyimpan!");
    }

    //Mengambil seluruh data dari database ke listview
    private void populateListView() {
        //mekanisme untuk mencoba menjalankan program
        try {
            //mengubah data dalam DB ke menajdi array list
            ArrayList<Alarm> items = databaseHelper.getAllData();

            //memanggil data dari ItemAdater class
            ItemAdapter itemsAdopter = new ItemAdapter(this, items);

            //menampikan data kedalam layout listview
            itemsListView.setAdapter(itemsAdopter);

            //pemberitauan data sudah diubah dan tampilan mengikuti perubahan data
            itemsAdopter.notifyDataSetChanged();
        }
        //jika terdapat masalah maka akan ditangkap dan ditampilkan
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Saat tombol fab di klik
    private void onFabClick() {
        //mekanisme untuk mencoba menjalankan program
        try {
            //methon untuk event floating button
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                //saat di klik btn fab akan manampikan dialog
                public void onClick(View v) {
                    showAddDialog();
                }
            });
        }
        //jika terdapat masalah maka akan ditangkap dan ditampilkan
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Implementasi klik dari tombol tambah
    @SuppressLint("SimpleDateFormat")
    //dialog yang akan muncul
    private void showAddDialog() {
        //
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getLayoutInflater().getContext());

        //
        LayoutInflater inflater = this.getLayoutInflater();
        @SuppressLint("InflateParams")

        //menghubungkan layout xml agar dapat ditampikan sebagai dialog
        final View dialogView = inflater.inflate(R.layout.activity_set_reminder, null);

        //dialog yang sudah dihubungkan ke layout kemudian di buat atau build
        dialogBuilder.setView(dialogView);

        //menghubungkan variabel EditText dengan variabel di layout .xml
        final EditText waktu = dialogView.findViewById(R.id.titleWaktu);

        //menghubungkan variabel EditText dengan variabel di layout .xml
        final EditText obat = dialogView.findViewById(R.id.titleObat);

        //menghubungkan variabel timepiker ke layout .xml
        final TextView timepick = dialogView.findViewById(R.id.pickTime);

        //menghubungkan variabel datepick ke layout .xml
        final TextView datepick = dialogView.findViewById(R.id.pickDate);

        //mengembalikan tanggal sekarang dalam millisecond
        final long date = System.currentTimeMillis();


        //untuk menampilkan format tanggal dimana akan ditampilkan tanggal dan bulan
        SimpleDateFormat dateSdf = new SimpleDateFormat("d MMMM");

        //data tanggal yang akan ditampikan berjenis String
        String dateString = dateSdf.format(date);

        //waktu yang sudah berjenis string semeblumnya kemudian ditampikan kedalam textView melalui datepick
        datepick.setText(dateString);


        //untuk menampilkan format tanggal dimana akan ditampilkan dalam jam dan menit
        SimpleDateFormat timeSdf = new SimpleDateFormat("hh : mm");

        //data tanggal yang akan ditampikan berjenis String
        String timeString = timeSdf.format(date);

        //waktu yang sudah berjenis string semeblumnya kemudian ditampikan kedalam textView melalui timepick
        timepick.setText(timeString);

        //
        final Calendar cal = Calendar.getInstance();

        //mengatur calendar menjadi waktu realtime saat dijalankan
        cal.setTimeInMillis(System.currentTimeMillis());

        //Set tanggal, dimana saat textview Datepick di pilih maka program dalam method tersebut akan dijalankan
        datepick.setOnClickListener(new View.OnClickListener() {
//            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            //respon terhadap input klik atau tekan
            public void onClick(View v) {
                //menginisiasi dialog DatePicker
                final DatePickerDialog datePickerDialog = new DatePickerDialog(getLayoutInflater().getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            //fungsi ini digunakan untuk menerima data yang di inputkan melalui DatePickerDialog
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                //set bulan menurut waktu sekarang
                                String newMonth = getMonth(monthOfYear);
                                //menampilkan text view waktu sekarang berupa tanggal dan bulan
                                datepick.setText(dayOfMonth + " " + newMonth);
                                //set tahun menurut waktu real
                                cal.set(Calendar.YEAR, year);
                                //set bulan menurut waktu real
                                cal.set(Calendar.MONTH, monthOfYear);
                                //set tanggal menurut waktu real
                                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            }
                            //menerima data input tanggal dari user berupa hari bulan dan tahun setelah dipilih
                        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
                //munculkan dialog pilih tanggal
                datePickerDialog.show();
                //menampilkan minimal tangal yang bisa dipilih yaitu tanggal sekarng tidak bisa tanggal sebelumnya
                datePickerDialog.getDatePicker().setMinDate(date);
            }
        });

        //saat txtview waktu ditekan maka akan memunculkan isi method
        timepick.setOnClickListener(new View.OnClickListener() {
            @Override
            //fungsi ini digunakan untuk menerima data yang di inputkan melalui timepicker dialog
            public void onClick(View v) {
                //menginisiasi dialog TimePicker
                TimePickerDialog timePickerDialog = new TimePickerDialog(getLayoutInflater().getContext(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            //fungsi ini digunakan untuk menerima data yang di inputkan melalui timepicker
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                String time;
                                //menuliskan format menit dengan 2 digit
                                String minTime = String.format("%02d", minute);
                                //mengatur format waktu(jam:menit) saat ini
                                time= hourOfDay +" : "+minTime;
                                //menampikan format waktu 'time' kedalam text view timepick
                                timepick.setText(time);
                                //set jam menurut waktu real
                                cal.set(Calendar.HOUR, hourOfDay);
                                //set menit menurut waktu real
                                cal.set(Calendar.MINUTE, minute);
                                //set detik ke 0
                                cal.set(Calendar.SECOND, 0);
                                //pesan jika set waktu berhasil dijalankan
                                Log.d(TAG, "onTimeSet: Time has been set successfully");
                            }
                            //menerima data input waktu dari user berupa jam dan menit
                        }, cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE), false);
                //memunculkan dialog pilih waktu
                timePickerDialog.show();
            }
        });

        //mengeset title dari dialog yang akan muncul
        dialogBuilder.setTitle("Buat Pengingat Baru");
        //set positif atau button pilih kedalam dialog yang akan muncul
        dialogBuilder.setPositiveButton("Tambah", new DialogInterface.OnClickListener() {
            //menerima action user berupa klik
            public void onClick(DialogInterface dialog, int whichButton) {
                //mmengubah data input user kedalam text string
                //title
                //title2
                //time
                //date akan diubah menjadi format string
                String title = waktu.getText().toString();
                String title2 = obat.getText().toString();
                String time = timepick.getText().toString();
                String date = datepick.getText().toString();

                //jika jududl tidak kosong maka akan dijalankan
                if (title.length() != 0) {
                    //mekanisme untuk mencoba menjalankan program
                    try {
                        //menginput data data yang sudah berupa string kedalam database
                        insertDataToDb(title, title2, date, time);
                        //meneruskan data data tersebut ke method sceduleNotification
                        scheduleNotification(getNotification(title), getNotification(title2), cal.getTimeInMillis());
                    }
                    //jika terdapat masalah maka akan ditangkap dan ditampilkan
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                //jika syarat tidak terpenuhi (title kosong) maka akan keluar pesan berupa toast
                else {
                    toastMsg("Harus Isi Kolom");
                }
            }
        });
        //set negative button atau tombol bata
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            //menerima respon berupa klik dari pengguna
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });
        //inisiasi dan buat dialog
        AlertDialog b = dialogBuilder.create();
        //memunculkan dialogyang sudah dibuat
        b.show();
    }

    //Metode pesan toast
    private void toastMsg(String msg) {
        Toast t = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        t.show();
    }

    //mendapatkan data bulan saat real waktu aplikasi tersebut dijalankan
    private String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month - 1];
    }
}



