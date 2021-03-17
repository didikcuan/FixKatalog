package com.example.fixkatalog;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.SimpleTimeZone;

public class KonfirmasiPembelian extends AppCompatActivity {

    ImageView fotoBuktiTF,fotoKonfirmasi,fotoUserPemesan,fotoTolak,fotoPrint;
    EditText textNoResi;
    TextView lKonfirmasiPembelian,statusKonfirmasi,textBayar,textBarang,textUkuranBarang,textTotalQTY,textNamaPemesan,textAlamatPemesan,textNoteleponPemesan;

    DatabaseReference Dataref, ref, Refuser, Refbarangkeluar, Refkodekeluar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.konfirmasi_pembelian);


        fotoBuktiTF=findViewById(R.id.fotoBuktiTF);
        fotoKonfirmasi=findViewById(R.id.fotoKonfirmasi);
        fotoUserPemesan=findViewById(R.id.fotoUserPemesan);
        fotoPrint=findViewById(R.id.fotoPrint);

        textNoResi=findViewById(R.id.textNoResi);

        lKonfirmasiPembelian=findViewById(R.id.lKonfirmasiPembelian);
        statusKonfirmasi=findViewById(R.id.statusKonfirmasi);
        textBayar=findViewById(R.id.textBayar);
        textBarang=findViewById(R.id.textBarang);
        textUkuranBarang=findViewById(R.id.textUkuranBarang);
        textTotalQTY=findViewById(R.id.textTotalQTY);
        textNamaPemesan=findViewById(R.id.textNamaPemesan);
        textAlamatPemesan=findViewById(R.id.textAlamatPemesan);
        textNoteleponPemesan=findViewById(R.id.textNoteleponPemesan);
        fotoTolak=findViewById(R.id.fotoTolak);


        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);


        String key=getIntent().getStringExtra("uid");

        Dataref= FirebaseDatabase.getInstance().getReference().child("konfirmasipembayaran");

        ref= FirebaseDatabase.getInstance().getReference().child("tampil");

        Refuser= FirebaseDatabase.getInstance().getReference().child("user");

        Refbarangkeluar= FirebaseDatabase.getInstance().getReference().child("barangkeluar");

        Refkodekeluar= FirebaseDatabase.getInstance().getReference().child("kodekeluar");


        if(getIntent().getExtras()!=null){

            Bundle bundle = getIntent().getExtras();
            lKonfirmasiPembelian.setText(bundle.getString("lnama"));

        }else{

            lKonfirmasiPembelian.setText("Nama Tidak Tersedia");

        }

        FirebaseAuth fAuth = FirebaseAuth.getInstance();

        Refuser.child(fAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    String ImageUrl = dataSnapshot.child("ImageUrl").getValue().toString();
                    Picasso.get().load(ImageUrl).into(fotoUserPemesan);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        Dataref.child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {

                    String ImageUrl=dataSnapshot.child("ImageUrl").getValue().toString();
                    String jumlahbarang=dataSnapshot.child("jumlahbarang").getValue().toString();
                    String konfirmasi=dataSnapshot.child("konfirmasi").getValue().toString();
                    String jumlahbayar=dataSnapshot.child("jumlahbayar").getValue().toString();

                    String uid=dataSnapshot.child("uid").getValue().toString();
                    String alamatpembeli=dataSnapshot.child("alamatpembeli").getValue().toString();
                    String namapembeli=dataSnapshot.child("namapembeli").getValue().toString();
                    String teleponpembeli=dataSnapshot.child("teleponpembeli").getValue().toString();
                    String tanggallahir=dataSnapshot.child("tanggallahir").getValue().toString();
                    String ImageUrlPembeli=dataSnapshot.child("ImageUrlPembeli").getValue().toString();

                    String ImageUrlBarang=dataSnapshot.child("ImageUrlBarang").getValue().toString();
                    String deskripsi=dataSnapshot.child("deskripsi").getValue().toString();
                    String hargabarang=dataSnapshot.child("hargabarang").getValue().toString();
                    String jenisbarang=dataSnapshot.child("jenisbarang").getValue().toString();
                    String kodebarang=dataSnapshot.child("kodebarang").getValue().toString();
                    String namabarang=dataSnapshot.child("namabarang").getValue().toString();
                    String ukuranbarang=dataSnapshot.child("ukuranbarang").getValue().toString();

                    if (dataSnapshot.child("noresi").exists())
                    {
                        String noresi=dataSnapshot.child("noresi").getValue().toString();
                        TextView textNoResi = findViewById(R.id.textNoResi);
                        textNoResi.setText(noresi);
                    }

                    Picasso.get().load(ImageUrl).into(fotoBuktiTF);


                    ImageView fotoPrint = findViewById(R.id.fotoPrint);



                    Integer l = Integer.valueOf(konfirmasi) ;

                    if (l == 0)
                    {

                        statusKonfirmasi.setText("Belum Dikonfirmasi");
                    }

                    if (l == 1)
                    {


                        statusKonfirmasi.setText("Sudah Dikonfirmasi");
                    }

                    if (l == 2)
                    {


                        statusKonfirmasi.setText("Pembayaran Ditolak");
                    }


/////////////////////////////////////////////////////////////////////////////////////////////////////////////
                    fotoPrint.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (l == 1) {

                                String kodekeluar = dataSnapshot.child("kodekeluar").getValue().toString();
///

                                Bitmap bitmap, scaleBitmap;
                                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.cogan);
                                scaleBitmap = Bitmap.createScaledBitmap(bitmap, 300, 300, false);

                                PdfDocument pdfDocument = new PdfDocument();
                                Paint paint = new Paint();
                                Paint titlePaint = new Paint();

                                PdfDocument.PageInfo pageInfo
                                        = new PdfDocument.PageInfo.Builder(1200, 2010, 1).create();
                                PdfDocument.Page page = pdfDocument.startPage(pageInfo);

                                Canvas canvas = page.getCanvas();


                                paint.setColor(Color.BLACK);
                                paint.setTextSize(30f);
                                paint.setTextAlign(Paint.Align.RIGHT);

                                Date tanggal = new Date();
                                SimpleDateFormat format = new SimpleDateFormat("dd MMMM yyyy");
                                String finalTanggal = format.format(tanggal);

                                SimpleDateFormat format1 = new SimpleDateFormat("HH:mm:ss");
                                String finalTime = format1.format(tanggal);

                                canvas.drawText("No. Pesanan: " + kodekeluar, 1160, 40, paint);
                                canvas.drawText("Sosial Media : Ig _Coganstore_", 1160, 80, paint);
                                canvas.drawText("Tanggal: " + finalTanggal, 1160, 120, paint);
                                canvas.drawText("Pukul: " + finalTime, 1160, 160, paint);


                                titlePaint.setTextAlign(Paint.Align.LEFT);
                                canvas.drawBitmap(scaleBitmap, 0, 0, paint);


                                paint.setTextAlign(Paint.Align.LEFT);
                                paint.setColor(Color.BLACK);
                                paint.setTextSize(35f);
                                canvas.drawText("Nama Pemesan: " + namapembeli, 20, 360, paint);
                                canvas.drawText("Nomor Tlp: " + teleponpembeli, 20, 400, paint);

                                int pageWidth = 1200;

                                paint.setStyle(Paint.Style.STROKE);
                                paint.setStrokeWidth(2);
                                canvas.drawRect(20, 460, pageWidth - 20, 540, paint);

                                paint.setTextAlign(Paint.Align.LEFT);
                                paint.setStyle(Paint.Style.FILL);
                                canvas.drawText("No.", 40, 510, paint);
                                canvas.drawText("Nama Barang", 200, 510, paint);
                                canvas.drawText("Harga", 640, 510, paint);
                                canvas.drawText("Ukuran", 900, 510, paint);
                                canvas.drawText("Jumlah", 1050, 510, paint);

                                canvas.drawLine(180, 470, 180, 520, paint);
                                canvas.drawLine(620, 470, 620, 520, paint);
                                canvas.drawLine(890, 470, 890, 520, paint);
                                canvas.drawLine(1030, 470, 1030, 520, paint);

                                canvas.drawText("1", 40, 670, paint);
                                canvas.drawText(namabarang, 200, 670, paint);
                                ///
                                String setTextView ;
                                String replace = hargabarang.replaceAll("[Rp. ]","");
                                if (!replace.isEmpty())
                                {
                                    setTextView = formatRupiah(Double.parseDouble(replace));


                                }else
                                {

                                    setTextView = "No data";
                                }

                                ///
                                canvas.drawText(setTextView, 640, 670, paint);
                                canvas.drawText(ukuranbarang, 900, 670, paint);

                                paint.setTextAlign(Paint.Align.RIGHT);
                                canvas.drawText(jumlahbarang, pageWidth - 40, 670, paint);
                                paint.setTextAlign(Paint.Align.LEFT);


                                canvas.drawLine(400, 820, pageWidth - 20, 820, paint);

                                paint.setColor(Color.rgb(247, 147, 30));
                                canvas.drawRect(680, 870, pageWidth - 20, 970, paint);

                                paint.setColor(Color.BLACK);
                                paint.setTextSize(50f);
                                paint.setTextAlign(Paint.Align.LEFT);
                                canvas.drawText("Total", 700, 935, paint);
                                paint.setTextAlign(Paint.Align.RIGHT);
                                ///
                                String setTextView1 ;
                                String replace1 = jumlahbayar.replaceAll("[Rp. ]","");
                                if (!replace1.isEmpty())
                                {
                                    setTextView1 = formatRupiah(Double.parseDouble(replace1));


                                }else
                                {

                                    setTextView1 = "No data";
                                }

                                ///
                                canvas.drawText(setTextView1, pageWidth - 40, 935, paint);

                                paint.setColor(Color.BLACK);
                                paint.setTextSize(50f);
                                paint.setTextAlign(Paint.Align.RIGHT);
                                canvas.drawText("Sudah Dibayar", pageWidth - 40, 1040, paint);

                                pdfDocument.finishPage(page);

                                File file = new File(Environment.getExternalStorageDirectory(), "/Pesanan" + kodekeluar + ".pdf");
                                try {
                                    pdfDocument.writeTo(new FileOutputStream(file));
                                    Toast.makeText(KonfirmasiPembelian.this, "nota sudah dibuat", Toast.LENGTH_LONG).show();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                pdfDocument.close();
                            }
                            if (l == 0)
                            {
                                Toast.makeText(KonfirmasiPembelian.this, "Tidak Bisa Cetak, Konfirmasi Dulu Pembayaran User", Toast.LENGTH_LONG).show();
                            }
                            if (l == 2)
                            {
                                Toast.makeText(KonfirmasiPembelian.this, "Tidak Bisa Cetak, Pembayaran User Sudah Ditolak", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
/////////////////////////////////////////////////////////////////////////////////////////////////////////////


                    fotoTolak.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String noresi   = textNoResi.getText().toString().trim();

                            if (TextUtils.isEmpty(noresi)) {
                                textNoResi.setError("Tolong isi No. Resi/Ket. Tolak Sebelum Menolak Konfirmasi");
                                return;
                            }

                            if (l == 0)
                            {
                                Date tanggal = new Date();
                                SimpleDateFormat format = new SimpleDateFormat("dd MM yyyy");
                                SimpleDateFormat format1 = new SimpleDateFormat("MM yyyy");
                                String finalTanggal = format.format(tanggal);
                                String finalTanggal1 = format1.format(tanggal);

                                dataSnapshot.getRef().child("tanggalditolak").setValue(finalTanggal);
                                dataSnapshot.getRef().child("bulantahun").setValue(finalTanggal1);
                                dataSnapshot.getRef().child("konfirmasi").setValue("2");
                                dataSnapshot.getRef().child("noresi").setValue(textNoResi.getText().toString());

                                DatabaseReference Reftampil = FirebaseDatabase.getInstance().getReference().child("tampil");
                                Reftampil.child("1"+kodebarang+ukuranbarang).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {

                                        Integer itu= Integer.valueOf(dataSnapshot1.child("jumlahbarang").getValue().toString());
                                        String hasil = String.valueOf(itu+Integer.valueOf(jumlahbarang));
                                        dataSnapshot1.getRef().child("jumlahbarang").setValue(hasil);
                                        Toast.makeText(KonfirmasiPembelian.this, " Penolakan Berhasil Dilakukan " , Toast.LENGTH_SHORT).show();

                                        Intent intent=new Intent(KonfirmasiPembelian.this,KonfirmasiPembelianHalaman.class);
                                        String nama = lKonfirmasiPembelian.getText().toString();
                                        intent.putExtra("lnama", nama);
                                        startActivity(intent);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }

                            if (l == 1)
                            {

                                Toast.makeText(KonfirmasiPembelian.this, " Tolak Tidak Bisa Dilakukan, Status Sudah Dikonfirmasi " , Toast.LENGTH_SHORT).show();

                            }

                            if (l == 2)
                            {

                                Toast.makeText(KonfirmasiPembelian.this, " Tolak Tidak Bisa Dilakukan, Status Sudah Ditolak " , Toast.LENGTH_SHORT).show();

                            }




                        }
                    });
///
                    String setTextView2 ;
                    String replace = jumlahbayar.replaceAll("[Rp. ]","");
                    if (!replace.isEmpty())
                    {
                        setTextView2= formatRupiah(Double.parseDouble(replace));


                    }else
                    {

                        setTextView2 = "No data";
                    }

                    ///
                    textBayar.setText(setTextView2);
                    textBarang.setText(namabarang);
                    textUkuranBarang.setText(ukuranbarang);
                    textTotalQTY.setText(jumlahbarang);
                    textNamaPemesan.setText(namapembeli);
                    textAlamatPemesan.setText(alamatpembeli);
                    textNoteleponPemesan.setText(teleponpembeli);


                    fotoKonfirmasi.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            if (l == 0)
                            {
                                String noresi   = textNoResi.getText().toString().trim();

                                if (TextUtils.isEmpty(noresi)){
                                    textNoResi.setError("Tolong isi No. Resi/Ket. Tolak Sebelum Mengkonfirmasi");
                                    return;
                                }

                                ///

                                String noresi1   = textNoResi.getText().toString().trim();

                                Dataref.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        dataSnapshot.getRef().child("konfirmasi").setValue("1");
                                        dataSnapshot.getRef().child("noresi").setValue(noresi1);
                                        statusKonfirmasi.setText("sudah");
                                        fotoKonfirmasi.setEnabled(false);
                                        fotoPrint.setEnabled(true);
                                        Toast.makeText(KonfirmasiPembelian.this, " Konfirmasi Berhasil Dilakukan " , Toast.LENGTH_SHORT).show();

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });


                                ///

                                Refkodekeluar.child("kodekeluar").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {

                                        Integer kodekeluar=Integer.valueOf(dataSnapshot2.child("kodekeluar").getValue().toString());
                                        String hasil = String.valueOf(kodekeluar+1);
                                        dataSnapshot2.getRef().child("kodekeluar").setValue(hasil);
                                        dataSnapshot.getRef().child("kodekeluar").setValue(hasil);
                                        String noresi1   = textNoResi.getText().toString().trim();

                                        Refbarangkeluar.child("1"+"bk"+hasil).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot3) {
                                                Date tanggal = new Date();
                                                SimpleDateFormat format = new SimpleDateFormat("dd MM yyyy");
                                                SimpleDateFormat format1 = new SimpleDateFormat("MM yyyy");
                                                String finaltanggal = format.format(tanggal);
                                                String finaltanggal1 = format1.format(tanggal);
                                                dataSnapshot3.getRef().child("kodebarang").setValue(kodebarang);
                                                dataSnapshot3.getRef().child("jenisbarang").setValue(jenisbarang);
                                                dataSnapshot3.getRef().child("ImageUrlBarang").setValue(ImageUrlBarang);
                                                dataSnapshot3.getRef().child("namabarang").setValue(namabarang);
                                                dataSnapshot3.getRef().child("deskripsi").setValue(deskripsi);
                                                dataSnapshot3.getRef().child("hargabarang").setValue(hargabarang);

                                                dataSnapshot3.getRef().child("uid").setValue(uid);
                                                dataSnapshot3.getRef().child("alamatpembeli").setValue(alamatpembeli);
                                                dataSnapshot3.getRef().child("teleponpembeli").setValue(teleponpembeli);
                                                dataSnapshot3.getRef().child("namapembeli").setValue(namapembeli);
                                                dataSnapshot3.getRef().child("tanggallahir").setValue(tanggallahir);
                                                dataSnapshot3.getRef().child("ImageUrlPembeli").setValue(ImageUrlPembeli);

                                                dataSnapshot3.getRef().child("bulantahun").setValue(finaltanggal1);
                                                dataSnapshot3.getRef().child("tanggalkeluar").setValue(finaltanggal);
                                                dataSnapshot3.getRef().child("noresi1").setValue(noresi1);
                                                dataSnapshot3.getRef().child("jumlahbarang").setValue(jumlahbarang);
                                                dataSnapshot3.getRef().child("ukuranbarang").setValue(ukuranbarang);
                                                dataSnapshot3.getRef().child("ImageUrlBuktitf").setValue(ImageUrl);
                                                dataSnapshot3.getRef().child("jumlahbayar").setValue(jumlahbayar);
                                                dataSnapshot3.getRef().child("kodebarangkeluar").setValue("bk"+hasil);

                                                //////////////////////////////////
                                                DatabaseReference Reftampil = FirebaseDatabase.getInstance().getReference().child("tampil");
                                                Reftampil.child("1"+kodebarang+ukuranbarang).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                                                        String hargabarangmasuk=dataSnapshot1.child("hargabarangmasuk").getValue().toString();
                                                        dataSnapshot3.getRef().child("hargabarangmasuk").setValue(hargabarangmasuk);
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });
                                                ///////////////////////////////////



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

                            if (l == 1)
                            {

                                Toast.makeText(KonfirmasiPembelian.this, " Konfirmasi Tidak Bisa Dilakukan, Status Sudah Dikonfirmasi " , Toast.LENGTH_SHORT).show();
                            }

                            if (l == 2)
                            {

                                Toast.makeText(KonfirmasiPembelian.this, " Konfirmasi Tidak Bisa Dilakukan, Status Sudah Ditolak " , Toast.LENGTH_SHORT).show();

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

    public void kembali(View view){
        Intent intent=new Intent(KonfirmasiPembelian.this,KonfirmasiPembelianHalaman.class);
        String nama = lKonfirmasiPembelian.getText().toString();
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
