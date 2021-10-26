package com.example.arrow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail;
    private EditText etPassword;
    private Button btnLogin;
    private Button btnGoogle;
    private TextView tvRegister;
    private ProgressBar pbLogin;

    // Firebase
    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;

    private FirebaseDatabase database;

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginpage);
        createRequest();
        this.initFirebase();
        this.initComponents();
    }

    private void initFirebase() {
        this.mAuth = FirebaseAuth.getInstance();
        this.database = FirebaseDatabase.getInstance("https://arrow-848c3-default-rtdb.asia-southeast1.firebasedatabase.app/");
    }

    private void initComponents() {
        this.etEmail = findViewById(R.id.et_email);
        this.etPassword = findViewById(R.id.et_password);
        this.btnLogin = findViewById(R.id.btn_login);
        this.btnGoogle = findViewById(R.id.btn_google);
        this.tvRegister = findViewById(R.id.tv_register);
        this.pbLogin = findViewById(R.id.pb_login);

        this.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                if (!checkEmpty(email, password)){
                    signIn(email, password);
                }
            }
        });

        this.btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleSignIn();
            }
        });

        this.tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void createRequest() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        this.mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void googleSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            //GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
            //Log.d(TAG, "the email is "+acct.getEmail());

            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            //FirebaseUser user = mAuth.getCurrentUser();

                            checkExist();

                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            //updateUI(null);
                        }
                    }
                });
    }

    private void checkExist() {
        String UID = mAuth.getUid();
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        String EMAIL = acct.getEmail();

        database.getReference().child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(UID)){
                    Log.d("SIGN IN", "OLD USER");
                    // GO TO PROFILE
                    Intent i = new Intent(LoginActivity.this, userDashboard.class);
                    pbLogin.setVisibility(View.GONE);
                    startActivity(i);
                } else {
                    Log.d("SIGN IN", "NEW USER");
                    // GO TO REGISTER

                    Intent i = new Intent(LoginActivity.this, Register2Activity.class);
                    i.putExtra("UID", UID);
                    i.putExtra("EMAIL", EMAIL);
                    i.putExtra("PASSWORD", "GOOGLE_DEFAULT_PASSWORD");
                    startActivity(i);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null){
            Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(LoginActivity.this, Register2Activity.class);
            startActivity(i);
        } else {
            Toast.makeText(LoginActivity.this, "Login Error", Toast.LENGTH_SHORT).show();
        }
    }

    private void signIn(String email, String password) {
        this.pbLogin.setVisibility(View.VISIBLE);
        this.mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent i = new Intent(LoginActivity.this, userDashboard.class);
                            pbLogin.setVisibility(View.GONE);
                            startActivity(i);
                        } else {
                            loginFailed();
                        }
                    }
                });
    }

    private boolean checkEmpty(String email, String password) {
        boolean hasEmpty = false;
        if (email.isEmpty()){
            this.etEmail.setError("Required Field");
            this.etEmail.requestFocus();
            hasEmpty = true;
        } else if (password.isEmpty()){
            this.etPassword.setError("Required Field");
            hasEmpty = true;
        }
        return hasEmpty;
    }

    private void loginFailed() {
        this.pbLogin.setVisibility(View.GONE);
        Toast.makeText(this, "User Login Failed", Toast.LENGTH_SHORT).show();
    }
}