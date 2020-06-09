package com.abutair.adventurerkids.activity;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
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
    private  ArrayList<String>mDate = new ArrayList<>();
    private  ArrayList<String>UserNames = new ArrayList<>();
    private FirebaseDatabase firebaseDatabase ;
    private DatabaseReference  myRef;
    private String  username ;
    private  ArrayList<String>hour = new ArrayList<>();
    private  ArrayList<String>action = new ArrayList<>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity);
        getSharedData();
        getAtivityData();
        setupCalnder();

        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        String day = calendar.get(Calendar.DATE)+"";
        if(day.length()==1)
        {
            day = "0"+day;
        }
        String month = (calendar.get(Calendar.MONTH)+1) +"";
        if(month.length()==1)
        {
            month = "0"+month;
        }
        String Currentdate = calendar.get(Calendar.YEAR)+"-"+month+"-"+day;
        Log.d("onCreate  :-",Currentdate+"");

       RetrieveData(Currentdate);


    }

    public void getAtivityData() {

        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference("activities");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {


                    String s1 = dataSnapshot1.child("hour").getValue(String.class);
                    String s2 = dataSnapshot1.child("desc").getValue(String.class);
                    String un = dataSnapshot1.child("userName").getValue(String.class);
                    UserNames.add(un);
                    mDate.add(dataSnapshot1.child("date").getValue(String.class));
                    mHour.add(s1);
                    mActions.add(s2);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

      //  ProgressDialog();
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
                    String day = date.get(Calendar.DATE)+"";
//                    if(day.length()==1)
//                    {
//                        day = "0"+day;
//                    }
//
//                    if(month.length()==1)
//                    {
//                        month = "0"+month;
//                    }
                    String month = (date.get(Calendar.MONTH)+1) +"";
                   String selected_date = date.get(Calendar.YEAR)+"-"+month+"-"+day;
                   RetrieveData(selected_date);


                }catch (Exception e)
                {
                    Log.d("mHour :-",e.getMessage());
                }

             }
        });
    }  //



    private  void initRecycleView()
    {
        RecyclerView recyclerView = findViewById(R.id.my_recycler_view);

        activity_adapter adapter = new activity_adapter(this,hour,action);

        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    public  void getSharedData()
    {
        SharedPreferences sharedPreferences ;

        sharedPreferences  = getSharedPreferences("myPerf",MODE_PRIVATE);
        username= sharedPreferences.getString("UserName","empty");
    } // end Method

   public  void  RetrieveData(final String d )
    {
        int index = mDate.indexOf(d);
        //0 1 2 3 4 6

        if(index>=0 )
        {

             Log.d("mHour :-",mHour.get(index)+"");
             Log.d("mAction :-",mActions.get(index)+"");
             for(int i=index;i<mDate.size();i++)
             {
                 //  5  6 7

                 if (mDate.get(i).equals(d) )
                 {
                     // 1 1 1 1 1 1 1 1

                         hour.add(mHour.get(i));
                         action.add(mActions.get(i));

                 }

             }

            initRecycleView();
        }
        else
        {
         hour.clear();
         action.clear();
            initRecycleView();
        }



        Log.d("index :-",mDate.indexOf(d)+"");

    }// end Method





    } // end Class

