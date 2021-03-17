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
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class DataSupplierEdit extends AppCompatActivity {

    private static final int REQUEST_CODE_IMAGE =101 ;
    ImageView editFotoSupplier,fotoChatEditSupplier,fotoEditSupplierBersihkan,fotoEditSupplierSimpan;
    EditText eKodeSupplier,eNamaSupplier,eTeleponSupplier,eAlamatSupplier;
    TextView persenSupplier,lAdminEditSupplier;

    Uri imageUri;
    boolean isImageAdded=false;

    DatabaseReference ref,Dataref;
    StorageReference StorageRef,RefStorage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_supplier_edit);

        editFotoSupplier=findViewById(R.id.editFotoSupplier);
        fotoChatEditSupplier=findViewById(R.id.fotoChatEditSupplier);
        fotoEditSupplierBersihkan=findViewById(R.id.fotoEditSupplierBersihkan);
        fotoEditSupplierSimpan=findViewById(R.id.fotoEditSupplierSimpan);

        eKodeSupplier=findViewById(R.id.eKodeSupplier);
        eNamaSupplier=findViewById(R.id.eNamaSupplier);
        eTeleponSupplier=findViewById(R.id.eTeleponSupplier);
        eAlamatSupplier=findViewById(R.id.eAlamatSupplier);


        persenSupplier=findViewById(R.id.persenSupplier);
        persenSupplier.setVisibility(View.GONE);
        lAdminEditSupplier=findViewById(R.id.lAdminEditSupplier);


        String SupplierKey=getIntent().getStringExtra("kodesupplier");

        Dataref= FirebaseDatabase.getInstance().getReference().child("supplier");

        ref=FirebaseDatabase.getInstance().getReference().child("supplier").child(SupplierKey);

        StorageRef= FirebaseStorage.getInstance().getReference().child("supplier");

        if(getIntent().getExtras()!=null){

            Bundle bundle = getIntent().getExtras();
            lAdminEditSupplier.setText(bundle.getString("lnama"));

        }else{

            lAdminEditSupplier.setText("Nama Tidak Tersedia");

        }

        RefStorage=FirebaseStorage.getInstance().getReference().child("supplier").child(SupplierKey+".jpg");

        Dataref.child(SupplierKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    String kodesupplier=dataSnapshot.child("kodesupplier").getValue().toString();
                    String namasupplier=dataSnapshot.child("namasupplier").getValue().toString();
                    String teleponsupplier=dataSnapshot.child("teleponsupplier").getValue().toString();
                    String alamatsupplier=dataSnapshot.child("alamatsupplier").getValue().toString();

                    String ImageUrl=dataSnapshot.child("ImageUrl").getValue().toString();

                    Picasso.get().load(ImageUrl).into(editFotoSupplier);

                    eKodeSupplier.setText(kodesupplier);
                    eNamaSupplier.setText(namasupplier);
                    eTeleponSupplier.setText(teleponsupplier);
                    eAlamatSupplier.setText(alamatsupplier);

                    simpan(ImageUrl);






                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        fotoEditSupplierBersihkan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String kodesupplier   = eKodeSupplier.getText().toString().trim();
                DatabaseReference Reftampil = FirebaseDatabase.getInstance().getReference().child("tampil");
                Reftampil.orderByChild("kodesupplier").equalTo(kodesupplier).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                            postSnapshot.getRef().child("alamatsupplier").setValue("Data Null");
                            postSnapshot.getRef().child("kodesupplier").setValue("Data Null");
                            postSnapshot.getRef().child("namasupplier").setValue("Data Null");
                            postSnapshot.getRef().child("teleponsupplier").setValue("Data Null");

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                DatabaseReference Refbarangmasuk = FirebaseDatabase.getInstance().getReference().child("barangmasuk");
                Refbarangmasuk.orderByChild("kodesupplier").equalTo(kodesupplier).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                            postSnapshot.getRef().child("alamatsupplier").setValue("Data Null");
                            postSnapshot.getRef().child("kodesupplier").setValue("Data Null");
                            postSnapshot.getRef().child("namasupplier").setValue("Data Null");
                            postSnapshot.getRef().child("teleponsupplier").setValue("Data Null");

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                ref.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Intent intent=new Intent(DataSupplierEdit.this,DataSupplier.class);
                        String nama = lAdminEditSupplier.getText().toString();
                        intent.putExtra("lnama", nama);
                        startActivity(intent);
                        Toast.makeText(DataSupplierEdit.this, " Data Berhasil Dihapus " , Toast.LENGTH_SHORT).show();


                    }
                });
            }
        });


        editFotoSupplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,REQUEST_CODE_IMAGE);
            }
        });


    }

    private void simpan(String ImageUrl) {

        fotoEditSupplierSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String kodesupplier   = eKodeSupplier.getText().toString().trim();
                String namasupplier   = eNamaSupplier.getText().toString().trim();
                String teleponsupplier  = eTeleponSupplier.getText().toString().trim();
                String alamatsupplier  = eAlamatSupplier.getText().toString().trim();



                if (TextUtils.isEmpty(kodesupplier)){
                    eKodeSupplier.setError("Tolong isi Kode Supplier");
                    return;
                }

                if (TextUtils.isEmpty(namasupplier)){
                    eNamaSupplier.setError("Tolong isi Nama Supplier");
                    return;
                }

                if (TextUtils.isEmpty(teleponsupplier)){
                    eTeleponSupplier.setError("Tolong isi Telepon Supplier");
                    return;
                }

                if (TextUtils.isEmpty(alamatsupplier)){
                    eAlamatSupplier.setError("Tolong isi Alamat Supplier");
                    return;

                }



                hapus(ImageUrl);


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
            editFotoSupplier.setImageURI(imageUri);

        }
    }
    public void kembali(View view){
        Intent intent=new Intent(DataSupplierEdit.this,DataSupplier.class);
        String nama = lAdminEditSupplier.getText().toString();
        intent.putExtra("lnama", nama);
        startActivity(intent);
    }

    public void hapus(String ImageUrl){
        persenSupplier.setVisibility(View.VISIBLE);

        String kodesupplier   = eKodeSupplier.getText().toString().trim();
        String namasupplier   = eNamaSupplier.getText().toString().trim();
        String teleponsupplier  = eTeleponSupplier.getText().toString().trim();
        String alamatsupplier  = eAlamatSupplier.getText().toString().trim();


        DatabaseReference Refbarangmasuk = FirebaseDatabase.getInstance().getReference().child("barangmasuk");
        Refbarangmasuk.orderByChild("kodesupplier").equalTo(kodesupplier).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    postSnapshot.getRef().child("kodesupplier").setValue(kodesupplier);
                    postSnapshot.getRef().child("namasupplier").setValue(namasupplier);
                    postSnapshot.getRef().child("teleponsupplier").setValue(teleponsupplier);
                    postSnapshot.getRef().child("alamatsupplier").setValue(alamatsupplier);

                    if (isImageAdded != true) {

                        postSnapshot.getRef().child("ImageUrl").setValue(ImageUrl);

                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DatabaseReference Reftampil = FirebaseDatabase.getInstance().getReference().child("tampil");
        Reftampil.orderByChild("kodesupplier").equalTo(kodesupplier).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    postSnapshot.getRef().child("kodesupplier").setValue(kodesupplier);
                    postSnapshot.getRef().child("namasupplier").setValue(namasupplier);
                    postSnapshot.getRef().child("teleponsupplier").setValue(teleponsupplier);
                    postSnapshot.getRef().child("alamatsupplier").setValue(alamatsupplier);

                    if (isImageAdded != true) {

                        postSnapshot.getRef().child("ImageUrl").setValue(ImageUrl);

                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        Dataref.child("1"+kodesupplier).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().child("kodesupplier").setValue(kodesupplier);
                dataSnapshot.getRef().child("namasupplier").setValue(namasupplier);
                dataSnapshot.getRef().child("teleponsupplier").setValue(teleponsupplier);
                dataSnapshot.getRef().child("alamatsupplier").setValue(alamatsupplier);

                if (isImageAdded != true) {

                    dataSnapshot.getRef().child("ImageUrl").setValue(ImageUrl);

                    Toast.makeText(DataSupplierEdit.this, " Data Berhasil Diedit ", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(DataSupplierEdit.this,DataSupplier.class);
                    String nama = lAdminEditSupplier.getText().toString();
                    intent.putExtra("lnama", nama);
                    startActivity(intent);


                }else {

                    StorageRef.child("1" + kodesupplier + ".jpg").putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            StorageRef.child("1" + kodesupplier + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    dataSnapshot.getRef().child("ImageUrl").setValue(uri.toString());


                                    DatabaseReference Refbarangmasuk = FirebaseDatabase.getInstance().getReference().child("barangmasuk");

                                    Refbarangmasuk.orderByChild("kodesupplier").equalTo(kodesupplier).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                                                postSnapshot.getRef().child("ImageUrlSupplier").setValue(uri.toString());
                                            }
                                        }
                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                    DatabaseReference Reftampil = FirebaseDatabase.getInstance().getReference().child("tampil");

                                    Reftampil.orderByChild("kodesupplier").equalTo(kodesupplier).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                                                postSnapshot.getRef().child("ImageUrlSupplier").setValue(uri.toString());
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
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (taskSnapshot.getBytesTransferred() * 100) / taskSnapshot.getTotalByteCount();
                            persenSupplier.setText(progress + " %");
                            if (progress == 100) {
                                {
                                    Toast.makeText(DataSupplierEdit.this, " Data Berhasil Diedit ", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(DataSupplierEdit.this, DataSupplier.class);
                                    String nama = lAdminEditSupplier.getText().toString();
                                    intent.putExtra("lnama", nama);
                                    startActivity(intent);
                                }

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
    public void chatadmin(View view){
        Intent intent = new Intent(DataSupplierEdit.this, ChatAdmin.class);
        String nama = lAdminEditSupplier.getText().toString();
        intent.putExtra("lnama", nama);
        startActivity(intent);
    }
}
