package com.example.aplikasimahasiswa;

public class mahasiswa {

    public mahasiswa(String postid,String nama, String npm, String jurusan, String image,String publisher) {
        this.postid = postid;
        this.nama = nama;
        this.npm = npm;
        this.jurusan = jurusan;
        this.image = image;
        this.publisher = publisher;
    }

    public mahasiswa(){}


    private String nama;

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    private String postid;
    private String npm;
    private String jurusan;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    private String image;

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    private String publisher;

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNpm() {
        return npm;
    }

    public void setNpm(String npm) {
        this.npm = npm;
    }

    public String getJurusan() {
        return jurusan;
    }

    public void setJurusan(String jurusan) {
        this.jurusan = jurusan;
    }
}
