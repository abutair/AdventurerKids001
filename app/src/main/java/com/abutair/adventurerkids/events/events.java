package com.abutair.adventurerkids.events;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.abutair.adventurerkids.R;
import com.abutair.adventurerkids.WeeklyPlan.plan;
import com.abutair.adventurerkids.WeeklyPlan.plan_adapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

public class events extends AppCompatActivity {

    private ArrayList<String> Title = new ArrayList<>();
    private ArrayList<String> Images = new ArrayList<>();
    private  ArrayList<String> Desc = new ArrayList<>();
    private FirebaseDatabase firebaseDatabase ;
    private DatabaseReference myRef;



    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference("events");
        RecycleView_Int() ;
    }



    private  void RecycleView_Int()
    {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    Title.add(dataSnapshot1.child("title").getValue(String.class));
                    Desc.add(dataSnapshot1.child("description").getValue(String.class));
                    Images.add(dataSnapshot1.child("deleteUrl").getValue(String.class));

                }
                initRecycleView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    private  void initRecycleView()
    {
        RecyclerView recyclerView = findViewById(R.id.events_recycler_view);
        events_adapter adapter = new events_adapter(events.this,Title,Images,Desc);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        recyclerView.setAdapter(adapter);
    }

}
