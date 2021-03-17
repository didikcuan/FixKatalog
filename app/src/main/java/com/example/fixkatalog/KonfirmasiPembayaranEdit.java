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
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class KonfirmasiPembayaranEdit extends AppCompatActivity {

    private static final int REQUEST_CODE_IMAGE =101 ;
    ImageView fotoPembayaran,fotoPembayaranSimpan,fotoPembayaranBersihkan,fotoPrintNota;
    EditText textNama,textAlamat,textNotelepon;
    TextView lUserPembayaranEdit,prosesPembayaran,textJumlahBayar,textNamaBarang,textUkuran,textTotalBarangPesan;

    String ImageUrl;

    Uri imageUri;
    boolean isImageAdded=false;

    DatabaseReference Dataref, ref;
    StorageReference  StorageRef,RefStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.konfirmasi_pembayaran_edit);

        fotoPembayaran=findViewById(R.id.fotoPembayaran);
        fotoPembayaranSimpan=findViewById(R.id.fotoPembayaranSimpan);
        fotoPembayaranBersihkan=findViewById(R.id.fotoPembayaranBersihkan);
        fotoPrintNota=findViewById(R.id.fotoPrintNota);



        textNama=findViewById(R.id.textNama);
        textAlamat=findViewById(R.id.textAlamat);
        textNotelepon=findViewById(R.id.textNotelepon);

        lUserPembayaranEdit=findViewById(R.id.lUserPembayaranEdit);
        prosesPembayaran=findViewById(R.id.prosesPembayaran);
        textJumlahBayar=findViewById(R.id.textJumlahBayar);
        textNamaBarang=findViewById(R.id.textNamaBarang);
        textUkuran=findViewById(R.id.textUkuran);
        textTotalBarangPesan=findViewById(R.id.textTotalBarangPesan);

        String key=getIntent().getStringExtra("uid");

        Dataref= FirebaseDatabase.getInstance().getReference().child("konfirmasipembayaran");

        ref= FirebaseDatabase.getInstance().getReference().child("konfirmasipembayaran").child(key);

        StorageRef= FirebaseStorage.getInstance().getReference().child("konfirmasipembayaran");

        RefStorage=FirebaseStorage.getInstance().getReference().child("konfirmasipembayaran").child(key+".jpg");


        if(getIntent().getExtras()!=null){

            Bundle bundle = getIntent().getExtras();
            lUserPembayaranEdit.setText(bundle.getString("lnama"));

        }else{

            lUserPembayaranEdit.setText("Nama Tidak Tersedia");

        }


        Dataref.child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {

                    String ImageUrl=dataSnapshot.child("ImageUrl").getValue().toString();
                    String alamatpembeli=dataSnapshot.child("alamatpembeli").getValue().toString();
                    String fotobarang=dataSnapshot.child("ImageUrlBarang").getValue().toString();
                    String jumlahbarang=dataSnapshot.child("jumlahbarang").getValue().toString();
                    String konfirmasi=dataSnapshot.child("konfirmasi").getValue().toString();
                    String jumlahbayar=dataSnapshot.child("jumlahbayar").getValue().toString();
                    String namabarang=dataSnapshot.child("namabarang").getValue().toString();
                    String namapembeli=dataSnapshot.child("namapembeli").getValue().toString();
                    String teleponpembeli=dataSnapshot.child("teleponpembeli").getValue().toString();
                    String uid=dataSnapshot.child("uid").getValue().toString();
                    String ukuranbarang=dataSnapshot.child("ukuranbarang").getValue().toString();
                    String hargabarang=dataSnapshot.child("hargabarang").getValue().toString();


                    if (Integer.valueOf(konfirmasi) == 2)
                    {
                        fotoPembayaranSimpan.setEnabled(true);
                        TextView pengganti = findViewById(R.id.pengganti);
                        pengganti.setText("Keterangan");
                    }


                    if (dataSnapshot.child("noresi").exists())
                    {
                        String noresi=dataSnapshot.child("noresi").getValue().toString();
                        TextView noResi = findViewById(R.id.noResi);
                        noResi.setText(noresi);
                    }


                    Picasso.get().load(ImageUrl).into(fotoPembayaran);
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
                    textJumlahBayar.setText(setTextView);
                    textNamaBarang.setText(namabarang);
                    textUkuran.setText(ukuranbarang);
                    textTotalBarangPesan.setText(jumlahbarang);
                    textNama.setText(namapembeli);
                    textAlamat.setText(alamatpembeli);
                    textNotelepon.setText(teleponpembeli);

                    TextView status = findViewById(R.id.status);


                    Integer l = Integer.valueOf(konfirmasi) ;

                    if (l == 0)
                    {
                        status.setText("Belum Dikonfirmasi");

                    }

                    if (l == 1)
                    {
                        status.setText("Sudah Dikonfirmasi");

                    }

                    if (l == 2)
                    {
                        status.setText("Pembayaran Ditolak");

                    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
                    fotoPrintNota.setOnClickListener(new View.OnClickListener() {
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
                                canvas.drawText("Harga", 700, 510, paint);
                                canvas.drawText("Jumlah", 900, 510, paint);
                                canvas.drawText("Total", 1050, 510, paint);

                                canvas.drawLine(180, 470, 180, 520, paint);
                                canvas.drawLine(680, 470, 680, 520, paint);
                                canvas.drawLine(880, 470, 880, 520, paint);
                                canvas.drawLine(1030, 470, 1030, 520, paint);

                                canvas.drawText(ukuranbarang, 40, 670, paint);
                                canvas.drawText(namabarang, 200, 670, paint);
                                canvas.drawText(hargabarang, 700, 670, paint);
                                canvas.drawText(jumlahbarang, 900, 670, paint);

                                paint.setTextAlign(Paint.Align.RIGHT);
                                canvas.drawText(jumlahbayar, pageWidth - 40, 670, paint);
                                paint.setTextAlign(Paint.Align.LEFT);


                                canvas.drawLine(400, 820, pageWidth - 20, 820, paint);

                                paint.setColor(Color.rgb(247, 147, 30));
                                canvas.drawRect(680, 870, pageWidth - 20, 970, paint);

                                paint.setColor(Color.BLACK);
                                paint.setTextSize(50f);
                                paint.setTextAlign(Paint.Align.LEFT);
                                canvas.drawText("Total", 700, 935, paint);
                                paint.setTextAlign(Paint.Align.RIGHT);
                                canvas.drawText("Rp. " + jumlahbayar, pageWidth - 40, 935, paint);

                                paint.setColor(Color.BLACK);
                                paint.setTextSize(50f);
                                paint.setTextAlign(Paint.Align.RIGHT);
                                canvas.drawText("Sudah Dibayar", pageWidth - 40, 1040, paint);

                                pdfDocument.finishPage(page);

                                File file = new File(Environment.getExternalStorageDirectory(), "/Pesanan" + kodekeluar + ".pdf");
                                try {
                                    pdfDocument.writeTo(new FileOutputStream(file));
                                    Toast.makeText(KonfirmasiPembayaranEdit.this, "nota sudah dibuat", Toast.LENGTH_LONG).show();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                pdfDocument.close();
                            }
                            if (l == 0)
                            {
                                Toast.makeText(KonfirmasiPembayaranEdit.this, "Tidak Bisa Cetak, Tunggu Pembayaran Dikonfirmasi Dulu", Toast.LENGTH_LONG).show();
                            }
                            if (l == 2)
                            {
                                Toast.makeText(KonfirmasiPembayaranEdit.this, "Tidak Bisa Cetak, Karena Pembayaran Ditolak", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
/////////////////////////////////////////////////////////////////////////////////////////////////////////////



                    fotoPembayaranSimpan.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Integer b = Integer.valueOf(konfirmasi) ;


                            if (b == 0)
                            {
                                String namapembeli   = textNama.getText().toString().trim();
                                String alamatpembeli   = textAlamat.getText().toString().trim();
                                String notelepon  = textNotelepon.getText().toString().trim();

                                if (TextUtils.isEmpty(namapembeli)){
                                    textNama.setError("Tolong isi Nama Anda");
                                    return;
                                }

                                if (TextUtils.isEmpty(alamatpembeli)){
                                    textAlamat.setError("Tolong isi Alamat Anda");
                                    return;
                                }


                                if (TextUtils.isEmpty(notelepon)){
                                    textNotelepon.setError("Tolong isi No Telepon Anda");
                                    return;
                                }


                                hapus(ImageUrl, key);

                            }

                            if (b == 1)
                            {
                                Toast.makeText(KonfirmasiPembayaranEdit.this, "Tidak Bisa Edit Data, Data Sudah Dikonfirmasi", Toast.LENGTH_LONG).show();


                            }

                            else if (b == 2)
                            {
                                Toast.makeText(KonfirmasiPembayaranEdit.this, "Tidak Bisa Edit Data, Data Sudah Ditolak", Toast.LENGTH_LONG).show();

                            }



                        }
                    });


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        fotoPembayaran.setOnClickListener(new View.OnClickListener() {
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
                fotoPembayaran.setImageURI(imageUri);

        }
    }
    public void kembali(View view){
        Intent intent=new Intent(KonfirmasiPembayaranEdit.this,KonfirmasiPembayaranHalaman.class);
        String nama = lUserPembayaranEdit.getText().toString();
        intent.putExtra("lnama", nama);
        startActivity(intent);
    }

    public void hapus(String ImageUrl, String key){

        prosesPembayaran.setVisibility(View.VISIBLE);

        String namapembeli   = textNama.getText().toString().trim();
        String alamatpembeli   = textAlamat.getText().toString().trim();
        String teleponpembeli  = textNotelepon.getText().toString().trim();

        Dataref.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().child("namapembeli").setValue(namapembeli);
                dataSnapshot.getRef().child("alamatpembeli").setValue(alamatpembeli);
                dataSnapshot.getRef().child("teleponpembeli").setValue(teleponpembeli);

                if (isImageAdded != true) {

                    dataSnapshot.getRef().child("ImageUrl").setValue(ImageUrl);

                    Toast.makeText(KonfirmasiPembayaranEdit.this, " Data Berhasil Di Edit ", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(KonfirmasiPembayaranEdit.this,KonfirmasiPembayaranHalaman.class);
                    String nama = lUserPembayaranEdit.getText().toString();
                    intent.putExtra("lnama", nama);
                    startActivity(intent);


                }else {

                    StorageRef.child(key + ".jpg").putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            StorageRef.child(key + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
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
                            prosesPembayaran.setText(progress + " %");
                            if (progress == 100) {
                                Toast.makeText(KonfirmasiPembayaranEdit.this, " Data Berhasil Di Edit ", Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(KonfirmasiPembayaranEdit.this,KonfirmasiPembayaranHalaman.class);
                                String nama = lUserPembayaranEdit.getText().toString();
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
    public void chat(View view){
        Intent intent = new Intent(KonfirmasiPembayaranEdit.this, ChatUser.class);
        String nama = lUserPembayaranEdit.getText().toString();
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
