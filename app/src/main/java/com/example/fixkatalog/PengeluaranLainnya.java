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

import com.google.firebase.auth.FirebaseAuth;
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

public class PengeluaranLainnya extends AppCompatActivity {

    TextView lAdminPengeluaran;
    EditText textBiayaListrik, textBiayaStiker, textBiayaToko, textBiayaWifi, textBiayaLainnya;
    ImageView fotoSimpanPengeluaran, fotoBersihkanPengeluaran;


    DatabaseReference Dataref,Refkodepengeluaran;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pengeluaran_lainnya);

        lAdminPengeluaran=findViewById(R.id.lAdminPengeluaran);
        textBiayaListrik=findViewById(R.id.textBiayaListrik);
        textBiayaStiker=findViewById(R.id.textBiayaStiker);
        textBiayaToko=findViewById(R.id.textBiayaToko);
        textBiayaWifi=findViewById(R.id.textBiayaWifi);
        textBiayaLainnya=findViewById(R.id.textBiayaLainnya);
        fotoSimpanPengeluaran=findViewById(R.id.fotoSimpanPengeluaran);
        fotoBersihkanPengeluaran=findViewById(R.id.fotoBersihkanPengeluaran);

        Dataref= FirebaseDatabase.getInstance().getReference().child("pengeluaran");

        Refkodepengeluaran= FirebaseDatabase.getInstance().getReference().child("kodepengeluaran");
        ///
        textBiayaListrik.addTextChangedListener(new TextWatcher() {
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


                    TextView bLi = findViewById(R.id.bLi);
                    bLi.setText(setTextView);


                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ///

        ///
        textBiayaStiker.addTextChangedListener(new TextWatcher() {
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


                    TextView bSt = findViewById(R.id.bSt);
                    bSt.setText(setTextView);


                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ///

        ///
        textBiayaToko.addTextChangedListener(new TextWatcher() {
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


                    TextView bTo = findViewById(R.id.bTo);
                    bTo.setText(setTextView);


                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ///

        ///
        textBiayaWifi.addTextChangedListener(new TextWatcher() {
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


                    TextView bWi = findViewById(R.id.bWi);
                    bWi.setText(setTextView);


                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ///

        ///
        textBiayaLainnya.addTextChangedListener(new TextWatcher() {
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


                    TextView bLa = findViewById(R.id.bLa);
                    bLa.setText(setTextView);


                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ///


        if(getIntent().getExtras()!=null){

            Bundle bundle = getIntent().getExtras();
            lAdminPengeluaran.setText(bundle.getString("lnama"));

        }else{

            lAdminPengeluaran.setText("Nama Tidak Tersedia");

        }



        fotoBersihkanPengeluaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                textBiayaListrik.setText(new String(""));
                textBiayaStiker.setText(new String(""));
                textBiayaToko.setText(new String(""));
                textBiayaWifi.setText(new String(""));
                textBiayaLainnya.setText(new String(""));

            }
        });


        fotoSimpanPengeluaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String listrik = textBiayaListrik.getText().toString().trim();
                String stiker = textBiayaStiker.getText().toString().trim();
                String toko = textBiayaToko.getText().toString().trim();
                String wifi = textBiayaWifi.getText().toString().trim();
                String lainnya = textBiayaLainnya.getText().toString().trim();

                if (TextUtils.isEmpty(listrik)) {
                    textBiayaListrik.setError("Tolong isi Biaya Listrik");
                    return;
                }
                if (TextUtils.isEmpty(stiker)) {
                    textBiayaStiker.setError("Tolong isi Biaya Stiker");
                    return;
                }
                if (TextUtils.isEmpty(toko)) {
                    textBiayaToko.setError("Tolong isi Biaya Toko");
                    return;
                }
                if (TextUtils.isEmpty(wifi)) {
                    textBiayaWifi.setError("Tolong isi Biaya Wifi");
                    return;
                }
                if (TextUtils.isEmpty(lainnya)) {
                    textBiayaLainnya.setError("Tolong isi Biaya Lainnya");
                    return;
                }

                ProgressBar bar = findViewById(R.id.barPengeluaranTambah);
                bar.setVisibility(View.VISIBLE);
                FirebaseAuth fAuth = FirebaseAuth.getInstance();
                DatabaseReference user= FirebaseDatabase.getInstance().getReference().child("user");
                user.child(fAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                        String uid = dataSnapshot.child("uid").getValue().toString();
                        String nama = dataSnapshot.child("nama").getValue().toString();
                        String ImageUrl = dataSnapshot.child("ImageUrl").getValue().toString();

                        Refkodepengeluaran.child("kodepengeluaran").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {

                                Integer kodepengeluaran = Integer.valueOf(dataSnapshot1.child("kodepengeluaran").getValue().toString());
                                String hasil = String.valueOf(kodepengeluaran+1);
                                dataSnapshot1.getRef().child("kodepengeluaran").setValue(hasil);

                                String biayalistrik = textBiayaListrik.getText().toString();
                                String biayastiker = textBiayaStiker.getText().toString();
                                String biayatoko = textBiayaToko.getText().toString();
                                String biayawifi = textBiayaWifi.getText().toString();
                                String biayalainnya = textBiayaLainnya.getText().toString();

                                Dataref.child("kp"+hasil).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {

                                        Date tanggal = new Date();
                                        SimpleDateFormat format = new SimpleDateFormat("dd MM yyyy");
                                        SimpleDateFormat format1 = new SimpleDateFormat("MM yyyy");
                                        String finalTanggal = format.format(tanggal);
                                        String finalTanggal1 = format1.format(tanggal);

                                        String totalbiaya = String.valueOf(Integer.valueOf(biayalistrik)+Integer.valueOf(biayastiker)
                                                +Integer.valueOf(biayatoko)+Integer.valueOf(biayawifi)+Integer.valueOf(biayalainnya));

                                        dataSnapshot2.getRef().child("kodepengeluaran").setValue(hasil);
                                        dataSnapshot2.getRef().child("biayalistrik").setValue(biayalistrik);
                                        dataSnapshot2.getRef().child("biayastiker").setValue(biayastiker);
                                        dataSnapshot2.getRef().child("biayatoko").setValue(biayatoko);
                                        dataSnapshot2.getRef().child("biayawifi").setValue(biayawifi);
                                        dataSnapshot2.getRef().child("biayalainnya").setValue(biayalainnya);
                                        dataSnapshot2.getRef().child("tanggal").setValue(finalTanggal);
                                        dataSnapshot2.getRef().child("bulantahun").setValue(finalTanggal1);
                                        dataSnapshot2.getRef().child("uid").setValue(uid);
                                        dataSnapshot2.getRef().child("totalbiaya").setValue(totalbiaya);
                                        dataSnapshot2.getRef().child("namaadmin").setValue(nama);
                                        dataSnapshot2.getRef().child("ImageUrl").setValue(ImageUrl);

                                        Intent intent=new Intent(PengeluaranLainnya.this,PengeluaranLainnyaHalaman.class);
                                        String nama = lAdminPengeluaran.getText().toString();
                                        intent.putExtra("lnama", nama);
                                        startActivity(intent);
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



            }
        });

    }
    public void kembali(View view){
        Intent intent=new Intent(PengeluaranLainnya.this,PengeluaranLainnyaHalaman.class);
        String nama = lAdminPengeluaran.getText().toString();
        intent.putExtra("lnama", nama);
        startActivity(intent);
    }
    public void chatadmin(View view){
        Intent intent = new Intent(PengeluaranLainnya.this, ChatAdmin.class);
        String nama = lAdminPengeluaran.getText().toString();
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