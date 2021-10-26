package com.example.arrow;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditProfile extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    ImageView iv_home;
    ImageView iv_profile;
    private Spinner sp_changeProfile;

    private EditText etFname;
    private EditText etLname;
    private Spinner sp_editCollege;
    private Button btnSubmit, btnDelete;

    private ImageView profImage;

    String[] avatars = { "Default", "Girl Icon", "Boy Icon" };
    String studentID;

    // Firebase
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    ProfileActivity profileActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.edit_profile);





        initFirebase();
         studentID = mAuth.getUid();

        this.viewHome();
        this.viewProfile();

        etFname = findViewById(R.id.edit_firstName);
        etLname = findViewById(R.id.edit_LastName);
        profImage = findViewById(R.id.prof_Image);

        btnSubmit = findViewById(R.id.btn_submitedit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUser();
            }
        });

        btnDelete = findViewById(R.id.btn_delete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteUser();
            }
        });


        //Getting the instance of Spinner and applying OnItemSelectedListener on it
        sp_changeProfile = (Spinner) findViewById(R.id.sp_changeProfile);
        sp_changeProfile.setOnItemSelectedListener(this);

        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,avatars);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        sp_changeProfile.setAdapter(aa);

        //Spinner for College
        sp_editCollege = (Spinner) findViewById(R.id.sp_editCollege);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.colleges, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_editCollege.setAdapter(adapter);



        getCollege();













    }

    private void getCollege() {

        Log.d("studid getcollege", ""+studentID);


        database.getReference().child("users").child(studentID)
                .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                Log.d("task successful", "1");

                if (task.isSuccessful()) {
                    DataSnapshot snapshot = task.getResult();
                    Log.d("task successful", ""+snapshot.child("college").getValue() );
                    String college = String.valueOf(snapshot.child("college").getValue());
                    String pfp =  String.valueOf(snapshot.child("pfp").getValue());
                    String lname = String.valueOf(snapshot.child("lName").getValue());
                    String fname = String.valueOf(snapshot.child("fName").getValue());
                    etFname.setText(fname);
                    etLname.setText(lname);
                    profImage.setImageResource(getResources().getIdentifier(pfp , "drawable", getPackageName()));

                    int index = 0;
                    for(int i = 0; i < sp_editCollege.getCount(); i++){
                        Log.d("sp position:", ""+sp_editCollege.getItemAtPosition(i));
                        if(sp_editCollege.getItemAtPosition(i).toString().equals(college)){

                            sp_editCollege.setSelection(i);
                            break;
                        }
                    }



                }
            }
        });
    }

    private void DeleteUser() {
        database.getReference().child("users").child(mAuth.getUid()).removeValue();
        Intent i = new Intent(EditProfile.this, LoginActivity.class);
        startActivity(i);


    }

    private void updateUser() {
        String fname = etFname.getText().toString().trim();
        String lname = etLname.getText().toString().trim();
        String college = sp_editCollege.getSelectedItem().toString();
        String pfp = pictoString(sp_changeProfile.getSelectedItem().toString());

        //profileActivity.profileImage.setImageResource(getResources().getIdentifier(pfp , "drawable", getPackageName()));
        if (!checkEmpty(fname, lname)){
            DatabaseReference tempdb = database.getReference().child("users").child(mAuth.getUid());
            tempdb.child("fName").setValue(fname);
            tempdb.child("lName").setValue(lname);
            tempdb.child("college").setValue(college);
            tempdb.child("pfp").setValue(pfp);
            Toast.makeText(this, "Profile Updated", Toast.LENGTH_SHORT).show();
        }

        Intent i = new Intent(EditProfile.this, ProfileActivity.class);
        startActivity(i);

    }

    private boolean checkEmpty(String fName, String lName) {
        boolean hasEmpty = false;
        if (fName.isEmpty()){
            this.etFname.setError("Required Field");
            this.etFname.requestFocus();
            hasEmpty = true;
        } else if (lName.isEmpty()){
            this.etLname.setError("Required Field");
            hasEmpty = true;
        }
        return hasEmpty;
    }

    private void initFirebase() {
        this.mAuth = FirebaseAuth.getInstance();
        this.database = FirebaseDatabase.getInstance("https://arrow-848c3-default-rtdb.asia-southeast1.firebasedatabase.app/");
    }

    //Performing action onItemSelected and onNothing selected
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        Toast.makeText(getApplicationContext(),avatars[position] , Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    private void viewHome() {
        this.iv_home= findViewById(R.id.iv_home);
        this.iv_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EditProfile.this, userDashboard.class);
                startActivity(i);
            }
        });
    }

    private String pictoString(String sync){
        if (sync.equals("Default")){
            return "default_avatar";
        } else if (sync.equals("Girl Icon")){
            return "girl_avatar";
        } else if (sync.equals("Boy Icon")){
            return "boy_avatar";
        }else {
            return "default_avatar";
        }
    }

    private void viewProfile() {
        this.iv_profile= findViewById(R.id.iv_profile);
        this.iv_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EditProfile.this, OwnProfileActivity.class);
                startActivity(i);
            }
        });
    }



}
