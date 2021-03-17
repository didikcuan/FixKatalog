package com.example.fixkatalog;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class KonfirmasiPembayaranHolder extends RecyclerView.ViewHolder {
    ImageView fotoKonfirmasiPembayaran;
    TextView textNamaBarangPembayaran,textTotalBarang,textNamaPembeli ;
    View v;

    public KonfirmasiPembayaranHolder(@NonNull View itemView) {
        super(itemView);
        textNamaBarangPembayaran=itemView.findViewById(R.id.textNamaBarangPembayaran);
        textTotalBarang=itemView.findViewById(R.id.textTotalBarang);
        textNamaPembeli=itemView.findViewById(R.id.textNamaPembeli);
        fotoKonfirmasiPembayaran=itemView.findViewById(R.id.fotoKonfirmasiPembayaran);
        v=itemView;
    }
}
