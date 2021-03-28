package com.example.fixkatalog;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
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

public class KeranjangCek extends AppCompatActivity {

    private static final int REQUEST_CODE_IMAGE =101 ;
    ImageView kcPembayaran,kcPrint,kcTolak,kcSimpan;
    TextView kcKeterangan,kcStatus,kcCaraPembayaran,kcJumlahBayar,lKCPengguna;
    EditText kcNama,kcAlamat,kcNoTelepon;
    Uri imageUri;
    boolean isImageAdded=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.keranjang_cek);

        kcPembayaran = findViewById(R.id.kcPembayaran);
        kcPrint = findViewById(R.id.kcPrint);
        kcTolak = findViewById(R.id.kcTolak);
        kcSimpan = findViewById(R.id.kcSimpan);

        kcStatus = findViewById(R.id.kcStatus);
        kcCaraPembayaran = findViewById(R.id.kcCaraPembayaran);
        kcJumlahBayar = findViewById(R.id.kcJumlahBayar);
        kcNama = findViewById(R.id.kcNama);
        kcAlamat = findViewById(R.id.kcAlamat);
        kcNoTelepon = findViewById(R.id.kcNoTelepon);
        lKCPengguna = findViewById(R.id.lKCPengguna);

        kcKeterangan = findViewById(R.id.kcKeterangan);
        String key=getIntent().getStringExtra("key_temp");

        kcPembayaran.setVisibility(View.VISIBLE);

        if(getIntent().getExtras()!=null){

            Bundle bundle = getIntent().getExtras();
            lKCPengguna.setText(bundle.getString("lnama"));

        }else{

            lKCPengguna.setText("Nama Tidak Tersedia");

        }
        DatabaseReference carabayar = FirebaseDatabase.getInstance().getReference().child("carapembayaran");
        carabayar.child("kcb01").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String carabayar = dataSnapshot.child("carabayar").getValue().toString();
                kcCaraPembayaran.setText(carabayar);
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
                String uid = dataSnapshot.child("uid").getValue().toString();

                if (Integer.valueOf(status) != 1){
                    kcNama.setEnabled(false);
                    kcAlamat.setEnabled(false);
                    kcNoTelepon.setEnabled(false);
                    kcPembayaran.setEnabled(false);
                }else{
                    kcNama.setEnabled(true);
                    kcAlamat.setEnabled(true);
                    kcNoTelepon.setEnabled(true);
                    kcPembayaran.setEnabled(true);
                }

                if(getIntent().getExtras()!=null){
                    String intent1;
                    Bundle bundle = getIntent().getExtras();
                    if (bundle.getString("intent1") !=null)
                    {
                        intent1 = bundle.getString("intent1");
                        if (intent1 != null)
                        {
                            Intent intent = new Intent(KeranjangCek.this, KonfirmasiPembelianPrint.class);
                            String nama1 = lKCPengguna.getText().toString();
                            intent.putExtra("lnama", nama1);
                            intent.putExtra("uid", uid);
                            intent.putExtra("key_temp", key);
                            intent.putExtra("intent1", intent1);
                            startActivity(intent);
                        }
                    }



                }

                kcSimpan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String namapembeli1 = kcNama.getText().toString();
                        String alamatpembeli1 = kcAlamat.getText().toString();
                        String notelepon1 = kcNoTelepon.getText().toString();

                        if (Integer.valueOf(status) != 1){
                            Toast.makeText(KeranjangCek.this, " Tidak Bisa Simpan, Karena Sudah Proses Admin ", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (TextUtils.isEmpty(namapembeli1)){
                            kcNama.setError("Tolong isi Nama Anda");
                            return;
                        }

                        if (TextUtils.isEmpty(alamatpembeli1)){
                            kcAlamat.setError("Tolong isi Alamat Anda");
                            return;
                        }


                        if (TextUtils.isEmpty(notelepon1)){
                            kcNoTelepon.setError("Tolong isi No. Telepon Anda");
                            return;
                        }

                        dataSnapshot.getRef().child("namapembeli").setValue(namapembeli1);
                        dataSnapshot.getRef().child("alamatpembeli").setValue(alamatpembeli1);
                        dataSnapshot.getRef().child("notelepon").setValue(notelepon1);

                        if (isImageAdded == true) {
                            StorageReference StorageRef = FirebaseStorage.getInstance().getReference().child("keranjang");
                            String key_temp = Dataref.push().getKey();
                            StorageRef.child(key_temp + ".jpg").putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                    StorageRef.child(key_temp + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {

                                            dataSnapshot.getRef().child("ImageUrl").setValue(uri.toString());
                                        }
                                    });

                                }
                            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress = (taskSnapshot.getBytesTransferred() * 100) / taskSnapshot.getTotalByteCount();
                                    TextView progressBar2 = findViewById(R.id.progressBar2);
                                    progressBar2.setVisibility(View.VISIBLE);
                                    progressBar2.setText(progress + " %");
                                    if (progress == 100) {
                                        Toast.makeText(KeranjangCek.this, " Berhasil Di Simpan ", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(KeranjangCek.this, KeranjangHalaman.class);
                                        String nama = lKCPengguna.getText().toString();
                                        intent.putExtra("lnama", nama);
                                        startActivity(intent);

                                    }
                                }
                            });

                        }else{
                            TextView progressBar2 = findViewById(R.id.progressBar2);
                            progressBar2.setVisibility(View.VISIBLE);
                            Toast.makeText(KeranjangCek.this, " Berhasil Di Simpan ", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(KeranjangCek.this, KeranjangHalaman.class);
                            String nama = lKCPengguna.getText().toString();
                            intent.putExtra("lnama", nama);
                            startActivity(intent);
                        }

                    }
                });

                kcTolak.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Integer.valueOf(status) != 1){
                            Toast.makeText(KeranjangCek.this, " Tidak Bisa Dibersihkan, Karena Sudah Proses Admin ", Toast.LENGTH_SHORT).show();
                            return;
                        }else {
                            kcAlamat.setText(new String(""));
                            kcNama.setText(new String(""));
                            kcNoTelepon.setText(new String(""));
                        }
                    }
                });

                kcPrint.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Integer.valueOf(status) == 2){
                            Intent intent = new Intent(KeranjangCek.this, KonfirmasiPembelianPrint.class);
                            String nama = lKCPengguna.getText().toString();
                            intent.putExtra("lnama", nama);
                            intent.putExtra("key_temp", key);
                            intent.putExtra("uid", uid);
                            startActivity(intent);
                        }else {
                            Toast.makeText(KeranjangCek.this, " Tidak Bisa Print, Karena Tidak Dikonfirmasi Admin ", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                });




                Picasso.get().load(ImageUrl).into(kcPembayaran);
                kcKeterangan.setText(keterangan);
                if (Integer.valueOf(status) == 1){
                    kcStatus.setText("Belum Dikonfirmasi");
                }
                if (Integer.valueOf(status) == 2){
                    kcStatus.setText("Dikonfirmasi");
                }
                if (Integer.valueOf(status) == 3){
                    kcStatus.setText("Ditolak");
                }
                kcNama.setText(nama);
                kcAlamat.setText(alamat);
                kcNoTelepon.setText(notelepon);

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
                kcJumlahBayar.setText(setTextView);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        kcPembayaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,REQUEST_CODE_IMAGE);
            }
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUEST_CODE_IMAGE && data!=null)
        {

            imageUri = data.getData();
            isImageAdded = true;
            kcPembayaran.setImageURI(imageUri);

        }
    }
    public void kembali(View view){
        Intent intent=new Intent(KeranjangCek.this,KeranjangHalaman.class);
        String nama = lKCPengguna.getText().toString();
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