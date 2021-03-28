package com.example.fixkatalog;

import android.content.Intent;
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
import androidx.recyclerview.widget.LinearLayoutManager;
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
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class KeranjangEdit extends AppCompatActivity {

    private static final int REQUEST_CODE_IMAGE =101 ;
    ImageView kPembayaran,kPembayaranSimpan,kPembayaranBersihkan;
    EditText kNamaPembeli,kAlamatPembeli,kNoTelepon;
    TextView lUserKeranjangEdit,kCaraPembayaran,kJumlahBayar,kProsesPembayaran;

    String jumlahbayar,jumlahpokok;
    Uri imageUri;
    boolean isImageAdded=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.keranjang_edit);

        kPembayaran=findViewById(R.id.kPembayaran);
        kPembayaranSimpan=findViewById(R.id.kPembayaranSimpan);
        kPembayaranBersihkan=findViewById(R.id.kPembayaranBersihkan);

        kNamaPembeli=findViewById(R.id.kNamaPembeli);
        kAlamatPembeli=findViewById(R.id.kAlamatPembeli);
        kNoTelepon=findViewById(R.id.kNoTelepon);

        lUserKeranjangEdit=findViewById(R.id.lUserKeranjangEdit);
        kCaraPembayaran=findViewById(R.id.kCaraPembayaran);
        kJumlahBayar=findViewById(R.id.kJumlahBayar);
        kProsesPembayaran=findViewById(R.id.kProsesPembayaran);

        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        String uid = fAuth.getCurrentUser().getUid();

        if(getIntent().getExtras()!=null){

            Bundle bundle = getIntent().getExtras();
            lUserKeranjangEdit.setText(bundle.getString("lnama"));
            jumlahbayar = bundle.getString("jumlah");
            jumlahpokok = bundle.getString("jumlahpokok");

        }else{

            lUserKeranjangEdit.setText("Nama Tidak Tersedia");

        }
        DatabaseReference Dataref= FirebaseDatabase.getInstance().getReference().child("carapembayaran");
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("user");
        Dataref.child("kcb01").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                String carabayar=dataSnapshot1.child("carabayar").getValue().toString();
                kCaraPembayaran.setText(carabayar);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ref.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {

                    String namapembeli=dataSnapshot.child("nama").getValue().toString();
                    String teleponpembeli=dataSnapshot.child("telepon").getValue().toString();
                    String alamatpembeli=dataSnapshot.child("alamat").getValue().toString();
                    String ImageUrl="https://firebasestorage.googleapis.com/v0/b/katalogonline-21c2f.appspot.com/o/konfirmasipembayaran%2Fdownload%20(1).png?alt=media&token=5347c44b-078b-4b56-81ef-0941dd7b6da2";
                    String ImageUrlPembeli=dataSnapshot.child("ImageUrl").getValue().toString();

                    Picasso.get().load(ImageUrl).into(kPembayaran);
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
                    kJumlahBayar.setText(setTextView);
                    kNamaPembeli.setText(namapembeli);
                    kAlamatPembeli.setText(alamatpembeli);
                    kNoTelepon.setText(teleponpembeli);

                    kPembayaranBersihkan.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            kNamaPembeli.setText(new String(""));
                            kAlamatPembeli.setText(new String(""));
                            kNoTelepon.setText(new String(""));
                        }
                    });
                    kPembayaranSimpan.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String namapembeli   = kNamaPembeli.getText().toString().trim();
                            String alamatpembeli   = kAlamatPembeli.getText().toString().trim();
                            String notelepon  = kNoTelepon.getText().toString().trim();
                            if (TextUtils.isEmpty(namapembeli)){
                                kNamaPembeli.setError("Tolong isi Nama Anda");
                                return;
                            }

                            if (TextUtils.isEmpty(alamatpembeli)){
                                kAlamatPembeli.setError("Tolong isi Alamat Anda");
                                return;
                            }


                            if (TextUtils.isEmpty(notelepon)){
                                kNoTelepon.setError("Tolong isi No Telepon Anda");
                                return;
                            }

                            if (isImageAdded!=false)
                            {
                                kProsesPembayaran.setVisibility(View.VISIBLE);
                                DatabaseReference tampil = FirebaseDatabase.getInstance().getReference().child("tampilkeranjang");
                                DatabaseReference keytampil = FirebaseDatabase.getInstance().getReference().child("keykeranjang");
                                keytampil.child("kt01").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        Integer key = Integer.valueOf(snapshot.child("key").getValue().toString());
                                        String key_temp = String.valueOf(key+1);
                                        snapshot.getRef().child("key").setValue(key_temp);

                                        tampil.child(key_temp).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {

                                                dataSnapshot1.getRef().child("jumlahbayar").setValue(jumlahbayar);
                                                dataSnapshot1.getRef().child("jumlahpokok").setValue(jumlahpokok);
                                                dataSnapshot1.getRef().child("namapembeli").setValue(namapembeli);
                                                dataSnapshot1.getRef().child("alamatpembeli").setValue(alamatpembeli);
                                                dataSnapshot1.getRef().child("notelepon").setValue(notelepon);
                                                dataSnapshot1.getRef().child("status").setValue("1");
                                                dataSnapshot1.getRef().child("kodekeranjang").setValue(uid+"1"+key_temp);
                                                dataSnapshot1.getRef().child("key").setValue(key_temp);
                                                dataSnapshot1.getRef().child("uid").setValue(uid);
                                                dataSnapshot1.getRef().child("ImageUrlPembeli").setValue(ImageUrlPembeli);
                                                dataSnapshot1.getRef().child("noresi").setValue("");

                                                DatabaseReference konfirmasi = FirebaseDatabase.getInstance().getReference().child("konfirmasipembayaran");
                                                konfirmasi.orderByChild("kodekeranjang").equalTo(uid+"0").addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                                                        for (DataSnapshot postSnapshot: dataSnapshot2.getChildren()) {
                                                            postSnapshot.getRef().child("kodekeranjang").setValue(uid + "1" + key_temp);
                                                            postSnapshot.getRef().child("konfirmasi").setValue("1");
                                                            postSnapshot.getRef().child("key").setValue(key_temp);

                                                            StorageReference StorageRef = FirebaseStorage.getInstance().getReference().child("keranjang");
                                                            StorageRef.child(key_temp + ".jpg").putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                                                @Override
                                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                                                    StorageRef.child(key_temp + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                        @Override
                                                                        public void onSuccess(Uri uri) {

                                                                            dataSnapshot1.getRef().child("ImageUrl").setValue(uri.toString());
                                                                            postSnapshot.getRef().child("ImageUrl").setValue(uri.toString());

                                                                        }
                                                                    });

                                                                }
                                                            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                                                @Override
                                                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                                                    double progress = (taskSnapshot.getBytesTransferred() * 100) / taskSnapshot.getTotalByteCount();
                                                                    kProsesPembayaran.setText(progress + " %");
                                                                    if (progress == 100) {
                                                                        Toast.makeText(KeranjangEdit.this, " Data Berhasil Di Tambah ", Toast.LENGTH_SHORT).show();
                                                                        Intent intent = new Intent(KeranjangEdit.this, Keranjang.class);
                                                                        String nama = lUserKeranjangEdit.getText().toString();
                                                                        intent.putExtra("lnama", nama);
                                                                        startActivity(intent);

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

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }else{
                                Toast.makeText(KeranjangEdit.this, " Pilih Dulu Bukti TF " , Toast.LENGTH_SHORT).show();
                                return;
                            }


                        }
                    });


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        kPembayaran.setOnClickListener(new View.OnClickListener() {
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
            kPembayaran.setImageURI(imageUri);

        }
    }
    public void kembali(View view){
        Intent intent=new Intent(KeranjangEdit.this,Keranjang.class);
        String nama = lUserKeranjangEdit.getText().toString();
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
