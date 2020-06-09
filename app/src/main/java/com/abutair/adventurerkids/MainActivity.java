package com.abutair.adventurerkids;

import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.abutair.adventurerkids.DoctorCheck.DoctorCheck;
import com.abutair.adventurerkids.WeeklyPlan.plan;
import com.abutair.adventurerkids.activity.Activity;
import com.abutair.adventurerkids.camera.ExampleDialog;
import com.abutair.adventurerkids.events.events;

import com.abutair.adventurerkids.notes.Note;
import com.abutair.adventurerkids.notifications.Notification;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;


import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

     // Vars
    CardView c1,c2,c3,c4,c5 ;
    String  username;
    private CircleImageView ProfileImage;
    private StorageReference storageref;
    private DatabaseReference mDatabase;
    private Uri mainImage = null;
    private  boolean isChanged  = false;
       String t= "",m="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        c1 = findViewById(R.id.activity);
        c2 = findViewById(R.id.notification_card);
        c3 = findViewById(R.id.doctor_card);
        c4 = findViewById(R.id.plan_cardview);
        c5= findViewById(R.id.live_camera);
        storageref = FirebaseStorage.getInstance().getReference();
        ProfileImage = findViewById(R.id.profile_image);
         ProfileImage.setOnClickListener(this);
        AuthCheck();
        RetriveImg();
        intOnClickEvents();
       Notification () ;


    } // end OnCreate

      private  void intOnClickEvents()
    {

        c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Activity.class);
                startActivity(i);

            }
        });

        c2.setOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick(View v) {
               Intent i = new Intent(MainActivity.this, Notification.class);
               i.putExtra("msg",m);
               startActivity(i);
           }
       });


        c3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, DoctorCheck.class);
                startActivity(i);
            }
        });


        c4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, plan.class);
                startActivity(i);
            }
        });

        c5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Note.class);
                i.putExtra("username",username);
                startActivity(i);
            }
        });

    }


    public void openDialog()
    {
        ExampleDialog exampleDialog = new ExampleDialog();
        exampleDialog.show(getSupportFragmentManager(),"");
    }

    public  void SharedData(String s)
    {
            SharedPreferences sharedPreferences = getSharedPreferences("myPerf",MODE_PRIVATE);
        SharedPreferences.Editor myedit =  sharedPreferences.edit();
        myedit.putString("UserName",s);
        myedit.commit();
    }

    @Override
    public void onClick(View v) {
        CheckUserPermisson() ;

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mainImage = result.getUri();
                ProfileImage.setImageURI(mainImage);
                isChanged = true;
                uploadImage();

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();

            }
        }
    }


    private void BringImagePicker() {

        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1,1)
                .start(MainActivity.this);
    } // end Method


    private void CheckUserPermisson()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)

            {
                ActivityCompat.requestPermissions(MainActivity.this , new String [] {Manifest.permission.READ_EXTERNAL_STORAGE},1);

            }
            else
            {
                BringImagePicker();
            }


        }
        else
        {
            BringImagePicker();
        }
    }


   private  void RetriveImg()
   {
     mDatabase = FirebaseDatabase.getInstance().getReference("ProfileImages");

     mDatabase.addValueEventListener(new ValueEventListener() {
         @Override
         public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

             if (dataSnapshot.child(username).exists())
             {
                 RequestOptions placeholderRequset = new RequestOptions() ;
                 placeholderRequset.placeholder(R.drawable.blank_profile_img);
                 String Path = dataSnapshot.child(username).getValue(String.class);
                 Glide.with(MainActivity.this)
                         .applyDefaultRequestOptions(placeholderRequset)
                         .load(Path)
                         .into(ProfileImage);
             }

         }

         @Override
         public void onCancelled(@NonNull DatabaseError databaseError) {

         }
     }) ;



   }

    private  void uploadImage()
    {
        final StorageReference ref = storageref.child("ProfileImages/"+username);
        ref.putFile(mainImage)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(MainActivity.this,"Profile Updated",Toast.LENGTH_LONG).show();
                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                DatabaseReference imageStore = FirebaseDatabase.getInstance().getReference()
                                        .child("ProfileImages");
                                HashMap<String,String> hashMap = new HashMap<>();
                                hashMap.put(username,String.valueOf(uri));
                                imageStore.setValue(hashMap);
                            }
                        }) ;

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();

            }
        });
    }



   private void Notification ()
    {
       FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
          DatabaseReference  myRef = firebaseDatabase.getReference("notif");

           //String bla = myRef.child("").

          myRef.addValueEventListener(new ValueEventListener() {
              @Override
              public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                  for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                  {
                      t= dataSnapshot1.child("title").getValue(String.class);
                      m = dataSnapshot1.child("desc").getValue(String.class);
                      CreateNotification(t,m);


                  }

              }

              @Override
              public void onCancelled(@NonNull DatabaseError databaseError) {

              }
          });



    }

    private void CreateNotification(String s , String s2) {



            NotificationCompat.Builder builder = new NotificationCompat.Builder(
                    MainActivity.this
            ).setContentTitle(s)
                    .setContentText(s2)
                    .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                    .setAutoCancel(true);
            Intent i = new Intent(MainActivity.this, Notification.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.putExtra("msg",m);
            PendingIntent  pendingIntent = PendingIntent.getActivity(MainActivity.this,
                    0,i,PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);

            NotificationManager notificationManager = (NotificationManager) getSystemService(
                    Context.NOTIFICATION_SERVICE
            );

            notificationManager.notify(0,builder.build());



    }

    private void AuthCheck()
    {
        SharedPreferences sharedPreferences = getSharedPreferences("myPerf", 0);
        username = sharedPreferences.getString("UserName", "empty");
        if (username.equals("empty")) {

            Intent i = new Intent(MainActivity.this, login.class);
            startActivity(i);
            finish();
        } else {
            SharedData(username);

        }
    }
} // end Class
