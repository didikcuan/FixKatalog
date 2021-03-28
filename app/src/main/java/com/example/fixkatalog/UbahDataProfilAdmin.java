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


public class UbahDataProfilAdmin extends AppCompatActivity {
    FirebaseAuth fAuth;
    String userId;
    FirebaseUser user;
    DatabaseReference Dataref,ref;
    EditText eEmail, eNamaUser, eTelpon, eDate, eAlamat;
    TextView persenUbahProfil,lAdminUbahDataProfil;
    ImageView fotoUser1, btnSimpan1, fotoUbahPassword;
    StorageReference refStorage,StorageRef;

    Uri imageUri;
    boolean isImageAdded=false;




    private static final int REQUEST_CODE_IMAGE =101 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ubah_data_profil_admin);
        fAuth = FirebaseAuth.getInstance();
        userId = fAuth.getCurrentUser().getUid();

        Dataref= FirebaseDatabase.getInstance().getReference().child("user");
        refStorage= FirebaseStorage.getInstance().getReference().child("user");
        ref=FirebaseDatabase.getInstance().getReference().child("user").child(userId);

        eEmail=findViewById(R.id.eEmail);
        eNamaUser=findViewById(R.id.eNamaUser);
        eTelpon=findViewById(R.id.eTelpon);
        eDate=findViewById(R.id.eDate);
        eAlamat=findViewById(R.id.eAlamat);

        persenUbahProfil=findViewById(R.id.persenUbahProfil);
        lAdminUbahDataProfil=findViewById(R.id.lAdminUbahDataProfil);

        fotoUser1=findViewById(R.id.fotoUser1);
        btnSimpan1=findViewById(R.id.btnSimpan1);
        fotoUbahPassword=findViewById(R.id.fotoUbahPassword);
        user            = fAuth.getCurrentUser();
        persenUbahProfil.setVisibility(View.GONE);

        Dataref= FirebaseDatabase.getInstance().getReference().child("user");

        StorageRef= FirebaseStorage.getInstance().getReference().child("user");


        if(getIntent().getExtras()!=null){

            Bundle bundle = getIntent().getExtras();
            lAdminUbahDataProfil.setText(bundle.getString("lnama"));

        }else{

            lAdminUbahDataProfil.setText("Nama Tidak Tersedia");

        }
        fotoUser1.setOnClickListener(new View.OnClickListener() {
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

                    Picasso.get().load(ImageUrl).fit().into(fotoUser1);
                    eEmail.setText(email);
                    eNamaUser.setText(nama);
                    eTelpon.setText(telepon);
                    eDate.setText(tanggal);
                    eAlamat.setText(alamat);


                    btnSimpan1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            String email   = eEmail.getText().toString().trim();
                            String namauser   = eNamaUser.getText().toString().trim();
                            String telpon  = eTelpon.getText().toString().trim();
                            String date    = eDate.getText().toString().trim();
                            String ealamat    = eAlamat.getText().toString().trim();

                            if (TextUtils.isEmpty(email)){
                                eEmail.setError("Tolong isi Email");
                                return;
                            }
                            if (TextUtils.isEmpty(namauser)){
                                eNamaUser.setError("Tolong isi Nama Lengkap");
                                return;
                            }
                            if (TextUtils.isEmpty(telpon)){
                                eTelpon.setError("Tolong isi Telpon");
                                return;
                            }
                            if (TextUtils.isEmpty(date)){
                                eDate.setError("Tolong isi Tanggal Lahir");
                                return;
                            }
                            if (TextUtils.isEmpty(ealamat)){
                                eAlamat.setError("Tolong isi Alamat");
                                return;
                            }

                            persenUbahProfil.setVisibility(View.VISIBLE);


                            DatabaseReference Refpengeluaran = FirebaseDatabase.getInstance().getReference().child("pengeluaran");
                            Refpengeluaran.orderByChild("uid").equalTo(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                                        postSnapshot.getRef().child("uid").setValue(uid);
                                        postSnapshot.getRef().child("namaadmin").setValue(namauser);


                                        if (isImageAdded != true) {
                                            postSnapshot.getRef().child("ImageUrl").setValue(ImageUrl);
                                        }

                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                            ///

                                Dataref.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {


                                        dataSnapshot.getRef().child("alamat").setValue(eAlamat.getText().toString());
                                        dataSnapshot.getRef().child("email").setValue(eEmail.getText().toString());
                                        dataSnapshot.getRef().child("nama").setValue(eNamaUser.getText().toString());
                                        dataSnapshot.getRef().child("tanggal").setValue(eDate.getText().toString());
                                        dataSnapshot.getRef().child("telepon").setValue(eTelpon.getText().toString());
                                        dataSnapshot.getRef().child("uid").setValue(uid);
                                        dataSnapshot.getRef().child("hak").setValue(1);

                                        if (isImageAdded == true)
                                        {


                                            StorageRef.child(userId + ".jpg").putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                                @Override
                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                                    StorageRef.child(userId + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                        @Override
                                                        public void onSuccess(Uri uri) {

                                                            dataSnapshot.getRef().child("ImageUrl").setValue(uri.toString());



                                                            DatabaseReference Refpengeluaran = FirebaseDatabase.getInstance().getReference().child("pengeluaran");
                                                            Refpengeluaran.orderByChild("uid").equalTo(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                                    for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                                                                        postSnapshot.getRef().child("ImageUrl").setValue(uri.toString());
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
                                                    persenUbahProfil.setText(progress + " %");
                                                    if (persenUbahProfil.getText().toString() == "100") {
                                                        Toast.makeText(UbahDataProfilAdmin.this, " Data Berhasil Di Edit ", Toast.LENGTH_SHORT).show();
                                                        Intent intent=new Intent(UbahDataProfilAdmin.this,MenuAdmin.class);
                                                        String nama = lAdminUbahDataProfil.getText().toString();
                                                        intent.putExtra("lnama", nama);
                                                        startActivity(intent);
                                                    }
                                                }
                                            });



                                        }else
                                        {

                                            dataSnapshot.getRef().child("ImageUrl").setValue(ImageUrl);
                                            Intent intent=new Intent(UbahDataProfilAdmin.this,MenuAdmin.class);
                                            String nama = lAdminUbahDataProfil.getText().toString();
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

        fotoUbahPassword.setOnClickListener(new View.OnClickListener() {
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
                                Toast.makeText(UbahDataProfilAdmin.this, "Password Berhasil Dirubah", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(UbahDataProfilAdmin.this, "Password Gagal Dirubah", Toast.LENGTH_SHORT).show();
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
            fotoUser1.setImageURI(imageUri);

        }
    }




    public void kembali(View view){
        Intent intent=new Intent(UbahDataProfilAdmin.this,MenuAdmin.class);
        String nama = lAdminUbahDataProfil.getText().toString();
        intent.putExtra("lnama", nama);
        startActivity(intent);
    }

}