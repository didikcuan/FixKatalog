package com.example.fixkatalog;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class LapPembelianDitolakHolder extends RecyclerView.ViewHolder {
    TextView kodePD,namaPD,tanggalPD,hargaJualPD,ketDitolak ;
    View v;

    public LapPembelianDitolakHolder(@NonNull View itemView) {
        super(itemView);
        kodePD=itemView.findViewById(R.id.kodePD);
        namaPD=itemView.findViewById(R.id.namaPD);
        tanggalPD=itemView.findViewById(R.id.tanggalPD);
        hargaJualPD=itemView.findViewById(R.id.hargaJualPD);
        ketDitolak=itemView.findViewById(R.id.ketDitolak);
        v=itemView;
    }
}
