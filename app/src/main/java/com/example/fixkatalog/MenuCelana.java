package com.example.fixkatalog;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class MenuCelana extends AppCompatActivity {
    TextView namaUserCelana;


    RecyclerView recyclerViewCelana;
    FirebaseRecyclerOptions<TampilClass> options;
    FirebaseRecyclerAdapter<TampilClass,TampilHolder> adapter;
    DatabaseReference Dataref;

    ProgressBar barCelana;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_celana);
        onRestart();
        onStart();
        Dataref= FirebaseDatabase.getInstance().getReference().child("tampil");

        recyclerViewCelana=findViewById(R.id.recyclerViewCelana);

        namaUserCelana=findViewById(R.id.namaUserCelana);

        barCelana=findViewById(R.id.barCelana);

        barCelana.setVisibility(View.GONE);

        recyclerViewCelana.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerViewCelana.setHasFixedSize(true);

        //////////////////////
        ImageView loginCelana = findViewById(R.id.loginCelana);
        ImageView profilCelana= findViewById(R.id.profilCelana);
        ImageView kembaliCelana = findViewById(R.id.kembaliCelana);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            profilCelana.setVisibility(View.VISIBLE);
            loginCelana.setVisibility(View.GONE);
        } else {
            // No user is signed in
            profilCelana.setVisibility(View.GONE);
            loginCelana.setVisibility(View.VISIBLE);

        }

        kembaliCelana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MenuCelana.this,MainActivity.class);
                String nama = namaUserCelana.getText().toString();
                intent.putExtra("lnama", nama);
                startActivity(intent);
            }
        });

        profilCelana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MenuCelana.this,UbahDataProfilUser.class);
                String nama = namaUserCelana.getText().toString();
                intent.putExtra("lnama", nama);
                startActivity(intent);
            }
        });

        loginCelana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),LoginMenu.class));
            }
        });
        /////////////

        if(getIntent().getExtras()!=null){

            Bundle bundle = getIntent().getExtras();
            namaUserCelana.setText(bundle.getString("lnama"));

        }else{

            namaUserCelana.setText("Nama Tidak Tersedia");

        }

        LoadData();


    }

    private void LoadData() {
        Query query=Dataref.orderByChild("jenisbarang").startAt("celana").endAt("celana");

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
                        Intent intent=new Intent(MenuCelana.this,DetailProduk.class);
                        intent.putExtra("kodebarangmasuk",getRef(position).getKey());
                        String nama = namaUserCelana.getText().toString();
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
        recyclerViewCelana.setAdapter(adapter);
        barCelana.setVisibility(View.INVISIBLE);
    }




    public void kembali(View view){
        Intent intent=new Intent(MenuCelana.this,MainActivity.class);
        String nama = namaUserCelana.getText().toString();
        intent.putExtra("lnama", nama);
        startActivity(intent);
    }

    public void ubahdataprofiluser(View view){
        Intent intent=new Intent(MenuCelana.this,UbahDataProfilUser.class);
        String nama = namaUserCelana.getText().toString();
        intent.putExtra("lnama", nama);
        startActivity(intent);
    }

    public void cari(View view){
        Intent intent=new Intent(MenuCelana.this,MenuUserPencarian.class);
        String nama = namaUserCelana.getText().toString();
        intent.putExtra("lnama", nama);
        startActivity(intent);
    }
    public void baju(View view){
        Intent intent=new Intent(MenuCelana.this,MenuBaju.class);
        String nama = namaUserCelana.getText().toString();
        intent.putExtra("lnama", nama);
        startActivity(intent);
    }

    public void sepatu(View view){
        Intent intent=new Intent(MenuCelana.this,MenuSepatu.class);
        String nama = namaUserCelana.getText().toString();
        intent.putExtra("lnama", nama);
        startActivity(intent);
    }

    public void celana(View view){
        Intent intent=new Intent(MenuCelana.this,MenuCelana.class);
        String nama = namaUserCelana.getText().toString();
        intent.putExtra("lnama", nama);
        startActivity(intent);
    }
    public void sendal(View view){
        Intent intent=new Intent(MenuCelana.this,MenuSendal.class);
        String nama = namaUserCelana.getText().toString();
        intent.putExtra("lnama", nama);
        startActivity(intent);
    }
    public void tas(View view){
        Intent intent=new Intent(MenuCelana.this,MenuTas.class);
        String nama = namaUserCelana.getText().toString();
        intent.putExtra("lnama", nama);
        startActivity(intent);
    }
    public void chat(View view){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            Intent intent=new Intent(MenuCelana.this,ChatUser.class);
            String nama = namaUserCelana.getText().toString();
            intent.putExtra("lnama", nama);
            startActivity(intent);
            startActivity(intent);
        } else {
            // No user is signed in
            Toast.makeText(MenuCelana.this, " Harap Login Dulu ", Toast.LENGTH_SHORT).show();

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

            Intent intent = new Intent(MenuCelana.this, Keranjang.class);
            String nama = namaUserCelana.getText().toString();
            intent.putExtra("lnama", nama);
            startActivity(intent);

        }else {
            Toast.makeText(MenuCelana.this, " Harap Login Dulu ", Toast.LENGTH_SHORT).show();
        }
    }
}