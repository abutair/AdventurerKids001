package com.abutair.adventurerkids;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BackendData  {

    private FirebaseDatabase firebaseDatabase ;
    private DatabaseReference myRef;
    private  ArrayList<String>Date = new ArrayList<>();

    private ArrayList<String> Activity_hour = new ArrayList<>();
    private  ArrayList<String>Activity_dec = new ArrayList<>();


    private  void getData()
    {
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference("activities");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {

                    String s1 = dataSnapshot1.child("time").getValue(String.class);
                    String s2 = dataSnapshot1.child("description").getValue(String.class);
                    Date.add(dataSnapshot1.child("date").getValue(String.class));
                    Activity_hour.add(s1);
                    Activity_dec.add(s2);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public  void  Activity_data(String text)
    {


         if (Date.contains(text))
        {
            int index = Date.indexOf(text);
            Activity_hour.get(index);
            Activity_dec.get(index);
        }
    }

    public  String getActivity_hour(String t)
    {
        int index = Date.indexOf(t);
        return Activity_hour.get(index);
    }






} // end Class
