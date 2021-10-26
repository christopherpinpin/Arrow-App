package com.example.arrow;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RateProfessor extends AppCompatActivity {

    private Spinner sp_editCollege;
    ImageView iv_home;
    ImageView iv_profile;

    RadioGroup rgSync;
    RadioButton rbSync;

    RadioGroup rgAttendance;
    RadioButton rbAttendance;

    RadioGroup rgGrading;
    RadioButton rbGrading;

    RatingBar rateOverall;

    EditText etReview;

    Button btnSubmit;

    String fname;
    String lname;
    String college;

    String profUID;
    String profname;

    int reviewCount;
    int profsCount;
    int ratedCount;

    // Firebase
    FirebaseAuth mAuth;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.rate_professor);

        this.viewHome();
        this.viewProfile();

        initFirebase();
        initComponents();
    }

    private void initFirebase() {
        this.mAuth = FirebaseAuth.getInstance();
        this.database = FirebaseDatabase.getInstance("https://arrow-848c3-default-rtdb.asia-southeast1.firebasedatabase.app/");
    }

    private void initComponents() {
        getName();

        getProfsCount();
        getRatedCount();

        Intent i = getIntent();
        profname = i.getStringExtra("NAME");

        this.rgSync = findViewById(R.id.rg_sync);
        this.rgAttendance = findViewById(R.id.rg_attendance);
        this.rgGrading = findViewById(R.id.rg_gradingcriteria);
        this.rateOverall = findViewById(R.id.ratingBarOverall);
        this.etReview = findViewById(R.id.et_reviewdesc);
        this.btnSubmit = findViewById(R.id.btn_submitreview);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedSync = rgSync.getCheckedRadioButtonId();
                rbSync = (RadioButton) findViewById(selectedSync);
                int sync = convertSyncToInt(rbSync.getText().toString());

                int selectedAttendance = rgAttendance.getCheckedRadioButtonId();
                rbAttendance = (RadioButton) findViewById(selectedAttendance);
                int attendance = convertAttendanceToInt(rbAttendance.getText().toString());

                int selectedGrading = rgGrading.getCheckedRadioButtonId();
                rbGrading = (RadioButton) findViewById(selectedGrading);
                int grading = convertGradingToInt(rbGrading.getText().toString());
                float rating = (float) rateOverall.getRating();
                updateOverallRating(profUID, rating);

                String comment = etReview.getText().toString().trim();

                String UID = mAuth.getUid();

                Review review = new Review(fname, lname, sync, attendance, grading, rating, comment, UID, college);

                database.getReference().child("professors").child(profUID).child("reviews").child(""+reviewCount)
                        .setValue(review).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            addToRated();
                            successReview();
                        }
                    }
                });
            }
        });
    }

    public void updateOverallRating(String UID, float rating){

        database.getReference().child("professors").child(UID)
                .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                Log.d("task successful", "1");

                if (task.isSuccessful()) {
                    DataSnapshot snapshot = task.getResult();
                    Log.d("task successful", ""+snapshot.child("overallRating").getValue() );
                    float curRating = Float.parseFloat(String.valueOf(snapshot.child("overallRating").getValue()));
                    DatabaseReference tempdb = database.getReference().child("professors").child(UID);
                    tempdb.child("overallRating").setValue((rating + curRating)/2.0);
                }
            }
        });
    }

    private void getRatedCount(){
        database.getReference().child("users").child(mAuth.getUid()).child("rated")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ratedCount = (int) snapshot.getChildrenCount();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void addToRated(){
        database.getReference().child("users").child(mAuth.getUid()).child("rated").child(""+ratedCount).setValue(profUID);
    }

    private void getReviewCount(){
        database.getReference().child("professors").child(profUID).child("reviews")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Log.d("FIREBASE", ""+snapshot.getChildrenCount());
                        reviewCount = (int) snapshot.getChildrenCount();
                        Log.d("REVIEW COUNT", ""+reviewCount);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void getProfUID(){
        for (int i = 1; i <= profsCount; i++){
            database.getReference().child("professors").child(String.format("%07d", i))
                    .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.isSuccessful()){
                        DataSnapshot snapshot = task.getResult();
                        if (String.valueOf(snapshot.child("lName").getValue()).equals(profname)) {
                            profUID = snapshot.getKey();
                            Log.d("PROF UID", snapshot.getKey());
                            getReviewCount();
                        }
                    }
                }
            });
        }
    }

    private void getProfsCount(){
        database.getReference().child("professors")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Log.d("FIREBASE", ""+snapshot.getChildrenCount());
                        profsCount = (int) snapshot.getChildrenCount();
                        getProfUID();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void getName(){
        database.getReference("users").child(mAuth.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()){
                            DataSnapshot snapshot = task.getResult();

                            fname = String.valueOf(snapshot.child("fName").getValue());
                            lname = String.valueOf(snapshot.child("lName").getValue());
                            college = String.valueOf(snapshot.child("college").getValue());
                        }
                    }
                });
    }

    private int convertSyncToInt(String text){
        if (text.equals("None")){
            return 1;
        } else if (text.equals("Few")){
            return 2;
        } else if (text.equals("Moderate")){
            return 3;
        } else if (text.equals("More")){
            return 4;
        } else {
            return 5;
        }
    }

    private int convertAttendanceToInt(String text){
        if (text.equals("None")){
            return 1;
        } else {
            return 2;
        }
    }

    private int convertGradingToInt(String text){
        if (text.equals("Pure Output-based")){
            return 1;
        } else if (text.equals("More Output-based")){
            return 2;
        } else if (text.equals("Half Output/Exams")){
            return 3;
        } else if (text.equals("More Exams based")){
            return 4;
        } else {
            return 5;
        }
    }

    private void successReview() {
        Toast.makeText(this, "Review Added.", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(RateProfessor.this, userDashboard.class);
        startActivity(i);
    }

    private void viewHome() {
        this.iv_home= findViewById(R.id.iv_home);
        this.iv_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RateProfessor.this, userDashboard.class);
                startActivity(i);
            }
        });
    }

    private void viewProfile() {
        this.iv_profile= findViewById(R.id.iv_profile);
        this.iv_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RateProfessor.this, OwnProfileActivity.class);
                startActivity(i);
            }
        });
    }

}
