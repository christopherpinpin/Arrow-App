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


public class AllCurrentProfs extends AppCompatActivity {

    RecyclerView.Adapter adapter;
    
    private int collegeProfessorsCount = 0;
    private ArrayList<String> allProfs = new ArrayList<String>();
    
    // Firebase
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;

    // Views
    ArrayList<RecommendedHelperClass> dataProfs = new ArrayList<>();
    
    
    RecyclerView currentProfsRecycler;
    ImageView iv_home;
    ImageView iv_profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.all_current_profs);

        //Hooks
        currentProfsRecycler = findViewById(R.id.allProfs_Recycler);

//        currentProfessors();

        this.viewHome();
        this.viewProfile();

        initFirebase();

        getCollegeProfessorsCount();


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
                Intent i = new Intent(AllCurrentProfs.this, userDashboard.class);
                startActivity(i);
            }
        });
    }

    private void viewProfile() {
        this.iv_profile= findViewById(R.id.iv_profile);
        this.iv_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AllCurrentProfs.this, OwnProfileActivity.class);
                startActivity(i);
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
                        displayCollegeProfs();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void displayCollegeProfs() {
        currentProfsRecycler.setHasFixedSize(true);
        currentProfsRecycler.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false));

        for (int i = 1; i <= collegeProfessorsCount; i++){
            database.getReference().child("professors").child(String.format("%07d", i))
                    .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.isSuccessful()){
                        DataSnapshot snapshot = task.getResult();

                        String pronoun = String.valueOf(snapshot.child("pronoun").getValue());
                        String lname = String.valueOf(snapshot.child("lName").getValue());
                        String college = String.valueOf(snapshot.child("college").getValue());
                        float rating = Float.parseFloat(String.valueOf(snapshot.child("overallRating").getValue()));
                        String pic = String.valueOf(snapshot.child("pic").getValue());


                        dataProfs.add(new RecommendedHelperClass(getResources().getIdentifier(pic, "drawable", getPackageName()) ,pronoun + " " + lname, college,  rating));

                        adapter = new AllCardsAdapter(dataProfs);
                        currentProfsRecycler.setAdapter(adapter);
                    }
                }
            });
        }
    }
}