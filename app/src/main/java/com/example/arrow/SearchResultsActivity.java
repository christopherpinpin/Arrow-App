package com.example.arrow;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class SearchResultsActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;

    int collegeProfessorsCount = 0;

    ArrayList<RecommendedHelperClass> resultItem = new ArrayList<>();

    ImageView iv_home;
    ImageView iv_profile;

    RecyclerView searchResultsRecycler;
    TextView tvSubmitRequest;
    TextView tvSearchedInfo;

    // Search
    String searchItem;

    RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.search_results);
        Intent i = getIntent();
        tvSearchedInfo = findViewById(R.id.searched_info);
        searchItem = i.getStringExtra("SEARCH");
        tvSearchedInfo.setText(searchItem);
        //Hooks
        searchResultsRecycler = findViewById(R.id.searchResults_Recycler);
        //displaySearchResults();
        submitRequest();

        initFirebase();
        getCollegeProfessorsCount();
        //SearchResults();

        this.viewHome();
        this.viewProfile();
    }

    private void viewHome() {
        this.iv_home= findViewById(R.id.iv_home);
        this.iv_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SearchResultsActivity.this, userDashboard.class);
                startActivity(i);
            }
        });
    }

    private void viewProfile() {
        this.iv_profile= findViewById(R.id.iv_profile);
        this.iv_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SearchResultsActivity.this, OwnProfileActivity.class);
                startActivity(i);
            }
        });
    }

    private void submitRequest() {
        this.tvSubmitRequest = findViewById(R.id.submit_request);
        this.tvSubmitRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SearchResultsActivity.this, SubmitRequestActivity.class);
                startActivity(i);
            }
        });
    }

    private void initFirebase() {
        this.mAuth = FirebaseAuth.getInstance();
        this.database = FirebaseDatabase.getInstance("https://arrow-848c3-default-rtdb.asia-southeast1.firebasedatabase.app/");
    }

    private void getCollegeProfessorsCount() {
        database.getReference().child("professors")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Log.d("FIREBASE-SEARCH", ""+snapshot.getChildrenCount());
                        collegeProfessorsCount = (int) snapshot.getChildrenCount();
                        SearchResults();



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }


    private void SearchResults(){
        searchResultsRecycler.setHasFixedSize(true);
        searchResultsRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        for (int i = 1; i <= collegeProfessorsCount; i++){
            database.getReference().child("professors").child(String.format("%07d", i))
                    .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {



                  //  String name = "Jules Tan";



                  //  Log.d("SearchItem", ""+searchItem);

                    if (task.isSuccessful()){
                        DataSnapshot snapshot = task.getResult();

                        String profFullname = String.valueOf(snapshot.child("fName").getValue())+" "+String.valueOf(snapshot.child("lName").getValue());

                       // Log.d("SearchItem", ""+searchItem);
                        Log.d("profFullName", ""+profFullname);
                        //Log.d("LastNames", ""+ String.valueOf(snapshot.child("lName").getValue()));

                        //Log.d("BOOL",""+(String.valueOf(snapshot.child("lName").getValue()).equals(searchItem) ));

                        // search for Last Name
                        if(String.valueOf(snapshot.child("lName").getValue()).equalsIgnoreCase(searchItem) ) {
                            String pronoun = String.valueOf(snapshot.child("pronoun").getValue());
                            String lname = String.valueOf(snapshot.child("lName").getValue());
                            String college = String.valueOf(snapshot.child("college").getValue());
                            float rating = Float.parseFloat(String.valueOf(snapshot.child("overallRating").getValue()));
                            String pic = String.valueOf(snapshot.child("pic").getValue());

                            resultItem.add(new RecommendedHelperClass(getResources().getIdentifier(pic, "drawable", getPackageName()),pronoun + " " + lname, college, rating));
                            adapter = new AddFeaturedProfAdapter(resultItem);
                            searchResultsRecycler.setAdapter(adapter);
                        }

                        // search for First Name
                        if(String.valueOf(snapshot.child("fName").getValue()).equalsIgnoreCase(searchItem) ) {
                            String pronoun = String.valueOf(snapshot.child("pronoun").getValue());
                            String lname = String.valueOf(snapshot.child("lName").getValue());
                            String college = String.valueOf(snapshot.child("college").getValue());
                            float rating = Float.parseFloat(String.valueOf(snapshot.child("overallRating").getValue()));
                            String pic = String.valueOf(snapshot.child("pic").getValue());





                            resultItem.add(new RecommendedHelperClass(getResources().getIdentifier(pic, "drawable", getPackageName()) ,pronoun + " " + lname, college, rating));

                            adapter = new AddFeaturedProfAdapter(resultItem);
                            searchResultsRecycler.setAdapter(adapter);

                        }

                        // search for College

                        if(String.valueOf(snapshot.child("college").getValue()).equalsIgnoreCase(searchItem) ) {
                            String pronoun = String.valueOf(snapshot.child("pronoun").getValue());
                            String lname = String.valueOf(snapshot.child("lName").getValue());
                            String college = String.valueOf(snapshot.child("college").getValue());
                            float rating = Float.parseFloat(String.valueOf(snapshot.child("overallRating").getValue()));
                            String pic = String.valueOf(snapshot.child("pic").getValue());


                            resultItem.add(new RecommendedHelperClass(getResources().getIdentifier(pic, "drawable", getPackageName()) ,pronoun + " " + lname, college, rating));

                            adapter = new AddFeaturedProfAdapter(resultItem);
                            searchResultsRecycler.setAdapter(adapter);

                        }

                        if(profFullname.equalsIgnoreCase(searchItem) ) {
                            String pronoun = String.valueOf(snapshot.child("pronoun").getValue());
                            String lname = String.valueOf(snapshot.child("lName").getValue());
                            String college = String.valueOf(snapshot.child("college").getValue());
                            float rating = Float.parseFloat(String.valueOf(snapshot.child("overallRating").getValue()));
                            String pic = String.valueOf(snapshot.child("pic").getValue());


                            resultItem.add(new RecommendedHelperClass(getResources().getIdentifier(pic, "drawable", getPackageName()),pronoun + " " + lname, college, rating));

                            adapter = new AddFeaturedProfAdapter(resultItem);
                            searchResultsRecycler.setAdapter(adapter);

                        }
                    }
                }
            });
        }
    }

    private void displaySearchResults() {
        searchResultsRecycler.setHasFixedSize(true);
        searchResultsRecycler.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false));

        ArrayList<RecommendedHelperClass> resultItem = new ArrayList<>();

        resultItem.add(new RecommendedHelperClass(R.drawable.prof_sample, "Mrs. Cruz", "IT Professor"));
        resultItem.add(new RecommendedHelperClass(R.drawable.prof_sample, "Mrs. Santos", "Team Sports Professor"));
        resultItem.add(new RecommendedHelperClass(R.drawable.prof_sample, "Mr. Perez", "History Professor"));
        resultItem.add(new RecommendedHelperClass(R.drawable.prof_sample, "Mrs. Cruz", "IT Professor"));
        resultItem.add(new RecommendedHelperClass(R.drawable.prof_sample, "Mrs. Santos", "Team Sports Professor"));
        resultItem.add(new RecommendedHelperClass(R.drawable.prof_sample, "Mr. Perez", "History Professor"));
        resultItem.add(new RecommendedHelperClass(R.drawable.prof_sample, "Mrs. Cruz", "IT Professor"));
        resultItem.add(new RecommendedHelperClass(R.drawable.prof_sample, "Mrs. Santos", "Team Sports Professor"));
        resultItem.add(new RecommendedHelperClass(R.drawable.prof_sample, "Mr. Perez", "History Professor"));
        resultItem.add(new RecommendedHelperClass(R.drawable.prof_sample, "Mrs. Cruz", "IT Professor"));

        adapter = new AddFeaturedProfAdapter(resultItem);
        searchResultsRecycler.setAdapter(adapter);
    }
}