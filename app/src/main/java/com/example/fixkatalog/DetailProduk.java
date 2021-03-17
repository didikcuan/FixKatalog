package com.example.fixkatalog;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class DetailProduk extends AppCompatActivity {

    ImageView fotoPesan,fotoDetailProduk;

    TextView namaDetailProduk, namaUserInfoProduk, hargaDetailProduk,stokDetailProduk, detailUkuranProduk, deskripsiDetailProduk,totalUser2 ;

    RatingBar ratingBar2;

    DatabaseReference Dataref,ref,refStokHabis, refKode, refTampilJumlahPesanStokHabis;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_produk);

        fotoPesan=findViewById(R.id.fotoPesan);
        fotoDetailProduk=findViewById(R.id.fotoDetailProduk);

        totalUser2=findViewById(R.id.totalUser2);
        ratingBar2=findViewById(R.id.ratingBar2);

        namaUserInfoProduk=findViewById(R.id.namaUserInfoProduk);
        hargaDetailProduk=findViewById(R.id.hargaDetailProduk);
        stokDetailProduk=findViewById(R.id.stokDetailProduk);
        detailUkuranProduk=findViewById(R.id.detailUkuranProduk);
        deskripsiDetailProduk=findViewById(R.id.deskripsiDetailProduk);
        namaDetailProduk=findViewById(R.id.namaDetailProduk);

        //////////////////////
        ImageView loginDetail = findViewById(R.id.loginDetail);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            loginDetail.setVisibility(View.GONE);
        } else {
            // No user is signed in
            loginDetail.setVisibility(View.VISIBLE);

        }

        loginDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),LoginMenu.class));
            }
        });
        /////////////


        String BarangMasukKey=getIntent().getStringExtra("kodebarangmasuk");

        Dataref= FirebaseDatabase.getInstance().getReference().child("tampil");

        ref=FirebaseDatabase.getInstance().getReference().child("tampil").child(BarangMasukKey);

        refStokHabis=FirebaseDatabase.getInstance().getReference().child("barangdipesanstokhabis");

        refKode=FirebaseDatabase.getInstance().getReference().child("kodestokhabis");

        refTampilJumlahPesanStokHabis=FirebaseDatabase.getInstance().getReference().child("tampiljumlahpesanstokhabis");

        if(getIntent().getExtras()!=null){

            Bundle bundle = getIntent().getExtras();
            namaUserInfoProduk.setText(bundle.getString("lnama"));

        }else{

            namaUserInfoProduk.setText("Nama Tidak Tersedia");

        }


        Dataref.child(BarangMasukKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    String kodebarang=dataSnapshot.child("kodebarang").getValue().toString();
                    String namabarang=dataSnapshot.child("namabarang").getValue().toString();
                    String kodesupplier=dataSnapshot.child("kodesupplier").getValue().toString();
                    String namasupplier=dataSnapshot.child("namasupplier").getValue().toString();
                    String kodebarangmasuk=dataSnapshot.child("kodebarangmasuk").getValue().toString();
                    String jumlahbarang=dataSnapshot.child("jumlahbarang").getValue().toString();
                    String ImageUrl=dataSnapshot.child("ImageUrl").getValue().toString();
                    String hargabarang = dataSnapshot.child("hargabarang").getValue().toString();
                    String deskripsi = dataSnapshot.child("deskripsi").getValue().toString();
                    String jenisbarang = dataSnapshot.child("jenisbarang").getValue().toString();

                    Picasso.get().load(ImageUrl).into(fotoDetailProduk);

                    ///
                    String setTextView ;
                    String replace = hargabarang.replaceAll("[Rp. ]","");
                    if (!replace.isEmpty())
                    {
                        setTextView = formatRupiah(Double.parseDouble(replace));


                    }else
                    {

                        setTextView = "No data";
                    }

                    ///

                        hargaDetailProduk.setText(setTextView);

                        stokDetailProduk.setText(jumlahbarang);

                        namaDetailProduk.setText(namabarang);

                        detailUkuranProduk.setText(kodebarangmasuk);

                        deskripsiDetailProduk.setText(deskripsi);

                    DatabaseReference Reftotaldatarating = FirebaseDatabase.getInstance().getReference().child("totaldatarating");
                    Reftotaldatarating.child(kodebarang+kodebarangmasuk).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                            if (dataSnapshot1.exists())
                            {
                                ratingBar2.setRating(Float.valueOf(dataSnapshot1.child("ratingbar").getValue().toString()));
                                totalUser2.setText("Dari "+ dataSnapshot1.child("totaluser").getValue().toString()+" User");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    fotoPesan.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            if (user != null) {
                                // User is signed in
                                if (Integer.valueOf(jumlahbarang)  == 0 ){
                                    Intent intent=new Intent(DetailProduk.this,MainActivity.class);
                                    String nama = namaUserInfoProduk.getText().toString();
                                    intent.putExtra("lnama", nama);
                                    Toast.makeText(DetailProduk.this, " Maaf stok barang habis ", Toast.LENGTH_SHORT).show();
                                    startActivity(intent);

                                    Date tanggal = new Date();
                                    SimpleDateFormat format = new SimpleDateFormat("dd MM yyyy");
                                    String finalTanggal = format.format(tanggal);
                                    SimpleDateFormat format1 = new SimpleDateFormat("MM yyyy");
                                    String finalTanggal1 = format1.format(tanggal);
                                    //
                                    refKode.child("kodestokhabis").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {


                                            Integer kodestokhabis=Integer.valueOf(dataSnapshot1.child("kodestokhabis").getValue().toString());
                                            String hasil = String.valueOf(kodestokhabis+1);
                                            dataSnapshot1.getRef().child("kodestokhabis").setValue(hasil);


                                            FirebaseAuth fAuth = FirebaseAuth.getInstance();

                                            DatabaseReference user= FirebaseDatabase.getInstance().getReference().child("user");
                                            user.child(fAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {

                                                    String nama = dataSnapshot2.child("nama").getValue().toString();
                                                    String alamat = dataSnapshot2.child("alamat").getValue().toString();
                                                    String telepon = dataSnapshot2.child("telepon").getValue().toString();
                                                    String uid = dataSnapshot2.child("uid").getValue().toString();
                                                    ///

                                                    refStokHabis.child("bdsh"+hasil).addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot3) {


                                                            dataSnapshot3.getRef().child("kodebarang").setValue(kodebarang);
                                                            dataSnapshot3.getRef().child("namabarang").setValue(namabarang);
                                                            dataSnapshot3.getRef().child("uid").setValue(uid);
                                                            dataSnapshot3.getRef().child("namapembeli").setValue(nama);
                                                            dataSnapshot3.getRef().child("kodebarangdipesanstokhabis").setValue(hasil);
                                                            dataSnapshot3.getRef().child("tanggalpesan").setValue(finalTanggal);
                                                            dataSnapshot3.getRef().child("bulantahun").setValue(finalTanggal1);
                                                            dataSnapshot3.getRef().child("ukuranbarang").setValue(kodebarangmasuk);
                                                            dataSnapshot3.getRef().child("ImageUrl").setValue(ImageUrl);
                                                            dataSnapshot3.getRef().child("hargabarang").setValue(hargabarang);
                                                            dataSnapshot3.getRef().child("deskripsi").setValue(deskripsi);
                                                            dataSnapshot3.getRef().child("jenisbarang").setValue(jenisbarang);

                                                            refTampilJumlahPesanStokHabis.child("1"+ kodebarang+kodebarangmasuk).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                    if (snapshot.exists())
                                                                    {
                                                                        Integer jumlahpesan = Integer.valueOf(snapshot.child("jumlahpesan").getValue().toString());
                                                                        String jumlahPesan = String.valueOf(jumlahpesan + 1) ;
                                                                        snapshot.getRef().child("jumlahpesan").setValue(jumlahPesan);

                                                                        snapshot.getRef().child("ImageUrl").setValue(ImageUrl);
                                                                        snapshot.getRef().child("deskripsi").setValue(deskripsi);
                                                                        snapshot.getRef().child("hargabarang").setValue(hargabarang);
                                                                        snapshot.getRef().child("jenisbarang").setValue(jenisbarang);
                                                                        snapshot.getRef().child("kodebarang").setValue(kodebarang);
                                                                        snapshot.getRef().child("ukuranbarang").setValue(kodebarangmasuk);
                                                                        snapshot.getRef().child("namabarang").setValue(namabarang);

                                                                    }
                                                                    else
                                                                    {
                                                                        snapshot.getRef().child("jumlahpesan").setValue("1");

                                                                        snapshot.getRef().child("ImageUrl").setValue(ImageUrl);
                                                                        snapshot.getRef().child("deskripsi").setValue(deskripsi);
                                                                        snapshot.getRef().child("hargabarang").setValue(hargabarang);
                                                                        snapshot.getRef().child("jenisbarang").setValue(jenisbarang);
                                                                        snapshot.getRef().child("kodebarang").setValue(kodebarang);
                                                                        snapshot.getRef().child("ukuranbarang").setValue(kodebarangmasuk);
                                                                        snapshot.getRef().child("namabarang").setValue(namabarang);
                                                                    }
                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                }
                                                            });
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });
                                                    ///

                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });




                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });


                                    //

                                }else {

                                    Intent intent = new Intent(DetailProduk.this, KonfirmasiPembayaran.class);
                                    String nama = namaUserInfoProduk.getText().toString();
                                    intent.putExtra("lnama", nama);
                                    intent.putExtra("tampilkey", kodebarang+kodebarangmasuk);
                                    startActivity(intent);
                                }
                            } else {
                                // No user is signed in
                                Toast.makeText(DetailProduk.this, " Harap Login Dulu ", Toast.LENGTH_SHORT).show();

                            }


                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
    private  String formatRupiah(Double number){
        Locale localeID = new Locale("IND","ID");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(localeID);
        String formatrupiah = numberFormat.format(number);
        String[] split = formatrupiah.split(",");
        int length = split[0].length();
        return split[0].substring(0,2)+". "+split[0].substring(2,length);
    }

    public void kembali(View view){

        Intent intent=new Intent(DetailProduk.this,MainActivity.class);
        String nama = namaUserInfoProduk.getText().toString();
        intent.putExtra("lnama", nama);
        startActivity(intent);

    }

}
