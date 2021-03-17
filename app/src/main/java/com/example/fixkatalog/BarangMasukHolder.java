package com.example.fixkatalog;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BarangMasukHolder extends RecyclerView.ViewHolder {
    ImageView fotoDataBarangMasukList;
    TextView textNamaBarangMasukList,textKodeBarangMasukList,textHargaBarangMasukList,textJumlahBarangList,edit ;
    View v;

    public BarangMasukHolder(@NonNull View itemView) {
        super(itemView);
        textJumlahBarangList=itemView.findViewById(R.id.textJumlahBarangList);
        textHargaBarangMasukList=itemView.findViewById(R.id.textHargaBarangMasukList);
        textKodeBarangMasukList=itemView.findViewById(R.id.textKodeBarangMasukList);
        textNamaBarangMasukList=itemView.findViewById(R.id.textNamaBarangMasukList);
        fotoDataBarangMasukList=itemView.findViewById(R.id.fotoDataBarangMasukList);
        edit=itemView.findViewById(R.id.edit);
        v=itemView;
    }
}
