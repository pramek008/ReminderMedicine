package com.example.reminderapp2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ItemAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ModelData> arrayList;

    public ItemAdapter(Context context, ArrayList<ModelData> arrayList) {
        super();
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return this.arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert layoutInflater != null;
        convertView = layoutInflater.inflate(R.layout.item_reminder, null);
        TextView kapanMinum = convertView.findViewById(R.id.item_titleWaktu);
        TextView namaObat = convertView.findViewById(R.id.item_namaObat);
        TextView dateTv = convertView.findViewById(R.id.time_item);
        TextView timeTv = convertView.findViewById(R.id.item_taggal);
        Button btn = convertView.findViewById(R.id.deleteBtn);
        btn.setTag(position);

        //Menghapus tugas dari database saat icon hapus di klik
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int pos = (int) v.getTag();
                deleteItem(pos);
            }
        });
        String x;
        ModelData modelData = arrayList.get(position);
        kapanMinum.setText(modelData.getTitle());
        namaObat.setText(modelData.getObat());
        dateTv.setText(modelData.getDate());
        timeTv.setText(modelData.getTime());
        return convertView;
    }

    //Menghapus tugas dari listview
    private void deleteItem(int position) {
        deleteItemFromDb(arrayList.get(position).getId());
        arrayList.remove(position);
        notifyDataSetChanged();
    }

    //Menghapus tugas dari database
    private void deleteItemFromDb(int id) {
        com.example.reminderapp2.DatabaseHelper databaseHelper = new com.example.reminderapp2.DatabaseHelper(context);
        try {
            databaseHelper.deleteData(id);
            toastMsg("Tugas di hapus");
        } catch (Exception e) {
            e.printStackTrace();
            toastMsg("Oppss.. ada kesalahan saat menghapus");
        }
    }

    //Metode pesan toast
    private void toastMsg(String msg) {
        Toast t = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        t.setGravity(Gravity.CENTER,0,0);
        t.show();
    }
}

