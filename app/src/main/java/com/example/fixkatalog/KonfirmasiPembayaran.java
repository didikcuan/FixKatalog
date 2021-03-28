package com.example.fixkatalog;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
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
import java.util.Locale;

public class KonfirmasiPembayaran extends AppCompatActivity {

    ImageView tambah,minus,fotoSimpanData;
    TextView jumlahBayar, namaBarang, ukuranBarang, totalBarang, jumlahdibayar, lUserKonfirmasiPembayaran;
    DatabaseReference Dataref, ref, user, refSimpanData;
    ProgressBar progresskonfirmasi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.konfirmasi_pembayaran);

        tambah=findViewById(R.id.tambah);
        minus=findViewById(R.id.minus);
        fotoSimpanData=findViewById(R.id.fotoSimpanData);

        jumlahBayar=findViewById(R.id.jumlahBayar);
        namaBarang=findViewById(R.id.namaBarang);
        ukuranBarang=findViewById(R.id.ukuranBarang);
        totalBarang=findViewById(R.id.totalBarang);
        jumlahdibayar=findViewById(R.id.jumlahdibayar);
        lUserKonfirmasiPembayaran=findViewById(R.id.lUserKonfirmasiPembayaran);

        progresskonfirmasi=findViewById(R.id.progresskonfirmasi);
        progresskonfirmasi.setVisibility(View.GONE);

        String BarangMasukKey=getIntent().getStringExtra("tampilkey");
        Dataref= FirebaseDatabase.getInstance().getReference().child("tampil");
        ref=FirebaseDatabase.getInstance().getReference().child("barangmasuk").child(BarangMasukKey);
        refSimpanData=FirebaseDatabase.getInstance().getReference().child("konfirmasipembayaran");

        ///
        FirebaseAuth fAuth = FirebaseAuth.getInstance();

        ///
        Dataref.child("1"+BarangMasukKey).addValueEventListener(new ValueEventListener() {
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

                    totalBarang.setText("1");


                    ImageView tambah,minus;
                    tambah = findViewById(R.id.tambah);
                    minus = findViewById(R.id.minus);

                    tambah.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Integer b = Integer.valueOf(totalBarang.getText().toString());
                            Integer e = Integer.valueOf(jumlahbarang);
                            if ( b == e ){
                                Toast.makeText(KonfirmasiPembayaran.this, " Tidak bisa menambah barang lagi, stok habis ", Toast.LENGTH_SHORT).show();
                            }else {

                                Integer a = Integer.valueOf("1");

                                Integer hasil = a + b;

                                String hasilQty = hasil.toString();

                                totalBarang.setText(hasilQty);

                                Integer c = Integer.valueOf(totalBarang.getText().toString());
                                Integer d = Integer.valueOf(hargabarang);
                                Integer hasilBayar = d * c;

                                String hasilJumlahBayar = hasilBayar.toString();

                                ///
                                String setTextView ;
                                String replace = hasilJumlahBayar.replaceAll("[Rp. ]","");
                                if (!replace.isEmpty())
                                {
                                    setTextView = formatRupiah(Double.parseDouble(replace));


                                }else
                                {

                                    setTextView = "No data";
                                }

                                ///
                                jumlahBayar.setText(setTextView);
                                jumlahdibayar.setText(hasilJumlahBayar);
                            }
                        }
                    });

                    minus.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Integer b = Integer.valueOf(totalBarang.getText().toString());
                            if (b == 1) {
                                Toast.makeText(KonfirmasiPembayaran.this, " Total barang tidak boleh 0 ", Toast.LENGTH_SHORT).show();
                            } else{

                            Integer a = Integer.valueOf("1");

                            Integer hasil = b - a;

                            String hasilQty = hasil.toString();

                            totalBarang.setText(hasilQty);

                            Integer c = Integer.valueOf(totalBarang.getText().toString());
                            Integer d = Integer.valueOf(hargabarang);
                            Integer hasilBayar = d * c;

                            String hasilJumlahBayar = hasilBayar.toString();
                                ///
                                String setTextView ;
                                String replace = hasilJumlahBayar.replaceAll("[Rp. ]","");
                                if (!replace.isEmpty())
                                {
                                    setTextView = formatRupiah(Double.parseDouble(replace));


                                }else
                                {

                                    setTextView = "No data";
                                }

                                ///
                                jumlahBayar.setText(setTextView);
                                jumlahdibayar.setText(hasilJumlahBayar);
                            }
                        }
                    });


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


                    jumlahdibayar.setText(hargabarang);
                    jumlahBayar.setText(setTextView);

                    namaBarang.setText(namabarang);

                    ukuranBarang.setText(kodebarangmasuk);



                    final  String key = Dataref.push().getKey();
                    Dataref.child("1"+BarangMasukKey).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            String ImageUrl=dataSnapshot.child("ImageUrl").getValue().toString();
                            String deskripsi = dataSnapshot.child("deskripsi").getValue().toString();
                            String hargabarang = dataSnapshot.child("hargabarang").getValue().toString();
                            String jenisbarang = dataSnapshot.child("jenisbarang").getValue().toString();
                            String jumlahbarang=dataSnapshot.child("jumlahbarang").getValue().toString();
                            String kodebarang=dataSnapshot.child("kodebarang").getValue().toString();
                            String ukuran=dataSnapshot.child("kodebarangmasuk").getValue().toString();
                            String kodesupplier=dataSnapshot.child("kodesupplier").getValue().toString();
                            String namabarang=dataSnapshot.child("namabarang").getValue().toString();
                            String namasupplier=dataSnapshot.child("namasupplier").getValue().toString();

                            fotoSimpanData.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    fotoSimpanData.setEnabled(false);
                                    progresskonfirmasi.setVisibility(View.VISIBLE);
                                    FirebaseAuth fAuth = FirebaseAuth.getInstance();
                                    String uri = "https://firebasestorage.googleapis.com/v0/b/katalogonline-21c2f.appspot.com/o/konfirmasipembayaran%2Fdownload%20(1).png?alt=media&token=5347c44b-078b-4b56-81ef-0941dd7b6da2" ;

                                    user= FirebaseDatabase.getInstance().getReference().child("user");
                                    user.child(fAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {

                                            String ImageUrlPembeli = dataSnapshot2.child("ImageUrl").getValue().toString();
                                            String alamat = dataSnapshot2.child("alamat").getValue().toString();
                                            String nama = dataSnapshot2.child("nama").getValue().toString();
                                            String tanggal = dataSnapshot2.child("tanggal").getValue().toString();
                                            String telepon = dataSnapshot2.child("telepon").getValue().toString();
                                            String uid = dataSnapshot2.child("uid").getValue().toString();

                                            refSimpanData.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {

                                                    DatabaseReference Refkurang = FirebaseDatabase.getInstance().getReference().child("tampil");
                                                    Refkurang.child("1"+kodebarang+ukuran).addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot3) {
                                                            Integer a = Integer.valueOf(totalBarang.getText().toString());
                                                            Integer b = Integer.valueOf(dataSnapshot3.child("jumlahbarang").getValue().toString()) ;
                                                            String hasil = String.valueOf(b-a);
                                                            dataSnapshot3.getRef().child("jumlahbarang").setValue(hasil);
                                                            String hargapokok = dataSnapshot3.child("hargabarangmasuk").getValue().toString();
                                                            dataSnapshot1.getRef().child("hargapokok").setValue(hargapokok);
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });


                                                    dataSnapshot1.getRef().child("konfirmasi").setValue("0");
                                                    dataSnapshot1.getRef().child("jumlahbayar").setValue(jumlahdibayar.getText().toString());
                                                    dataSnapshot1.getRef().child("ukuranbarang").setValue(ukuran);
                                                    dataSnapshot1.getRef().child("jumlahbarang").setValue(totalBarang.getText().toString());

                                                    dataSnapshot1.getRef().child("uid").setValue(uid);
                                                    dataSnapshot1.getRef().child("alamatpembeli").setValue(alamat);
                                                    dataSnapshot1.getRef().child("teleponpembeli").setValue(telepon);
                                                    dataSnapshot1.getRef().child("namapembeli").setValue(nama);
                                                    dataSnapshot1.getRef().child("tanggallahir").setValue(tanggal);
                                                    dataSnapshot1.getRef().child("ImageUrlPembeli").setValue(ImageUrlPembeli);

                                                    dataSnapshot1.getRef().child("kodebarang").setValue(kodebarang);
                                                    dataSnapshot1.getRef().child("jenisbarang").setValue(jenisbarang);
                                                    dataSnapshot1.getRef().child("ImageUrlBarang").setValue(ImageUrl);
                                                    dataSnapshot1.getRef().child("namabarang").setValue(namabarang);
                                                    dataSnapshot1.getRef().child("deskripsi").setValue(deskripsi);
                                                    dataSnapshot1.getRef().child("hargabarang").setValue(hargabarang);

                                                    dataSnapshot1.getRef().child("ImageUrl").setValue(uri);
                                                    dataSnapshot1.getRef().child("kodekeranjang").setValue(uid+0);


                                                    Toast.makeText(KonfirmasiPembayaran.this, " Berhasil Dimasukan Ke Keranjang, Silahkan Cek Out Untuk Lanjut Proses Pembayaran ", Toast.LENGTH_SHORT).show();


                                                    FirebaseAuth fAuth = FirebaseAuth.getInstance();
                                                    DatabaseReference  user= FirebaseDatabase.getInstance().getReference().child("user");
                                                    user.child(fAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                                                            String uid = dataSnapshot1.child("uid").getValue().toString();
                                                            DatabaseReference Dataref= FirebaseDatabase.getInstance().getReference().child("datarating");
                                                            Dataref.child(kodebarang+ukuran).child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                                                                    if (dataSnapshot2.exists())
                                                                    {
                                                                        Intent intent=new Intent(KonfirmasiPembayaran.this,MainActivity.class);
                                                                        String nama = getIntent().getStringExtra("lnama");
                                                                        intent.putExtra("lnama", nama);
                                                                        startActivity(intent);
                                                                        finish();

                                                                    }else
                                                                    {
                                                                        Intent intent = new Intent(KonfirmasiPembayaran.this, DataRating.class);
                                                                        String nama = getIntent().getStringExtra("lnama");
                                                                        intent.putExtra("lnama", nama);
                                                                        intent.putExtra("tampilkey", kodebarang+kodebarangmasuk);
                                                                        startActivity(intent);
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



                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
                                            ////
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });






                                }

                            });




                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    public void kembali(View view){
        String nama=getIntent().getStringExtra("lnama");
        Intent intent=new Intent(KonfirmasiPembayaran.this,MainActivity.class);
        intent.putExtra("lnama", nama);
        startActivity(intent);

    }
    private  String formatRupiah(Double number){
        Locale localeID = new Locale("IND","ID");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(localeID);
        String formatrupiah = numberFormat.format(number);
        String[] split = formatrupiah.split(",");
        int length = split[0].length();
        return split[0].substring(0,2)+". "+split[0].substring(2,length);
    }
}
