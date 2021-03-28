package com.example.fixkatalog;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
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

import java.util.HashMap;


public class UbahDataProfilUser extends AppCompatActivity {
    FirebaseAuth fAuth;
    String userId;
    FirebaseUser user;
    DatabaseReference Dataref,ref;
    EditText uEmail, uNamaUser, uTelpon, uDate, uAlamat;
    TextView persenUbahProfilUser,lAdminUbahDataProfilUser;
    ImageView fotoUser2, btnSimpan2, fotoUbahPassword2;
    StorageReference refStorage,StorageRef;

    Uri imageUri;
    boolean isImageAdded=false;

    private static final int REQUEST_CODE_IMAGE =101 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ubah_data_profil_user);

        fAuth = FirebaseAuth.getInstance();
        userId = fAuth.getCurrentUser().getUid();

        ImageView keranjang = findViewById(R.id.keranjang);
        keranjang.setEnabled(false);
        Dataref= FirebaseDatabase.getInstance().getReference().child("user");
        refStorage= FirebaseStorage.getInstance().getReference().child("user");
        ref=FirebaseDatabase.getInstance().getReference().child("user").child(userId);

        uEmail=findViewById(R.id.uEmail);
        uNamaUser=findViewById(R.id.uNamaUser);
        uTelpon=findViewById(R.id.uTelpon);
        uDate=findViewById(R.id.uDate);
        uAlamat=findViewById(R.id.uAlamat);

        persenUbahProfilUser=findViewById(R.id.persenUbahProfilUser);
        lAdminUbahDataProfilUser=findViewById(R.id.lAdminUbahDataProfilUser);

        fotoUser2=findViewById(R.id.fotoUser2);
        btnSimpan2=findViewById(R.id.btnSimpan2);
        fotoUbahPassword2=findViewById(R.id.fotoUbahPassword2);
        user            = fAuth.getCurrentUser();
        persenUbahProfilUser.setVisibility(View.GONE);

        Dataref= FirebaseDatabase.getInstance().getReference().child("user");

        StorageRef= FirebaseStorage.getInstance().getReference().child("user");


        if(getIntent().getExtras()!=null){

            Bundle bundle = getIntent().getExtras();
            lAdminUbahDataProfilUser.setText(bundle.getString("lnama"));

        }else{

            lAdminUbahDataProfilUser.setText("Nama Tidak Tersedia");

        }
        fotoUser2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,REQUEST_CODE_IMAGE);
            }
        });

        Dataref.child(fAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    String ImageUrl = dataSnapshot.child("ImageUrl").getValue().toString();
                    String alamat = dataSnapshot.child("alamat").getValue().toString();
                    String email = dataSnapshot.child("email").getValue().toString();
                    String nama = dataSnapshot.child("nama").getValue().toString();
                    String tanggal = dataSnapshot.child("tanggal").getValue().toString();
                    String telepon = dataSnapshot.child("telepon").getValue().toString();
                    String uid = dataSnapshot.child("uid").getValue().toString();

                    Picasso.get().load(ImageUrl).fit().into(fotoUser2);
                    uEmail.setText(email);
                    uNamaUser.setText(nama);
                    uTelpon.setText(telepon);
                    uDate.setText(tanggal);
                    uAlamat.setText(alamat);


                    btnSimpan2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String email   = uEmail.getText().toString().trim();
                            String namauser   = uNamaUser.getText().toString().trim();
                            String telpon  = uTelpon.getText().toString().trim();
                            String date    = uDate.getText().toString().trim();
                            String ealamat    = uAlamat.getText().toString().trim();

                            if (TextUtils.isEmpty(email)){
                                uEmail.setError("Tolong isi Email");
                                return;
                            }
                            if (TextUtils.isEmpty(namauser)){
                                uNamaUser.setError("Tolong isi Nama Lengkap");
                                return;
                            }
                            if (TextUtils.isEmpty(telpon)){
                                uTelpon.setError("Tolong isi Telpon");
                                return;
                            }
                            if (TextUtils.isEmpty(date)){
                                uDate.setError("Tolong isi Tanggal Lahir");
                                return;
                            }
                            if (TextUtils.isEmpty(ealamat)){
                                uAlamat.setError("Tolong isi Alamat");
                                return;
                            }

                            persenUbahProfilUser.setVisibility(View.VISIBLE);


                            ////
                            DatabaseReference Refbarangdipesan = FirebaseDatabase.getInstance().getReference().child("barangdipesanstokhabis");
                            Refbarangdipesan.orderByChild("uid").equalTo(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                                        postSnapshot.getRef().child("uid").setValue(uid);
                                        postSnapshot.getRef().child("namapembeli").setValue(nama);


                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                            DatabaseReference Refbarangkeluar = FirebaseDatabase.getInstance().getReference().child("barangkeluar");
                            Refbarangkeluar.orderByChild("uid").equalTo(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                                        postSnapshot.getRef().child("uid").setValue(uid);
                                        postSnapshot.getRef().child("alamatpembeli").setValue(alamat);
                                        postSnapshot.getRef().child("teleponpembeli").setValue(telepon);
                                        postSnapshot.getRef().child("namapembeli").setValue(nama);
                                        postSnapshot.getRef().child("tanggallahir").setValue(tanggal);;

                                        if (isImageAdded != true) {
                                            postSnapshot.getRef().child("ImageUrlPembeli").setValue(ImageUrl);
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });


                            DatabaseReference Refkonfirmasi = FirebaseDatabase.getInstance().getReference().child("konfirmasipembayaran");
                            Refkonfirmasi.orderByChild("uid").equalTo(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                                        postSnapshot.getRef().child("uid").setValue(uid);
                                        postSnapshot.getRef().child("alamatpembeli").setValue(alamat);
                                        postSnapshot.getRef().child("teleponpembeli").setValue(telepon);
                                        postSnapshot.getRef().child("namapembeli").setValue(nama);
                                        postSnapshot.getRef().child("tanggallahir").setValue(tanggal);

                                        if (isImageAdded != true) {
                                            postSnapshot.getRef().child("ImageUrlPembeli").setValue(ImageUrl);
                                        }

                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });


                            Dataref.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {


                                    dataSnapshot.getRef().child("alamat").setValue(uAlamat.getText().toString());
                                    dataSnapshot.getRef().child("email").setValue(uEmail.getText().toString());
                                    dataSnapshot.getRef().child("nama").setValue(uNamaUser.getText().toString());
                                    dataSnapshot.getRef().child("tanggal").setValue(uDate.getText().toString());
                                    dataSnapshot.getRef().child("telepon").setValue(uTelpon.getText().toString());
                                    dataSnapshot.getRef().child("uid").setValue(uid);


                                    if (isImageAdded == true)
                                    {


                                        StorageRef.child(userId + ".jpg").putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                                StorageRef.child(userId + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {

                                                        dataSnapshot.getRef().child("ImageUrl").setValue(uri.toString());

                                                        DatabaseReference Refbarangkeluar = FirebaseDatabase.getInstance().getReference().child("barangkeluar");
                                                        Refbarangkeluar.orderByChild("uid").equalTo(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                                                                    postSnapshot.getRef().child("ImageUrlPembeli").setValue(uri.toString());
                                                                }
                                                            }
                                                            @Override
                                                            public void onCancelled(DatabaseError databaseError) {

                                                            }
                                                        });

///////
                                                        DatabaseReference Refkonfirmasi = FirebaseDatabase.getInstance().getReference().child("konfirmasipembayaran");
                                                        Refkonfirmasi.orderByChild("uid").equalTo(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                                                                    postSnapshot.getRef().child("ImageUrlPembeli").setValue(uri.toString());
                                                                }
                                                            }
                                                            @Override
                                                            public void onCancelled(DatabaseError databaseError) {

                                                            }
                                                        });

                                                    }
                                                });

                                            }
                                        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                                double progress = (taskSnapshot.getBytesTransferred() * 100) / taskSnapshot.getTotalByteCount();
                                                persenUbahProfilUser.setText(progress + " %");
                                                if (persenUbahProfilUser.getText().toString() == "100") {
                                                    Toast.makeText(UbahDataProfilUser.this, " Data Berhasil Di Edit ", Toast.LENGTH_SHORT).show();
                                                    Intent intent=new Intent(UbahDataProfilUser.this,MainActivity.class);
                                                    String nama = lAdminUbahDataProfilUser.getText().toString();
                                                    intent.putExtra("lnama", nama);
                                                    startActivity(intent);
                                                }
                                            }
                                        });



                                    }else
                                    {

                                        dataSnapshot.getRef().child("ImageUrl").setValue(ImageUrl);
                                        Intent intent=new Intent(UbahDataProfilUser.this,MainActivity.class);
                                        String nama = lAdminUbahDataProfilUser.getText().toString();
                                        intent.putExtra("lnama", nama);
                                        startActivity(intent);

                                    }

                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Log.d("User", databaseError.getMessage());
                                }
                            });

                        }
                    });

                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        fotoUbahPassword2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText resetPassword = new EditText(v.getContext());
                final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset Password ?");
                passwordResetDialog.setMessage("Masukan Password, harus lebih dari 6 karakter");
                passwordResetDialog.setView(resetPassword);

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String newPassword = resetPassword.getText().toString();
                        user.updatePassword(newPassword).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(UbahDataProfilUser.this, "Password Berhasil Dirubah", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(UbahDataProfilUser.this, "Password Gagal Dirubah", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });

                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                passwordResetDialog.create().show();
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
            fotoUser2.setImageURI(imageUri);

        }
    }


    public void konfirmasi(View view){
        Intent intent=new Intent(UbahDataProfilUser.this,KonfirmasiPembayaranHalaman.class);
        String nama = lAdminUbahDataProfilUser.getText().toString();
        intent.putExtra("lnama", nama);
        startActivity(intent);
    }

    public void kembali(View view){
        Intent intent=new Intent(UbahDataProfilUser.this,MainActivity.class);
        String nama = lAdminUbahDataProfilUser.getText().toString();
        intent.putExtra("lnama", nama);
        startActivity(intent);
    }

}