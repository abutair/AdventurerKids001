package com.abutair.adventurerkids.notes;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.abutair.adventurerkids.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Note extends AppCompatActivity {
    String s;
    String state ="" ;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                s= null;
            } else {
                s= extras.getString("username");

            }
        } else {
            s= (String) savedInstanceState.getSerializable("username");

        }

        getState(s);



    } // end onCreate

    private void getState( final String s) {

        System.out.println("getState: the s value is"+s);





          myRef.child("emotion").addValueEventListener(new ValueEventListener() {
              @Override
              public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                  for (DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){
                      String d = dataSnapshot1.child("username").getValue(String.class);
                      System.out.println("getState: the d value is"+d);
                      if(d.equals(s)){
                          state = dataSnapshot1.child("state").getValue(String.class);
                          System.out.println("getState: the sate value is "+state);
                      }
                  }
                  System.out.println("state is "+state);
                  if(state.equals("happy")){
                      Intent i = new Intent(Note.this,NoteSucc.class);
                      startActivity(i);
                      finish();

                  }else{
                      Intent i = new Intent(Note.this,NoteFailed.class);
                      startActivity(i);
                      finish();
                  }

              }

              @Override
              public void onCancelled(@NonNull DatabaseError databaseError) {

              }

          });




    }


} // end Class
