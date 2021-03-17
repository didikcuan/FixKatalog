package com.example.fixkatalog;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;


public class LoginDaftar extends AppCompatActivity {


    EditText dEmail, dPassword, dNama, dTelpon, dAlamat, dDate;
    ImageView btnDaftar, btnBersihkan, fotoUser;
    FirebaseAuth fAuth;
    ProgressBar progressBar1;
    private TextView textViewProgress;
    String userId;

    DatabaseReference Dataref;
    StorageReference StorageRef;

    Uri imageUri;
    boolean isImageAdded=false;

    private static final int REQUEST_CODE_IMAGE =101 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_daftar);
        dEmail    = findViewById(R.id.dEmail);
        dPassword    = findViewById(R.id.dPassword);
        dNama        = findViewById(R.id.dNama);
        dTelpon      = findViewById(R.id.dTelpon);
        dAlamat      = findViewById(R.id.dAlamat);
        dDate        = findViewById(R.id.dDate);
        btnDaftar      = findViewById(R.id.btnDaftar);
        btnBersihkan   = findViewById(R.id.btnBersihkan);
        fAuth           = FirebaseAuth.getInstance();
        progressBar1     = findViewById(R.id.progressBar1);
        textViewProgress =findViewById(R.id.textViewProgress);
        fotoUser    = findViewById(R.id.fotoUser);

        progressBar1.setVisibility(View.INVISIBLE);
        textViewProgress.setVisibility(View.INVISIBLE);

        Dataref= FirebaseDatabase.getInstance().getReference().child("user");
        StorageRef= FirebaseStorage.getInstance().getReference().child("user");

        konfirmasifoto();




        fotoUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,REQUEST_CODE_IMAGE);
            }
        });


        btnBersihkan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    dEmail.setText(new String(""));
                    dPassword.setText(new String(""));
                    dNama.setText(new String(""));
                    dTelpon.setText(new String(""));
                    dDate.setText(new String(""));
                    dAlamat.setText(new String(""));



            }
        });


        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email    = dEmail.getText().toString().trim();
                String password = dPassword.getText().toString().trim();
                String nama     = dNama.getText().toString().trim();
                String telpon   = dTelpon.getText().toString().trim();
                String tanggal  = dDate.getText().toString().trim();
                String alamat   = dAlamat.getText().toString().trim();

                if (TextUtils.isEmpty(email)){
                    dEmail.setError("Tolong isi emailnya");
                    return;
                }

                if (TextUtils.isEmpty(password)){
                    dPassword.setError("Tolong Isi Passwordnya");
                    return;
                }

                if (password.length() <6) {
                    dPassword.setError("Password harus lebih dari 6 karakter");
                    return;
                }

                if (TextUtils.isEmpty(nama)){
                    dNama.setError("Tolong Isi Nama Anda");
                    return;
                }

                if (TextUtils.isEmpty(telpon)){
                    dTelpon.setError("Tolong Isi No Telpon Anda");
                    return;
                }

                if (TextUtils.isEmpty(tanggal)){
                    dDate.setError("Tolong Isi Tanggal Lahir Anda");
                    return;
                }

                if (TextUtils.isEmpty(alamat)){
                    dAlamat.setError("Tolong Isi Alamat Anda");
                    return;
                }


                if (isImageAdded!=false)
                {

                }else{
                    Toast.makeText(LoginDaftar.this, " Pilih Dulu Foto Untuk Foto Profil Anda " , Toast.LENGTH_SHORT).show();
                    return;
                }

                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            final String imageName=dNama.getText().toString();

                            Daftar(imageName);



                        } else {
                            Toast.makeText(LoginDaftar.this, "Error !! "+ task.getException().getMessage() , Toast.LENGTH_SHORT).show();
                            //
                            progressBar1.setVisibility(View.GONE);
                        }
                    }
                });

            }
        });


    }

        private void Daftar(final String imageName) {

            String email    = dEmail.getText().toString().trim();
            String telpon   = dTelpon.getText().toString().trim();
            String tanggal  = dDate.getText().toString().trim();
            String alamat   = dAlamat.getText().toString().trim();

        textViewProgress.setVisibility(View.VISIBLE);
        progressBar1.setVisibility(View.VISIBLE);

        userId = fAuth.getCurrentUser().getUid();


        StorageRef.child(userId+".jpg").putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                StorageRef.child(userId +".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        HashMap hashMap=new HashMap();
                        hashMap.put("uid",userId);
                        hashMap.put("email",email);
                        hashMap.put("nama",imageName);
                        hashMap.put("telepon",telpon);
                        hashMap.put("tanggal",tanggal);
                        hashMap.put("alamat",alamat);



                        hashMap.put("ImageUrl",uri.toString());

                        Dataref.child(userId).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(LoginDaftar.this, " User Sudah Ditambahkan " , Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(),LoginMenu.class));


                            }
                        });
                    }
                });

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress=(taskSnapshot.getBytesTransferred()*100)/taskSnapshot.getTotalByteCount();
                progressBar1.setProgress((int) progress);
                textViewProgress.setText(progress +" %");
                if (progress == 100)
                {
                    startActivity(new Intent(getApplicationContext(),LoginMenu.class));
                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUEST_CODE_IMAGE && data!=null)
        {
            imageUri=data.getData();
            isImageAdded=true;
            fotoUser.setImageURI(imageUri);
        }
    }

    public void konfirmasifoto(){
        Picasso.get().load(R.drawable.tambahpoto).fit().into(fotoUser);
    }

    public void login(View view){
        startActivity(new Intent(getApplicationContext(),LoginMenu.class));
    }

}