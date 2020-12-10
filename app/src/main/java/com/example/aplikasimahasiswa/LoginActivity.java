package com.example.aplikasimahasiswa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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

public class LoginActivity extends AppCompatActivity {

    TextView textSignup;
    EditText emailTxt;
    EditText passwordTxt;
    ImageButton btnSignin;

    FirebaseAuth firebaseAuth;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailTxt = findViewById(R.id.emailTxt);
        passwordTxt = findViewById(R.id.passwordTxt);
        btnSignin = findViewById(R.id.btnSignin);
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        textSignup = findViewById(R.id.textSignup);
        textSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pindah = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(pindah);

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prosesValidasi(emailTxt.getText().toString(), passwordTxt.getText().toString());
            }
        });
    }

    public void prosesValidasi(String user_email, String user_password) {
        progressDialog.setMessage("Verifikasi Berhasil");
        progressDialog.show();
        if (notif()) {
            firebaseAuth.signInWithEmailAndPassword(user_email, user_password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                cekEmail();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(LoginActivity.this, "Login Gagal !", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    public Boolean notif() {
        Boolean hasil = false;
        String email = emailTxt.getText().toString();
        String password = passwordTxt.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Isi Kolom Dengan Benar", Toast.LENGTH_SHORT).show();
        } else {
            hasil = true;
        }

        return hasil;
    }

    private void cekEmail() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        Boolean email = firebaseUser.isEmailVerified();

        if (email) {
            Toast.makeText(LoginActivity.this, "Login Berhasil", Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(LoginActivity.this, ListActivity.class));
        } else {
            Toast.makeText(LoginActivity.this, "Harap Verifikasi Email", Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }

    }

}
