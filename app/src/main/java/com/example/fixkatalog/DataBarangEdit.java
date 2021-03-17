package com.example.fixkatalog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Locale;

public class DataBarangEdit extends AppCompatActivity {

    private static final int REQUEST_CODE_IMAGE =101 ;
    ImageView editFoto,fotoChatEditBarang,fotoEditBersihkan,fotoEditSimpan;
    EditText eKode,eNama,eHarga,eDeskripsi;
    TextView textViewEditProgress,lAdminDataBarangEdit,eJenis;

    String jenisBarangCheck, ImageUrl;

    RadioGroup list_opsi1;

    Uri imageUri;
    boolean isImageAdded=false;

    DatabaseReference ref,Dataref;
    StorageReference  StorageRef,RefStorage;

    RadioButton rSepatu1, rBaju1, rCelana1, rSendal1, rTas1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_barang_edit);

        editFoto=findViewById(R.id.editFoto);
        fotoChatEditBarang=findViewById(R.id.fotoChatEditBarang);
        fotoEditBersihkan=findViewById(R.id.fotoEditBersihkan);
        fotoEditSimpan=findViewById(R.id.fotoEditSimpan);

        rSepatu1=findViewById(R.id.rSepatu1);
        rBaju1=findViewById(R.id.rBaju1);
        rCelana1=findViewById(R.id.rCelana1);
        rSendal1=findViewById(R.id.rSendal1);
        rTas1=findViewById(R.id.rTas1);

        eKode=findViewById(R.id.eKode);
        eNama=findViewById(R.id.eNama);
        eJenis=findViewById(R.id.eJenis);

        eHarga=findViewById(R.id.eHarga);
        eDeskripsi=findViewById(R.id.eDeskripsi);

        textViewEditProgress=findViewById(R.id.textViewEditProgress);
        lAdminDataBarangEdit=findViewById(R.id.lAdminDataBarangEdit);

        textViewEditProgress.setVisibility(View.GONE);
        String BarangKey=getIntent().getStringExtra("kodebarang");

        Dataref= FirebaseDatabase.getInstance().getReference().child("barang");

        ref=FirebaseDatabase.getInstance().getReference().child("barang").child(BarangKey);

        StorageRef= FirebaseStorage.getInstance().getReference().child("barang");

        RefStorage=FirebaseStorage.getInstance().getReference().child("barang").child(BarangKey+".jpg");

///
        eHarga.addTextChangedListener(new TextWatcher() {
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


                    TextView txtHarga = findViewById(R.id.txtHarga);
                    txtHarga.setText(setTextView);


                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ///

        if(getIntent().getExtras()!=null){

            Bundle bundle = getIntent().getExtras();
            lAdminDataBarangEdit.setText(bundle.getString("lnama"));

        }else{

            lAdminDataBarangEdit.setText("Nama Tidak Tersedia");

        }

        list_opsi1 = findViewById(R.id.radio1);
        list_opsi1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id1) {
                switch (id1){
                    case R.id.rCelana1:
                        eJenis.setText("celana");
                        break;
                    case R.id.rBaju1:
                        eJenis.setText("baju");
                        break;
                    case R.id.rSepatu1:
                        eJenis.setText("sepatu");
                        break;
                    case R.id.rSendal1:
                        eJenis.setText("sendal");
                         break;
                    case R.id.rTas1:
                        eJenis.setText("tas");
                        break;
                }
            }
        });

        Dataref.child(BarangKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {

                    String kodebarang=dataSnapshot.child("kodebarang").getValue().toString();
                    String namabarang=dataSnapshot.child("namabarang").getValue().toString();
                    jenisBarangCheck=dataSnapshot.child("jenisbarang").getValue().toString();
                    String hargabarang=dataSnapshot.child("hargabarang").getValue().toString();
                    String deskripsi=dataSnapshot.child("deskripsi").getValue().toString();
                    ImageUrl=dataSnapshot.child("ImageUrl").getValue().toString();

                    Picasso.get().load(ImageUrl).into(editFoto);
                    
                    eKode.setText(kodebarang);
                    eNama.setText(namabarang);
                    eJenis.setText(jenisBarangCheck);

                    eHarga.setText(hargabarang);
                    eDeskripsi.setText(deskripsi);



                    fotoEditSimpan.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String kodebarang   = eKode.getText().toString().trim();
                            String namabarang   = eNama.getText().toString().trim();
                            String hargabarang  = eHarga.getText().toString().trim();
                            String deskripsi    = eDeskripsi.getText().toString().trim();



                            if (TextUtils.isEmpty(kodebarang)){
                                eKode.setError("Tolong isi Kode Barang");
                                return;
                            }

                            if (TextUtils.isEmpty(namabarang)){
                                eNama.setError("Tolong isi Nama Barang");
                                return;
                            }


                            if (TextUtils.isEmpty(hargabarang)){
                                eHarga.setError("Tolong isi Harga");
                                return;
                            }

                            if (TextUtils.isEmpty(deskripsi)){
                               eDeskripsi.setError("Tolong isi Deskripsinya");
                                return;
                            }



                            hapus(ImageUrl);





                        }
                    });






                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        fotoEditBersihkan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String kodebarang   = eKode.getText().toString().trim();

                DatabaseReference Refkonfirmasi = FirebaseDatabase.getInstance().getReference().child("konfirmasipembayaran");
                Refkonfirmasi.orderByChild("kodebarang").equalTo(kodebarang).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                            postSnapshot.getRef().child("kodebarang").setValue("Data Null");
                            postSnapshot.getRef().child("namabarang").setValue("Data Null");
                            postSnapshot.getRef().child("jenisbarang").setValue("Data Null");
                            postSnapshot.getRef().child("hargabarang").setValue("Data Null");
                            postSnapshot.getRef().child("deskripsi").setValue("Data Null");

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


                DatabaseReference Reftampil = FirebaseDatabase.getInstance().getReference().child("tampil");
                Reftampil.orderByChild("kodebarang").equalTo(kodebarang).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                            postSnapshot.getRef().child("kodebarang").setValue("Data Null");
                            postSnapshot.getRef().child("namabarang").setValue("Data Null");
                            postSnapshot.getRef().child("jenisbarang").setValue("Data Null");
                            postSnapshot.getRef().child("hargabarang").setValue("Data Null");
                            postSnapshot.getRef().child("deskripsi").setValue("Data Null");

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                DatabaseReference Refbarangmasuk = FirebaseDatabase.getInstance().getReference().child("barangmasuk");
                Refbarangmasuk.orderByChild("kodebarang").equalTo(kodebarang).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                            postSnapshot.getRef().child("kodebarang").setValue("Data Null");
                            postSnapshot.getRef().child("namabarang").setValue("Data Null");
                            postSnapshot.getRef().child("jenisbarang").setValue("Data Null");
                            postSnapshot.getRef().child("hargabarang").setValue("Data Null");
                            postSnapshot.getRef().child("deskripsi").setValue("Data Null");

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                DatabaseReference Refbarangkeluar = FirebaseDatabase.getInstance().getReference().child("barangkeluar");
                Refbarangkeluar.orderByChild("kodebarang").equalTo(kodebarang).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                            postSnapshot.getRef().child("kodebarang").setValue("Data Null");
                            postSnapshot.getRef().child("namabarang").setValue("Data Null");
                            postSnapshot.getRef().child("jenisbarang").setValue("Data Null");
                            postSnapshot.getRef().child("hargabarang").setValue("Data Null");
                            postSnapshot.getRef().child("deskripsi").setValue("Data Null");


                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                ref.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Intent intent=new Intent(DataBarangEdit.this,DataBarang.class);
                        String nama = lAdminDataBarangEdit.getText().toString();
                        intent.putExtra("lnama", nama);
                        startActivity(intent);
                        Toast.makeText(DataBarangEdit.this, " Data Berhasil Dihapus " , Toast.LENGTH_SHORT).show();


                    }
                });
            }
        });


        editFoto.setOnClickListener(new View.OnClickListener() {
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
                editFoto.setImageURI(imageUri);

        }
    }
    public void kembali(View view){
        Intent intent=new Intent(DataBarangEdit.this,DataBarang.class);
        String nama = lAdminDataBarangEdit.getText().toString();
        intent.putExtra("lnama", nama);
        startActivity(intent);
    }

    public void hapus(String ImageUrl){

        textViewEditProgress.setVisibility(View.VISIBLE);

        String kodebarang   = eKode.getText().toString().trim();
        String namabarang   = eNama.getText().toString().trim();
        String hargabarang  = eHarga.getText().toString().trim();
        String deskripsi    = eDeskripsi.getText().toString().trim();
        String jenisbarang    = eJenis.getText().toString().trim();


        DatabaseReference Refbarangkeluar = FirebaseDatabase.getInstance().getReference().child("barangkeluar");
        Refbarangkeluar.orderByChild("kodebarang").equalTo(kodebarang).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    postSnapshot.getRef().child("kodebarang").setValue(kodebarang);
                    postSnapshot.getRef().child("namabarang").setValue(namabarang);
                    postSnapshot.getRef().child("jenisbarang").setValue(jenisbarang);
                    postSnapshot.getRef().child("hargabarang").setValue(hargabarang);
                    postSnapshot.getRef().child("deskripsi").setValue(deskripsi);

                    if (isImageAdded != true) {
                        postSnapshot.getRef().child("ImageUrlBarang").setValue(ImageUrl);
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        DatabaseReference Refkonfirmasi = FirebaseDatabase.getInstance().getReference().child("konfirmasipembayaran");

        Refkonfirmasi.orderByChild("kodebarang").equalTo(kodebarang).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    postSnapshot.getRef().child("kodebarang").setValue(kodebarang);
                    postSnapshot.getRef().child("namabarang").setValue(namabarang);
                    postSnapshot.getRef().child("jenisbarang").setValue(jenisbarang);
                    postSnapshot.getRef().child("hargabarang").setValue(hargabarang);
                    postSnapshot.getRef().child("deskripsi").setValue(deskripsi);

                    if (isImageAdded != true) {
                        postSnapshot.getRef().child("ImageUrlBarang").setValue(ImageUrl);
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        DatabaseReference Refbarangmasuk = FirebaseDatabase.getInstance().getReference().child("barangmasuk");

        Refbarangmasuk.orderByChild("kodebarang").equalTo(kodebarang).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    postSnapshot.getRef().child("kodebarang").setValue(kodebarang);
                    postSnapshot.getRef().child("namabarang").setValue(namabarang);
                    postSnapshot.getRef().child("jenisbarang").setValue(jenisbarang);
                    postSnapshot.getRef().child("hargabarang").setValue(hargabarang);
                    postSnapshot.getRef().child("deskripsi").setValue(deskripsi);

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

        Reftampil.orderByChild("kodebarang").equalTo(kodebarang).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    postSnapshot.getRef().child("kodebarang").setValue(kodebarang);
                    postSnapshot.getRef().child("namabarang").setValue(namabarang);
                    postSnapshot.getRef().child("jenisbarang").setValue(jenisbarang);
                    postSnapshot.getRef().child("hargabarang").setValue(hargabarang);
                    postSnapshot.getRef().child("deskripsi").setValue(deskripsi);

                    if (isImageAdded != true) {
                        postSnapshot.getRef().child("ImageUrl").setValue(ImageUrl);
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        Dataref.child("1"+kodebarang).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().child("kodebarang").setValue(kodebarang);
                dataSnapshot.getRef().child("namabarang").setValue(namabarang);
                dataSnapshot.getRef().child("jenisbarang").setValue(jenisbarang);
                dataSnapshot.getRef().child("hargabarang").setValue(hargabarang);
                dataSnapshot.getRef().child("deskripsi").setValue(deskripsi);

                if (isImageAdded != true) {

                    dataSnapshot.getRef().child("ImageUrl").setValue(ImageUrl);

                    Toast.makeText(DataBarangEdit.this, " Data Berhasil Di Edit ", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(DataBarangEdit.this,DataBarang.class);
                    String nama = lAdminDataBarangEdit.getText().toString();
                    intent.putExtra("lnama", nama);
                    startActivity(intent);


                }else {

                    StorageRef.child("1"+ kodebarang + ".jpg").putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            StorageRef.child("1"+ kodebarang + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    dataSnapshot.getRef().child("ImageUrl").setValue(uri.toString());


                                    DatabaseReference Refbarangkeluar = FirebaseDatabase.getInstance().getReference().child("barangkeluar");
                                    Refbarangkeluar.orderByChild("kodebarang").equalTo(kodebarang).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                                                postSnapshot.getRef().child("ImageUrlBarang").setValue(uri.toString());
                                            }
                                        }
                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });


                                    DatabaseReference Refkonfirmasi = FirebaseDatabase.getInstance().getReference().child("konfirmasipembayaran");
                                    Refkonfirmasi.orderByChild("kodebarang").equalTo(kodebarang).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                                                postSnapshot.getRef().child("ImageUrlBarang").setValue(uri.toString());
                                            }
                                        }
                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                    DatabaseReference Refbarangmasuk = FirebaseDatabase.getInstance().getReference().child("barangmasuk");

                                    Refbarangmasuk.orderByChild("kodebarang").equalTo(kodebarang).addListenerForSingleValueEvent(new ValueEventListener() {
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

                                    DatabaseReference Reftampil = FirebaseDatabase.getInstance().getReference().child("tampil");

                                    Reftampil.orderByChild("kodebarang").equalTo(kodebarang).addListenerForSingleValueEvent(new ValueEventListener() {
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
                            textViewEditProgress.setText(progress + " %");
                            if (progress == 100) {
                                Toast.makeText(DataBarangEdit.this, " Data Berhasil Di Edit ", Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(DataBarangEdit.this,DataBarang.class);
                                String nama = lAdminDataBarangEdit.getText().toString();
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
    public void chatadmin(View view){
        Intent intent = new Intent(DataBarangEdit.this, ChatAdmin.class);
        String nama = lAdminDataBarangEdit.getText().toString();
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
