package com.example.reminderapp2;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

//di dalam class ini berfungsi untuk membuat database
public class DatabaseHelper extends SQLiteOpenHelper {
    //sebelumnya menginisiasi variabel agar saat akan digunaka tinggal menuliskan nama variabelnya saja sehingga mempermudah penulisan kode
    //terlebih dahulu ini isasi untuk danam DB
    private static final String TAG = "DatabaseHelper";

    //inisiasi nama tabel
    private static final String TABLE_NAME = "Reminder";

    //kemudian inisiasi atribut yang ada didalam tabel
    private static final String COL1 = "ID"; //inisaisi cololom 1 berisi id
    private static final String COL2 = "Judul"; //inisaisi cololom 2 berisi judul
    private static final String COL3 = "Kapan"; //inisaisi cololom 3 berisi kapan
    private static final String COL4 = "Time"; //inisaisi cololom 4 berisi time
    private static final String COL5 = "Date"; //inisaisi cololom 5 berisi date

    //method dibuat untuk memudahkan dalam mengakses database
    public DatabaseHelper(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    //digunakan untuk membuat database
    public void onCreate(SQLiteDatabase db) {
        //query yang di inisiasi ke variabel string 'createTable' agar kemudian bisa dipanggil saat digunakan
        String createTable = "CREATE TABLE " + TABLE_NAME + "(" +
                ""                + COL1 + " integer primary key, " + //data berjenis int karena sebuah id dan merupakan primary key dari tabel
                ""                + COL2 + " TEXT, " + // tipe data judul yaitu 'text'
                ""                + COL3 + " TEXT, " + //tipe data 'kapan'(waktu minum obat) juga berbentuk text
                ""                + COL4 + " DATE," + //tipe data yang digunakan yaitu date
                ""                + COL5 + " TIME" + ")"; //tipe yang digunakan yaitu time atau waktu
        //log pesan saat table dibuat
        Log.d(TAG, "Creating table " + createTable);

        //execute query
        db.execSQL(createTable);
    }

    @Override
    //method untuk memperbarui database
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    //Memasukkan data ke database
    public boolean insertData(String item, String item2,String date, String time) {
        //menulisa data kedalam database
        SQLiteDatabase db = this.getWritableDatabase();
        //besaran data yang akan di isert
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, item);  //input user yang ada dalam item akan masuk ke colom2
        contentValues.put(COL3, item2); //input user yang ada dalam item2 akan masuk ke colom3
        contentValues.put(COL4, date); //input user yang ada dalam date akan masuk ke colom4
        contentValues.put(COL5, time); ////input user yang ada dalam time akan masuk ke colom5

        //pesan log yang akan ditampilkan dalam terminal saat proses insert berjalan
        Log.d(TAG, "insertData: Inserting " + item +  " to " + TABLE_NAME);
        //nilai nilai yang sudah ditangkap oleh content values kemudian di masukan kedalam database
        long result = db.insert(TABLE_NAME, null, contentValues);
        db.close(); //tutup database ketika selesai digunakan
        return result != -1; //kembalikan nilai jika tidak sama dengan -1
    }

    //Menghapus data dari database
    void deleteData(int id) {
        //menulis data kedalam database
        SQLiteDatabase db = this.getWritableDatabase();
        //hapus data berdasarkan id
        db.delete(TABLE_NAME, COL1 + "=" + id, null);
    }

    //Memuat semua data ke listview
    public ArrayList<Alarm> getAllData() {
        //data dalam alarm diubah kedalam bentuk arraylist
        ArrayList<Alarm> arrayList = new ArrayList<>();
        //membaca data dalam database
        SQLiteDatabase db = this.getReadableDatabase();
        //query memilih semua data dalam databae
        String query = "SELECT * FROM " + TABLE_NAME;
        //cursor untuk membaca data
        Cursor cursor = db.rawQuery(query, null);

        //perulangan pergerakan cursor dalam membaca data
        while (cursor.moveToNext()) {
            //baca id dengan type int dalam index 0
            int id = cursor.getInt(0);

            //baca title dengan type string dalam index 1
            String title = cursor.getString(1);

            //baca title2 dengan type string dalam index 1
            String title2 = cursor.getString(2);

            //baca date dengan tipe data string dalam index 3
            String date = cursor.getString(3);

            //baca time dengan tipe data string dalam index 4
            String time = cursor.getString(4);

            //inisiasi variabel alarm yng berisi data yang di input oleh user
            Alarm alarm = new Alarm(id, title, title2, date, time);

            //data yang ada di dalam variabel alarm di masukkan kedalam array list
            arrayList.add(alarm);
        }
        //tutup database
        db.close();
        //kebalikan nilai dari array list
        return arrayList;
    }
}
