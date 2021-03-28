package com.example.fixkatalog;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class KeranjangHolder extends RecyclerView.ViewHolder {
    ImageView kDelete;
    TextView kNama,kHarga,kUkuran,kJumlah ;
    View v;

    public KeranjangHolder(@NonNull View itemView) {
        super(itemView);
        kDelete=itemView.findViewById(R.id.kDelete);
        kNama=itemView.findViewById(R.id.kNama);
        kHarga=itemView.findViewById(R.id.kHarga);
        kUkuran=itemView.findViewById(R.id.kUkuran);
        kJumlah=itemView.findViewById(R.id.kJumlah);

        v=itemView;
    }
}
