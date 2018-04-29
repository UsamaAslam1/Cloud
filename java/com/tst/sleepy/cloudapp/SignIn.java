package com.tst.sleepy.cloudapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tst.sleepy.cloudapp.model.User;

import static android.provider.Settings.NameValueTable.NAME;

public class SignIn extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private Button login;
    private Button register;

    private FirebaseAuth mAuth;
    FirebaseDatabase db;
    DatabaseReference ref;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);


        // check if already logged in
        SharedPreferences preferences;
        preferences = getApplicationContext().getSharedPreferences(NAME, Context.MODE_PRIVATE);
        String uid= preferences.getString("uid","");
        String type=preferences.getString("type","");
        if(uid!="" && type.equals("Customer") )
        {
            Intent intent= new Intent(getBaseContext(),UserProfile.class);
            startActivity(intent);
        }
        if(uid!="" && type.equals("Admin") )
        {
            Intent intent= new Intent(getBaseContext(),AdminProfile.class);
            startActivity(intent);
        }
        login= findViewById(R.id.button);

        email=findViewById(R.id.username);
        password=findViewById(R.id.password);
        mAuth = FirebaseAuth.getInstance();
        db=FirebaseDatabase.getInstance();
        ref=db.getReference();

        register= findViewById(R.id.register);
        register.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent= new Intent(getBaseContext(),SignUp.class);
                        startActivity(intent);
                    }
                }
        );
        login.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        SignIn();

                    }
                }
        );



    }

    void SignIn()
    {
        dialog = new ProgressDialog(this);
        dialog.setMessage("Kindly wait...");
        dialog.show();
        mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            FirebaseUser user = mAuth.getCurrentUser();

                            updateUI(user);

                              } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(SignIn.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
        dialog.dismiss();
    }

    private void updateUI(FirebaseUser user) {

        final String uid=user.getUid();

        FirebaseDatabase.getInstance().getReference().child("users").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.child(uid).getValue(User.class);
                String u_type=user.getUser_type();
                save(uid,u_type);
                Intent intent;
                if(u_type=="Customer" )
                {
                    intent= new Intent(getBaseContext(),UserProfile.class);
                }
                else
                {
                    intent= new Intent(getBaseContext(),AdminProfile.class);
                }
                dialog.dismiss();
                startActivity(intent);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });

    }
    public void save(String uid, String u_type)
    {
        SharedPreferences dataStore = getApplicationContext().getSharedPreferences(NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = dataStore.edit();
        editor.putString("uid",uid);
        editor.putString("type",u_type);
        editor.commit();
    }

}
