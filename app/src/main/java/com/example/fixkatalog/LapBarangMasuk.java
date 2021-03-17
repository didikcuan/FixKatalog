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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
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
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;

public class LapBarangMasuk extends AppCompatActivity {


    TextView bulanTahun,totalHargaBM;

    RecyclerView recyclerLapBarangMasuk;
    FirebaseRecyclerOptions<LapBarangMasukClass> options;
    FirebaseRecyclerAdapter<LapBarangMasukClass,LapBarangMasukHolder> adapter;
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
    Button btnPrint;
    ////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lap_barang_masuk);

        /////
        btnPrint = findViewById(R.id.btn_print);

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

        btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnPrint.setVisibility(View.GONE);

                takeScreenShot();

                btnPrint.setVisibility(View.VISIBLE);
            }
        });
        ////

        Dataref= FirebaseDatabase.getInstance().getReference().child("barangmasuk");
        bulanTahun=findViewById(R.id.bulanTahun);
        totalHargaBM=findViewById(R.id.totalHargaBM);
        recyclerLapBarangMasuk=findViewById(R.id.recyclerLapBarangMasuk);

        recyclerLapBarangMasuk.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        if(getIntent().getExtras()!=null){

            Bundle bundle = getIntent().getExtras();
            bulanTahun.setText(bundle.getString("bulanTahun"));

        }

        String tanggal = bulanTahun.getText().toString();
        Query query = FirebaseDatabase.getInstance().getReference("barangmasuk")
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

                    Toast.makeText(LapBarangMasuk.this, " Data Tidak Ada, Benarkan Bulan dan Tahun Cetak " , Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LapBarangMasuk.this, MenuAdmin.class);
                    intent.putExtra("lnama", nama);
                    startActivity(intent);
                }
                int sum = 0;
                for(DataSnapshot ds : dataSnapshot.getChildren()){

                    Map<String,Object> map = (Map<String, Object>) ds.getValue();
                    Object hargabarangmasuk =  map.get("hargabarangmasuk");
                    Object jumlah = map.get("jumlahbarang");

                    int pValue = Integer.parseInt(String.valueOf(hargabarangmasuk));
                    int jValue = Integer.parseInt(String.valueOf(jumlah));
                    int sumi = pValue * jValue ;
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

                    totalHargaBM.setText(setTextView);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





        LoadData();



    }

    private void LoadData() {
        String tanggal = bulanTahun.getText().toString();
        Query query=Dataref.orderByChild("bulantahun").startAt(tanggal).endAt(tanggal+"\uf8ff");

        options=new FirebaseRecyclerOptions.Builder<LapBarangMasukClass>().setQuery(query,LapBarangMasukClass.class).build();
        adapter=new FirebaseRecyclerAdapter<LapBarangMasukClass, LapBarangMasukHolder> (options) {
            @Override
            protected void onBindViewHolder(@NonNull LapBarangMasukHolder holder, final int position, @NonNull LapBarangMasukClass model) {
                holder.kodeBM.setText(model.getKodebarangmasuk());
                holder.namaBM.setText(model.getNamabarang());
                holder.namaBM1.setText(model.getNamasupplier());
                holder.tanggalBM.setText(model.getTanggalmasuk());
                holder.qtyBM.setText(model.getJumlahbarang());

                ///
                String setTextView ;
                String replace = model.getHargabarangmasuk().replaceAll("[.]","");
                if (!replace.isEmpty())
                {
                    setTextView = formatRupiah(Double.parseDouble(replace));


                }else
                {

                    setTextView = "No data";
                }

                ///

                String setView ;
                String replace1 = String.valueOf(Integer.valueOf(model.getHargabarangmasuk())*Integer.valueOf(model.getJumlahbarang())).replaceAll("[.]","");
                if (!replace1.isEmpty())
                {
                    setView = formatRupiah(Double.parseDouble(replace1));


                }else
                {

                    setView = "No data";
                }

                ///
                holder.hargaBM.setText(setTextView);
                holder.totalBM.setText(setView);



                holder.v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {



                    }
                });

            }

            @NonNull
            @Override
            public LapBarangMasukHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.lap_barang_masuk_list,parent,false);
                return new LapBarangMasukHolder(v);
            }
        };
        adapter.startListening();
        recyclerLapBarangMasuk.setAdapter(adapter);

    }

    public void kembali(View view){
        Intent intent=new Intent(LapBarangMasuk.this,MenuAdmin.class);
        //String nama = lAdminDataBarangMasuk.getText().toString();
       // intent.putExtra("lnama", nama);
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
