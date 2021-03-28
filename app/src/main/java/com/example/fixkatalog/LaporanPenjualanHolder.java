package com.example.fixkatalog;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class LaporanPenjualanHolder extends RecyclerView.ViewHolder {

    TextView kodePenjualan,noNota,namaPembeli,tanggalPenjualan,jumlahBayarPenjualan;
    View v;

    public LaporanPenjualanHolder(@NonNull View itemView) {
        super(itemView);
        kodePenjualan=itemView.findViewById(R.id.kodePenjualan);
        noNota=itemView.findViewById(R.id.noNota);
        namaPembeli=itemView.findViewById(R.id.namaPembeli);
        tanggalPenjualan=itemView.findViewById(R.id.tanggalPenjualan);
        jumlahBayarPenjualan=itemView.findViewById(R.id.jumlahBayarPenjualan);

        v=itemView;
    }
}
