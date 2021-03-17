package com.example.fixkatalog;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TampilHolder extends RecyclerView.ViewHolder {
    ImageView tampilFoto;
    TextView tampilBarang,tampilUkuran,tampilHarga,tampilJumlah,totalUser ;
    RatingBar ratingBar;
    View v;

    public TampilHolder(@NonNull View itemView) {
        super(itemView);
        tampilFoto=itemView.findViewById(R.id.tampilFoto);
        tampilBarang=itemView.findViewById(R.id.tampilBarang);
        tampilUkuran=itemView.findViewById(R.id.tampilUkuran);
        tampilHarga=itemView.findViewById(R.id.tampilHarga);
        tampilJumlah=itemView.findViewById(R.id.tampilJumlah);
        ratingBar=itemView.findViewById(R.id.ratingBar);
        totalUser=itemView.findViewById(R.id.totalUser);
        v=itemView;
    }
}
