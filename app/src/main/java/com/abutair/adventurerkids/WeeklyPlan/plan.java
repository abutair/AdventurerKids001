package com.abutair.adventurerkids.WeeklyPlan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abutair.adventurerkids.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class plan extends AppCompatActivity {

    private ArrayList<String> mImages = new ArrayList<>();
    private  ArrayList<String>mDesc = new ArrayList<>();
    private FirebaseDatabase firebaseDatabase ;
    private DatabaseReference myRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference("plans");
       RecycleView_Int();

    }


    private  void RecycleView_Int()
    {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    mDesc.add(dataSnapshot1.child("description").getValue(String.class));
                    mImages.add(dataSnapshot1.child("deleteUrl").getValue(String.class));
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
        RecyclerView recyclerView = findViewById(R.id.plan_recycler_view);

        plan_adapter adapter = new plan_adapter(plan.this,mImages,mDesc);

        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
    }


}
