package com.example.aplikasimahasiswa;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class BiodataActivity extends AppCompatActivity {

    ImageView foto;
    TextView nama, npm, email, jurusan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biodata);
        foto = findViewById(R.id.foto);
        nama = findViewById(R.id.nama);
        npm = findViewById(R.id.npm);
        email = findViewById(R.id.email);
        jurusan = findViewById(R.id.jurusan);
    }
}