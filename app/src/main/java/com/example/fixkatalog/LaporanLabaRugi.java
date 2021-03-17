package com.example.fixkatalog;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class LaporanLabaRugi extends AppCompatActivity {

    TextView bulanTahunLR,omsetPenjualan,totalHargaPokok,biayaLLR,biayaSLR,biayaTLR,biayaWLR,biayaLARL,JumlahBO,labaKotor,labaBersih;

    DatabaseReference Dataref;

    ////
    Display mDisplay;
    String imagesUri;
    String path;
    Bitmap bitmap;

    int totalHeight;
    int totalWidth;

    public static final int READ_PHONE = 110;
    String file_name = "Screenshot";
    File myPath;
    Button btnPrintLR;
    ////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lap_laba_rugi);

/////
        btnPrintLR = findViewById(R.id.btnPrintLR);

        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        mDisplay = wm.getDefaultDisplay();

        if(Build.VERSION.SDK_INT >= 23){
            if(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED){
            }else{
                requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE}, READ_PHONE);
            }
        }

        btnPrintLR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnPrintLR.setVisibility(View.GONE);

                takeScreenShot();

                btnPrintLR.setVisibility(View.VISIBLE);
            }
        });
        ////

        Dataref= FirebaseDatabase.getInstance().getReference().child("pengeluaran");
        bulanTahunLR=findViewById(R.id.bulanTahunLR);
        omsetPenjualan=findViewById(R.id.omsetPenjualan);
        totalHargaPokok=findViewById(R.id.totalHargaPokok);
        biayaLLR=findViewById(R.id.biayaLLR);
        biayaSLR=findViewById(R.id.biayaSLR);
        biayaTLR=findViewById(R.id.biayaTLR);
        biayaWLR=findViewById(R.id.biayaWLR);
        biayaLARL=findViewById(R.id.biayaLARL);
        JumlahBO=findViewById(R.id.JumlahBO);
        labaKotor=findViewById(R.id.labaKotor);
        labaBersih=findViewById(R.id.labaBersih);
        if(getIntent().getExtras()!=null){

            Bundle bundle = getIntent().getExtras();
            bulanTahunLR.setText(bundle.getString("bulanTahun"));

        }
        String tanggal = bulanTahunLR.getText().toString();
        Query query = FirebaseDatabase.getInstance().getReference("barangkeluar")
                .orderByChild("bulantahun")
                .startAt(tanggal).endAt(tanggal+"\uf8ff");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){

                }else
                {
                    Bundle bundle = getIntent().getExtras();
                    String nama = (bundle.getString("lnama"));

                    Toast.makeText(LaporanLabaRugi.this, " Data Tidak Ada, Benarkan Bulan dan Tahun Cetak " , Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LaporanLabaRugi.this, MenuAdmin.class);
                    intent.putExtra("lnama", nama);
                    startActivity(intent);
                }
                int sum = 0;
                int sum1 = 0;
                for(DataSnapshot ds : dataSnapshot.getChildren()){

                    Map<String,Object> map = (Map<String, Object>) ds.getValue();
                    Object hargabarang =  map.get("hargabarang");
                    Object jumlah = map.get("jumlahbarang");
                    Object hargabarangmasuk =  map.get("hargabarangmasuk");

                    int hValue = Integer.parseInt(String.valueOf(hargabarangmasuk));
                    int pValue = Integer.parseInt(String.valueOf(hargabarang));
                    int jValue = Integer.parseInt(String.valueOf(jumlah));
                    int sumi = pValue * jValue ;
                    int humi = hValue * jValue ;

                    ////
                    sum1 += humi;
                    Log.d("Sum1",String.valueOf(sum1));

                    String setView ;
                    String replace1 = String.valueOf(sum1).replaceAll("[.]","");
                    if (!replace1.isEmpty())
                    {
                        setView = formatRupiah(Double.parseDouble(replace1));


                    }else
                    {

                        setView = "No data";
                    }
                    ////

                    sum += sumi;
                    Log.d("Sum",String.valueOf(sum));

                    String setTextView ;
                    String replace = String.valueOf(sum).replaceAll("[.]","");
                    if (!replace.isEmpty())
                    {
                        setTextView = formatRupiah(Double.parseDouble(replace));


                    }else
                    {

                        setTextView = "No data";
                    }

                    omsetPenjualan.setText(setTextView);
                    labaKotor.setText(setTextView);
                    totalHargaPokok.setText(setView);

                    Query query1 = FirebaseDatabase.getInstance().getReference("pengeluaran")
                            .orderByChild("bulantahun")
                            .startAt(tanggal).endAt(tanggal+"\uf8ff");

                    query1.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {

                            int sum = 0;
                            for(DataSnapshot ds : dataSnapshot1.getChildren()) {

                                Map<String, Object> map = (Map<String, Object>) ds.getValue();

                                Object biayalistrik1 = map.get("biayalistrik");
                                Object biayastiker1 = map.get("biayastiker");
                                Object biayatoko1 = map.get("biayatoko");
                                Object biayawifi1 = map.get("biayawifi");
                                Object biayalainnya1 = map.get("biayalainnya");

                                String biayalistrik = String.valueOf(biayalistrik1);
                                String biayastiker = String.valueOf(biayastiker1);
                                String biayatoko = String.valueOf(biayatoko1);
                                String biayawifi = String.valueOf(biayawifi1);
                                String biayalainnya = String.valueOf(biayalainnya1);

                                String setTextView ;
                                String replace = biayalainnya.replaceAll("[.]","");
                                if (!replace.isEmpty())
                                {
                                    setTextView = formatRupiah(Double.parseDouble(replace));


                                }else
                                {

                                    setTextView = "No data";
                                }
                                biayaLARL.setText(setTextView);

                                String setTextView1 ;
                                String replace1 = biayastiker.replaceAll("[.]","");
                                if (!replace1.isEmpty())
                                {
                                    setTextView1 = formatRupiah(Double.parseDouble(replace1));


                                }else
                                {

                                    setTextView1 = "No data";
                                }

                                biayaSLR.setText(setTextView1);

                                String setTextView2 ;
                                String replace2 = biayatoko.replaceAll("[.]","");
                                if (!replace2.isEmpty())
                                {
                                    setTextView2 = formatRupiah(Double.parseDouble(replace2));


                                }else
                                {

                                    setTextView2 = "No data";
                                }

                                biayaTLR.setText(setTextView2);

                                String setTextView3 ;
                                String replace3 = biayawifi.replaceAll("[.]","");
                                if (!replace3.isEmpty())
                                {
                                    setTextView3 = formatRupiah(Double.parseDouble(replace3));


                                }else
                                {

                                    setTextView3 = "No data";
                                }

                                biayaWLR.setText(setTextView3);

                                String setTextView4 ;
                                String replace4 = biayalistrik.replaceAll("[.]","");
                                if (!replace4.isEmpty())
                                {
                                    setTextView4 = formatRupiah(Double.parseDouble(replace4));


                                }else
                                {

                                    setTextView4 = "No data";
                                }

                                biayaLLR.setText(setTextView4);

                                String setTextView5 ;
                                String replace5 = String.valueOf(Integer.valueOf(biayalainnya) + Integer.valueOf(biayastiker) +
                                        Integer.valueOf(biayatoko) + Integer.valueOf(biayawifi) + Integer.valueOf(biayalistrik))
                                        .replaceAll("[.]","");
                                if (!replace5.isEmpty())
                                {
                                    setTextView5 = formatRupiah(Double.parseDouble(replace5));


                                }else
                                {

                                    setTextView5 = "No data";
                                }

                                JumlahBO.setText(setTextView5);

                                int total = Integer.valueOf(biayalainnya) + Integer.valueOf(biayastiker) +
                                        Integer.valueOf(biayatoko) + Integer.valueOf(biayawifi) + Integer.valueOf(biayalistrik);
                                //////////////////////////////////////
                                int bersih = sumi - humi - total ;

                                String setTextView6 ;
                                String replace6 = String.valueOf(bersih).replaceAll("[Rp .]","Rp .");
                                if (!replace6.isEmpty())
                                {
                                    setTextView6 = formatRupiah(Double.parseDouble(replace6));


                                }else
                                {

                                    setTextView6 = "No data";
                                }

                                labaBersih.setText(setTextView6);


                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });






    }




    private  String formatRupiah(Double number){
        Locale localeID = new Locale("IND","ID");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(localeID);
        String formatrupiah = numberFormat.format(number);
        String[] split = formatrupiah.split(",");
        int length = split[0].length();
        return split[0].substring(0,2)+". "+split[0].substring(2,length);
    }
    ///////////////////////
    public Bitmap getBitmapFromView(View view, int totalHeight, int totalWidth){

        Bitmap returnedBitmap = Bitmap.createBitmap(totalWidth, totalHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();

        if(bgDrawable != null){
            bgDrawable.draw(canvas);
        }else{
            canvas.drawColor(Color.WHITE);
        }

        view.draw(canvas);
        return returnedBitmap;
    }

    private void takeScreenShot(){

        File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/ScreenShot/");

        if(!folder.exists()){
            boolean success = folder.mkdir();
        }

        path = folder.getAbsolutePath();
        path = path + "/" + file_name + System.currentTimeMillis() + ".pdf";

        View u = findViewById(R.id.abay);

        ScrollView z = findViewById(R.id.abay);
        totalHeight = z.getChildAt(0).getHeight();
        totalWidth = z.getChildAt(0).getWidth();

        String extr = Environment.getExternalStorageDirectory() + "/Flight Ticket/";
        File file = new File(extr);
        if(!file.exists())
            file.mkdir();
        String fileName = file_name + ".jpg";
        myPath = new File(extr, fileName);
        imagesUri = myPath.getPath();
        bitmap = getBitmapFromView(u, totalHeight, totalWidth);

        try{
            FileOutputStream fos = new FileOutputStream(myPath);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        createPdf();


    }

    private void createPdf() {

        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(bitmap.getWidth(), bitmap.getHeight(), 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();

        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#ffffff"));
        canvas.drawPaint(paint);

        Bitmap bitmap = Bitmap.createScaledBitmap(this.bitmap, this.bitmap.getWidth(), this.bitmap.getHeight(), true);

        paint.setColor(Color.BLUE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        document.finishPage(page);
        File filePath = new File(path);
        try{
            document.writeTo(new FileOutputStream(filePath));
        }catch (IOException e){
            e.printStackTrace();
            Toast.makeText(this, "Something Wrong: "+e.toString(), Toast.LENGTH_SHORT).show();
        }

        document.close();

        if (myPath.exists())
            myPath.delete();

        openPdf(path);

    }

    private void openPdf(String path) {
        File file = new File(path);
        Intent target = new Intent(Intent.ACTION_VIEW);
        target.setDataAndType(Uri.fromFile(file), "application/pdf");
        target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        Intent intent = Intent.createChooser(target, "Open FIle");
        try{
            startActivity(intent);
        }catch (ActivityNotFoundException e){
            Toast.makeText(this, "No Apps to read PDF FIle", Toast.LENGTH_SHORT).show();
        }
    }
    ////////////////
}
