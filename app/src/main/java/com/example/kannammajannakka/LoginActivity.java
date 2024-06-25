package com.example.kannammajannakka;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    private GoogleSignInClient mSignInClient;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressBar;
    private SignInButton signInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance();

        // Initialize progress dialog
        progressBar = new ProgressDialog(this);
        progressBar.setTitle("Please Wait...");
        progressBar.setMessage("We are setting everything up for you...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        // Initialize sign-in button
        signInButton = findViewById(R.id.GoogleSignBtn);

        // Configure Google Sign-In options
        GoogleSignInOptions signInOptions = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))  // Ensure this ID is in strings.xml
                .requestEmail()
                .build();

        mSignInClient = GoogleSignIn.getClient(this, signInOptions);

        // Set onClick listener for the sign-in button
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = mSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, 100);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            if (account != null) {
                firebaseAuthWithGoogle(account);
            }
        } catch (ApiException e) {
            e.printStackTrace();
            Toast.makeText(LoginActivity.this, "Google sign-in failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        progressBar.show();
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.dismiss();
                        if (task.isSuccessful()) {
                            saveUserToDatabase(account);
                        } else {
                            Toast.makeText(LoginActivity.this, "Firebase Authentication failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void saveUserToDatabase(GoogleSignInAccount account) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("users");

        HashMap<String, Object> user_details = new HashMap<>();
        String id = account.getId();
        if (id == null) {
            Log.e("LoginActivity", "GoogleSignInAccount ID is null");
            return;
        }
        String name = account.getDisplayName();
        String mail = account.getEmail();
        String pic = account.getPhotoUrl() != null ? account.getPhotoUrl().toString() : "";

        user_details.put("id", id);
        user_details.put("name", name);
        user_details.put("mail", mail);
        user_details.put("profilepic", pic);
        user_details.put("role", "empty");

        myRef.child(id).updateChildren(user_details).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    navigateToAppropriateActivity(account);
                } else {
                    Log.e("LoginActivity", "Failed to save user data", task.getException());
                    Toast.makeText(LoginActivity.this, "Failed to save user data", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void navigateToAppropriateActivity(GoogleSignInAccount account) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("users").child(account.getId()).child("role");

        myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    String role = task.getResult().getValue(String.class);
                    if (role == null || role.equals("empty")) {
                        navigateToRoleActivity();
                    } else if (role.equals("user")) {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else if (role.equals("admin")) {
                        Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Failed to get user role", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void navigateToRoleActivity() {
        Intent intent = new Intent(LoginActivity.this, RoleActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
