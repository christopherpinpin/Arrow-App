package com.example.arrow;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;




public class AllRecommendedProfs extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;

    int userGrading=0;
    int userAttendance=0;
    int userSync = 0;
    int collegeProfessorsCount = 0;


    ArrayList<RecommendedHelperClass> recProfs = new ArrayList<>();

    ImageView iv_home;
    ImageView iv_profile;

    RecyclerView recommendedRecycler;

    RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.all_recommended_profs);

        //Hooks
        recommendedRecycler = findViewById(R.id.allRecommended_Recycler);

        //recommendedProfessors();
        initFirebase();
        getCollegeProfessorsCount();
        getRecCount();

        this.viewHome();
        this.viewProfile();


    }

    private void initFirebase() {
        this.mAuth = FirebaseAuth.getInstance();
        this.database = FirebaseDatabase.getInstance("https://arrow-848c3-default-rtdb.asia-southeast1.firebasedatabase.app/");
    }
    private void viewHome() {
        this.iv_home= findViewById(R.id.iv_home);
        this.iv_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AllRecommendedProfs.this, userDashboard.class);
                startActivity(i);
            }
        });
    }

    private void viewProfile() {
        this.iv_profile= findViewById(R.id.iv_profile);
        this.iv_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AllRecommendedProfs.this, OwnProfileActivity.class);
                startActivity(i);
            }
        });
    }

    private void getRecCount() {

        database.getReference().child("users").child(mAuth.getUid())
                .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot snapshot = task.getResult();
                    userAttendance= Integer.parseInt(String.valueOf(snapshot.child("attendance").getValue()));
                    userSync = Integer.parseInt(String.valueOf(snapshot.child("sync").getValue()));
                    userGrading=Integer.parseInt(String.valueOf(snapshot.child("grading").getValue()));
                    Log.d("GRADING", ""+userGrading);

                    tryRec();

                }

            }
        });





    }

    private void getCollegeProfessorsCount() {
        database.getReference().child("professors")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Log.d("FIREBASE", ""+snapshot.getChildrenCount());
                        collegeProfessorsCount = (int) snapshot.getChildrenCount();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }


    private void tryRec(){
        recommendedRecycler.setHasFixedSize(true);
        recommendedRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        //CurrentProfessorsRecycler.setHasFixedSize(true);
        //CurrentProfessorsRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        for (int i = 1; i <= collegeProfessorsCount; i++){
            database.getReference().child("professors").child(String.format("%07d", i))
                    .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.isSuccessful()){
                        DataSnapshot snapshot = task.getResult();

                        if((Integer.parseInt(String.valueOf(snapshot.child("grading").getValue())) == userGrading)) {

                            String pronoun = String.valueOf(snapshot.child("pronoun").getValue());
                            String lname = String.valueOf(snapshot.child("lName").getValue());
                            String college = String.valueOf(snapshot.child("college").getValue());
                            float rating = Float.parseFloat(String.valueOf(snapshot.child("overallRating").getValue()));
                            String pic = String.valueOf(snapshot.child("pic").getValue());


                            recProfs.add(new RecommendedHelperClass(getResources().getIdentifier(pic, "drawable", getPackageName()) ,pronoun + " " + lname, college, rating));

                            adapter = new AllCardsAdapter(recProfs);
                            recommendedRecycler.setAdapter(adapter);

                        } else if ((Integer.parseInt(String.valueOf(snapshot.child("attendance").getValue())) == userAttendance)) {
                            String pronoun = String.valueOf(snapshot.child("pronoun").getValue());
                            String lname = String.valueOf(snapshot.child("lName").getValue());
                            String college = String.valueOf(snapshot.child("college").getValue());
                            float rating = Float.parseFloat(String.valueOf(snapshot.child("overallRating").getValue()));
                            String pic = String.valueOf(snapshot.child("pic").getValue());


                            recProfs.add(new RecommendedHelperClass(getResources().getIdentifier(pic, "drawable", getPackageName()) ,pronoun + " " + lname, college, rating));


                            adapter = new AllCardsAdapter(recProfs);
                            recommendedRecycler.setAdapter(adapter);
                        }

                        else if ((Integer.parseInt(String.valueOf(snapshot.child("sync").getValue())) == userSync)) {
                            String pronoun = String.valueOf(snapshot.child("pronoun").getValue());
                            String lname = String.valueOf(snapshot.child("lName").getValue());
                            String college = String.valueOf(snapshot.child("college").getValue());
                            float rating = Float.parseFloat(String.valueOf(snapshot.child("overallRating").getValue()));

                            recProfs.add(new RecommendedHelperClass(R.drawable.prof_sample ,pronoun + " " + lname, college, rating));

                            adapter = new AllCardsAdapter(recProfs);
                            recommendedRecycler.setAdapter(adapter);
                        }

                    }
                }
            });
        }





    }


    /**private void recommendedProfessors() {
        recommendedProfsRecycler.setHasFixedSize(true);
        recommendedProfsRecycler.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false));

        ArrayList<RecommendedHelperClass> recommendedProfs = new ArrayList<>();

        recommendedProfs.add(new RecommendedHelperClass(R.drawable.prof_sample, "Mrs. Cruz", "IT Professor"));
        recommendedProfs.add(new RecommendedHelperClass(R.drawable.prof_sample, "Mrs. Santos", "Team Sports Professor"));
        recommendedProfs.add(new RecommendedHelperClass(R.drawable.prof_sample, "Mr. Perez", "History Professor"));
        recommendedProfs.add(new RecommendedHelperClass(R.drawable.prof_sample, "Mrs. Cruz", "IT Professor"));
        recommendedProfs.add(new RecommendedHelperClass(R.drawable.prof_sample, "Mrs. Santos", "Team Sports Professor"));
        recommendedProfs.add(new RecommendedHelperClass(R.drawable.prof_sample, "Mr. Perez", "History Professor"));
        recommendedProfs.add(new RecommendedHelperClass(R.drawable.prof_sample, "Mrs. Cruz", "IT Professor"));
        recommendedProfs.add(new RecommendedHelperClass(R.drawable.prof_sample, "Mrs. Santos", "Team Sports Professor"));
        recommendedProfs.add(new RecommendedHelperClass(R.drawable.prof_sample, "Mr. Perez", "History Professor"));
        recommendedProfs.add(new RecommendedHelperClass(R.drawable.prof_sample, "Mrs. Cruz", "IT Professor"));



        adapter = new AllCardsAdapter(recommendedProfs);
        recommendedProfsRecycler.setAdapter(adapter);
    }**/



}