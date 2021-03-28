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
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
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
import java.util.Locale;
import java.util.Map;

public class KonfirmasiPembelianPrint extends AppCompatActivity {

    ImageView kPPrint2, kPKembali2;
    RecyclerView kpRecycler2;
    FirebaseRecyclerOptions<KeranjangClass> options;
    FirebaseRecyclerAdapter<KeranjangClass,KeranjangHolder> adapter;
    String key, user, uid, intent1;
    TextView lTotalBayar2,lNamaPembeli2,lAlamatPembeli2,lNoTelepon2,lNoPesan2,lTanggal2,lPukul2;

    ////
    Display mDisplay;
    String imagesUri;
    String path;
    Bitmap bitmap;

    int totalHeight;
    int totalWidth;

    public static final int READ_PHONE = 110;
    String file_name = "Nota";
    File myPath;
    ////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.konfirmasi_pembelian_print);

        lNamaPembeli2 = findViewById(R.id.lNamaPembeli2);
        lAlamatPembeli2 = findViewById(R.id.lAlamatPembeli2);
        lNoTelepon2 = findViewById(R.id.lNoTelepon2);
        lNoPesan2 = findViewById(R.id.lNoPesan2);
        lTanggal2 = findViewById(R.id.lTanggal2);
        lPukul2 = findViewById(R.id.lPukul2);

        kPPrint2 = findViewById(R.id.kPPrint2);
        kPKembali2 = findViewById(R.id.kPKembali2);
        kpRecycler2 = findViewById(R.id.kpRecycler2);
        lTotalBayar2 = findViewById(R.id.lTotalBayar2);

        kpRecycler2.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        kpRecycler2.setHasFixedSize(true);

        if(getIntent().getExtras()!=null){

            Bundle bundle = getIntent().getExtras();
            user = bundle.getString("lnama");
            uid = bundle.getString("uid");
            key = bundle.getString("key_temp");
            if (bundle.getString("intent1") !=null)
            {
                intent1 = bundle.getString("intent1");
            }

        }

        load();
        total();

        DatabaseReference tampil= FirebaseDatabase.getInstance().getReference().child("tampilkeranjang");
        tampil.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String tanggal=snapshot.child("tanggal").getValue().toString();
                String waktu=snapshot.child("waktu").getValue().toString();

                lTanggal2.setText(tanggal);
                lPukul2.setText(waktu);

                String nama2=snapshot.child("namapembeli").getValue().toString();
                String alamat2=snapshot.child("alamatpembeli").getValue().toString();
                String notelepon2=snapshot.child("notelepon").getValue().toString();

                lNamaPembeli2.setText("Nama Pembeli : "+nama2);
                lAlamatPembeli2.setText("Alamat Pembeli : "+alamat2);
                lNoTelepon2.setText("No. Telepon Pembeli : "+notelepon2);
                lNoPesan2.setText("No. Pesananan : " + key);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



/////

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

        kPPrint2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kPPrint2.setVisibility(View.GONE);
                kPKembali2.setVisibility(View.GONE);

                takeScreenShot();

                kPPrint2.setVisibility(View.VISIBLE);
                kPKembali2.setVisibility(View.VISIBLE);
            }
        });
        ////

        if (intent1 != null)
        {

        }else
        {
            Intent intent = new Intent(KonfirmasiPembelianPrint.this, KeranjangCek.class);
            intent.putExtra("lnama", user);
            intent.putExtra("uid", uid);
            intent.putExtra("key_temp", key);
            intent1 = "2";
            intent.putExtra("intent1", intent1);
            startActivity(intent);
        }


    }

    private void total(){
        ///
        Query query = FirebaseDatabase.getInstance().getReference("konfirmasipembayaran")
                .orderByChild("kodekeranjang").equalTo(uid+"1"+key);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                TextView totalBayar = findViewById(R.id.totalBayar);
                if (dataSnapshot.exists())
                {
                    int sum = 0;
                    for(DataSnapshot ds : dataSnapshot.getChildren()){

                        Map<String,Object> map = (Map<String, Object>) ds.getValue();
                        Object hargabarang =  map.get("hargabarang");
                        Object jumlah = map.get("jumlahbarang");

                        int pValue = Integer.parseInt(String.valueOf(hargabarang));
                        int jValue = Integer.parseInt(String.valueOf(jumlah));
                        int sumi = pValue * jValue ;
                        sum += sumi;
                        Log.d("Sum",String.valueOf(sum));
                        String jumlah1 = String.valueOf(sum);

                        String setTextView ;
                        String replace = String.valueOf(sum).replaceAll("[.]","");
                        if (!replace.isEmpty())
                        {
                            setTextView = formatRupiah(Double.parseDouble(replace));


                        }else
                        {

                            setTextView = "No data";
                        }

                        lTotalBayar2.setText(setTextView);

                    }
                }else{
                    lTotalBayar2.setText("Rp. 0");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ///////
    }

    private void load() {
        DatabaseReference Dataref= FirebaseDatabase.getInstance().getReference().child("konfirmasipembayaran");

        Query query= Dataref.orderByChild("kodekeranjang").equalTo(uid+"1"+key);

        options=new FirebaseRecyclerOptions.Builder<KeranjangClass>().setQuery(query,KeranjangClass.class).build();
        adapter=new FirebaseRecyclerAdapter<KeranjangClass, KeranjangHolder> (options) {
            @Override
            protected void onBindViewHolder(@NonNull KeranjangHolder holder, final int position, @NonNull KeranjangClass model) {
                holder.kNama.setText(model.getNamabarang());
                holder.kUkuran.setText(model.getUkuranbarang());
                holder.kJumlah.setText(model.getJumlahbarang());

                ///
                String setTextView ;
                String replace = model.getHargabarang().replaceAll("[Rp. ]","");
                if (!replace.isEmpty())
                {
                    setTextView = formatRupiah(Double.parseDouble(replace));


                }else
                {

                    setTextView = "No data";
                }

                ///

                holder.kHarga.setText(setTextView);

                holder.kDelete.setVisibility(View.GONE);
            }

            @NonNull
            @Override
            public KeranjangHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.keranjang_list,parent,false);
                return new KeranjangHolder(v);
            }
        };
        adapter.startListening();
        kpRecycler2.setAdapter(adapter);

    }


    public void kembali(View view){
        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        String uid = fAuth.getCurrentUser().getUid();
        DatabaseReference Ref = FirebaseDatabase.getInstance().getReference().child("user");
        Ref.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (Integer.valueOf(snapshot.child("tanggal").getValue().toString()) == 1){
                    Intent intent=new Intent(KonfirmasiPembelianPrint.this,MenuAdmin.class);
                    intent.putExtra("lnama", user);
                    startActivity(intent);
                }else {
                    Intent intent=new Intent(KonfirmasiPembelianPrint.this,MainActivity.class);
                    intent.putExtra("lnama", user);
                    startActivity(intent);
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
