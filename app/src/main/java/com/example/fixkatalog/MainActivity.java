package com.example.fixkatalog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.Image;
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

public class MainActivity extends AppCompatActivity {
    TextView namaUser;

    ImageView searchNamaProduk;
    RecyclerView recyclerViewHotPromo;
    FirebaseRecyclerOptions<TampilClass> options;
    FirebaseRecyclerAdapter<TampilClass,TampilHolder> adapter;
    DatabaseReference Dataref;

    ProgressBar Satu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        onRestart();
        onStart();
        Dataref= FirebaseDatabase.getInstance().getReference().child("tampil");
        searchNamaProduk=findViewById(R.id.searchNamaProduk);
        recyclerViewHotPromo=findViewById(R.id.recyclerViewHome);

        namaUser=findViewById(R.id.namaUser);

        Satu=findViewById(R.id.Satu);

        Satu.setVisibility(View.GONE);

        recyclerViewHotPromo.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerViewHotPromo.setHasFixedSize(true);

        ImageView loginUser = findViewById(R.id.loginUser);
        ImageView profilUser = findViewById(R.id.profilUser);
        ImageView logOut = findViewById(R.id.logOut);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            logOut.setVisibility(View.VISIBLE);
            profilUser.setVisibility(View.VISIBLE);
            loginUser.setVisibility(View.GONE);
            FirebaseAuth fAuth = FirebaseAuth.getInstance();

            DatabaseReference usercek= FirebaseDatabase.getInstance().getReference().child("user");
            usercek.child(fAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists())
                    {
                        String nama = dataSnapshot.child("nama").getValue().toString();
                        namaUser.setText(nama);
                        if (dataSnapshot.child("hak").getValue() != null)
                        {



                            Intent intent = new Intent(MainActivity.this, MenuAdmin.class);
                            intent.putExtra("lnama", nama);
                            startActivity(intent);

                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            // No user is signed in
            logOut.setVisibility(View.GONE);
            profilUser.setVisibility(View.GONE);
            loginUser.setVisibility(View.VISIBLE);

        }

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity((new Intent(getApplicationContext(),MainActivity.class)));
                Toast.makeText(MainActivity.this, " Berhasil Log Out " , Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        profilUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,UbahDataProfilUser.class);
                String nama = namaUser.getText().toString();
                intent.putExtra("lnama", nama);
                startActivity(intent);
            }
        });

        loginUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),LoginMenu.class));
            }
        });


        if(getIntent().getExtras()!=null){

            Bundle bundle = getIntent().getExtras();
            namaUser.setText(bundle.getString("lnama"));

        }else{

            namaUser.setText("Nama Tidak Tersedia");

        }

        LoadData();


    }

    private void LoadData() {
        Query query=Dataref.orderByChild("hotpromo").startAt("Yes");

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
                        Intent intent=new Intent(MainActivity.this,DetailProduk.class);
                        intent.putExtra("kodebarangmasuk",getRef(position).getKey());
                        String nama = namaUser.getText().toString();
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
        recyclerViewHotPromo.setAdapter(adapter);
        Satu.setVisibility(View.INVISIBLE);
    }

    public void cari(View view){
        Intent intent=new Intent(MainActivity.this,MenuUserPencarian.class);
        String nama = namaUser.getText().toString();
        intent.putExtra("lnama", nama);
        startActivity(intent);
    }
    public void baju(View view){
        Intent intent=new Intent(MainActivity.this,MenuBaju.class);
        String nama = namaUser.getText().toString();
        intent.putExtra("lnama", nama);
        startActivity(intent);
    }

    public void sepatu(View view){
        Intent intent=new Intent(MainActivity.this,MenuSepatu.class);
        String nama = namaUser.getText().toString();
        intent.putExtra("lnama", nama);
        startActivity(intent);
    }

    public void celana(View view){
        Intent intent=new Intent(MainActivity.this,MenuCelana.class);
        String nama = namaUser.getText().toString();
        intent.putExtra("lnama", nama);
        startActivity(intent);
    }
    public void sendal(View view){
        Intent intent=new Intent(MainActivity.this,MenuSendal.class);
        String nama = namaUser.getText().toString();
        intent.putExtra("lnama", nama);
        startActivity(intent);
    }
    public void tas(View view){
        Intent intent=new Intent(MainActivity.this,MenuTas.class);
        String nama = namaUser.getText().toString();
        intent.putExtra("lnama", nama);
        startActivity(intent);
    }

    public void chat(View view){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            Intent intent=new Intent(MainActivity.this,ChatUser.class);
            String nama = namaUser.getText().toString();
            intent.putExtra("lnama", nama);
            startActivity(intent);
        } else {
            // No user is signed in
            Toast.makeText(MainActivity.this, " Harap Login Dulu ", Toast.LENGTH_SHORT).show();

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

            Intent intent = new Intent(MainActivity.this, Keranjang.class);
            String nama = namaUser.getText().toString();
            intent.putExtra("lnama", nama);
            startActivity(intent);

        }else {
            Toast.makeText(MainActivity.this, " Harap Login Dulu ", Toast.LENGTH_SHORT).show();
        }
    }
}