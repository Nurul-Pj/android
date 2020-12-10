package com.example.aplikasimahasiswa;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder>{

    public Context mContext;
    public List<mahasiswa> mPost;

    private FirebaseUser firebaseUser;

    public ListAdapter(Context mContext, List<mahasiswa> mPost) {
        this.mContext = mContext;
        this.mPost = mPost;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.posting_layout,parent,false);
        return new ListAdapter.ViewHolder(view) ;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        final mahasiswa post = mPost.get(position);
        Glide.with(mContext).load(post.getImage()).into(holder.post_image);
        if(post.getNama().equals("")){ // kalau datanya ga ada
            holder.name.setVisibility(View.GONE); //bakal hilang
        }else {// kalau ada datanya
            holder.name.setText(post.getNama());// data nama di firebase diambil dimasukin ke name
            holder.npm.setText(post.getNpm());// data npm di firebase diambil dimasukin ke npm
            holder.jurusan.setText(post.getJurusan()); // data jurusan di firebase diambil dimasukin ke jurusan
        }
        publisherInfo(post.getPublisher(),holder.email); //yang ini buat nampilin email
        FirebaseAuth auth = FirebaseAuth.getInstance();

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PopupMenu popupMenu = new PopupMenu(mContext,v);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.deleteData:
                                FirebaseDatabase.getInstance().getReference("dataMahasiswa")
                                        .child(post.getPostid()).removeValue()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    Toast.makeText(mContext,"Data berhasil dihapus",Toast.LENGTH_SHORT).show();

                                                }
                                            }
                                        });
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popupMenu.inflate(R.menu.menu_list);
                popupMenu.show();
             return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return mPost.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView post_image;
        public TextView name,npm,jurusan,email;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            post_image = itemView.findViewById(R.id.fotoList);
            name = itemView.findViewById(R.id.namaList);
            npm = itemView.findViewById(R.id.npmList);
            jurusan = itemView.findViewById(R.id.jurusanList);
            email = itemView.findViewById(R.id.emailList);
        }


    }

    private void publisherInfo(String userid,final TextView email){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(userid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user User = dataSnapshot.getValue(user.class);
               // name.setText(User.getNama());
                email.setText(User.getEmail());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }






}
