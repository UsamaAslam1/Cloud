package com.tst.sleepy.cloudapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.tst.sleepy.cloudapp.model.User;

import org.w3c.dom.Text;

import java.io.IOException;

import static android.provider.Settings.NameValueTable.NAME;

public class UserProfile extends AppCompatActivity {

    ImageView dp;
    TextView Name;
    Firebase rootURL;
    FirebaseDatabase db;
    String uid ;
    DatabaseReference ref;
    StorageReference mStorage;
    Uri returned_img_path;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rootURL = new Firebase(firebaselinks.rooturl);
        mStorage= FirebaseStorage.getInstance().getReference();
        setContentView(R.layout.activity_user_profile);
        dp=findViewById(R.id.dp);
        Name=findViewById(R.id.name);
        Button hotels=findViewById(R.id.hotels);
        Button logout=findViewById(R.id.logout);

        logout.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferences preferences;
                        preferences = getApplicationContext().getSharedPreferences(NAME, Context.MODE_PRIVATE);
                        preferences.edit().clear().commit();
                        Intent intent=new Intent(getBaseContext(),SignIn.class);
                        startActivity(intent);
                                            }
                }
        );
        hotels.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent= new Intent(getBaseContext(),HotelsList.class);
                        startActivity(intent);
                    }
                }
        );
         uid=load();

        dp.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(UserProfile.this);
                        alertDialog.setTitle("Update Image");
                        alertDialog.setMessage("Do you want to update your image?");
                        alertDialog.setPositiveButton("Upload", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int which) {

                                PickImage();
                                // Write your code here to invoke Submit event

                            }
                        });

                        // Setting Negative "NO" Button
                        alertDialog.setNegativeButton("Go Back", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to invoke NO event
                                Toast.makeText(getApplicationContext(), "Make changes", Toast.LENGTH_SHORT).show();
                                dialog.cancel();
                            }
                        });

                        // Showing Alert Message
                        alertDialog.show();
                    }
                }
        );

        FirebaseDatabase.getInstance().getReference().child("users").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.child(uid).getValue(User.class);
                Name.setText("Welcome back Mr."+user.getName()+"!!");
                if(user.getPhotoURL()!=null)
                LoadImage(user.getPhotoURL());


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });

    }
    private void LoadImage(String path)
    {

        Picasso.with(getBaseContext()).load(path).resize(ConversionFunctions.dpToPx(getApplicationContext(),80 ), ConversionFunctions.dpToPx(getApplicationContext(),80 )).centerCrop().into(dp);
    }
    private String load()
    {
        SharedPreferences preferences;
        preferences = getApplicationContext().getSharedPreferences(NAME, Context.MODE_PRIVATE);
        return preferences.getString("uid","");
    }
    private void PickImage()
    {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, 1);
    }

    public void SetPicture(Bitmap imageBitmap){
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(
                imageBitmap,imageBitmap.getWidth()/5,imageBitmap.getHeight()/5, false);
        dp.setImageBitmap(resizedBitmap);
        dp.setScaleType(ImageView.ScaleType.FIT_XY);

    }
    public void SetImagePathOnFirebase(){

        Firebase PictureURL = rootURL.child("users").child(uid).child("photoURL");
            PictureURL.setValue(returned_img_path.toString());

    }

    public void UploadImageOnFirebase(Uri uploadProfilePictureURI){
        StorageReference filePath = mStorage.child(firebaselinks.databaseurl).child(uploadProfilePictureURI.getLastPathSegment());
        filePath.putFile(uploadProfilePictureURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                returned_img_path=taskSnapshot.getDownloadUrl();
                SetImagePathOnFirebase();
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            Uri uri = data.getData();
            try {

                SetPicture(MediaStore.Images.Media.getBitmap(UserProfile.this.getContentResolver(),uri));
                UploadImageOnFirebase(uri);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
