package com.example.fixkatalog;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class DataSupplier extends AppCompatActivity {

    ImageView fotoTambahSupplier;

    TextView lAdminDataSupplier;

    EditText searchSupplier;
    RecyclerView recyclerViewSupplier;
    FirebaseRecyclerOptions<DataSupplierClass> options;
    FirebaseRecyclerAdapter<DataSupplierClass,DataSupplierHolder> adapter;
    DatabaseReference Dataref;

    ProgressBar barDataSupplier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_supplier);
        onRestart();
        onStart();
        Dataref= FirebaseDatabase.getInstance().getReference().child("supplier");
        searchSupplier=findViewById(R.id.searchSupplier);
        recyclerViewSupplier=findViewById(R.id.recyclerViewSupplier);
        fotoTambahSupplier=findViewById(R.id.fotoTambahSupplier);
        lAdminDataSupplier=findViewById(R.id.lAdminDataSupplier);
        barDataSupplier=findViewById(R.id.barDataSupplier);

        barDataSupplier.setVisibility(View.VISIBLE);


        recyclerViewSupplier.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerViewSupplier.setHasFixedSize(true);
        fotoTambahSupplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DataSupplier.this,DataSupplierTambah.class);
                String nama = lAdminDataSupplier.getText().toString();
                intent.putExtra("lnama", nama);
                startActivity(intent);
            }
        });

        if(getIntent().getExtras()!=null){

            Bundle bundle = getIntent().getExtras();
            lAdminDataSupplier.setText(bundle.getString("lnama"));

        }else{

            lAdminDataSupplier.setText("Nama Tidak Tersedia");

        }

        LoadData("");

        searchSupplier.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString()!=null)
                {
                    LoadData(s.toString());
                }
                else
                {
                    LoadData("");
                }

            }
        });


    }

    private void LoadData(String data) {

        Query query=Dataref.orderByChild("kodesupplier").startAt(data).endAt(data+"\uf8ff");

        options=new FirebaseRecyclerOptions.Builder<DataSupplierClass>().setQuery(query,DataSupplierClass.class).build();
        adapter=new FirebaseRecyclerAdapter<DataSupplierClass, DataSupplierHolder> (options) {
            @Override
            protected void onBindViewHolder(@NonNull DataSupplierHolder holder, final int position, @NonNull DataSupplierClass model) {
                holder.textKodeSupplierList.setText(model.getKodesupplier());
                holder.textNamaSupplierList.setText(model.getNamasupplier());
                holder.textTeleponSupplierList.setText(model.getTeleponsupplier());
                Picasso.get().load(model.getImageUrl()).into(holder.fotoDataSupplierList);

                holder.v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(DataSupplier.this,DataSupplierEdit.class);
                        intent.putExtra("kodesupplier",getRef(position).getKey());
                        String nama = lAdminDataSupplier.getText().toString();
                        intent.putExtra("lnama", nama);
                        startActivity(intent);
                    }
                });

            }

            @NonNull
            @Override
            public DataSupplierHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.data_supplier_list,parent,false);
                return new DataSupplierHolder(v);
            }
        };
        adapter.startListening();
        recyclerViewSupplier.setAdapter(adapter);
        barDataSupplier.setVisibility(View.INVISIBLE);
    }

    public void kembali(View view){
        Intent intent=new Intent(DataSupplier.this,MenuAdmin.class);
        String nama = lAdminDataSupplier.getText().toString();
        intent.putExtra("lnama", nama);
        startActivity(intent);
    }

}
