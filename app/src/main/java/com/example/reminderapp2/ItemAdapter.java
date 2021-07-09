package com.example.reminderapp2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ItemAdapter extends BaseAdapter {

    //deklarasi
    private Context context; //deklarasi contex
    private ArrayList<Alarm> arrayList; //deklarasi arraylist dari alarm

    public ItemAdapter(Context context, ArrayList<Alarm> arrayList) {
        super();
        this.context = context;
        this.arrayList = arrayList;
    }

    //mengembalikan jumlah item yang akan ditampilkan di list
    @Override
    public int getCount() {
        return this.arrayList.size();
    }

    //mendapatkan posisi data dalam array list
    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    //mengembalikan nilai dari posisi intem ke adapter
    @Override
    public long getItemId(int position) {
        return position;
    }

    //ketika semua komponen data item sudah didapat maka akan ditampilkan kedalam list
    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        //mengatur layout inflater dari kontext yang diberika
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //assert digunakan untuk menetukan kondisi bernilai true atau benar
        assert layoutInflater != null;

        //menghubungkan inflater ke layout xml item_reminder.xml
        convertView = layoutInflater.inflate(R.layout.item_reminder, null);

        //menghubungkan variabel ke layout yang ada di xml
        TextView kapanMinum = convertView.findViewById(R.id.item_titleWaktu); // kapanMinum dengan item_titlewaktu
        TextView namaObat = convertView.findViewById(R.id.item_namaObat);// namaObat dengan item_namaobat
        TextView dateTv = convertView.findViewById(R.id.time_item); // dateTv dengan time item
        TextView timeTv = convertView.findViewById(R.id.item_taggal); //timeTv dengan item tanggal
        Button btn = convertView.findViewById(R.id.deleteBtn); //btn dengan Button deleteBtn

        //set btn untuk posisi dari data
        btn.setTag(position);

        //Menghapus pengingat dari database saat icon hapus di klik
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            //saat ada action clik dari user maka code di dalam akan dijalankan
            public void onClick(View v) {
                //menerima posisi data
                final int pos = (int) v.getTag();
                //posisi data yang sudah diketahui kemudia dikirimkan untuk di gapus
                deleteItem(pos);
            }
        });

        //inisasi alaram menurut posisi data dialam array
        Alarm alarm = arrayList.get(position);

        //menampilkan data kedalam layout
        kapanMinum.setText(alarm.getTitle());
        namaObat.setText(alarm.getObat());
        dateTv.setText(alarm.getDate());
        timeTv.setText(alarm.getTime());

        //mengembalikan nilai dari convert view
        return convertView;
    }

    //Menghapus pengingat dari listview
    private void deleteItem(int position) {
        deleteItemFromDb(arrayList.get(position).getId());
        arrayList.remove(position);
        notifyDataSetChanged();
    }

    //Menghapus pengingat dari database
    private void deleteItemFromDb(int id) {
        //method dibuat untuk memudahkan dalam mengakses database
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        //coba kalankan blok data
        try {
            //hapus data menurut id
            databaseHelper.deleteData(id);
            //pesan yang akan muncul
            toastMsg("Tugas di hapus");
        }
        //jika terdapat eror atau kegagalan tangkap pesan
        catch (Exception e) {
            e.printStackTrace();
            //pesan yang akan muncul
            toastMsg("Oppss.. ada kesalahan saat menghapus");
        }
    }

    //Metode pesan toast
    private void toastMsg(String msg) {
        //buat pesan toast dalam rentang waktu tampil length short
        Toast t = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        //munculkan pesan
        t.show();
    }
}

