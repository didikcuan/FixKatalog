package com.example.fixkatalog;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class PengeluaranLainnyaEdit extends AppCompatActivity {

    TextView lAdminPengeluaranEdit;
    EditText textBiayaListrikEdit, textBiayaStikerEdit, textBiayaTokoEdit, textBiayaWifiEdit, textBiayaLainnyaEdit;
    ImageView fotoSimpanPengeluaranEdit, fotoBersihkanPengeluaranEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pengeluaran_lainnya_edit);

        lAdminPengeluaranEdit=findViewById(R.id.lAdminPengeluaranEdit);
        textBiayaListrikEdit=findViewById(R.id.textBiayaListrikEdit);
        textBiayaStikerEdit=findViewById(R.id.textBiayaStikerEdit);
        textBiayaTokoEdit=findViewById(R.id.textBiayaTokoEdit);
        textBiayaWifiEdit=findViewById(R.id.textBiayaWifiEdit);
        textBiayaLainnyaEdit=findViewById(R.id.textBiayaLainnyaEdit);
        fotoSimpanPengeluaranEdit=findViewById(R.id.fotoSimpanPengeluaranEdit);
        fotoBersihkanPengeluaranEdit=findViewById(R.id.fotoBersihkanPengeluaranEdit);

        ///
        textBiayaListrikEdit.addTextChangedListener(new TextWatcher() {
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


                    TextView bLi1 = findViewById(R.id.bLi1);
                    bLi1.setText(setTextView);


                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ///

        ///
        textBiayaStikerEdit.addTextChangedListener(new TextWatcher() {
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


                    TextView bSt1 = findViewById(R.id.bSi1);
                    bSt1.setText(setTextView);


                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ///

        ///
        textBiayaTokoEdit.addTextChangedListener(new TextWatcher() {
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


                    TextView bTo1 = findViewById(R.id.bTo1);
                    bTo1.setText(setTextView);


                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ///

        ///
        textBiayaWifiEdit.addTextChangedListener(new TextWatcher() {
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


                    TextView bWi1 = findViewById(R.id.bWi1);
                    bWi1.setText(setTextView);


                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ///

        ///
        textBiayaLainnyaEdit.addTextChangedListener(new TextWatcher() {
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


                    TextView bLa1 = findViewById(R.id.bLa1);
                    bLa1.setText(setTextView);


                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ///

        if(getIntent().getExtras()!=null){

            Bundle bundle = getIntent().getExtras();
            lAdminPengeluaranEdit.setText(bundle.getString("lnama"));

        }else{

            lAdminPengeluaranEdit.setText("Nama Tidak Tersedia");

        }
        String key=getIntent().getStringExtra("namaadmin");
        DatabaseReference Dataref = FirebaseDatabase.getInstance().getReference().child("pengeluaran");
        Dataref.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                String biayalainnya = dataSnapshot.child("biayalainnya").getValue().toString();
                String biayalistrik = dataSnapshot.child("biayalistrik").getValue().toString();
                String biayastiker = dataSnapshot.child("biayastiker").getValue().toString();
                String biayatoko = dataSnapshot.child("biayatoko").getValue().toString();
                String biayawifi = dataSnapshot.child("biayawifi").getValue().toString();

                textBiayaListrikEdit.setText(biayalistrik);
                textBiayaStikerEdit.setText(biayastiker);
                textBiayaTokoEdit.setText(biayatoko);
                textBiayaWifiEdit.setText(biayawifi);
                textBiayaLainnyaEdit.setText(biayalainnya);

                simpan(key);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void simpan(String key) {

        fotoSimpanPengeluaranEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String listrik = textBiayaListrikEdit.getText().toString().trim();
                String stiker = textBiayaStikerEdit.getText().toString().trim();
                String toko = textBiayaTokoEdit.getText().toString().trim();
                String wifi = textBiayaWifiEdit.getText().toString().trim();
                String lainnya = textBiayaLainnyaEdit.getText().toString().trim();

                if (TextUtils.isEmpty(listrik)) {
                    textBiayaListrikEdit.setError("Tolong isi Biaya Listrik");
                    return;
                }
                if (TextUtils.isEmpty(stiker)) {
                    textBiayaStikerEdit.setError("Tolong isi Biaya Stiker");
                    return;
                }
                if (TextUtils.isEmpty(toko)) {
                    textBiayaTokoEdit.setError("Tolong isi Biaya Toko");
                    return;
                }
                if (TextUtils.isEmpty(wifi)) {
                    textBiayaWifiEdit.setError("Tolong isi Biaya Wifi");
                    return;
                }
                if (TextUtils.isEmpty(lainnya)) {
                    textBiayaLainnyaEdit.setError("Tolong isi Biaya Lainnya");
                    return;
                }

                ProgressBar bar = findViewById(R.id.barPengeluaranEdit);
                bar.setVisibility(View.VISIBLE);
                String biayalistrik1 = textBiayaListrikEdit.getText().toString();
                String biayastiker1 = textBiayaStikerEdit.getText().toString();
                String biayatoko1 = textBiayaTokoEdit.getText().toString();
                String biayawifi1 = textBiayaWifiEdit.getText().toString();
                String biayalainnya1 = textBiayaLainnyaEdit.getText().toString();

                if (TextUtils.isEmpty(biayalistrik1)){
                    textBiayaListrikEdit.setError("Tolong isi Biaya Listrik");
                    return;
                }

                if (TextUtils.isEmpty(biayastiker1)){
                    textBiayaStikerEdit.setError("Tolong isi Biaya Stiker");
                    return;
                }

                if (TextUtils.isEmpty(biayatoko1)){
                    textBiayaTokoEdit.setError("Tolong isi Biaya Toko");
                    return;
                }

                if (TextUtils.isEmpty(biayawifi1)){
                    textBiayaWifiEdit.setError("Tolong isi Biaya Wifi");
                    return;
                }
                if (TextUtils.isEmpty(biayalainnya1)){
                    textBiayaLainnyaEdit.setError("Tolong isi Biaya Lainnya");
                    return;
                }

                DatabaseReference Dataref = FirebaseDatabase.getInstance().getReference().child("pengeluaran");
                Dataref.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Date tanggal = new Date();
                        SimpleDateFormat format = new SimpleDateFormat("dd MM yyyy");
                        SimpleDateFormat format1 = new SimpleDateFormat("MM yyyy");
                        String finalTanggal = format.format(tanggal);
                        String finalTanggal1 = format1.format(tanggal);

                        String biayatotal = String.valueOf(Integer.valueOf(biayalistrik1)+Integer.valueOf(biayastiker1)
                                +Integer.valueOf(biayatoko1)+Integer.valueOf(biayawifi1)+Integer.valueOf(biayalainnya1));

                        dataSnapshot.getRef().child("biayalistrik").setValue(biayalistrik1);
                        dataSnapshot.getRef().child("biayastiker").setValue(biayastiker1);
                        dataSnapshot.getRef().child("biayatoko").setValue(biayatoko1);
                        dataSnapshot.getRef().child("biayawifi").setValue(biayawifi1);
                        dataSnapshot.getRef().child("biayalainnya").setValue(biayalainnya1);
                        dataSnapshot.getRef().child("tanggal").setValue(finalTanggal);
                        dataSnapshot.getRef().child("bulantahun").setValue(finalTanggal1);
                        dataSnapshot.getRef().child("totalbiaya").setValue(biayatotal);

                        Intent intent=new Intent(PengeluaranLainnyaEdit.this,PengeluaranLainnyaHalaman.class);
                        String nama = lAdminPengeluaranEdit.getText().toString();
                        intent.putExtra("lnama", nama);
                        startActivity(intent);
                        Toast.makeText(PengeluaranLainnyaEdit.this, " Data Diedit " , Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("pengeluaran").child(key);
        fotoBersihkanPengeluaranEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressBar bar = findViewById(R.id.barPengeluaranEdit);
                bar.setVisibility(View.VISIBLE);
                ref.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Intent intent=new Intent(PengeluaranLainnyaEdit.this,PengeluaranLainnyaHalaman.class);
                        String nama = lAdminPengeluaranEdit.getText().toString();
                        intent.putExtra("lnama", nama);
                        startActivity(intent);
                        Toast.makeText(PengeluaranLainnyaEdit.this, " Data Berhasil Dihapus " , Toast.LENGTH_SHORT).show();


                    }
                });


            }
        });
    }


    public void kembali(View view){
        Intent intent=new Intent(PengeluaranLainnyaEdit.this,PengeluaranLainnyaHalaman.class);
        String nama = lAdminPengeluaranEdit.getText().toString();
        intent.putExtra("lnama", nama);
        startActivity(intent);
    }
    public void chatadmin(View view){
        Intent intent = new Intent(PengeluaranLainnyaEdit.this, ChatAdmin.class);
        String nama = lAdminPengeluaranEdit.getText().toString();
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