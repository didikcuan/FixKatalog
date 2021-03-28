package com.example.fixkatalog;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class KonfirmasiPembelianHolder extends RecyclerView.ViewHolder {
    ImageView fotoPembelianBarang;
    TextView textNo,textStatus,textPembeli,textJumlahHarga ;
    View v;

    public KonfirmasiPembelianHolder(@NonNull View itemView) {
        super(itemView);
        textStatus=itemView.findViewById(R.id.textStatus);
        textJumlahHarga=itemView.findViewById(R.id.textJumlahHarga);
        textPembeli=itemView.findViewById(R.id.textPembeli);
        fotoPembelianBarang=itemView.findViewById(R.id.fotoPembelianBarang);
        textNo=itemView.findViewById(R.id.textNo);
        v=itemView;
    }
}
