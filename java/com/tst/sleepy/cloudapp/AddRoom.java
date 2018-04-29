package com.tst.sleepy.cloudapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.tst.sleepy.cloudapp.model.Room;
import com.tst.sleepy.cloudapp.model.User;

import java.io.IOException;
import java.util.ArrayList;

public class AddRoom extends AppCompatActivity {

    Button submit;
    Button Add_Image;
    EditText price;
    Spinner type;
    CheckBox ac;
    CheckBox mattress;
    CheckBox Tv;
    CheckBox wifi;
    TextView count;
    ArrayList<Uri> selectedImages;
    private Firebase rootURL;
    private StorageReference mStorage;
    private DatabaseReference mReference;
    int roomCount;
    private ArrayList<Uri> returned_img_paths;
    private  ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room);

        ac=findViewById(R.id.ac);
        Tv=findViewById(R.id.tv);
        mattress=findViewById(R.id.mattress);
        wifi=findViewById(R.id.internet);
        price=findViewById(R.id.price);
        submit=findViewById(R.id.submit);
        Add_Image=findViewById(R.id.add);
        count=findViewById(R.id.count);
        count.setText("0");
        rootURL = new Firebase(firebaselinks.rooturl);
        selectedImages=new ArrayList<Uri>();
        returned_img_paths=new ArrayList<Uri>();
        mStorage= FirebaseStorage.getInstance().getReference();
        mReference=FirebaseDatabase.getInstance().getReference();
        RoomCount(mReference);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Kindly wait...");

        submit.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        final DatabaseReference Roomref = mReference.child("Rooms").child("Room No" + String.valueOf(roomCount));
                        roomCount++;
                        FirebaseDatabase.getInstance().getReference().child("RoomCount").setValue(String.valueOf(roomCount));
                                //upload images to firebase

                                for(int i=0; i<selectedImages.size();i++)
                                    UploadImageOnFirebase(selectedImages.get(i));


                    }

                }
        );

        Add_Image.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PickImage();
                    }
                }
        );
    }
    private void RoomCount(DatabaseReference ref)
    {
        ref.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String counts = String.valueOf(dataSnapshot.child("RoomCount").getValue());
                roomCount= Integer.parseInt(counts);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }
    private void PickImage()
    {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            Uri uri = data.getData();

            selectedImages.add(uri);
            count.setText(String.valueOf(selectedImages.size()));
            //  SetPicture(MediaStore.Images.Media.getBitmap(UserProfile.this.getContentResolver(),uri));
            //UploadImageOnFirebase(uri);

        }
    }
    public void UploadImageOnFirebase(Uri uploadProfilePictureURI){
        StorageReference filePath = mStorage.child(firebaselinks.databaseurl).child(uploadProfilePictureURI.getLastPathSegment());
        dialog.show();

        filePath.putFile(uploadProfilePictureURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                returned_img_paths.add(taskSnapshot.getDownloadUrl());

                int f1 = 0 , f2 = 0, f3 = 0, f4 = 0;
                if(ac.isChecked())
                    f1=1;
                if(Tv.isChecked())
                    f2=1;
                if(wifi.isChecked())
                    f3=1;
                if(mattress.isChecked())
                    f4=1;

                int rprice=Integer.parseInt(price.getText().toString());
                Room room=new Room(f1,f2,f3,f4,rprice,"Available");
                FirebaseDatabase.getInstance().getReference().child("Rooms").child("Room No"+ String.valueOf(roomCount)).setValue(room);

                for(int i=0;i<returned_img_paths.size();i++) {

                    FirebaseDatabase.getInstance().getReference().child("Rooms").child("Room No" + String.valueOf(roomCount)).child("images").push().setValue(returned_img_paths.get(i).toString());
                }
            dialog.dismiss();
            Intent intent=new Intent(getApplicationContext(),AdminProfile.class);
            startActivity(intent);
            }
        });
    }
}
