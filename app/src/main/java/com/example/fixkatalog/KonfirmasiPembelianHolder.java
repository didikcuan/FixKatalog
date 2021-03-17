package com.example.fixkatalog;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class KonfirmasiPembelianHolder extends RecyclerView.ViewHolder {
    ImageView fotoPembelianBarang;
    TextView textNamaProduk,textQTY,textPembeli ;
    View v;

    public KonfirmasiPembelianHolder(@NonNull View itemView) {
        super(itemView);
        textNamaProduk=itemView.findViewById(R.id.textNamaProduk);
        textQTY=itemView.findViewById(R.id.textQTY);
        textPembeli=itemView.findViewById(R.id.textPembeli);
        fotoPembelianBarang=itemView.findViewById(R.id.fotoPembelianBarang);
        v=itemView;
    }
}
