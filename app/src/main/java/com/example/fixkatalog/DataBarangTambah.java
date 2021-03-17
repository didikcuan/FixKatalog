package com.example.fixkatalog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.NumberFormat;
import java.util.Locale;

public class DataBarangTambah extends AppCompatActivity {

    private static final int REQUEST_CODE_IMAGE =101 ;
    ImageView tambahFoto,fotoChatTambahBarang,fotoTambahBersihkan,fotoTambahSimpan;
    EditText tKode,tNama,tJenis,tHarga,tDeskripsi;
    TextView textViewTambahProgress,lAdminTambahBarang,eJenis4;
    String jenisBarangCheck;

    RadioGroup list_opsi;

    Uri imageUri;
    boolean isImageAdded=false;

    DatabaseReference Dataref;
    StorageReference  StorageRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_barang_tambah);

        tambahFoto=findViewById(R.id.tambahFoto);
        fotoChatTambahBarang=findViewById(R.id.fotoChatTambahBarang);
        fotoTambahBersihkan=findViewById(R.id.fotoTambahBersihkan);
        fotoTambahSimpan=findViewById(R.id.fotoTambahSimpan);

        tKode=findViewById(R.id.tKode);
        tNama=findViewById(R.id.tNama);
        eJenis4=findViewById(R.id.eJenis4);

        tHarga=findViewById(R.id.tHarga);
        tDeskripsi=findViewById(R.id.tDeskripsi);

        textViewTambahProgress=findViewById(R.id.textViewTambahProgress);
        lAdminTambahBarang=findViewById(R.id.lAdminTambahBarang);

        textViewTambahProgress.setVisibility(View.GONE);

        Dataref= FirebaseDatabase.getInstance().getReference().child("barang");
        StorageRef= FirebaseStorage.getInstance().getReference().child("barang");

        ///
        tHarga.addTextChangedListener(new TextWatcher() {
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


                    TextView txtHargaTambah = findViewById(R.id.txtHargaTambah);
                    txtHargaTambah.setText(setTextView);


                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ///

        if(getIntent().getExtras()!=null){

            Bundle bundle = getIntent().getExtras();
            lAdminTambahBarang.setText(bundle.getString("lnama"));

        }else{

            lAdminTambahBarang.setText("Nama Tidak Tersedia");

        }
        tambahFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,REQUEST_CODE_IMAGE);
            }
        });

        fotoTambahBersihkan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tKode.setText(new String(""));
                tNama.setText(new String(""));

                RadioButton rSepatu = findViewById(R.id.rSepatu);
                rSepatu.setChecked(false);
                RadioButton rBaju = findViewById(R.id.rBaju);
                rBaju.setChecked(false);
                RadioButton rSendal = findViewById(R.id.rSendal);
                rSendal.setChecked(false);
                RadioButton rCelana = findViewById(R.id.rCelana);
                rCelana.setChecked(false);
                RadioButton rTas = findViewById(R.id.rTas);
                rTas.setChecked(false);
                eJenis4.setText("pilih dulu");

                tHarga.setText(new String(""));
                tDeskripsi.setText(new String(""));

            }
        });

        list_opsi = findViewById(R.id.radio);
        list_opsi.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                switch (id){
                    case R.id.rCelana:
                        jenisBarangCheck = "celana";
                        eJenis4.setText("celana");
                        break;
                    case R.id.rBaju:
                        jenisBarangCheck = "baju";
                        eJenis4.setText("baju");
                        break;
                    case R.id.rSepatu:
                        jenisBarangCheck = "sepatu";
                        eJenis4.setText("sepatu");
                        break;
                    case R.id.rSendal:
                        jenisBarangCheck = "sendal";
                        eJenis4.setText("sendal");
                        break;
                    case R.id.rTas:
                        jenisBarangCheck = "tas";
                        eJenis4.setText("tas");
                        break;
                }
            }
        });

        fotoTambahSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String kodebarang   = tKode.getText().toString().trim();
                String namabarang   = tNama.getText().toString().trim();
                String hargabarang  = tHarga.getText().toString().trim();
                String deskripsi    = tDeskripsi.getText().toString().trim();



                if (TextUtils.isEmpty(kodebarang)){
                    tKode.setError("Tolong isi Kode Barang");
                    return;
                }

                if (TextUtils.isEmpty(namabarang)){
                    tNama.setError("Tolong isi Nama Barang");
                    return;
                }


                if (TextUtils.isEmpty(hargabarang)){
                    tHarga.setError("Tolong isi Harga");
                    return;
                }

                if (TextUtils.isEmpty(deskripsi)){
                    tDeskripsi.setError("Tolong isi Deskripsinya");
                    return;
                }

                if (jenisBarangCheck == null){
                    Toast.makeText(DataBarangTambah.this, " Pilih dulu salah satu Jenis Barang " , Toast.LENGTH_SHORT).show();
                    return;
                }

                if (isImageAdded!=false)
                {
                    uploadImage(jenisBarangCheck);
                }else{
                    Toast.makeText(DataBarangTambah.this, " Pilih Dulu Foto Barang " , Toast.LENGTH_SHORT).show();
                    return;
                }




            }
        });
    }

    private void uploadImage(String jenisBarangCheck) {
        textViewTambahProgress.setVisibility(View.VISIBLE);

        String kodebarang   = tKode.getText().toString().trim();
        String namabarang   = tNama.getText().toString().trim();
        String hargabarang  = tHarga.getText().toString().trim();
        String deskripsi    = tDeskripsi.getText().toString().trim();




        Dataref.child("1"+kodebarang).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().child("kodebarang").setValue(kodebarang);
                dataSnapshot.getRef().child("namabarang").setValue(namabarang);
                dataSnapshot.getRef().child("jenisbarang").setValue(jenisBarangCheck);
                dataSnapshot.getRef().child("hargabarang").setValue(hargabarang);
                dataSnapshot.getRef().child("deskripsi").setValue(deskripsi);


                StorageRef.child("1"+ kodebarang + ".jpg").putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        StorageRef.child("1"+ kodebarang + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
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
                        textViewTambahProgress.setText(progress + " %");
                        if (progress == 100) {
                            Toast.makeText(DataBarangTambah.this, " Data Berhasil Di Tambah ", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(DataBarangTambah.this,DataBarang.class);
                            String nama = lAdminTambahBarang.getText().toString();
                            intent.putExtra("lnama", nama);
                            startActivity(intent);

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
            tambahFoto.setImageURI(imageUri);
        }
    }
    public void kembali(View view){
        Intent intent=new Intent(DataBarangTambah.this,DataBarang.class);
        String nama = lAdminTambahBarang.getText().toString();
        intent.putExtra("lnama", nama);
        startActivity(intent);
    }
    public void chatadmin(View view){
        Intent intent = new Intent(DataBarangTambah.this, ChatAdmin.class);
        String nama = lAdminTambahBarang.getText().toString();
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
