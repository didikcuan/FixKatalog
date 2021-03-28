package com.example.fixkatalog;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.SimpleTimeZone;

public class KonfirmasiPembelian extends AppCompatActivity {

    ImageView kpPembayaran,kpPrint,kpTolak,kpSimpan;
    TextView kpStatus,kpCaraPembayaran,kpJumlahBayar,kpNama,kpAlamat,kpNoTelepon,lNamaPengguna;
    EditText kpKeterangan;
    FirebaseRecyclerOptions<KeranjangClass> options;
    FirebaseRecyclerAdapter<KeranjangClass,KeranjangHolder> adapter;
    String key,uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.konfirmasi_pembelian);

        kpPembayaran = findViewById(R.id.kpPembayaran);
        kpPrint = findViewById(R.id.kpPrint);
        kpTolak = findViewById(R.id.kpTolak);
        kpSimpan = findViewById(R.id.kpSimpan);

        kpStatus = findViewById(R.id.kpStatus);
        kpCaraPembayaran = findViewById(R.id.kpCaraPembayaran);
        kpJumlahBayar = findViewById(R.id.kpJumlahBayar);
        kpNama = findViewById(R.id.kpNama);
        kpAlamat = findViewById(R.id.kpAlamat);
        kpNoTelepon = findViewById(R.id.kpNoTelepon);
        lNamaPengguna = findViewById(R.id.lNamaPengguna);

        kpKeterangan = findViewById(R.id.kpKeterangan);
        key =getIntent().getStringExtra("key_temp");

        kpPembayaran.setVisibility(View.VISIBLE);

        if(getIntent().getExtras()!=null){

            Bundle bundle = getIntent().getExtras();
            lNamaPengguna.setText(bundle.getString("lnama"));

        }else{

            lNamaPengguna.setText("Nama Tidak Tersedia");

        }
        DatabaseReference carabayar = FirebaseDatabase.getInstance().getReference().child("carapembayaran");
        carabayar.child("kcb01").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String carabayar = dataSnapshot.child("carabayar").getValue().toString();
                kpCaraPembayaran.setText(carabayar);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        DatabaseReference Dataref = FirebaseDatabase.getInstance().getReference().child("tampilkeranjang");
        Dataref.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String ImageUrl = dataSnapshot.child("ImageUrl").getValue().toString();
                String keterangan = dataSnapshot.child("noresi").getValue().toString();
                String status = dataSnapshot.child("status").getValue().toString();
                String jumlahbayar = dataSnapshot.child("jumlahbayar").getValue().toString();
                String nama = dataSnapshot.child("namapembeli").getValue().toString();
                String alamat = dataSnapshot.child("alamatpembeli").getValue().toString();
                String notelepon = dataSnapshot.child("notelepon").getValue().toString();
                uid = dataSnapshot.child("uid").getValue().toString();
                String jumlahpokok = dataSnapshot.child("jumlahpokok").getValue().toString();

                kpPrint.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Integer.valueOf(status) == 2){
                            Intent intent = new Intent(KonfirmasiPembelian.this, KonfirmasiPembelianPrint.class);
                            String nama = lNamaPengguna.getText().toString();
                            intent.putExtra("lnama", nama);
                            intent.putExtra("key_temp", key);
                            intent.putExtra("uid", uid);
                            startActivity(intent);
                        }else {
                            Toast.makeText(KonfirmasiPembelian.this, " Tidak Bisa Print, Karena Tidak Dikonfirmasi ", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                });


                if (Integer.valueOf(status) != 1){
                    kpKeterangan.setEnabled(false);
                }else{
                    kpKeterangan.setEnabled(true);
                }
                kpSimpan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (Integer.valueOf(status) == 1){

                            String keterangan = kpKeterangan.getText().toString();
                            if (TextUtils.isEmpty(keterangan)){
                                kpKeterangan.setError("Tolong isi No Resi");
                                return;
                            }
                            dataSnapshot.getRef().child("noresi").setValue(keterangan);
                            dataSnapshot.getRef().child("status").setValue("2");

                            Date tanggal = new Date();
                            SimpleDateFormat format = new SimpleDateFormat("dd MMMM yyyy");
                            String finalTanggal = format.format(tanggal);

                            SimpleDateFormat format1 = new SimpleDateFormat("HH:mm:ss");
                            String finalTime = format1.format(tanggal);

                            dataSnapshot.getRef().child("tanggal").setValue(finalTanggal);
                            dataSnapshot.getRef().child("waktu").setValue(finalTime);

                            DatabaseReference Refkonfirmasi = FirebaseDatabase.getInstance().getReference().child("konfirmasipembayaran");
                            Refkonfirmasi.orderByChild("key").equalTo(key).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                                    for (DataSnapshot postSnapshot: dataSnapshot1.getChildren()) {
                                        postSnapshot.getRef().child("konfirmasi").setValue("2");

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                            Toast.makeText(KonfirmasiPembelian.this, " Berhasil Di Konfirmasi ", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(KonfirmasiPembelian.this, KonfirmasiPembelianHalaman.class);
                            String nama = lNamaPengguna.getText().toString();
                            intent.putExtra("lnama", nama);
                            startActivity(intent);
                        }else{
                            Toast.makeText(KonfirmasiPembelian.this, " Tidak Bisa Konfirmasi, Karena Sudah Di Tolak ", Toast.LENGTH_SHORT).show();
                        }


                        ///
                        DatabaseReference Refkodekeluar = FirebaseDatabase.getInstance().getReference().child("kodekeluar");
                        Refkodekeluar.child("kodekeluar").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {

                                Integer kodekeluar=Integer.valueOf(dataSnapshot2.child("kodekeluar").getValue().toString());
                                String hasil = String.valueOf(kodekeluar+1);
                                dataSnapshot2.getRef().child("kodekeluar").setValue(hasil);
                                dataSnapshot.getRef().child("kodekeluar").setValue(hasil);

                                DatabaseReference Refbarangkeluar = FirebaseDatabase.getInstance().getReference().child("penjualan");
                                Refbarangkeluar.child("1"+"bk"+hasil).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot3) {
                                        Date tanggal = new Date();
                                        SimpleDateFormat format = new SimpleDateFormat("dd MM yyyy");
                                        SimpleDateFormat format1 = new SimpleDateFormat("MM yyyy");
                                        String finaltanggal = format.format(tanggal);
                                        String finaltanggal1 = format1.format(tanggal);
                                        String keterangan1 = kpKeterangan.getText().toString();

                                        dataSnapshot3.getRef().child("nonota").setValue(key);
                                        dataSnapshot3.getRef().child("noresi").setValue(keterangan1);
                                        dataSnapshot3.getRef().child("namapembeli").setValue(nama);
                                        dataSnapshot3.getRef().child("jumlahbayar").setValue(jumlahbayar);
                                        dataSnapshot3.getRef().child("jumlahpokok").setValue(jumlahpokok);
                                        dataSnapshot3.getRef().child("kodepenjualan").setValue("bk"+hasil);

                                        dataSnapshot3.getRef().child("uid").setValue(uid);

                                        dataSnapshot3.getRef().child("bulantahun").setValue(finaltanggal1);
                                        dataSnapshot3.getRef().child("tanggalkeluar").setValue(finaltanggal);




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

                        /////////////

                    }
                });

                kpTolak.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        if (Integer.valueOf(status) == 1){

                            String keterangan = kpKeterangan.getText().toString();
                            if (TextUtils.isEmpty(keterangan)){
                                kpKeterangan.setError("Tolong isi Keterangan Tolak");
                                return;
                            }
                            dataSnapshot.getRef().child("noresi").setValue(keterangan);
                            dataSnapshot.getRef().child("status").setValue("3");
                            DatabaseReference Refkonfirmasi = FirebaseDatabase.getInstance().getReference().child("konfirmasipembayaran");
                            Refkonfirmasi.orderByChild("key").equalTo(key).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                                    for (DataSnapshot postSnapshot: dataSnapshot1.getChildren()) {
                                        postSnapshot.getRef().child("konfirmasi").setValue("3");

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            Toast.makeText(KonfirmasiPembelian.this, " Berhasil Ditolak ", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(KonfirmasiPembelian.this, Dummy.class);
                            String nama = lNamaPengguna.getText().toString();
                            intent.putExtra("lnama", nama);
                            intent.putExtra("uid", uid);
                            intent.putExtra("key", key);
                            startActivity(intent);

                        }else{
                            Toast.makeText(KonfirmasiPembelian.this, " Tidak Bisa Ditolak, Karena Sudah Di Konfirmasi ", Toast.LENGTH_SHORT).show();
                        }

                        ///
                        DatabaseReference Refkodetolak = FirebaseDatabase.getInstance().getReference().child("kodetolak");
                        Refkodetolak.child("kodetolak").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {

                                Integer kodekeluar=Integer.valueOf(dataSnapshot2.child("kodetolak").getValue().toString());
                                String hasil = String.valueOf(kodekeluar+1);
                                dataSnapshot2.getRef().child("kodetolak").setValue(hasil);
                                dataSnapshot.getRef().child("kodetolak").setValue(hasil);

                                DatabaseReference Refbarangkeluar = FirebaseDatabase.getInstance().getReference().child("tolak");
                                Refbarangkeluar.child("1"+"t"+hasil).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot3) {
                                        Date tanggal = new Date();
                                        SimpleDateFormat format = new SimpleDateFormat("dd MM yyyy");
                                        SimpleDateFormat format1 = new SimpleDateFormat("MM yyyy");
                                        String finaltanggal = format.format(tanggal);
                                        String finaltanggal1 = format1.format(tanggal);
                                        String keterangan1 = kpKeterangan.getText().toString();

                                        dataSnapshot3.getRef().child("noresi").setValue(key);
                                        dataSnapshot3.getRef().child("nonota").setValue(keterangan1);
                                        dataSnapshot3.getRef().child("namapembeli").setValue(nama);
                                        dataSnapshot3.getRef().child("jumlahbayar").setValue(jumlahbayar);
                                        dataSnapshot3.getRef().child("kodepenjualan").setValue("bk"+hasil);

                                        dataSnapshot3.getRef().child("uid").setValue(uid);

                                        dataSnapshot3.getRef().child("bulantahun").setValue(finaltanggal1);
                                        dataSnapshot3.getRef().child("tanggalkeluar").setValue(finaltanggal);




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

                        /////////////

                    }
                });


                Picasso.get().load(ImageUrl).into(kpPembayaran);
                kpKeterangan.setText(keterangan);
                if (Integer.valueOf(status) == 1){
                    kpStatus.setText("Belum Dikonfirmasi");
                }
                if (Integer.valueOf(status) == 2){
                    kpStatus.setText("Dikonfirmasi");
                }
                if (Integer.valueOf(status) == 3){
                    kpStatus.setText("Ditolak");
                }
                kpNama.setText(nama);
                kpAlamat.setText(alamat);
                kpNoTelepon.setText(notelepon);

                ///
                String setTextView ;
                String replace = jumlahbayar.replaceAll("[Rp. ]","");
                if (!replace.isEmpty())
                {
                    setTextView = formatRupiah(Double.parseDouble(replace));


                }else
                {

                    setTextView = "No data";
                }

                ///
                kpJumlahBayar.setText(setTextView);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public void kembali(View view){
        Intent intent=new Intent(KonfirmasiPembelian.this,KonfirmasiPembelianHalaman.class);
        String nama = lNamaPengguna.getText().toString();
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