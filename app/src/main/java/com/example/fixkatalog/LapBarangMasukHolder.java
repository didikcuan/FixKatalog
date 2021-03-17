package com.example.fixkatalog;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class LapBarangMasukHolder extends RecyclerView.ViewHolder {
    TextView kodeBM,namaBM,namaBM1,tanggalBM,hargaBM,qtyBM,totalBM ;
    View v;

    public LapBarangMasukHolder(@NonNull View itemView) {
        super(itemView);
        kodeBM=itemView.findViewById(R.id.kodeBM);
        namaBM=itemView.findViewById(R.id.namaBM);
        namaBM1=itemView.findViewById(R.id.namaBM1);
        tanggalBM=itemView.findViewById(R.id.tanggalBM);
        hargaBM=itemView.findViewById(R.id.hargaBM);
        qtyBM=itemView.findViewById(R.id.qtyBM);
        totalBM=itemView.findViewById(R.id.totalBM);
        v=itemView;
    }
}
