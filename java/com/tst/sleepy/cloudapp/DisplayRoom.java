package com.tst.sleepy.cloudapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tst.sleepy.cloudapp.model.Room;

import java.util.ArrayList;
import java.util.HashMap;

public class DisplayRoom extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_room);
        load();

    }

    public void load()
    {
        FirebaseDatabase.getInstance().getReference().child("Rooms").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot datas: dataSnapshot.getChildren()){
                    HashMap<String,String> map= (HashMap<String,String>) datas.child("images").getValue();
                    Room myroom=new Room();
                    ArrayList<String> images=new ArrayList<>(map.values()) ;
                    myroom.setImages(images);
                    myroom.setAc(datas.child("ac").getValue(Integer.class));
                    myroom.setMattress((Integer) datas.child("mattress").getValue((Integer.class)));
                    myroom.setPrice((Integer) datas.child("price").getValue((Integer.class)));
                    myroom.setTv((Integer) datas.child("tv").getValue((Integer.class)));
                    myroom.setWifi((Integer) datas.child("wifi").getValue((Integer.class)));
                    myroom.setStatus((String) datas.child("status").getValue());

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }
}
