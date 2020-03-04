package com.abutair.adventurerkids.activity;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.abutair.adventurerkids.DoctorCheck.DoctorCheck;
import com.abutair.adventurerkids.R;
import com.abutair.adventurerkids.login;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import de.hdodenhof.circleimageview.CircleImageView;
import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;

public class Activity extends AppCompatActivity {


    private ArrayList<String> mHour = new ArrayList<>();
    private  ArrayList<String>mActions = new ArrayList<>();
    private FirebaseDatabase firebaseDatabase ;
    private DatabaseReference  myRef;
    private String  username ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity);
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference("activities");
        getSharedData();

        setupCalnder();
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        String Currentdate = calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.DATE);

        RetrieveData(Currentdate);


    }

    @Override
    protected void onResume() {
        super.onResume();
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
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDateSelected(Calendar date, int position) {

                try {
                        // 2019-02-03
         String selected_date = date.get(Calendar.YEAR)+"-"+(date.get(Calendar.MONTH)+1)+"-"+date.get(Calendar.DATE);
                   RetrieveData(selected_date);

                   Toast.makeText(Activity.this,selected_date,Toast.LENGTH_LONG).show();


                }catch (Exception e)
                {
                    Toast.makeText(Activity.this,"Error :- "+e.getMessage(),Toast.LENGTH_LONG).show();
                }

             }
        });
    }  //




    private  void initRecycleView()
    {
        RecyclerView recyclerView = findViewById(R.id.my_recycler_view);

        activity_adapter adapter = new activity_adapter(this,mHour,mActions);

        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }



    public  void getSharedData()
    {     SharedPreferences sharedPreferences ;

        sharedPreferences  = getSharedPreferences("myPerf",MODE_PRIVATE);
        username= sharedPreferences.getString("UserName","empty");
    } // end Method


    public  void  RetrieveData(final String d )
    {
        myRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {



                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    String user_name =dataSnapshot1.child("userName").getValue(String.class) ;
                    String date = dataSnapshot1.child("date").getValue(String.class);



                    if (user_name.equals(username)  && d.equals(date)  )
                    {
                        mHour.add(dataSnapshot1.child("time").getValue(String.class));
                        mActions.add(dataSnapshot1.child("description").getValue(String.class));
                    }
                    else
                    {
                      mHour.clear();
                      mActions.clear();
                    //  Toast.makeText(Activity.this,"No logs for this day ",Toast.LENGTH_LONG).show();
                    }

                    initRecycleView() ;



                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }



        });
    }// end Method


    } // end Class

