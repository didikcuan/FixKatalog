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

    ImageView fotoBuktiPembayaran,fotoUpload;

    TextView jumlahBayar, namaBarang, caraPembayaran,ukuranBarang, lKonfirmasi,totalBarang, progresUploadBayar, jumlahdibayar;

    DatabaseReference Dataref, ref, user, caraBayar, refSimpanData;
    StorageReference storageRefSimpanData;

    EditText dataNama, dataTelepon, dataAlamat;

    Uri imageUri;
    boolean isImageAdded=false;

    private static final int REQUEST_CODE_IMAGE =101 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.konfirmasi_pembayaran);

        fotoBuktiPembayaran=findViewById(R.id.fotoBuktiPembayaran);
        fotoUpload=findViewById(R.id.fotoUpload);

        jumlahBayar=findViewById(R.id.jumlahBayar);
        jumlahdibayar=findViewById(R.id.jumlahdibayar);
        namaBarang=findViewById(R.id.namaBarang);
        caraPembayaran=findViewById(R.id.caraPembayaran);
        ukuranBarang=findViewById(R.id.ukuranBarang);
        lKonfirmasi=findViewById(R.id.lKonfirmasi);
        totalBarang=findViewById(R.id.totalBarang);
        dataNama=findViewById(R.id.dataNama);
        dataTelepon=findViewById(R.id.dataTelepon);
        dataAlamat=findViewById(R.id.dataAlamat);
        progresUploadBayar=findViewById(R.id.progresUploadBayar);
        progresUploadBayar.setVisibility(View.INVISIBLE);

        String BarangMasukKey=getIntent().getStringExtra("tampilkey");

        Dataref= FirebaseDatabase.getInstance().getReference().child("tampil");

        ref=FirebaseDatabase.getInstance().getReference().child("barangmasuk").child(BarangMasukKey);

        caraBayar=FirebaseDatabase.getInstance().getReference().child("carapembayaran");

        refSimpanData=FirebaseDatabase.getInstance().getReference().child("konfirmasipembayaran");
        storageRefSimpanData= FirebaseStorage.getInstance().getReference().child("konfirmasipembayaran");

        ///
        FirebaseAuth fAuth = FirebaseAuth.getInstance();

        user= FirebaseDatabase.getInstance().getReference().child("user");
        user.child(fAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    String nama = dataSnapshot.child("nama").getValue().toString();
                    String alamat = dataSnapshot.child("alamat").getValue().toString();
                    String telepon = dataSnapshot.child("telepon").getValue().toString();

                    dataNama.setText(nama);
                    dataTelepon.setText(telepon);
                    dataAlamat.setText(alamat);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        fotoBuktiPembayaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,REQUEST_CODE_IMAGE);
            }
        });

        caraBayar.child("kcb01").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    String carabayar=dataSnapshot.child("carabayar").getValue().toString();
                    String notifikasi=dataSnapshot.child("notifikasi").getValue().toString();
                    caraPembayaran.setText(carabayar);
                    TextView lNotif = findViewById(R.id.lNotif);
                    lNotif.setText(notifikasi);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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

                            ///
                            fotoUpload.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {


                                    String nama   = dataNama.getText().toString().trim();
                                    String telepon   = dataTelepon.getText().toString().trim();
                                    String alamat   = dataAlamat.getText().toString().trim();


                                    if (TextUtils.isEmpty(nama)){
                                        dataNama.setError("Tolong isi nama guna mempermudah pengiriman");
                                        return;
                                    }
                                    if (TextUtils.isEmpty(telepon)){
                                        dataTelepon.setError("Tolong isi telepon guna mempermudah pengiriman");
                                        return;
                                    }
                                    if (TextUtils.isEmpty(alamat)){
                                        dataAlamat.setError("Tolong isi alamat guna mempermudah pengiriman");
                                        return;
                                    }
                                    if (isImageAdded==false)
                                    {
                                        Toast.makeText(KonfirmasiPembayaran.this, " Pilih Tombol Simpan Data Jika Mau Memasukan Bukti TF Nanti " , Toast.LENGTH_SHORT).show();
                                        return;
                                    }

                                    if (isImageAdded!=false) {

                                        progresUploadBayar.setVisibility(View.VISIBLE);
                                        FirebaseAuth fAuth = FirebaseAuth.getInstance();

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


                                                        storageRefSimpanData.child(key + ".jpg").putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                                            @Override
                                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                                                storageRefSimpanData.child(key + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                    @Override
                                                                    public void onSuccess(Uri uri) {

                                                                        dataSnapshot1.getRef().child("ImageUrl").setValue(uri.toString());


                                                                    }
                                                                });

                                                            }
                                                        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                                            @Override
                                                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                                                double progress = (taskSnapshot.getBytesTransferred() * 100) / taskSnapshot.getTotalByteCount();
                                                                progresUploadBayar.setText(progress + " %");
                                                                if (progress == 100) {
                                                                    Toast.makeText(KonfirmasiPembayaran.this, " Berhasil Dipesan, Tunggu Konfirmasi ya ", Toast.LENGTH_SHORT).show();

                                                                    ///
                                                                    ////
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
                                                                    ////
                                                                    ///




                                                                }
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
                                }
                            });
                            ImageView fotoSimpanData = findViewById(R.id.fotoSimpanData);
                            fotoSimpanData.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String nama   = dataNama.getText().toString().trim();
                                    String telepon   = dataTelepon.getText().toString().trim();
                                    String alamat   = dataAlamat.getText().toString().trim();

                                    fotoBuktiPembayaran.setVisibility(View.GONE);
                                    fotoUpload.setVisibility(View.GONE);
                                    TextView textView21 = findViewById(R.id.textView21);
                                    textView21.setVisibility(View.GONE);

                                    if (TextUtils.isEmpty(nama)){
                                        dataNama.setError("Tolong isi nama guna mempermudah pengiriman");
                                        return;
                                    }
                                    if (TextUtils.isEmpty(telepon)){
                                        dataTelepon.setError("Tolong isi telepon guna mempermudah pengiriman");
                                        return;
                                    }
                                    if (TextUtils.isEmpty(alamat)){
                                        dataAlamat.setError("Tolong isi alamat guna mempermudah pengiriman");
                                        return;
                                    }
                                    if (isImageAdded!=false)
                                    {
                                        Toast.makeText(KonfirmasiPembayaran.this, " Pilih Tombol Upload Foto " , Toast.LENGTH_SHORT).show();
                                        return;
                                    }

                                    if (isImageAdded==false) {

                                        progresUploadBayar.setVisibility(View.VISIBLE);
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

                                                        dataSnapshot1.getRef().child("ImageUrl").setValue(uri.toString());


                                                        Toast.makeText(KonfirmasiPembayaran.this, " Berhasil Dipesan, Silahkan Upload Bukti TF Di Menu Konfirmasi ya ", Toast.LENGTH_SHORT).show();


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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUEST_CODE_IMAGE && data!=null)
        {
            imageUri=data.getData();
            isImageAdded=true;
            fotoBuktiPembayaran.setImageURI(imageUri);
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
}
