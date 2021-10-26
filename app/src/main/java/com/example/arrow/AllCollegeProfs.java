package com.example.arrow;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class AllCollegeProfs extends AppCompatActivity {

    ImageView iv_home;
    ImageView iv_profile;

    RecyclerView collegeProfsRecycler;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    ArrayList<RecommendedHelperClass> topRatedProfs = new ArrayList<>();
    int topRatedProfessorsCount = 0;

    RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.all_college_profs);

        //Hooks
        collegeProfsRecycler = findViewById(R.id.allCollege_Recycler);

        initFirebase();
        getTopRatedProfessorsCount();

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
                Intent i = new Intent(AllCollegeProfs.this, userDashboard.class);
                startActivity(i);
            }
        });
    }

    private void viewProfile() {
        this.iv_profile= findViewById(R.id.iv_profile);
        this.iv_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AllCollegeProfs.this, OwnProfileActivity.class);
                startActivity(i);
            }
        });
    }

    private void getTopRatedProfessorsCount() {
        database.getReference().child("professors").orderByChild("overallRating")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                      //  Log.d("FIREBASE TOP RATED", ""+snapshot.getChildrenCount());
                        topRatedProfessorsCount = (int) snapshot.getChildrenCount();
                        TopRatedProfessors();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }

    private void TopRatedProfessors() {
        collegeProfsRecycler.setHasFixedSize(true);
        collegeProfsRecycler.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false));

        for (int i = 1; i <= topRatedProfessorsCount; i++){

            database.getReference().child("professors").child(String.format("%07d", i))
                    .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.isSuccessful()){
                        DataSnapshot snapshot = task.getResult();
                        Log.d("FIREBASE TOP RATED", ""+ snapshot.child("lName").getValue());
                        String pronoun = String.valueOf(snapshot.child("pronoun").getValue());
                        String lname = String.valueOf(snapshot.child("lName").getValue());
                        String college = String.valueOf(snapshot.child("college").getValue());
                        float rating = Float.parseFloat(String.valueOf(snapshot.child("overallRating").getValue()));
                        String pic = String.valueOf(snapshot.child("pic").getValue());

                        topRatedProfs.add(new RecommendedHelperClass(getResources().getIdentifier(pic, "drawable", getPackageName()) ,pronoun + " " + lname, college, rating));
                        topRatedProfs.sort(new RateSorter());
                        adapter = new AllCardsAdapter(topRatedProfs);
                        collegeProfsRecycler.setAdapter(adapter);
                    }
                }
            });
        }
    }



    private void collegeProfessors() {
        collegeProfsRecycler.setHasFixedSize(true);
        collegeProfsRecycler.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false));

        ArrayList<RecommendedHelperClass> collProfs = new ArrayList<>();

        collProfs.add(new RecommendedHelperClass(R.drawable.prof_sample, "Mrs. Cruz", "IT Professor"));
        collProfs.add(new RecommendedHelperClass(R.drawable.prof_sample, "Mrs. Santos", "Team Sports Professor"));
        collProfs.add(new RecommendedHelperClass(R.drawable.prof_sample, "Mr. Perez", "History Professor"));
        collProfs.add(new RecommendedHelperClass(R.drawable.prof_sample, "Mrs. Cruz", "IT Professor"));
        collProfs.add(new RecommendedHelperClass(R.drawable.prof_sample, "Mrs. Santos", "Team Sports Professor"));
        collProfs.add(new RecommendedHelperClass(R.drawable.prof_sample, "Mr. Perez", "History Professor"));
        collProfs.add(new RecommendedHelperClass(R.drawable.prof_sample, "Mrs. Cruz", "IT Professor"));
        collProfs.add(new RecommendedHelperClass(R.drawable.prof_sample, "Mrs. Santos", "Team Sports Professor"));
        collProfs.add(new RecommendedHelperClass(R.drawable.prof_sample, "Mr. Perez", "History Professor"));
        collProfs.add(new RecommendedHelperClass(R.drawable.prof_sample, "Mrs. Cruz", "IT Professor"));



        adapter = new AllCardsAdapter(collProfs);
        collegeProfsRecycler.setAdapter(adapter);
    }
}