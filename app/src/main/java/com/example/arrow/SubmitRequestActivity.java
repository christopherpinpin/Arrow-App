package com.example.arrow;

import android.content.Intent;
import java.util.Random;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arrow.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SubmitRequestActivity extends AppCompatActivity {

    ImageView iv_home;
    ImageView iv_profile;

    EditText submitProfName, submitProfCollege,  submitDesc;

    Button btn_submitreview;

    FirebaseDatabase database;
    DatabaseReference reference;

    int reqCount;
    int newcount;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.submit_request);
        this.viewHome();
        this.viewProfile();


        initFirebase();
        initComponents();
    }



    private void initComponents() {


        database.getReference().child("requests")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // get total available quest
                        reqCount = (int) dataSnapshot.getChildrenCount();
                        reqCount++;
                        newcount+=reqCount;
                        Log.d("REQUEST COUNT", ""+reqCount);

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });




        //Random rand = new Random();
        submitProfName = findViewById(R.id.submitProfName);
        submitProfCollege = findViewById(R.id.submitProfCollege);
        submitDesc = findViewById(R.id.submitDesc);
        btn_submitreview = findViewById(R.id.btn_submitreview);

        //newcount = rand.nextInt(upperbound);
        //newcount = reqCount+1;
        Log.d("NEWCOUNT COUNT", ""+newcount);

        //Save data in Firebase on click

        btn_submitreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get all the values
                //EditText submitProfName, submitProfCollege,  submitDesc;
                String name = submitProfName.getText().toString().trim();
                String college = submitProfCollege.getText().toString().trim();
                String desc = submitDesc.getText().toString().trim();

                RequestHelper helperClass = new RequestHelper(name,college,desc);
                database.getReference().child("requests").child("req"+String.valueOf(newcount)).setValue(helperClass);
                submitProfName.setText(null);
                submitProfCollege.setText(null);
                submitDesc.setText(null);
                newcount++;

            }
        });
    }




    private void initFirebase() {
        this.database = FirebaseDatabase.getInstance("https://arrow-848c3-default-rtdb.asia-southeast1.firebasedatabase.app/");
    }



    private void viewHome() {
        this.iv_home= findViewById(R.id.iv_home);
        this.iv_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SubmitRequestActivity.this, userDashboard.class);
                startActivity(i);
            }
        });
    }

    private void viewProfile() {
        this.iv_profile= findViewById(R.id.iv_profile);
        this.iv_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SubmitRequestActivity.this, OwnProfileActivity.class);
                startActivity(i);
            }
        });
    }

}