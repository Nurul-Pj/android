package com.example.aplikasimahasiswa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    TextView textSignin;
    EditText editNama, emailTxt, passwordTxt;
    ImageButton clickBtn;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editNama = findViewById(R.id.editNama);
        emailTxt = findViewById(R.id.emailTxt);
        passwordTxt = findViewById(R.id.passwordTxt);
        clickBtn = findViewById(R.id.clickBtn);

        firebaseAuth = FirebaseAuth.getInstance(); // mendapatkan data autentikasi dari Firebase

        textSignin = findViewById(R.id.textSignin);
        textSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent balik = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(balik);
            }
        });

        clickBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
                if (notif() && registerUser()) {
                    String user_nama = editNama.getText().toString();
                    String user_email = emailTxt.getText().toString();
                    String user_password = passwordTxt.getText().toString();
                    firebaseAuth.createUserWithEmailAndPassword(user_email, user_password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        user dataUser = new user(user_nama, user_email);
                                        FirebaseDatabase.getInstance().getReference("users")
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .setValue(dataUser);
                                        verification();
                                        Toast.makeText(RegisterActivity.this, "Registrasi Berhasil", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));

                                    }else{
                                        Toast.makeText(RegisterActivity.this,"Registrasi Gagal",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }


            }
        });
    }


    private void verification() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser != null) {
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(RegisterActivity.this, "Verifikasi Terkirim ke email", Toast.LENGTH_SHORT).show();
                        firebaseAuth.signOut();
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    }else{
                        Toast.makeText(RegisterActivity.this, "Verifikasi Tidak Terkirim",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public Boolean notif() {
        Boolean hasil = false;
        String nama = editNama.getText().toString();
        String email = emailTxt.getText().toString();
        String password = passwordTxt.getText().toString();

        if (nama.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Isi Kolom Dengan Benar", Toast.LENGTH_SHORT).show();
        } else {
            hasil = true;
        }

        return hasil;
    }

    public boolean registerUser() {
        Boolean hasil = false;
        String password = passwordTxt.getText().toString();

        if (password.length() < 8) {
            passwordTxt.setError("Karakter Tidak Valid");
            passwordTxt.requestFocus();
            return false;
        } else {
            hasil = true;
        }

        return hasil;
    }


}