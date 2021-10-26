package com.example.arrow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class OwnProfileActivity extends AppCompatActivity {

    RecyclerView rvFeaturedProfs;
    RecyclerView rvRatedProfs;
    RecyclerView.Adapter adapter;
    ImageView iv_home;
    ImageView profileImage;
    TextView tvLogout;

    TextView tvFname;
    TextView tvLname;
    TextView editProfileButton;

    // Counts
    private int featuredCount = 0;
    private ArrayList<String> allFeatured = new ArrayList<String>();
    private int ratedCount = 0;
    private ArrayList<String> allRated = new ArrayList<String>();

    // Firebase
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;

    // Views
    ArrayList<MyCardHelperClass> dataProfs = new ArrayList<>();
    ArrayList<MyCardHelperClass> dataRated = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_profile);

        initFirebase();

        tvFname = findViewById(R.id.profile_firstName);
        tvLname = findViewById(R.id.profile_lastName);

        profileImage = findViewById(R.id.prof_Image);

        String studentID = mAuth.getUid();
        Log.d("student ID?", ""+studentID);

        /**Intent intent = getIntent();
        if(intent.getStringExtra(CommentCardAdapter.KEY_NAME) != "")
            studentID = intent.getStringExtra(CommentCardAdapter.KEY_NAME);**/

        Log.d("student ID 2", ""+studentID);


        changeHeading(studentID);

        rvFeaturedProfs = findViewById(R.id.myFeat_Recycler);
        rvRatedProfs = findViewById(R.id.myRated_Recycler);

        tvLogout = findViewById(R.id.logout_name);

        displayMyFeaturedProfessors(studentID);
        displayMyRatedProfessors(studentID);

        //this.viewAddFeaturedPage();
        this.viewHome();
        this.viewEditProf();


        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent i = new Intent(OwnProfileActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void viewEditProf() {
        this.editProfileButton= findViewById(R.id.editprofile_button);
        this.editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(OwnProfileActivity.this, EditProfile.class);
                startActivity(i);
            }
        });
    }

    private void changeHeading(String ID) {
        database.getReference().child("users").child(ID)
                .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot snapshot = task.getResult();
                    String fname = String.valueOf(snapshot.child("fName").getValue());
                    String pfp =  String.valueOf(snapshot.child("pfp").getValue());
                    String lname = String.valueOf(snapshot.child("lName").getValue());
                    tvFname.setText(fname);
                    tvLname.setText(lname);
                    profileImage.setImageResource(getResources().getIdentifier(pfp , "drawable", getPackageName()));
                }
            }
        });
    }


    private void initFirebase() {
        this.mAuth = FirebaseAuth.getInstance();
        this.database = FirebaseDatabase.getInstance("https://arrow-848c3-default-rtdb.asia-southeast1.firebasedatabase.app/");
    }

    private void displayMyFeaturedProfessors(String id) {
        getFeaturedCount(id);
    }

    private void getFeaturedCount(String id) {
        database.getReference().child("users").child(id).child("featured")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Log.d("FIREBASE", ""+snapshot.getChildrenCount());
                        featuredCount = (int) snapshot.getChildrenCount();
                        getFeatured(id);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void getFeatured(String id) {
        Log.d("FIREBASE", ""+featuredCount);
        DatabaseReference tempdb = database.getReference().child("users").child(id).child("featured");
        for (int i = 0; i < featuredCount; i++){
            tempdb.child(Integer.toString(i))
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String value = snapshot.getValue().toString();
                            allFeatured.add(value);
                            Log.d("ALL FEATURED", ""+allFeatured);
                            if (allFeatured.size() == featuredCount){
                                displayFeatured();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }
    }

    private void displayFeatured() {
        rvFeaturedProfs.setHasFixedSize(true);
        rvFeaturedProfs.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        for (int i = 0; i < featuredCount; i++){
            database.getReference().child("professors").child(allFeatured.get(i))
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


                        dataProfs.add(new MyCardHelperClass(pronoun + " " + lname, college, getResources().getIdentifier(pic, "drawable", getPackageName()), rating));

                        adapter = new OtherProfsAdapter(dataProfs);
                        rvFeaturedProfs.setAdapter(adapter);
                    }
                }
            });
        }
    }

    private void viewHome() {
        this.iv_home= findViewById(R.id.iv_home);
        this.iv_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(OwnProfileActivity.this, userDashboard.class);
                startActivity(i);
            }
        });
    }

    private void displayMyRatedProfessors(String id) {
        getRatedCount(id);
    }

    private void getRatedCount(String id) {
        database.getReference().child("users").child(id).child("rated")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Log.d("FIREBASE", ""+snapshot.getChildrenCount());
                        ratedCount = (int) snapshot.getChildrenCount();
                        getRated(id);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void getRated(String id) {
        Log.d("FIREBASE", ""+ratedCount);
        DatabaseReference tempdb = database.getReference().child("users").child(id).child("rated");
        for (int i = 0; i < ratedCount; i++){
            tempdb.child(Integer.toString(i))
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String value = snapshot.getValue().toString();
                            allRated.add(value);
                            Log.d("ALL RATED", ""+allRated);
                            if (allRated.size() == ratedCount){
                                displayRated();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
        }
    }

    private void displayRated() {
        rvRatedProfs.setHasFixedSize(true);
        rvRatedProfs.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        for (int i = 0; i < ratedCount; i++){
            database.getReference().child("professors").child(allRated.get(i))
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


                        dataRated.add(new MyCardHelperClass(pronoun + " " + lname, college, getResources().getIdentifier(pic, "drawable", getPackageName()), rating));

                        adapter = new MyProfsAdapter(dataRated);
                        rvRatedProfs.setAdapter(adapter);
                    }
                }
            });
        }
    }
}