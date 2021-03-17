package com.example.fixkatalog;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.util.HashMap;

public class DataSupplierTambah extends AppCompatActivity {


    private static final int REQUEST_CODE_IMAGE =101 ;
    ImageView tambahFotoSupplier,fotoChatTambahSupplier,fotoTambahSupplierBersihkan,fotoTambahSupplierSimpan;
    EditText tKodeSupplier,tNamaSupplier,tTeleponSupplier,tAlamatSupplier;
    TextView persenanSupplier,lAdminTambahSupplier;

    Uri imageUri;
    boolean isImageAdded=false;

    DatabaseReference Dataref;
    StorageReference StorageRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_supplier_tambah);

        tambahFotoSupplier=findViewById(R.id.tambahFotoSupplier);
        fotoChatTambahSupplier=findViewById(R.id.fotoChatTambahSupplier);
        fotoTambahSupplierBersihkan=findViewById(R.id.fotoTambahSupplierBersihkan);
        fotoTambahSupplierSimpan=findViewById(R.id.fotoTambahSupplierSimpan);

        tKodeSupplier=findViewById(R.id.tKodeSupplier);
        tNamaSupplier=findViewById(R.id.tNamaSupplier);
        tTeleponSupplier=findViewById(R.id.tTeleponSupplier);
        tAlamatSupplier=findViewById(R.id.tAlamatSupplier);

        persenanSupplier=findViewById(R.id.persenanSupplier);
        persenanSupplier.setVisibility(View.GONE);
        lAdminTambahSupplier=findViewById(R.id.lAdminTambahSupplier);

        Dataref= FirebaseDatabase.getInstance().getReference().child("supplier");
        StorageRef= FirebaseStorage.getInstance().getReference().child("supplier");


        if(getIntent().getExtras()!=null){

            Bundle bundle = getIntent().getExtras();
            lAdminTambahSupplier.setText(bundle.getString("lnama"));

        }else{

            lAdminTambahSupplier.setText("Nama Tidak Tersedia");

        }


        tambahFotoSupplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,REQUEST_CODE_IMAGE);
            }
        });

        fotoTambahSupplierBersihkan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tKodeSupplier.setText(new String(""));
                tNamaSupplier.setText(new String(""));
                tTeleponSupplier.setText(new String(""));
                tAlamatSupplier.setText(new String(""));


            }
        });

        fotoTambahSupplierSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String kodesupplier   = tKodeSupplier.getText().toString().trim();
                String namasupplier   = tNamaSupplier.getText().toString().trim();
                String teleponsupplier  = tTeleponSupplier.getText().toString().trim();
                String alamatsupplier  = tAlamatSupplier.getText().toString().trim();



                if (TextUtils.isEmpty(kodesupplier)){
                    tKodeSupplier.setError("Tolong isi Kode Supplier");
                    return;
                }

                if (TextUtils.isEmpty(namasupplier)){
                    tNamaSupplier.setError("Tolong isi Nama Supplier");
                    return;
                }

                if (TextUtils.isEmpty(teleponsupplier)){
                    tTeleponSupplier.setError("Tolong isi Telepon Supplier");
                    return;
                }

                if (TextUtils.isEmpty(alamatsupplier)){
                    tAlamatSupplier.setError("Tolong isi Alamat Supplier");
                    return;

                }


                if (isImageAdded!=false)
                {
                    uploadImage();
                }else{
                    Toast.makeText(DataSupplierTambah.this, " Pilih Dulu Foto Untuk Foto Supplier " , Toast.LENGTH_SHORT).show();
                    return;
                }




            }
        });
    }

    private void uploadImage() {
        persenanSupplier.setVisibility(View.VISIBLE);

        String kodesupplier   = tKodeSupplier.getText().toString().trim();
        String namasupplier   = tNamaSupplier.getText().toString().trim();
        String teleponsupplier  = tTeleponSupplier.getText().toString().trim();
        String alamatsupplier  = tAlamatSupplier.getText().toString().trim();
        Dataref.child("1"+kodesupplier).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().child("kodesupplier").setValue(kodesupplier);
                dataSnapshot.getRef().child("namasupplier").setValue(namasupplier);
                dataSnapshot.getRef().child("teleponsupplier").setValue(teleponsupplier);
                dataSnapshot.getRef().child("alamatsupplier").setValue(alamatsupplier);
                StorageRef.child("1"+ kodesupplier + ".jpg").putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        StorageRef.child("1"+ kodesupplier + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                dataSnapshot.getRef().child("ImageUrl").setValue(uri.toString());


                            }
                        });


                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                        double progress=(taskSnapshot.getBytesTransferred()*100)/taskSnapshot.getTotalByteCount();
                        persenanSupplier.setText(progress +" %");
                        if (progress == 100) {
                            {
                                Toast.makeText(DataSupplierTambah.this, " Data Berhasil Ditambahkan " , Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(DataSupplierTambah.this,DataSupplier.class);
                                String nama = lAdminTambahSupplier.getText().toString();
                                intent.putExtra("lnama", nama);
                                startActivity(intent);
                            }

                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUEST_CODE_IMAGE && data!=null)
        {
            imageUri=data.getData();
            isImageAdded=true;
            tambahFotoSupplier.setImageURI(imageUri);
        }
    }
    public void kembali(View view){
        Intent intent=new Intent(DataSupplierTambah.this,DataSupplier.class);
        String nama = lAdminTambahSupplier.getText().toString();
        intent.putExtra("lnama", nama);
        startActivity(intent);
    }
    public void chatadmin(View view){
        Intent intent = new Intent(DataSupplierTambah.this, ChatAdmin.class);
        String nama = lAdminTambahSupplier.getText().toString();
        intent.putExtra("lnama", nama);
        startActivity(intent);
    }
}
