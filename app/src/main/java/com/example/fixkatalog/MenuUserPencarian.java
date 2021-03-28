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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.Locale;

import javax.annotation.Nullable;

public class MenuUserPencarian extends AppCompatActivity {
    TextView namaUser1;

    EditText searchNamaProduk;
    RecyclerView recyclerViewPencarian;
    FirebaseRecyclerOptions<TampilClass> options;
    FirebaseRecyclerAdapter<TampilClass,TampilHolder> adapter;
    DatabaseReference Dataref;

    ProgressBar barPencarian;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_user_pencarian);
        onRestart();
        onStart();
        Dataref= FirebaseDatabase.getInstance().getReference().child("tampil");
        searchNamaProduk=findViewById(R.id.searchNamaProduk);
        recyclerViewPencarian=findViewById(R.id.recyclerViewPencarian);

        namaUser1=findViewById(R.id.namaUser1);

        barPencarian=findViewById(R.id.barPencarian);

        barPencarian.setVisibility(View.GONE);

        recyclerViewPencarian.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerViewPencarian.setHasFixedSize(true);


        if(getIntent().getExtras()!=null){

            Bundle bundle = getIntent().getExtras();
            namaUser1.setText(bundle.getString("lnama"));

        }else{

            namaUser1.setText("Nama Tidak Tersedia");

        }

        LoadData("");


        //////////////////////
        ImageView loginPencarian = findViewById(R.id.loginPencarian);
        ImageView profilPencarian = findViewById(R.id.profilPencarian);
        ImageView kembaliPencarian = findViewById(R.id.kembaliPencarian);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            profilPencarian.setVisibility(View.VISIBLE);
            loginPencarian.setVisibility(View.GONE);
        } else {
            // No user is signed in
            profilPencarian.setVisibility(View.GONE);
            loginPencarian.setVisibility(View.VISIBLE);

        }

        kembaliPencarian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MenuUserPencarian.this,MainActivity.class);
                String nama = namaUser1.getText().toString();
                intent.putExtra("lnama", nama);
                startActivity(intent);
            }
        });

        profilPencarian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MenuUserPencarian.this,UbahDataProfilUser.class);
                String nama = namaUser1.getText().toString();
                intent.putExtra("lnama", nama);
                startActivity(intent);
            }
        });

        loginPencarian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),LoginMenu.class));
            }
        });
        /////////////

        searchNamaProduk.addTextChangedListener(new TextWatcher() {
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
        Query query=Dataref.orderByChild("namabarang").startAt(data).endAt(data+"\uf8ff");

        options=new FirebaseRecyclerOptions.Builder<TampilClass>().setQuery(query,TampilClass.class).build();
        adapter=new FirebaseRecyclerAdapter<TampilClass, TampilHolder> (options) {
            @Override
            protected void onBindViewHolder(@NonNull TampilHolder holder, final int position, @NonNull TampilClass model) {
                DatabaseReference Reftotaldatarating = FirebaseDatabase.getInstance().getReference().child("totaldatarating");
                Reftotaldatarating.child(model.getKodebarang()+model.getKodebarangmasuk()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists())
                        {
                            holder.ratingBar.setRating(Float.valueOf(dataSnapshot.child("ratingbar").getValue().toString()));
                            holder.totalUser.setText("Dari "+ dataSnapshot.child("totaluser").getValue().toString()+" User");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                holder.tampilBarang.setText(model.getNamabarang());
                holder.tampilUkuran.setText(model.getKodebarangmasuk());
                ///
                String setTextView ;
                String replace = model.getHargabarang().replaceAll("[Rp. ]","");
                if (!replace.isEmpty())
                {
                    setTextView = formatRupiah(Double.parseDouble(replace));


                }else
                {

                    setTextView = "No data";
                }

                ///
                holder.tampilHarga.setText(setTextView);
                holder.tampilJumlah.setText(model.getJumlahbarang());
                Picasso.get().load(model.getImageUrl()).into(holder.tampilFoto);

                holder.v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(MenuUserPencarian.this,DetailProduk.class);
                        intent.putExtra("kodebarangmasuk",getRef(position).getKey());
                        String nama = namaUser1.getText().toString();
                        intent.putExtra("lnama", nama);
                        startActivity(intent);



                    }
                });

            }

            @NonNull
            @Override
            public TampilHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.tampil_list,parent,false);
                return new TampilHolder(v);
            }
        };
        adapter.startListening();
        recyclerViewPencarian.setAdapter(adapter);
        barPencarian.setVisibility(View.INVISIBLE);
    }



    public void ubahdataprofiluser(View view){
        Intent intent=new Intent(MenuUserPencarian.this,UbahDataProfilUser.class);
        String nama = namaUser1.getText().toString();
        intent.putExtra("lnama", nama);
        startActivity(intent);
    }

    public void sepatu(View view){
        Intent intent=new Intent(MenuUserPencarian.this,MenuSepatu.class);
        String nama = namaUser1.getText().toString();
        intent.putExtra("lnama", nama);
        startActivity(intent);
    }
    public void baju(View view){
        Intent intent=new Intent(MenuUserPencarian.this,MenuBaju.class);
        String nama = namaUser1.getText().toString();
        intent.putExtra("lnama", nama);
        startActivity(intent);
    }
    public void celana(View view){
        Intent intent=new Intent(MenuUserPencarian.this,MenuCelana.class);
        String nama = namaUser1.getText().toString();
        intent.putExtra("lnama", nama);
        startActivity(intent);
    }
    public void sendal(View view){
        Intent intent=new Intent(MenuUserPencarian.this,MenuSendal.class);
        String nama = namaUser1.getText().toString();
        intent.putExtra("lnama", nama);
        startActivity(intent);
    }
    public void tas(View view){
        Intent intent=new Intent(MenuUserPencarian.this,MenuTas.class);
        String nama = namaUser1.getText().toString();
        intent.putExtra("lnama", nama);
        startActivity(intent);
    }
    public void chat(View view){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            Intent intent=new Intent(MenuUserPencarian.this,ChatUser.class);
            String nama = namaUser1.getText().toString();
            intent.putExtra("lnama", nama);
            startActivity(intent);
        } else {
            // No user is signed in
            Toast.makeText(MenuUserPencarian.this, " Harap Login Dulu ", Toast.LENGTH_SHORT).show();

        }

    }
    private  String formatRupiah(Double number){
        Locale localeID = new Locale("IND","ID");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(localeID);
        String formatrupiah = numberFormat.format(number);
        String[] split = formatrupiah.split(",");
        int length = split[0].length();
        return split[0].substring(0,2)+". "+split[0].substring(2,length);
    }
    public void keranjang(View view){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {

            Intent intent = new Intent(MenuUserPencarian.this, Keranjang.class);
            String nama = namaUser1.getText().toString();
            intent.putExtra("lnama", nama);
            startActivity(intent);

        }else {
            Toast.makeText(MenuUserPencarian.this, " Harap Login Dulu ", Toast.LENGTH_SHORT).show();
        }
    }
}