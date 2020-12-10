package com.example.aplikasimahasiswa;

public class user {

    private String nama;
    private String email;

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public user(String nama, String email) {
        this.nama = nama;
        this.email = email;
    }

    public user(){

    }
}
