package com.abutair.adventurerkids;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class login extends AppCompatActivity implements View.OnClickListener {

 private Button b ;
 private  EditText name,password;
 private FirebaseDatabase database  ;
 private DatabaseReference myRef  ;

 private ProgressDialog mProgress ;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users");
         b= findViewById(R.id.cirLoginButton);
        name = findViewById(R.id.editTextEmail);

        password = findViewById(R.id.editTextPassword);
        mProgress = new ProgressDialog(login.this);
        mProgress.setTitle("Processing...");
        mProgress.setMessage("Please wait...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);

        b.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
         String n,p;

        mProgress.show();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                boolean bla = false ;


                for (DataSnapshot dataSnapshot2:dataSnapshot.getChildren() )
                {
                    String n = dataSnapshot2.child("userName").getValue(String.class);

                    String p = dataSnapshot2.child("password").getValue(String.class);

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

                    Toast.makeText(login.this,"Wrong username or password",Toast.LENGTH_LONG).show();

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
        myedit.apply();
        Toast.makeText(login.this,name.getText(),Toast.LENGTH_LONG).show();
    }


}
