package com.example.fixkatalog;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginMenu extends AppCompatActivity {
    ImageView fotoDaftar, fotoMasuk, fotoReset;
    EditText editUsername, editPassword;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    DatabaseReference Dataref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_menu);

        fotoReset = findViewById(R.id.fotoReset);
        fotoDaftar = findViewById(R.id.fotoDaftar);
        fotoMasuk = findViewById(R.id.fotoMasuk);
        editUsername = findViewById(R.id.editUsername);
        editPassword = findViewById(R.id.editPassword);
        fAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);

        Dataref= FirebaseDatabase.getInstance().getReference().child("user");



        fotoReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText resetMail = new EditText(v.getContext());
                AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset Password ?");
                passwordResetDialog.setMessage("Masukan Email Untuk Login");
                passwordResetDialog.setView(resetMail);

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String mail = resetMail.getText().toString();
                        fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(LoginMenu.this, "Periksa link reset di kotak masuk email", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(LoginMenu.this, "Error ! Reset Gagal (Alamat Email Tidak Terdaftar", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });

                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                passwordResetDialog.create().show();

            }
        });


        fotoMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editUsername.getText().toString().trim();
                String password = editPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    editUsername.setError("Tolong isi emailnya");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    editPassword.setError("Tolong Isi Passwordnya");
                    return;
                }

                if (password.length() < 6) {
                    editPassword.setError("Password kurang dari 6 huruf");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);

                fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {


                            getUserInfo();



                        } else {
                            Toast.makeText(LoginMenu.this, " Login Gagal " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                            progressBar.setVisibility(View.GONE);
                        }

                    }
                });

            }
        });
        if (fAuth.getCurrentUser() != null){
            FirebaseAuth.getInstance().signOut();
            return;
        }
    }



    public void LoginDaftar(View view) {
        startActivity(new Intent(getApplicationContext(),LoginDaftar.class));

    }
    private void getUserInfo() {
        Dataref.child(fAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    String nama = dataSnapshot.child("nama").getValue().toString();
                    if (dataSnapshot.child("hak").getValue() != null)
                    {



                        Intent intent = new Intent(LoginMenu.this, MenuAdmin.class);
                        intent.putExtra("lnama", nama);
                        startActivity(intent);

                    }else
                    {


                        Intent intent = new Intent(LoginMenu.this, MainActivity.class);
                        intent.putExtra("lnama", nama);
                        startActivity(intent);
                       

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}