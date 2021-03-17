package com.example.fixkatalog;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
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
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DataRating extends AppCompatActivity {

    ImageView fotoDataRating,kirimRating;

    TextView userRating,namaBarangRating;

    RatingBar dataRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_rating);

        fotoDataRating=findViewById(R.id.fotoDataRating);
        kirimRating=findViewById(R.id.kirimRating);
        namaBarangRating=findViewById(R.id.namaBarangRating);

        userRating=findViewById(R.id.userRating);

        dataRating=findViewById(R.id.dataRating);

        String key=getIntent().getStringExtra("tampilkey");

        if(getIntent().getExtras()!=null){

            Bundle bundle = getIntent().getExtras();
            userRating.setText(bundle.getString("lnama"));

        }else{

            userRating.setText("Nama Tidak Tersedia");

        }
        DatabaseReference Dataref= FirebaseDatabase.getInstance().getReference().child("tampil");
        Dataref.child("1"+key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String ImageUrl = dataSnapshot.child("ImageUrl").getValue().toString();
                String namabarang = dataSnapshot.child("namabarang").getValue().toString();
                String ukuranbarang = dataSnapshot.child("kodebarangmasuk").getValue().toString();
                String kodebarang = dataSnapshot.child("kodebarang").getValue().toString();

                Picasso.get().load(ImageUrl).into(fotoDataRating);
                namaBarangRating.setText(namabarang);




            kirimRating.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Float rating = dataRating.getRating();

                    if (rating == 0.0) {
                        Toast.makeText(DataRating.this, "Isi Dulu Data Rating" , Toast.LENGTH_SHORT).show();
                        return;
                    }

                    ProgressBar barDataRating = findViewById(R.id.barDataRating);
                    barDataRating.setVisibility(View.VISIBLE);

                    FirebaseAuth fAuth = FirebaseAuth.getInstance();
                    DatabaseReference  user= FirebaseDatabase.getInstance().getReference().child("user");
                    user.child(fAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                            String ImageUrl = dataSnapshot1.child("ImageUrl").getValue().toString();
                            String alamat = dataSnapshot1.child("alamat").getValue().toString();
                            String email = dataSnapshot1.child("email").getValue().toString();
                            String nama = dataSnapshot1.child("nama").getValue().toString();
                            String tanggal = dataSnapshot1.child("tanggal").getValue().toString();
                            String telepon = dataSnapshot1.child("telepon").getValue().toString();
                            String uid = dataSnapshot1.child("uid").getValue().toString();
                            DatabaseReference Dataref= FirebaseDatabase.getInstance().getReference().child("datarating");
                            Dataref.child(kodebarang+ukuranbarang).child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                                    if (dataSnapshot2.exists())
                                    {
                                        Intent intent=new Intent(DataRating.this,MainActivity.class);
                                        String nama = namaBarangRating.getText().toString();
                                        intent.putExtra("lnama", nama);
                                        startActivity(intent);
                                        finish();

                                    }else {

                                        Date tanggal1 = new Date();
                                        SimpleDateFormat format = new SimpleDateFormat("dd MM yyyy");
                                        String finalTanggal = format.format(tanggal1);

                                        dataSnapshot2.getRef().child("uid").setValue(uid);
                                        dataSnapshot2.getRef().child("alamat").setValue(alamat);
                                        dataSnapshot2.getRef().child("telepon").setValue(telepon);
                                        dataSnapshot2.getRef().child("nama").setValue(nama);
                                        dataSnapshot2.getRef().child("tanggal").setValue(tanggal);
                                        dataSnapshot2.getRef().child("ImageUrl").setValue(ImageUrl);

                                        dataSnapshot2.getRef().child("rating").setValue(String.valueOf(dataRating.getRating()));
                                        dataSnapshot2.getRef().child("tanggal").setValue(finalTanggal);

                                        DatabaseReference Dataref= FirebaseDatabase.getInstance().getReference().child("totaldatarating");
                                        Dataref.child(kodebarang+ukuranbarang).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot3) {

                                                if (dataSnapshot3.exists())
                                                {
                                                    Double ratingbar = Double.valueOf(dataSnapshot3.child("ratingbar").getValue().toString());
                                                    Integer totaluser = Integer.valueOf(dataSnapshot3.child("totaluser").getValue().toString());

                                                    Integer usertotal =totaluser+1;
                                                    Double barrating = ratingbar+Double.valueOf(dataRating.getRating());

                                                    dataSnapshot3.getRef().child("ratingbar").setValue(String.valueOf(barrating/usertotal) );
                                                    dataSnapshot3.getRef().child("totaluser").setValue(String.valueOf(usertotal));

                                                }else {

                                                    dataSnapshot3.getRef().child("ratingbar").setValue(String.valueOf(String.valueOf(dataRating.getRating())));
                                                    dataSnapshot3.getRef().child("totaluser").setValue(String.valueOf("1"));
                                                }

                                                Intent intent=new Intent(DataRating.this,MainActivity.class);
                                                String nama = namaBarangRating.getText().toString();
                                                intent.putExtra("lnama", nama);
                                                startActivity(intent);
                                                Toast.makeText(DataRating.this, " Berhasil memberikan rating"+String.valueOf(dataRating.getRating()) , Toast.LENGTH_SHORT).show();

                                                finish();

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

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });



                }
            });




            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


}
