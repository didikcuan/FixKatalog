package com.example.fixkatalog;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PengeluaranLainnyaHolder extends RecyclerView.ViewHolder {
    ImageView fotoAdmin;
    TextView textTotalBiaya,textTanggalPengeluaran,textNamaAdmin ;
    View v;

    public PengeluaranLainnyaHolder(@NonNull View itemView) {
        super(itemView);
        fotoAdmin=itemView.findViewById(R.id.fotoAdmin);
        textTotalBiaya=itemView.findViewById(R.id.textTotalBiaya);
        textTanggalPengeluaran=itemView.findViewById(R.id.textTanggalPengeluaran);
        textNamaAdmin=itemView.findViewById(R.id.textNamaAdmin);
        
        v=itemView;
    }
}
