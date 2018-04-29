package com.tst.sleepy.cloudapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tst.sleepy.cloudapp.model.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.provider.Settings.NameValueTable.NAME;

public class SignUp extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private EditText name;
    private Button SignUp;
    private FirebaseAuth mAuth;
    FirebaseDatabase db;
    DatabaseReference ref;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        name= findViewById(R.id.name);
        username=findViewById(R.id.username);
        password=findViewById(R.id.password);

        mAuth = FirebaseAuth.getInstance();
        db=FirebaseDatabase.getInstance();
        ref=db.getReference();


    }

    public void Register(View v){
        String email=username.getText().toString();
        String pass=password.getText().toString();

        if(password.getText().length() < 8){
            Toast.makeText(getApplicationContext(), "Kindly enter password of at least 8 characters", Toast.LENGTH_SHORT).show();
            return;

        }
        if(name.getText().length() < 3){
            Toast.makeText(getApplicationContext(), "Kindly enter name of at least 3 characters", Toast.LENGTH_SHORT).show();
            return;

        }

        if(!isEmailValid(email))
        {
            Toast.makeText(getApplicationContext(), "Kindly enter a valid email format", Toast.LENGTH_SHORT).show();
            return;
        }
        dialog = new ProgressDialog(this);
        dialog.setMessage("Kindly wait...");
        dialog.show();
        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(SignUp.this, "Authentication Succes",
                                    Toast.LENGTH_SHORT).show();

                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            dialog.dismiss();
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUp.this, "Authentication failed." +task.getException(),
                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void updateUI(FirebaseUser u) {

        String uid=u.getUid();

        User temp=new User(name.getText().toString(),username.getText().toString(),password.getText().toString(),"Customer",null);

        ref.child("users").child(uid).setValue(temp);
        save(uid,"Customer");
        dialog.dismiss();
        Intent intent= new Intent(getBaseContext(),UserProfile.class);
        startActivity(intent);

    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public void save(String uid,String type)
    {
        SharedPreferences dataStore = getApplicationContext().getSharedPreferences(NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = dataStore.edit();
        editor.putString("uid",uid);
        editor.putString("type",type);
        editor.commit();
    }
}
