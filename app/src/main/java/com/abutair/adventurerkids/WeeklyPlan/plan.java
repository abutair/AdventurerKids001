package com.abutair.adventurerkids.WeeklyPlan;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.abutair.adventurerkids.MainActivity;
import com.abutair.adventurerkids.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.util.FitPolicy;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

public class plan extends AppCompatActivity {

    private ImageView img;
    private FirebaseDatabase firebaseDatabase ;
    private DatabaseReference myRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);
        img = findViewById(R.id.plan_Image);
        firebaseDatabase = FirebaseDatabase.getInstance();


       RetriveImg();

    }

    private  void RetriveImg()
    {
        myRef = FirebaseDatabase.getInstance().getReference("PlanImage");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                    RequestOptions placeholderRequset = new RequestOptions() ;
                  placeholderRequset.placeholder(R.drawable.empty);
                    String Path = dataSnapshot.child("plan").getValue(String.class);
                    Glide.with(plan.this)
                            .applyDefaultRequestOptions(placeholderRequset)
                            .load(Path)
                            .into(img);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        }) ;



    }


}
