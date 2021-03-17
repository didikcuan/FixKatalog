package com.example.fixkatalog;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class LapBarangDipesanHabisHolder extends RecyclerView.ViewHolder {
    TextView kodeBPH,namaBPH,tanggalBPH,hargaJualBPH ;
    View v;

    public LapBarangDipesanHabisHolder(@NonNull View itemView) {
        super(itemView);
        kodeBPH=itemView.findViewById(R.id.kodeBPH);
        namaBPH=itemView.findViewById(R.id.namaBPH);
        tanggalBPH=itemView.findViewById(R.id.tanggalBPH);
        hargaJualBPH=itemView.findViewById(R.id.hargaJualBPH);

        v=itemView;
    }
}
