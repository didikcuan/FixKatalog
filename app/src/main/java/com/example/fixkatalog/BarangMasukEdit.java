package com.example.fixkatalog;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
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
import java.util.HashMap;
import java.util.Locale;

public class BarangMasukEdit extends AppCompatActivity {

    ImageView editBarangMasuk,fotoChatEditBarangMasuk,fotoEditBersihkanMasuk,fotoEditSimpanMasuk;
    EditText meHargaBarangMasuk,meKodeBarang,meNamaBarang,meKodeSupplier,meNamaSupplier,meKodeBarangMasuk,meTanggalMasuk,meUkuran,meJumlah ;
    TextView textViewEditMasukProgress, lAdminEditBarangMasuk, hasil;

    RadioGroup radioGroup;

    DatabaseReference Dataref,ref, Supplierref,Refdelete;

    RadioButton radioYes,radioNo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.barang_masuk_edit);

        editBarangMasuk=findViewById(R.id.editBarangMasuk);
        fotoChatEditBarangMasuk=findViewById(R.id.fotoChatEditBarangMasuk);
        fotoEditBersihkanMasuk=findViewById(R.id.fotoEditBersihkanMasuk);
        fotoEditSimpanMasuk=findViewById(R.id.fotoEditSimpanMasuk);

        meKodeBarang=findViewById(R.id.meKodeBarang);
        meNamaBarang=findViewById(R.id.meNamaBarang);
        meKodeSupplier=findViewById(R.id.meKodeSupplier);
        meNamaSupplier=findViewById(R.id.meNamaSupplier);
        meKodeBarangMasuk=findViewById(R.id.meKodeBarangMasuk);
        meTanggalMasuk=findViewById(R.id.meTanggalMasuk);
        meUkuran=findViewById(R.id.meUkuran);
        meJumlah=findViewById(R.id.meJumlah);
        meHargaBarangMasuk=findViewById(R.id.meHargaBarangMasuk);


        ///
        meHargaBarangMasuk.addTextChangedListener(new TextWatcher() {
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


                    TextView tVNumber = findViewById(R.id.tVNumber);
                    tVNumber.setText(setTextView);


                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ///


        hasil=findViewById(R.id.hasil);
        radioYes=findViewById(R.id.radioYes);
        radioNo=findViewById(R.id.radioNo);

        textViewEditMasukProgress=findViewById(R.id.textViewEditMasukProgress);
        lAdminEditBarangMasuk=findViewById(R.id.lAdminEditBarangMasuk);

        textViewEditMasukProgress.setVisibility(View.GONE);
        String BarangMasukKey=getIntent().getStringExtra("kodebarangmasuk");
        Supplierref= FirebaseDatabase.getInstance().getReference().child("supplier");
        Dataref= FirebaseDatabase.getInstance().getReference().child("barangmasuk");
        Refdelete= FirebaseDatabase.getInstance().getReference().child("barangmasuk").child(BarangMasukKey);

        ref=FirebaseDatabase.getInstance().getReference().child("barang");


        if(getIntent().getExtras()!=null){

            Bundle bundle = getIntent().getExtras();
            lAdminEditBarangMasuk.setText(bundle.getString("lnama"));

        }else{

            lAdminEditBarangMasuk.setText("Nama Tidak Tersedia");

        }

        radioGroup = findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                switch (id){
                    case R.id.radioYes:
                        hasil.setText("Yes");
                        break;
                    case R.id.radioNo:
                        hasil.setText("No");
                        break;

                }
            }
        });

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
                    String tanggalmasuk=dataSnapshot.child("tanggalmasuk").getValue().toString();
                    String ukuranbarang=dataSnapshot.child("ukuranbarang").getValue().toString();
                    String jumlahbarang=dataSnapshot.child("jumlahbarang").getValue().toString();
                    String ImageUrl=dataSnapshot.child("ImageUrl").getValue().toString();
                    String hargabarangmasuk=dataSnapshot.child("hargabarangmasuk").getValue().toString();


                        String hotpromo = dataSnapshot.child("hotpromo").getValue().toString();
                        Picasso.get().load(ImageUrl).into(editBarangMasuk);

                    hasil.setText(hotpromo);
                    meKodeBarang.setText(kodebarang);
                    meNamaBarang.setText(namabarang);
                    meKodeSupplier.setText(kodesupplier);
                    meNamaSupplier.setText(namasupplier);
                    meKodeBarangMasuk.setText(kodebarangmasuk);
                    meTanggalMasuk.setText(tanggalmasuk);
                    meUkuran.setText(ukuranbarang);
                    meJumlah.setText(jumlahbarang);
                    meHargaBarangMasuk.setText(hargabarangmasuk);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        fotoEditBersihkanMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ///
                String kodebarang   = meKodeBarang.getText().toString().trim();
                String kodebarangmasuk    = meKodeBarangMasuk.getText().toString().trim();
                String ukuranbarang  = meUkuran.getText().toString().trim();
                Dataref.child("1"+kodebarangmasuk).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        String jumlahbarang1 = dataSnapshot.child("jumlahbarang").getValue().toString();

                        DatabaseReference refTampil = FirebaseDatabase.getInstance().getReference().child("tampil");
                        refTampil.child("1"+kodebarang+ukuranbarang).removeValue();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                ///
                Refdelete.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Intent intent=new Intent(BarangMasukEdit.this,BarangMasuk.class);
                        String nama = lAdminEditBarangMasuk.getText().toString();
                        intent.putExtra("lnama", nama);
                        startActivity(intent);
                        Toast.makeText(BarangMasukEdit.this, " Data Berhasil Dihapus ", Toast.LENGTH_SHORT).show();


                    }
                });
            }
        });

        ///////

        meKodeBarang.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override

            public void afterTextChanged(Editable s) {


                ref.child("1"+String.valueOf(s)).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()) {

                            String kodebarang = dataSnapshot.child("kodebarang").getValue().toString();
                            String namabarang = dataSnapshot.child("namabarang").getValue().toString();
                            String jenisbarang = dataSnapshot.child("jenisbarang").getValue().toString();
                            String hargabarang = dataSnapshot.child("hargabarang").getValue().toString();
                            String deskripsi = dataSnapshot.child("deskripsi").getValue().toString();
                            String ImageUrl = dataSnapshot.child("ImageUrl").getValue().toString();

                            Picasso.get().load(ImageUrl).into(editBarangMasuk);

                            meNamaBarang.setText(namabarang);



                            fotoEditSimpanMasuk.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    String kodebarang = meKodeBarang.getText().toString().trim();
                                    String namabarang = meNamaBarang.getText().toString().trim();
                                    String kodesupplier = meKodeSupplier.getText().toString().trim();
                                    String namasupplier = meNamaSupplier.getText().toString().trim();
                                    String kodebarangmasuk = meKodeBarangMasuk.getText().toString().trim();
                                    String tanggalmasuk = meTanggalMasuk.getText().toString().trim();
                                    String ukuranbarang = meUkuran.getText().toString().trim();
                                    String jumlahbarang = meJumlah.getText().toString().trim();
                                    String hargabarangmasuk = meHargaBarangMasuk.getText().toString().trim();

                                    if (TextUtils.isEmpty(namabarang)) {
                                        meNamaBarang.setError("Tolong isi Nama Barang");
                                        return;
                                    }

                                    if (TextUtils.isEmpty(kodesupplier)) {
                                        meKodeSupplier.setError("Tolong isi Kode Supplier");
                                        return;
                                    }

                                    if (TextUtils.isEmpty(namasupplier)) {
                                        meNamaSupplier.setError("Tolong isi Nama Supplier");
                                        return;
                                    }

                                    if (TextUtils.isEmpty(kodebarangmasuk)) {
                                        meKodeBarangMasuk.setError("Tolong isi Kode Barang Masuk");
                                        return;
                                    }

                                    if (TextUtils.isEmpty(hargabarangmasuk)) {
                                        meHargaBarangMasuk.setError("Tolong isi Harga Barang Masuk");
                                        return;
                                    }

                                    if (TextUtils.isEmpty(tanggalmasuk)) {
                                        meTanggalMasuk.setError("Tolong isi Tanggal Masuk Barang");
                                        return;
                                    }

                                    if (TextUtils.isEmpty(ukuranbarang)) {
                                        meUkuran.setError("Tolong isi Ukuran Barang");
                                        return;
                                    }
                                    if (TextUtils.isEmpty(jumlahbarang)) {
                                        meJumlah.setError("Tolong isi Jumlah Barang Masuk");
                                        return;
                                    }


                                    hapus(jenisbarang, hargabarang, deskripsi, ImageUrl);
                                }

                            });

                        }else
                        {
                            meNamaBarang.setText("");



                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });

        meKodeSupplier.addTextChangedListener(new TextWatcher() {
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

                            meNamaSupplier.setText(namasupplier);


                        }else
                        {
                            meNamaSupplier.setText("");

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

    }





    public void hapus(String jenisbarang,String hargabarang,String deskripsi,String ImageUrl){

        textViewEditMasukProgress.setVisibility(View.VISIBLE);

        String kodebarang   = meKodeBarang.getText().toString().trim();
        String namabarang   = meNamaBarang.getText().toString().trim();
        String kodesupplier  = meKodeSupplier.getText().toString().trim();
        String namasupplier  = meNamaSupplier.getText().toString().trim();
        String kodebarangmasuk    = meKodeBarangMasuk.getText().toString().trim();
        String tanggalmasuk  = meTanggalMasuk.getText().toString().trim();
        String ukuranbarang  = meUkuran.getText().toString().trim();
        String jumlahbarang    = meJumlah.getText().toString().trim();
        String hotpromo = hasil.getText().toString().trim();
        String hargabarangmasuk = meHargaBarangMasuk.getText().toString().trim();

        Dataref.child("1"+kodebarangmasuk).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String jumlahbarang1 = dataSnapshot.child("jumlahbarang").getValue().toString();
                Integer b = Integer.valueOf(jumlahbarang);
                DatabaseReference refTampil = FirebaseDatabase.getInstance().getReference().child("tampil");
                refTampil.child("1"+kodebarang+ukuranbarang).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                        Integer c = Integer.valueOf(jumlahbarang1);
                        Integer a = Integer.valueOf(dataSnapshot1.child("jumlahbarang").getValue().toString());

                        String hasil1 = String.valueOf(a-c+b);
                        dataSnapshot1.getRef().child("hargabarangmasuk").setValue(hargabarangmasuk);
                        dataSnapshot1.getRef().child("kodebarang").setValue(kodebarang);
                        dataSnapshot1.getRef().child("namabarang").setValue(namabarang);
                        dataSnapshot1.getRef().child("kodesupplier").setValue(kodesupplier);
                        dataSnapshot1.getRef().child("namasupplier").setValue(namasupplier);
                        dataSnapshot1.getRef().child("kodebarangmasuk").setValue(ukuranbarang);
                        dataSnapshot1.getRef().child("jumlahbarang").setValue(hasil1);
                        dataSnapshot1.getRef().child("deskripsi").setValue(deskripsi);
                        dataSnapshot1.getRef().child("ImageUrl").setValue(ImageUrl);
                        dataSnapshot1.getRef().child("jenisbarang").setValue(jenisbarang);
                        dataSnapshot1.getRef().child("hargabarang").setValue(hargabarang);
                        dataSnapshot1.getRef().child("hotpromo").setValue(hotpromo);
                        dataSnapshot1.getRef().child("ImageUrl").setValue(ImageUrl);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                dataSnapshot.getRef().child("hargabarangmasuk").setValue(hargabarangmasuk);
                dataSnapshot.getRef().child("kodebarang").setValue(kodebarang);
                dataSnapshot.getRef().child("namabarang").setValue(namabarang);
                dataSnapshot.getRef().child("kodesupplier").setValue(kodesupplier);
                dataSnapshot.getRef().child("namasupplier").setValue(namasupplier);
                dataSnapshot.getRef().child("kodebarangmasuk").setValue(kodebarangmasuk);
                dataSnapshot.getRef().child("tanggalmasuk").setValue(tanggalmasuk);
                dataSnapshot.getRef().child("ukuranbarang").setValue(ukuranbarang);
                dataSnapshot.getRef().child("jumlahbarang").setValue(jumlahbarang);
                dataSnapshot.getRef().child("deskripsi").setValue(deskripsi);
                dataSnapshot.getRef().child("ImageUrl").setValue(ImageUrl);
                dataSnapshot.getRef().child("jenisbarang").setValue(jenisbarang);
                dataSnapshot.getRef().child("hargabarang").setValue(hargabarang);
                dataSnapshot.getRef().child("hotpromo").setValue(hotpromo);
                dataSnapshot.getRef().child("ImageUrl").setValue(ImageUrl);

                Toast.makeText(BarangMasukEdit.this, " Data Berhasil Diedit " , Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(BarangMasukEdit.this,BarangMasuk.class);
                String nama = lAdminEditBarangMasuk.getText().toString();
                intent.putExtra("lnama", nama);
                startActivity(intent);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    public void kembali(View view){

        Intent intent=new Intent(BarangMasukEdit.this,BarangMasuk.class);
        String nama = lAdminEditBarangMasuk.getText().toString();
        intent.putExtra("lnama", nama);
        startActivity(intent);

    }

    public void chatadmin(View view){
        Intent intent = new Intent(BarangMasukEdit.this, ChatAdmin.class);
        String nama = lAdminEditBarangMasuk.getText().toString();
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
