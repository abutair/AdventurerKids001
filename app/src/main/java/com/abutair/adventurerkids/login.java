package com.abutair.adventurerkids;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class login extends AppCompatActivity implements View.OnClickListener {

 private Button b ;
 private EditText name,password;
 private FirebaseDatabase database  ;
 private DatabaseReference myRef  ;

 private ProgressDialog mProgress ;
    String n = "";
    String p = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


         b= findViewById(R.id.cirLoginButton);
        name = findViewById(R.id.editTextEmail);

        password = findViewById(R.id.editTextPassword);
        mProgress = new ProgressDialog(login.this);
        mProgress.setTitle("Processing...");
        mProgress.setMessage("Please wait...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);

        database= FirebaseDatabase.getInstance();
        myRef= database.getReference();
        b.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {


        mProgress.show();
        myRef.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                boolean bla = false ;


                for (DataSnapshot dataSnapshot2:dataSnapshot.getChildren() )
                {
                     n = dataSnapshot2.child("UserId").getValue(String.class);

                     p = dataSnapshot2.child("password").getValue(String.class);
                    Toast.makeText(login.this,"UserId"+n, Toast.LENGTH_LONG).show();
                    Toast.makeText(login.this,"passowrd"+p, Toast.LENGTH_LONG).show();

                    if (n.equals(name.getText().toString()) && p.equals(password.getText().toString()))
                    {

                        SharedData();

                        mProgress.dismiss();
                        bla= true ;
                        Intent i = new Intent(login.this,MainActivity.class);
                        startActivity(i);
                        finish();


                    }

                }
                if (!bla)
                {
                    mProgress.dismiss();

                    Toast.makeText(login.this,"Wrong username or password", Toast.LENGTH_LONG).show();

                }




            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        }) ;


    }

    public  void SharedData()
    {
        SharedPreferences sharedPreferences = getSharedPreferences("myPerf",0);
        SharedPreferences.Editor myedit   =  sharedPreferences.edit();
        myedit.putString("UserName",name.getText().toString());
        myedit.commit();
    }


}
