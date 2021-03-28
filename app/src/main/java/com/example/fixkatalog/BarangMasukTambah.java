package com.example.fixkatalog;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
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

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
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

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class BarangMasukTambah extends AppCompatActivity {



    ImageView tambahBarangMasuk,fotoChatTambahBarangMasuk,fotoTambahBersihkanMasuk,fotoTambahSimpanMasuk;
    EditText mtHargaBarangMasuk,mtKodeBarang,mtNamaBarang,mtKodeSupplier,mtNamaSupplier,mtKodeBarangMasuk,mtTanggalMasuk,mtUkuran,mtJumlah ;
    TextView textViewTambahMasukProgress, lAdminTambahBarangMasuk;

    DatabaseReference Dataref,Refdata, Supplierref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.barang_masuk_tambah);

        tambahBarangMasuk=findViewById(R.id.tambahBarangMasuk);
        fotoChatTambahBarangMasuk=findViewById(R.id.fotoChatTambahBarangMasuk);
        fotoTambahBersihkanMasuk=findViewById(R.id.fotoTambahBersihkanMasuk);
        fotoTambahSimpanMasuk=findViewById(R.id.fotoTambahSimpanMasuk);

        mtKodeBarang=findViewById(R.id.mtKodeBarang);
        mtNamaBarang=findViewById(R.id.mtNamaBarang);
        mtKodeSupplier=findViewById(R.id.mtKodeSupplier);
        mtNamaSupplier=findViewById(R.id.mtNamaSupplier);
        mtKodeBarangMasuk=findViewById(R.id.mtKodeBarangMasuk);
        mtTanggalMasuk=findViewById(R.id.mtTanggalMasuk);
        mtUkuran=findViewById(R.id.mtUkuran);
        mtJumlah=findViewById(R.id.mtJumlah);
        mtHargaBarangMasuk=findViewById(R.id.mtHargaBarangMasuk);

        textViewTambahMasukProgress=findViewById(R.id.textViewTambahMasukProgress);
        lAdminTambahBarangMasuk=findViewById(R.id.lAdminTambahBarangMasuk);

        textViewTambahMasukProgress.setVisibility(View.GONE);

        textViewTambahMasukProgress.setVisibility(View.GONE);
        Refdata= FirebaseDatabase.getInstance().getReference().child("barang");
        Supplierref= FirebaseDatabase.getInstance().getReference().child("supplier");
        Dataref= FirebaseDatabase.getInstance().getReference().child("barangmasuk");

///
        mtHargaBarangMasuk.addTextChangedListener(new TextWatcher() {
            String setTextView ;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!s.toString().equals(setTextView))
                {
                    String replace = s.toString().replaceAll("[Rp. ]","");
                    if (!replace.isEmpty())
                    {
                        setTextView = formatRupiah(Double.parseDouble(replace));


                    }else
                    {

                        setTextView = "Hasil Input";
                    }


                    TextView tVNumber1 = findViewById(R.id.tVNumber1);
                    tVNumber1.setText(setTextView);


                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ///

        if(getIntent().getExtras()!=null){

            Bundle bundle = getIntent().getExtras();
            lAdminTambahBarangMasuk.setText(bundle.getString("lnama"));

        }else{

            lAdminTambahBarangMasuk.setText("Nama Tidak Tersedia");

        }


        fotoTambahBersihkanMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mtKodeBarang.setText(new String(""));
                mtNamaBarang.setText(new String(""));
                mtKodeSupplier.setText(new String(""));
                mtNamaSupplier.setText(new String(""));
                mtKodeBarangMasuk.setText(new String(""));
                mtTanggalMasuk.setText(new String(""));
                mtUkuran.setText(new String(""));
                mtJumlah.setText(new String(""));
                mtHargaBarangMasuk.setText(new String(""));
            }
        });


        mtKodeBarang.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override

            public void afterTextChanged(Editable s) {


                Refdata.child("1"+String.valueOf(s)).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()) {


                            DatabaseReference Refkodebarangmasuk = FirebaseDatabase.getInstance().getReference().child("kodebarangmasuk");
                            Refkodebarangmasuk.child("kbm1").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot1  ) {

                                    Integer kodebarangmasuk = Integer.valueOf(dataSnapshot1.child("kodebarangmasuk").getValue().toString()) ;
                                    String hasil = String.valueOf(kodebarangmasuk+1);
                                    dataSnapshot1.getRef().child("kodebarangmasuk").setValue(hasil);
                                    mtKodeBarangMasuk.setText("kbm"+hasil);

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                            String kodebarang = dataSnapshot.child("kodebarang").getValue().toString();
                            String namabarang = dataSnapshot.child("namabarang").getValue().toString();
                            String jenisbarang = dataSnapshot.child("jenisbarang").getValue().toString();
                            String hargabarang = dataSnapshot.child("hargabarang").getValue().toString();
                            String deskripsi = dataSnapshot.child("deskripsi").getValue().toString();
                            String ImageUrl = dataSnapshot.child("ImageUrl").getValue().toString();

                            Picasso.get().load(ImageUrl).into(tambahBarangMasuk);

                            Date tanggal = new Date();
                            SimpleDateFormat format = new SimpleDateFormat("dd MM yyyy");
                            String finalTanggal = format.format(tanggal);

                            mtTanggalMasuk.setText(finalTanggal);

                            mtNamaBarang.setText(namabarang);

                            mtKodeSupplier.setEnabled(true);

                            fotoTambahSimpanMasuk.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    String kodebarang = mtKodeBarang.getText().toString().trim();
                                    String namabarang = mtNamaBarang.getText().toString().trim();
                                    String kodesupplier = mtKodeSupplier.getText().toString().trim();
                                    String hargabarangmasuk = mtHargaBarangMasuk.getText().toString().trim();
                                    String kodebarangmasuk = mtKodeBarangMasuk.getText().toString().trim();
                                    String tanggalmasuk = mtTanggalMasuk.getText().toString().trim();
                                    String ukuranbarang = mtUkuran.getText().toString().trim();
                                    String jumlahbarang = mtJumlah.getText().toString().trim();


                                    if (TextUtils.isEmpty(kodebarang)) {
                                        mtKodeBarang.setError("Tolong isi Kode Barang");
                                        return;
                                    }

                                    if (TextUtils.isEmpty(kodesupplier)) {
                                        mtKodeSupplier.setError("Tolong isi Kode Supplier");
                                        return;
                                    }

                                    if (TextUtils.isEmpty(kodebarangmasuk)) {
                                        mtKodeBarangMasuk.setError("Tolong isi Kode Barang Masuk");
                                        return;
                                    }

                                    if (TextUtils.isEmpty(hargabarangmasuk)) {
                                        mtHargaBarangMasuk.setError("Tolong isi Hagra Barang Masuk");
                                        return;
                                    }

                                    if (TextUtils.isEmpty(tanggalmasuk)) {
                                        mtTanggalMasuk.setError("Tolong isi Tanggal Masuk");
                                        return;
                                    }

                                    if (TextUtils.isEmpty(ukuranbarang)) {
                                        mtUkuran.setError("Tolong isi Ukuran Barang");
                                        return;
                                    }
                                    if (TextUtils.isEmpty(jumlahbarang)) {
                                        mtJumlah.setError("Tolong isi Jumlah Barang Masuk");
                                        return;
                                    }

                                    uploadImage(jenisbarang, hargabarang, deskripsi, ImageUrl);
                                }

                            });

                        }else
                        {
                            mtNamaBarang.setText("");
                            mtKodeSupplier.setEnabled(false);


                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });

        mtKodeSupplier.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Supplierref.child("1"+String.valueOf(s)).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()) {

                            String namasupplier = dataSnapshot.child("namasupplier").getValue().toString();
                            String ImageUrlSupplier = dataSnapshot.child("ImageUrl").getValue().toString();
                            String alamatsupplier = dataSnapshot.child("alamatsupplier").getValue().toString();
                            String kodesupplier = dataSnapshot.child("kodesupplier").getValue().toString();
                            String teleponsupplier = dataSnapshot.child("teleponsupplier").getValue().toString();

                            mtNamaSupplier.setText(namasupplier);

                            mtKodeBarangMasuk.setEnabled(false);
                            mtTanggalMasuk.setEnabled(false);
                            mtUkuran.setEnabled(true);
                            mtJumlah.setEnabled(true);
                            mtHargaBarangMasuk.setEnabled(true);




                        }else
                        {
                            mtNamaSupplier.setText("");
                            mtKodeBarangMasuk.setEnabled(false);
                            mtTanggalMasuk.setEnabled(false);
                            mtUkuran.setEnabled(false);
                            mtJumlah.setEnabled(false);
                            mtHargaBarangMasuk.setEnabled(false);

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

    }


    private void uploadImage(String jenisbarang,String hargabarang,String deskripsi,String ImageUrl) {
        textViewTambahMasukProgress.setVisibility(View.VISIBLE);

        String kodebarang   = mtKodeBarang.getText().toString().trim();
        String namabarang   = mtNamaBarang.getText().toString().trim();
        String kodesupplier  = mtKodeSupplier.getText().toString().trim();
        String namasupplier  = mtNamaSupplier.getText().toString().trim();
        String kodebarangmasuk    = mtKodeBarangMasuk.getText().toString().trim();
        String tanggalmasuk  = mtTanggalMasuk.getText().toString().trim();
        String ukuranbarang  = mtUkuran.getText().toString().trim();
        String jumlahbarang    = mtJumlah.getText().toString().trim();
        String hargabarangmasuk    = mtHargaBarangMasuk.getText().toString().trim();


        Supplierref.child("1"+kodesupplier).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {

                String ImageUrlSupplier = dataSnapshot1.child("ImageUrl").getValue().toString();
                String alamatsupplier = dataSnapshot1.child("alamatsupplier").getValue().toString();
                String teleponsupplier = dataSnapshot1.child("teleponsupplier").getValue().toString();
                ///
                Dataref.child("1"+kodebarangmasuk).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        Date tanggal = new Date();
                        SimpleDateFormat format = new SimpleDateFormat("dd MM yyyy");
                        SimpleDateFormat format1 = new SimpleDateFormat("MM yyyy");
                        String finalTanggal = format.format(tanggal);
                        String finaltanggal1 = format1.format(tanggal);
                        dataSnapshot.getRef().child("ImageUrlSupplier").setValue(ImageUrlSupplier);
                        dataSnapshot.getRef().child("alamatsupplier").setValue(alamatsupplier);
                        dataSnapshot.getRef().child("teleponsupplier").setValue(teleponsupplier);
                        dataSnapshot.getRef().child("kodesupplier").setValue(kodesupplier);
                        dataSnapshot.getRef().child("namasupplier").setValue(namasupplier);

                        dataSnapshot.getRef().child("kodebarang").setValue(kodebarang);
                        dataSnapshot.getRef().child("namabarang").setValue(namabarang);
                        dataSnapshot.getRef().child("hargabarang").setValue(hargabarang);
                        dataSnapshot.getRef().child("deskripsi").setValue(deskripsi);
                        dataSnapshot.getRef().child("ImageUrl").setValue(ImageUrl);
                        dataSnapshot.getRef().child("jenisbarang").setValue(jenisbarang);

                        dataSnapshot.getRef().child("bulantahun").setValue(finaltanggal1);
                        dataSnapshot.getRef().child("kodebarangmasuk").setValue(kodebarangmasuk);
                        dataSnapshot.getRef().child("tanggalmasuk").setValue(finalTanggal);
                        dataSnapshot.getRef().child("ukuranbarang").setValue(ukuranbarang);
                        dataSnapshot.getRef().child("jumlahbarang").setValue(jumlahbarang);
                        dataSnapshot.getRef().child("hargabarangmasuk").setValue(hargabarangmasuk);
                        dataSnapshot.getRef().child("hotpromo").setValue("No");

                        Toast.makeText(BarangMasukTambah.this, " Data Berhasil Ditambahkan " , Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(BarangMasukTambah.this,BarangMasuk.class);
                        String nama = lAdminTambahBarangMasuk.getText().toString();
                        intent.putExtra("lnama", nama);
                        startActivity(intent);

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d("User", databaseError.getMessage());
                    }
                });

                DatabaseReference refTampil = FirebaseDatabase.getInstance().getReference().child("tampil");

                refTampil.child("1"+kodebarang+ukuranbarang).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ///////////////////////////////////////////////////////////////////
                        if (dataSnapshot.exists()){
                            Integer a = Integer.valueOf(dataSnapshot.child("jumlahbarang").getValue().toString());
                            Integer b = Integer.valueOf(jumlahbarang);

                            String jumlah = String.valueOf(a+b);


                            dataSnapshot.getRef().child("ImageUrlSupplier").setValue(ImageUrlSupplier);
                            dataSnapshot.getRef().child("alamatsupplier").setValue(alamatsupplier);
                            dataSnapshot.getRef().child("teleponsupplier").setValue(teleponsupplier);
                            dataSnapshot.getRef().child("hotpromo").setValue("No");


                            dataSnapshot.getRef().child("kodebarang").setValue(kodebarang);
                            dataSnapshot.getRef().child("namabarang").setValue(namabarang);
                            dataSnapshot.getRef().child("kodesupplier").setValue(kodesupplier);
                            dataSnapshot.getRef().child("namasupplier").setValue(namasupplier);
                            dataSnapshot.getRef().child("kodebarangmasuk").setValue(ukuranbarang);
                            dataSnapshot.getRef().child("jumlahbarang").setValue(jumlah);
                            dataSnapshot.getRef().child("hargabarang").setValue(hargabarang);
                            dataSnapshot.getRef().child("deskripsi").setValue(deskripsi);
                            dataSnapshot.getRef().child("ImageUrl").setValue(ImageUrl);
                            dataSnapshot.getRef().child("hargabarangmasuk").setValue(hargabarangmasuk);
                            dataSnapshot.getRef().child("jenisbarang").setValue(jenisbarang);
                        }else {
                            dataSnapshot.getRef().child("ImageUrlSupplier").setValue(ImageUrlSupplier);
                            dataSnapshot.getRef().child("alamatsupplier").setValue(alamatsupplier);
                            dataSnapshot.getRef().child("teleponsupplier").setValue(teleponsupplier);
                            dataSnapshot.getRef().child("hotpromo").setValue("No");

                            dataSnapshot.getRef().child("kodebarang").setValue(kodebarang);
                            dataSnapshot.getRef().child("namabarang").setValue(namabarang);
                            dataSnapshot.getRef().child("kodesupplier").setValue(kodesupplier);
                            dataSnapshot.getRef().child("namasupplier").setValue(namasupplier);
                            dataSnapshot.getRef().child("kodebarangmasuk").setValue(ukuranbarang);
                            dataSnapshot.getRef().child("jumlahbarang").setValue(jumlahbarang);
                            dataSnapshot.getRef().child("hargabarang").setValue(hargabarang);
                            dataSnapshot.getRef().child("deskripsi").setValue(deskripsi);
                            dataSnapshot.getRef().child("ImageUrl").setValue(ImageUrl);
                            dataSnapshot.getRef().child("hargabarangmasuk").setValue(hargabarangmasuk);
                            dataSnapshot.getRef().child("jenisbarang").setValue(jenisbarang);
                        }
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

    public void chatadmin(View view){
        Intent intent = new Intent(BarangMasukTambah.this, ChatAdmin.class);
        String nama = lAdminTambahBarangMasuk.getText().toString();
        intent.putExtra("lnama", nama);
        startActivity(intent);
    }

    public void kembali(View view){
        Intent intent=new Intent(BarangMasukTambah.this,BarangMasuk.class);
        String nama = lAdminTambahBarangMasuk.getText().toString();
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
