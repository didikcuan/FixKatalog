package com.example.fixkatalog;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MenuAdmin extends AppCompatActivity {
    FirebaseAuth fAuth;
    DatabaseReference databaseReference;
    TextView lAdmin,textTanggal;
    EditText tanggal;
    RadioGroup listMenu;
    Button adBarangMasuk, adDataBarang,adSupplier,adKonfirmasi,adDataUser,adPembayaran,adStok,adPengeluaran,
            adLaporanBarangMasuk, adLaporanPenjualan,adLaporanStokTersedia,adLaporanStokHabis,adLaporanLabaRugi,adLaporanBarangDipesanStokHabis,adLaporanDataPelanggan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_admin);
        fAuth = FirebaseAuth.getInstance();

        lAdmin = findViewById(R.id.lAdmin);

        adLaporanBarangMasuk = findViewById(R.id.adLaporanBarangMasuk);
        adLaporanPenjualan = findViewById(R.id.adLaporanPenjualan);
        adLaporanStokTersedia = findViewById(R.id.adLaporanStokTersedia);
        adLaporanStokHabis = findViewById(R.id.adLaporanStokHabis);
        adLaporanLabaRugi = findViewById(R.id.adLaporanLabaRugi);
        adLaporanBarangDipesanStokHabis = findViewById(R.id.adLaporanBarangDipesanStokHabis);
        adLaporanDataPelanggan = findViewById(R.id.adLaporanDataPelanggan);


        adBarangMasuk = findViewById(R.id.adBarangMasuk);
        adDataBarang = findViewById(R.id.adDataBarang);
        adDataUser = findViewById(R.id.adDataUser);
        adKonfirmasi = findViewById(R.id.adKonfirmasi);
        adPembayaran = findViewById(R.id.adPembayaran);
        adStok = findViewById(R.id.adStok);
        adSupplier = findViewById(R.id.adSupplier);
        adPengeluaran = findViewById(R.id.adPengeluaran);
        textTanggal = findViewById(R.id.textTanggal);
        tanggal = findViewById(R.id.tanggal);

        adLaporanBarangMasuk.setVisibility(View.GONE);
        adLaporanPenjualan.setVisibility(View.GONE);
        adLaporanStokTersedia.setVisibility(View.GONE);
        adLaporanStokHabis.setVisibility(View.GONE);
        adLaporanLabaRugi.setVisibility(View.GONE);
        adLaporanBarangDipesanStokHabis.setVisibility(View.GONE);
        adLaporanDataPelanggan.setVisibility(View.GONE);
        textTanggal.setVisibility(View.GONE);
        tanggal.setVisibility(View.GONE);
        adBarangMasuk.setVisibility(View.GONE);
        adDataBarang.setVisibility(View.VISIBLE);
        adDataUser.setVisibility(View.VISIBLE);
        adKonfirmasi.setVisibility(View.GONE);
        adPembayaran.setVisibility(View.GONE);
        adStok.setVisibility(View.GONE);
        adSupplier.setVisibility(View.VISIBLE);
        adPengeluaran.setVisibility(View.GONE);


        if(getIntent().getExtras()!=null){

            Bundle bundle = getIntent().getExtras();
            lAdmin.setText(bundle.getString("lnama"));

        }else{

            lAdmin.setText("Nama Tidak Tersedia");

        }

        adLaporanBarangMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String kolom = tanggal.getText().toString().trim();
                if (TextUtils.isEmpty(kolom)){
                    tanggal.setError("Isi dulu bulan dan tahun");
                    Toast.makeText(MenuAdmin.this, " Isi dulu bulan dan tahun " , Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(MenuAdmin.this, LapBarangMasuk.class);
                    String bulanTahun = tanggal.getText().toString();
                    intent.putExtra("bulanTahun", bulanTahun);
                    String nama = lAdmin.getText().toString();
                    intent.putExtra("lnama", nama);
                    startActivity(intent);
                }
            }
        });
        adLaporanPenjualan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String kolom = tanggal.getText().toString().trim();
                if (TextUtils.isEmpty(kolom)){
                    tanggal.setError("Isi dulu bulan dan tahun");
                    Toast.makeText(MenuAdmin.this, " Isi dulu bulan dan tahun " , Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(MenuAdmin.this, LaporanPenjualan.class);
                    String bulanTahun = tanggal.getText().toString();
                    intent.putExtra("bulanTahun", bulanTahun);
                    String nama = lAdmin.getText().toString();
                    intent.putExtra("lnama", nama);
                    startActivity(intent);
                }

            }
        });
        adLaporanStokTersedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuAdmin.this, LaporanStokAda.class);
                startActivity(intent);
            }
        });
        adLaporanStokHabis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuAdmin.this, LaporanStokHabis.class);
                startActivity(intent);
            }
        });
        adLaporanLabaRugi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String kolom = tanggal.getText().toString().trim();

                if (TextUtils.isEmpty(kolom)){
                    tanggal.setError("Isi dulu bulan dan tahun");
                    Toast.makeText(MenuAdmin.this, " Isi dulu bulan dan tahun " , Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(MenuAdmin.this, LaporanLabaRugi.class);
                    String bulanTahun = tanggal.getText().toString();
                    intent.putExtra("bulanTahun", bulanTahun);
                    String nama = lAdmin.getText().toString();
                    intent.putExtra("lnama", nama);
                    startActivity(intent);
                }
            }
        });

        adLaporanBarangDipesanStokHabis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuAdmin.this, LapBarangDipesanHabis.class);
                String bulanTahun = tanggal.getText().toString();
                intent.putExtra("bulanTahun", bulanTahun);
                startActivity(intent);
            }
        });
        adLaporanDataPelanggan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String kolom = tanggal.getText().toString().trim();

                if (TextUtils.isEmpty(kolom)){
                    tanggal.setError("Isi dulu bulan dan tahun");
                    Toast.makeText(MenuAdmin.this, " Isi dulu bulan dan tahun " , Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(MenuAdmin.this, LapPembelianDitolak.class);
                    String bulanTahun = tanggal.getText().toString();
                    intent.putExtra("bulanTahun", bulanTahun);
                    String nama = lAdmin.getText().toString();
                    intent.putExtra("lnama", nama);
                    startActivity(intent);
                }
            }
        });

        listMenu = findViewById(R.id.listMenu);
        listMenu.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                switch (id){
                    case R.id.radioMaster:
                        adLaporanBarangMasuk.setVisibility(View.GONE);
                        adLaporanPenjualan.setVisibility(View.GONE);
                        adLaporanStokTersedia.setVisibility(View.GONE);
                        adLaporanStokHabis.setVisibility(View.GONE);
                        adLaporanLabaRugi.setVisibility(View.GONE);
                        adLaporanBarangDipesanStokHabis.setVisibility(View.GONE);
                        adLaporanDataPelanggan.setVisibility(View.GONE);
                        textTanggal.setVisibility(View.GONE);
                        tanggal.setVisibility(View.GONE);
                        adBarangMasuk.setVisibility(View.GONE);
                        adDataBarang.setVisibility(View.VISIBLE);
                        adDataUser.setVisibility(View.VISIBLE);
                        adKonfirmasi.setVisibility(View.GONE);

                        adPembayaran.setVisibility(View.GONE);
                        adStok.setVisibility(View.GONE);
                        adSupplier.setVisibility(View.VISIBLE);
                        adPengeluaran.setVisibility(View.GONE);
                        break;
                    case R.id.radioPenjualan:
                        adLaporanBarangMasuk.setVisibility(View.GONE);
                        adLaporanPenjualan.setVisibility(View.GONE);
                        adLaporanStokTersedia.setVisibility(View.GONE);
                        adLaporanStokHabis.setVisibility(View.GONE);
                        adLaporanLabaRugi.setVisibility(View.GONE);
                        adLaporanBarangDipesanStokHabis.setVisibility(View.GONE);
                        adLaporanDataPelanggan.setVisibility(View.GONE);
                        textTanggal.setVisibility(View.GONE);
                        tanggal.setVisibility(View.GONE);
                        adBarangMasuk.setVisibility(View.VISIBLE);
                        adDataBarang.setVisibility(View.GONE);
                        adDataUser.setVisibility(View.GONE);
                        adKonfirmasi.setVisibility(View.VISIBLE);

                        adPembayaran.setVisibility(View.VISIBLE);
                        adStok.setVisibility(View.VISIBLE);
                        adSupplier.setVisibility(View.GONE);
                        adPengeluaran.setVisibility(View.GONE);
                        break;
                    case R.id.radioReport:
                        adLaporanBarangMasuk.setVisibility(View.VISIBLE);
                        adLaporanPenjualan.setVisibility(View.VISIBLE);
                        adLaporanStokTersedia.setVisibility(View.VISIBLE);
                        adLaporanStokHabis.setVisibility(View.VISIBLE);
                        adLaporanLabaRugi.setVisibility(View.VISIBLE);
                        adLaporanBarangDipesanStokHabis.setVisibility(View.VISIBLE);
                        adLaporanDataPelanggan.setVisibility(View.VISIBLE);
                        textTanggal.setVisibility(View.VISIBLE);
                        tanggal.setVisibility(View.VISIBLE);
                        adBarangMasuk.setVisibility(View.GONE);
                        adDataBarang.setVisibility(View.GONE);
                        adDataUser.setVisibility(View.GONE);
                        adKonfirmasi.setVisibility(View.GONE);

                        adPembayaran.setVisibility(View.GONE);
                        adStok.setVisibility(View.GONE);
                        adSupplier.setVisibility(View.GONE);
                        adPengeluaran.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });


    }

    public void databarang(View view){
        Intent intent = new Intent(MenuAdmin.this, DataBarang.class);
        String nama = lAdmin.getText().toString();
        intent.putExtra("lnama", nama);
        startActivity(intent);
    }

    public void datasupplier(View view){
        Intent intent = new Intent(MenuAdmin.this, DataSupplier.class);
        String nama = lAdmin.getText().toString();
        intent.putExtra("lnama", nama);
        startActivity(intent);
    }

    public void barangmasuk(View view){
        Intent intent = new Intent(MenuAdmin.this, BarangMasuk.class);
        String nama = lAdmin.getText().toString();
        intent.putExtra("lnama", nama);
        startActivity(intent);
    }
    public void konfirmasipenjualan(View view){
        Intent intent = new Intent(MenuAdmin.this, KonfirmasiPembelianHalaman.class);
        String nama = lAdmin.getText().toString();
        intent.putExtra("lnama", nama);
        startActivity(intent);
    }

    public void ubahdataprofiladmin(View view){
        Intent intent = new Intent(MenuAdmin.this, UbahDataProfilAdmin.class);
        String nama = lAdmin.getText().toString();
        intent.putExtra("lnama", nama);
        startActivity(intent);
    }

    public void pembayaran(View view){
        Intent intent = new Intent(MenuAdmin.this, CaraPembayaran.class);
        String nama = lAdmin.getText().toString();
        intent.putExtra("lnama", nama);
        startActivity(intent);
    }

    public void datastokbarang(View view){
        Intent intent = new Intent(MenuAdmin.this, DataStok.class);
        String nama = lAdmin.getText().toString();
        intent.putExtra("lnama", nama);
        startActivity(intent);
    }

    public void pengeluaran(View view){
        Intent intent = new Intent(MenuAdmin.this, PengeluaranLainnyaHalaman.class);
        String nama = lAdmin.getText().toString();
        intent.putExtra("lnama", nama);
        startActivity(intent);
    }


    public void chatadmin(View view){
        Intent intent = new Intent(MenuAdmin.this, ChatAdmin.class);
        String nama = lAdmin.getText().toString();
        intent.putExtra("lnama", nama);
        startActivity(intent);
    }


    public void keluar(View view){
        FirebaseAuth.getInstance().signOut();
        startActivity((new Intent(getApplicationContext(),LoginMenu.class)));
        Toast.makeText(MenuAdmin.this, " Berhasil Log Out " , Toast.LENGTH_SHORT).show();
        finish();

    }
}