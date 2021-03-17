package com.example.fixkatalog;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class LaporanPenjualanHolder extends RecyclerView.ViewHolder {

    TextView kodeBK,namaBK,namaP,tanggalBK,hargaBK,qtyBK,totalBK ;
    View v;

    public LaporanPenjualanHolder(@NonNull View itemView) {
        super(itemView);
        kodeBK=itemView.findViewById(R.id.kodeBK);
        namaBK=itemView.findViewById(R.id.namaBK);
        namaP=itemView.findViewById(R.id.namaP);
        tanggalBK=itemView.findViewById(R.id.tanggalBK);
        hargaBK=itemView.findViewById(R.id.hargaBK);
        qtyBK=itemView.findViewById(R.id.qtyBK);
        totalBK=itemView.findViewById(R.id.totalBK);
        v=itemView;
    }
}
