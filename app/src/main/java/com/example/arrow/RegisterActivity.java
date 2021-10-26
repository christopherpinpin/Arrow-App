package com.example.arrow;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Collections;

public class RegisterActivity extends AppCompatActivity {

    private EditText etRegEmail;
    private EditText etRegPassword;
    private Button btnSubmit;
    private ProgressBar pbRegister;

    // Firebase
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        this.initFirebase();
        this.initComponents();
    }

    private void initFirebase() {
        this.mAuth = FirebaseAuth.getInstance();
        this.database = FirebaseDatabase.getInstance("https://arrow-848c3-default-rtdb.asia-southeast1.firebasedatabase.app/");
    }

    private void initComponents() {
        this.etRegEmail = findViewById(R.id.et_reg_email);
        this.etRegPassword = findViewById(R.id.et_reg_password);
        this.btnSubmit = findViewById(R.id.btn_submit);
        this.pbRegister = findViewById(R.id.pb_register);

        this.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etRegEmail.getText().toString().trim();
                String password = etRegPassword.getText().toString().trim();
                if (!checkEmpty(email, password)){
                    // Add a new user to DB
                    storeUser();
                }
            }
        });
    }

    private boolean checkEmpty(String email, String password) {
        boolean hasEmpty = false;
        if (email.isEmpty()){
            this.etRegEmail.setError("Required Field");
            hasEmpty = true;
        } else if (password.isEmpty()){
            this.etRegPassword.setError("Required Field");
            hasEmpty = true;
        }
        if (!email.contains("dlsu.edu.ph")){
            this.etRegEmail.setError("Requires DLSU Email");
            this.etRegEmail.requestFocus();
            hasEmpty = true;
        }
        return hasEmpty;
    }

    private void storeUser() {
        this.pbRegister.setVisibility(View.VISIBLE);

        // Register to Firebase
        mAuth.createUserWithEmailAndPassword(etRegEmail.getText().toString().trim(), etRegPassword.getText().toString().trim())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            successfulRegistration();
                        } else {
                            failedRegistration();
                        }
                    }
                });
    }

    private void successfulRegistration() {
        this.pbRegister.setVisibility(View.GONE);
        Intent i = new Intent(RegisterActivity.this, Register2Activity.class);
        i.putExtra("UID", this.mAuth.getUid());
        i.putExtra("EMAIL", etRegEmail.getText().toString().trim());
        i.putExtra("PASSWORD", etRegPassword.getText().toString().trim());
        startActivity(i);
    }

    private void failedRegistration() {
        this.pbRegister.setVisibility(View.GONE);
        Toast.makeText(this, "User Registration Failed", Toast.LENGTH_SHORT).show();
    }
}
