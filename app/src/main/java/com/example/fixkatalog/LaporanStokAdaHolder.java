package com.example.fixkatalog;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class LaporanStokAdaHolder extends RecyclerView.ViewHolder {

    TextView kodeStokAda,namaSA,hargaJualSA,hargaPokokSA,qtySA,totalHP ;
    View v;

    public LaporanStokAdaHolder(@NonNull View itemView) {
        super(itemView);
        kodeStokAda=itemView.findViewById(R.id.kodeStokAda);
        namaSA=itemView.findViewById(R.id.namaSA);
        hargaJualSA=itemView.findViewById(R.id.hargaJualSA);
        hargaPokokSA=itemView.findViewById(R.id.hargaPokokSA);
        qtySA=itemView.findViewById(R.id.qtySA);
        totalHP=itemView.findViewById(R.id.totalHP);
        v=itemView;
    }
}
