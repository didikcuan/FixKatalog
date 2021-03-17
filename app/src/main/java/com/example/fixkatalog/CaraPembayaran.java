package com.example.fixkatalog;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class CaraPembayaran extends AppCompatActivity {
    DatabaseReference Dataref;
    TextView lCaraPembayaran;
    EditText editCaraPembayaran, editNotifikasi;
    ImageView fotoEdit;
    String kodeCaraBayar = "kcb01";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cara_pembayaran);
        Dataref= FirebaseDatabase.getInstance().getReference().child("carapembayaran");

        lCaraPembayaran=findViewById(R.id.lCaraPembayaran);
        editCaraPembayaran=findViewById(R.id.editCaraPembayaran);
        fotoEdit=findViewById(R.id.fotoEdit);
        editNotifikasi=findViewById(R.id.editNotifikasi);

        Dataref.child(kodeCaraBayar).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    String carabayar=dataSnapshot.child("carabayar").getValue().toString();
                    String notifikasi=dataSnapshot.child("notifikasi").getValue().toString();
                    editCaraPembayaran.setText(carabayar);
                    editNotifikasi.setText(notifikasi);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if(getIntent().getExtras()!=null){

            Bundle bundle = getIntent().getExtras();
            lCaraPembayaran.setText(bundle.getString("lnama"));

        }else{

            lCaraPembayaran.setText("Nama Tidak Tersedia");

        }

        fotoEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String carapembayaran = editCaraPembayaran.getText().toString().trim();

                if (TextUtils.isEmpty(carapembayaran)){
                    editCaraPembayaran.setError("Tidak boleh kosong");
                    return;
                }

                String notifikasi = editNotifikasi.getText().toString().trim();

                if (TextUtils.isEmpty(notifikasi)){
                    editNotifikasi.setError("Tidak boleh kosong");
                    return;
                }

                simpan(carapembayaran,notifikasi);

            }
        });
    }

    private void simpan(String carapembayaran, String notifikasi) {
        Dataref.child(kodeCaraBayar).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().child("kodecarabayar").setValue(kodeCaraBayar);
                dataSnapshot.getRef().child("carabayar").setValue(carapembayaran);
                dataSnapshot.getRef().child("notifikasi").setValue(notifikasi);
                Intent intent = new Intent(CaraPembayaran.this, MenuAdmin.class);
                String nama = lCaraPembayaran.getText().toString();
                intent.putExtra("lnama", nama);
                startActivity(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void kembali(View view){
        Intent intent = new Intent(CaraPembayaran.this, MenuAdmin.class);
        String nama = lCaraPembayaran.getText().toString();
        intent.putExtra("lnama", nama);
        startActivity(intent);
    }
    public void chatadmin(View view){
        Intent intent = new Intent(CaraPembayaran.this, ChatAdmin.class);
        String nama = lCaraPembayaran.getText().toString();
        intent.putExtra("lnama", nama);
        startActivity(intent);
    }

}
