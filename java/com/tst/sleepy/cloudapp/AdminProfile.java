package com.tst.sleepy.cloudapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AdminProfile extends AppCompatActivity {

    Button view;
    Button add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_profile);

        view=findViewById(R.id.view);
        add=findViewById(R.id.add);

        add.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(getBaseContext(),AddRoom.class);
                        startActivity(intent);
                    }
                }
        );

    }
}
