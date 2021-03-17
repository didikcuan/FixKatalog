package com.example.fixkatalog;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DataBarangHolder extends RecyclerView.ViewHolder {
    ImageView fotoDataBarangList;
    TextView textNamaBarangList,textKodeBarangList,textHargaBarangList ;
    View v;

    public DataBarangHolder(@NonNull View itemView) {
        super(itemView);
        textHargaBarangList=itemView.findViewById(R.id.textHargaBarangList);
        textKodeBarangList=itemView.findViewById(R.id.textKodeBarangList);
        textNamaBarangList=itemView.findViewById(R.id.textNamaBarangList);
        fotoDataBarangList=itemView.findViewById(R.id.fotoDataBarangList);
        v=itemView;
    }
}
