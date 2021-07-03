package com.example.reminderapp2;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";

    private static final String TABLE_NAME = "Reminder";
    private static final String COL1 = "ID";
    private static final String COL2 = "Judul";
    private static final String COL3 = "Kapan";
    private static final String COL4 = "Time";
    private static final String COL5 = "Date";

    public DatabaseHelper(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        String createTable = "CREATE TABLE " + TABLE_NAME + "(" +
//                ""                + COL1 + " integer primary key, " +
//                ""                + COL2 + " TEXT, " +
//                ""                + COL3 + " TEXT," +
//                ""                + COL4 + " TIME" + ")";

        String createTable = "CREATE TABLE " + TABLE_NAME + "(" +
                ""                + COL1 + " integer primary key, " +
                ""                + COL2 + " TEXT, " +
                ""                + COL3 + " TEXT, " +
                ""                + COL4 + " DATE," +
                ""                + COL5 + " TIME" + ")";

        Log.d(TAG, "Creating table " + createTable);
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    //Memasukkan data ke database
    public boolean insertData(String item, String item2,String date, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, item);
        contentValues.put(COL3, item2);
        contentValues.put(COL4, date);
        contentValues.put(COL5, time);

        Log.d(TAG, "insertData: Inserting " + item +  " to " + TABLE_NAME);
        long result = db.insert(TABLE_NAME, null, contentValues);
        db.close();
        return result != -1;
    }

    //Menghapus data dari database
    void deleteData(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COL1 + "=" + id, null);
    }

    //Memuat semua data ke listview
    public ArrayList<ModelData> getAllData() {
        ArrayList<ModelData> arrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        @SuppressLint("Recycle")
        Cursor cursor = db.rawQuery(query, null);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String title = cursor.getString(1);
            String title2 = cursor.getString(2);
            String date = cursor.getString(3);
            String time = cursor.getString(4);
            ModelData modelData = new ModelData(id, title, title2, date, time);
            arrayList.add(modelData);
        }
        db.close();
        return arrayList;
    }
}
