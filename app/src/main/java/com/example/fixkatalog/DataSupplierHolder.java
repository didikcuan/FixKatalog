package com.example.fixkatalog;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DataSupplierHolder extends RecyclerView.ViewHolder {
    ImageView fotoDataSupplierList;
    TextView textNamaSupplierList,textKodeSupplierList,textTeleponSupplierList ;
    View v;

    public DataSupplierHolder(@NonNull View itemView) {
        super(itemView);
        textTeleponSupplierList=itemView.findViewById(R.id.textTeleponSupplierList);
        textKodeSupplierList=itemView.findViewById(R.id.textKodeSupplierList);
        textNamaSupplierList=itemView.findViewById(R.id.textNamaSupplierList);
        fotoDataSupplierList=itemView.findViewById(R.id.fotoDataSupplierList);
        
        v=itemView;
    }
}
