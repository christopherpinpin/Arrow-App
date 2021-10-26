package com.example.arrow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

public class Register2Activity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    // Page 0
    private String UID;
    private String etEmail;
    private String etPassword;

    // Page 1
    private Spinner spCollege;
    private EditText etFName;
    private EditText etLName;
    private Button btnContinue;

    // Page 2
    private Spinner spSync;
    private Spinner spAttendance;
    private Spinner spGrading;
    private Button btnFinish;
    private ProgressBar pbFinish;

    // Firebase
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        this.database = FirebaseDatabase.getInstance("https://arrow-848c3-default-rtdb.asia-southeast1.firebasedatabase.app/");

        Intent i = getIntent();
        UID = i.getStringExtra("UID");
        etEmail = i.getStringExtra("EMAIL");
        etPassword = i.getStringExtra("PASSWORD");

        // Page 1
        etFName = findViewById(R.id.et_fName);
        etLName = findViewById(R.id.et_lName);
        btnContinue = findViewById(R.id.btn_continue);

        spCollege = findViewById(R.id.sp_college);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.colleges, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCollege.setAdapter(adapter1);
        spCollege.setOnItemSelectedListener(this);


        initComponents();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void initComponents() {
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fName = etFName.getText().toString().trim();
                String lName = etLName.getText().toString().trim();
                String college = spCollege.getSelectedItem().toString();

                if (!checkEmpty(fName, lName)){
                    setContentView(R.layout.activity_register3);
                    initComponents2();
                }
            }
        });
    }

    private void initComponents2() {
        // Page 2
        btnFinish = findViewById(R.id.btn_finish);

        spSync = findViewById(R.id.sp_sync);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.synchronous, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSync.setAdapter(adapter2);
        spSync.setOnItemSelectedListener(this);

        spAttendance = findViewById(R.id.sp_attendance);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this, R.array.attendance, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spAttendance.setAdapter(adapter3);
        spAttendance.setOnItemSelectedListener(this);

        spGrading = findViewById(R.id.sp_grading);
        ArrayAdapter<CharSequence> adapter4 = ArrayAdapter.createFromResource(this, R.array.grading, android.R.layout.simple_spinner_item);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spGrading.setAdapter(adapter4);
        spGrading.setOnItemSelectedListener(this);

        pbFinish = findViewById(R.id.pb_finish);

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeData();
            }
        });
    }

    private void storeData(){
        this.pbFinish.setVisibility(View.VISIBLE);

        String college = spCollege.getSelectedItem().toString();
        int nSync = syncToInt(spSync.getSelectedItem().toString());
        int nAttendance = syncToAttendance(spAttendance.getSelectedItem().toString());
        int nGrading = syncToGrading(spGrading.getSelectedItem().toString());
        String pfp = "default_avatar";


        User user = new User(etEmail, etPassword, etFName.getText().toString().trim(), etLName.getText().toString().trim(), college, nSync, nAttendance, nGrading, pfp);

        database.getReference("users")
                .child(UID)
                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    successfulRegistration();
                } else {
                    failedRegistration();
                }
            }
        });
    }

    private int syncToInt(String sync){
        if (sync.equals("None")){
            return 1;
        } else if (sync.equals("Few")){
            return 2;
        } else if (sync.equals("Moderate")){
            return 3;
        } else if (sync.equals("More")){
            return 4;
        } else {
            return 5;
        }
    }

    private int syncToAttendance(String attendance){
        if (attendance.equals("Yes")){
            return 1;
        } else {
            return 2;
        }
    }

    private int syncToGrading(String grading){
        if (grading.equals("Pure Output-based")){
            return 1;
        } else if (grading.equals("More Output")){
            return 2;
        } else if (grading.equals("Balanced")){
            return 3;
        } else if (grading.equals("More Exams")){
            return 4;
        } else {
            return 5;
        }
    }

    private boolean checkEmpty(String fName, String lName) {
        boolean hasEmpty = false;
        if (fName.isEmpty()){
            this.etFName.setError("Required Field");
            this.etFName.requestFocus();
            hasEmpty = true;
        } else if (lName.isEmpty()){
            this.etLName.setError("Required Field");
            hasEmpty = true;
        }
        return hasEmpty;
    }

    private void successfulRegistration() {
        this.pbFinish.setVisibility(View.GONE);
        Intent i = new Intent(Register2Activity.this, LoginActivity.class);
        startActivity(i);
    }

    private void failedRegistration() {
        this.pbFinish.setVisibility(View.GONE);
        Toast.makeText(this, "User Registration Failed", Toast.LENGTH_SHORT).show();
    }
}