package com.example.arrow;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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


public class ProfessorProfile extends AppCompatActivity {

    RecyclerView commentsRecycler;
    TextView name;
    ImageView img;
    ImageView iv_profile;
    ImageView iv_home;
    ImageView commentButton;
    RatingBar rbRating;
    ImageView mv_image;

    Button btn_addFeatured;

    // Firebase
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;

    ArrayList<CommentHelperClass> commentItem = new ArrayList<>();
    ArrayList<String> lNames = new ArrayList<>();

    int featuredCount;

    int commentCount = 0;
    int collegeProfessorsCount = 0;
    String profId;
    int i;
    //TextView delete_review;

    String loggedName;

    String profUID = "";

    Boolean bool;

    RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.professor_profile);

        //Hooks
        commentsRecycler = findViewById(R.id.comments_Recycler);
        name = findViewById(R.id.name);
        rbRating = findViewById(R.id.prof_rating);
        mv_image=findViewById(R.id.mv_image);
        btn_addFeatured = findViewById(R.id.btn_addToFeatured);

        Intent intent = getIntent();

        String profname = "";
        if(intent.getStringExtra(CollegeProfAdapter.KEY_NAME) != "")
            profname = intent.getStringExtra(CollegeProfAdapter.KEY_NAME);
        else if(intent.getStringExtra(AllCardsAdapter.KEY_NAME) != "")
            profname = intent.getStringExtra(AllCardsAdapter.KEY_NAME);
        else if(intent.getStringExtra(RecommendedAdapter.KEY_NAME) != "")
            profname = intent.getStringExtra(RecommendedAdapter.KEY_NAME);

        this.name.setText(profname);

        float rating = -1;
        if(intent.getFloatExtra(CollegeProfAdapter.KEY_RATING, 0) != -1)
            rating = intent.getFloatExtra(CollegeProfAdapter.KEY_RATING, 0);
        else if(intent.getFloatExtra(AllCardsAdapter.KEY_RATING, 0) != -1)
            rating = intent.getFloatExtra(AllCardsAdapter.KEY_RATING, 0);
        else if(intent.getFloatExtra(RecommendedAdapter.KEY_RATING, 0) != -1)
            rating = intent.getFloatExtra(RecommendedAdapter.KEY_RATING, 0);

        this.rbRating.setRating(rating);


//        int profimg = -1;
//        if(intent.getIntExtra(CollegeProfAdapter.KEY_IMG, 0) != -1)
//            profimg = intent.getIntExtra(CollegeProfAdapter.KEY_IMG, 0);
//        else if(intent.getIntExtra(AllCardsAdapter.KEY_IMG, 0) != -1)
//            profimg = intent.getIntExtra(AllCardsAdapter.KEY_IMG, 0);
//        else if(intent.getIntExtra(RecommendedAdapter.KEY_IMG, 0) != -1)
//            profimg = intent.getIntExtra(RecommendedAdapter.KEY_IMG, 0);
//
//        this.img.setImageResource(profimg);

        initFirebase();
//        getCollegeProfessorsCount(profname);
//        getCommentCount("0000001");

        getCollegeProfessorsCount(profname);

        this.viewHome();
        this.viewProfile();
        this.rateProf();

        btn_addFeatured.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFeaturedCount();
            }
        });
    }

    private void addToFeatured() {
        database.getReference().child("users").child(mAuth.getUid()).child("featured").child(featuredCount+"")
                .setValue(profUID).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    isSuccessful();
                } else {
                    isFail();
                }
            }
        });
    }

    private void isSuccessful(){
        Toast.makeText(this, "Successfully Added", Toast.LENGTH_SHORT).show();
    }

    private void isFail(){
        Toast.makeText(this, "Error Occurred.", Toast.LENGTH_SHORT).show();
    }

    private void getFeaturedCount(){
        database.getReference().child("users").child(mAuth.getUid()).child("featured")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Log.d("SNAPSHOT COUNT", ""+snapshot.getChildrenCount());
                        featuredCount = (int) snapshot.getChildrenCount();
                        addToFeatured();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void viewHome() {
        this.iv_home= findViewById(R.id.iv_home);
        this.iv_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfessorProfile.this, userDashboard.class);
                startActivity(i);
            }
        });
    }

    private void viewProfile() {
        this.iv_profile= findViewById(R.id.iv_profile);
        this.iv_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfessorProfile.this, OwnProfileActivity.class);
                startActivity(i);
            }
        });
    }

    private void rateProf() {
        this.commentButton= findViewById(R.id.commentButton);
        this.commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfessorProfile.this, RateProfessor.class);
                String x = name.getText().toString().trim();
                String lName = x.split(" ",2)[1];
                i.putExtra("NAME", lName);

                startActivity(i);
            }
        });
    }

    private void initFirebase() {
        this.mAuth = FirebaseAuth.getInstance();
        this.database = FirebaseDatabase.getInstance("https://arrow-848c3-default-rtdb.asia-southeast1.firebasedatabase.app/");
    }

  private void getCollegeProfessorsCount(String profname) {
       database.getReference().child("professors")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Log.d("FIREBASE", ""+snapshot.getChildrenCount());
                        collegeProfessorsCount = (int) snapshot.getChildrenCount();
                        getUID(profname);
                    }

                   @Override
                   public void onCancelled(@NonNull DatabaseError error) {

                 }
                });
   }

    private void getUID(String fullname){
        String x = fullname;
        String searchlName = x.split(" ",2)[1];
        for (i = 1; i <= collegeProfessorsCount; i++){
            database.getReference().child("professors").child(String.format("%07d", i))
                    .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.isSuccessful()){
                        DataSnapshot snapshot = task.getResult();
                        Log.d("NAME", ""+searchlName);
                        Log.d("DATABASE - LastNames", ""+ String.valueOf(snapshot.child("lName").getValue()));
                        Log.d("BOOL",""+(String.valueOf(snapshot.child("lName").getValue()).equals(searchlName)));
                        if (String.valueOf(snapshot.child("lName").getValue()).equals(searchlName)) {
                            Log.d("UID-try", ""+snapshot.getKey());
                            String fName = String.valueOf(snapshot.child("fName").getValue());
                            String lName = String.valueOf(snapshot.child("lName").getValue());
                            String pic = String.valueOf(snapshot.child("pic").getValue());

                            mv_image.setImageResource(getResources().getIdentifier(pic , "drawable", getPackageName()));


                            name.setText(fName + " " + lName);




                            getCommentCount(snapshot.getKey());

                            profUID = snapshot.getKey();
                        }
                    }
                }
            });
        }

    }


    private String[] getDetailsfromID (String ID){

        String[] id_details = new String[3];
        database.getReference().child("users").child(ID)
                .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()){
                    DataSnapshot snapshot = task.getResult();

                    id_details[0] = String.valueOf(snapshot.child("fName").getValue());
                    id_details[1] = String.valueOf(snapshot.child("lName").getValue());
                    id_details[2] = String.valueOf(snapshot.child("college").getValue());
                }
            }
        });

        return id_details;
    }

    private void getCommentCount(String ID) {
        database.getReference().child("professors").child(ID).child("reviews")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Log.d("FIREBASE", ""+snapshot.getChildrenCount());
                        commentCount = (int) snapshot.getChildrenCount();
                        displayRated(ID);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void displayRated(String ID) {
        commentsRecycler.setHasFixedSize(true);
        commentsRecycler.setLayoutManager(new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false));

        for (i = 0; i < commentCount; i++){
            database.getReference().child("professors").child(ID).child("reviews").child(Integer.toString(i))
                    .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.isSuccessful()){
                        DataSnapshot snapshot = task.getResult();

                        String id = String.valueOf(snapshot.child("uid").getValue());
                        String fName = String.valueOf(snapshot.child("fname").getValue());
                        String lName = String.valueOf(snapshot.child("lname").getValue());
                        String college = String.valueOf(snapshot.child("college").getValue());
                        float rating = Float.parseFloat(String.valueOf(snapshot.child("overall").getValue()));
                        int learning = Integer.parseInt(String.valueOf(snapshot.child("sync").getValue()));
                        int grading = Integer.parseInt(String.valueOf(snapshot.child("grading").getValue()));
                        int attendance = Integer.parseInt(String.valueOf(snapshot.child("attendance").getValue()));
                        String review = String.valueOf(snapshot.child("comment").getValue());


//                        commentItem.add(new CommentHelperClass(fName, lName, college, learning, attendance, grading, rating, review));

                        commentItem.add(new CommentHelperClass(id, fName, lName, college, learning, attendance, grading, rating, review));



                        adapter = new CommentCardAdapter(commentItem);
                        commentsRecycler.setAdapter(adapter);



                    }
                }
            });
        }
    }





    private void commentsReview() {
        commentsRecycler.setHasFixedSize(true);
        commentsRecycler.setLayoutManager(new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false));

        ArrayList<CommentHelperClass> commentItem = new ArrayList<>();

        commentItem.add(new CommentHelperClass("123", "Jane", "Dela Cruz", "BSIT", "Pure Synchronous",
                "Imporant Attendance", "Pure Exams", getResources().getString(R.string.rev_comment)));
        commentItem.add(new CommentHelperClass("123", "Juan", "Santos", "BSIT", "Pure Synchronous",
                "Imporant Attendance", "Pure Exams", getResources().getString(R.string.rev_comment)));
        commentItem.add(new CommentHelperClass("123", "John", "Perez", "BSIT", "Pure Synchronous",
                "Imporant Attendance", "Pure Exams", getResources().getString(R.string.rev_comment)));
        commentItem.add(new CommentHelperClass("123", "Peter", "Parker", "BSIT", "Pure Synchronous",
                "Imporant Attendance", "Pure Exams", getResources().getString(R.string.rev_comment)));
        commentItem.add(new CommentHelperClass("123", "Tony", "Stark", "BSIT", "Pure Synchronous",
                "Imporant Attendance", "Pure Exams", getResources().getString(R.string.rev_comment)));


        adapter = new CommentCardAdapter(commentItem);
        commentsRecycler.setAdapter(adapter);
    }
}