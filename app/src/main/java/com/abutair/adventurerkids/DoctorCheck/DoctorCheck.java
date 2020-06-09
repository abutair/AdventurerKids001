package com.abutair.adventurerkids.DoctorCheck;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.abutair.adventurerkids.R;
import com.abutair.adventurerkids.activity.Activity;
import com.abutair.adventurerkids.activity.activity_adapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;

public class DoctorCheck extends AppCompatActivity {

    private ArrayList<String> check = new ArrayList<>();
    private String  username ;
    private FirebaseDatabase firebaseDatabase ;
    private DatabaseReference myRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_check);
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference("doctor-checks");

        setupCalnder();
        getSharedData();
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        String Currentdate = calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.DATE);

        RetrieveData(Currentdate);
    }



    private void setupCalnder()
    {
        /* starts before 1 month from now */
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -1);

        /* ends after 1 month from now */
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 1);

        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(this, R.id.calendarView)
                .range(startDate, endDate)
                .datesNumberOnScreen(5)
                .build();

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {

                String selected_date = date.get(Calendar.YEAR)+"-"+(date.get(Calendar.MONTH)+1)+"-"+date.get(Calendar.DATE);
                RetrieveData(selected_date);
            }
        });
    }


    private  void initRecycleView()
    {
        RecyclerView recyclerView = findViewById(R.id.DoctorCheck_recycle_view);

        doctor_check_adapter adapter = new doctor_check_adapter(this,check);

        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public  void getSharedData()
    {     SharedPreferences sharedPreferences ;

        sharedPreferences  = getSharedPreferences("myPerf",MODE_PRIVATE);
        username= sharedPreferences.getString("UserName","empty");
    }


    public  void  RetrieveData(final  String d)
    {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                   // String user_name =dataSnapshot1.child("userName").getValue(String.class) ;
                    String date = dataSnapshot1.child("date").getValue(String.class);

                    if (d.equals(date))
                    {
                        check.add(dataSnapshot1.child("desc").getValue(String.class));

                    }
                    else
                    {
                        check.clear();
                        Toast.makeText(DoctorCheck.this,"No logs for this day ",Toast.LENGTH_LONG).show();
                    }

                    initRecycleView();

                }


            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
