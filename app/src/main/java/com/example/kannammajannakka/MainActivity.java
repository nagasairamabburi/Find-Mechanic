package com.example.kannammajannakka;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    FrameLayout frameLayout;
    BottomNavigationView bottomNavigationView;
    DatabaseReference databaseReference;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        frameLayout = findViewById(R.id.FragmentContainer);
        bottomNavigationView = findViewById(R.id.BottomNavigationView);

        getSupportFragmentManager().beginTransaction().replace(R.id.FragmentContainer, new HomeFragment()).commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavigationMethod);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if (account != null) {
            userId = account.getId();
            Log.d(TAG, "User ID: " + userId);
            databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
        } else {
            Log.d(TAG, "User not signed in");
            Toast.makeText(this, "User not signed in", Toast.LENGTH_SHORT).show();
            redirectToLogin();
            return;
        }
    }

    private void redirectToLogin() {
        Intent intent = new Intent(MainActivity.this, StartingActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mUser == null) {
            redirectToLogin();
        } else {
            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
            if (account != null) {
                String id = account.getId();
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users").child(id).child("role");
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String data = snapshot.getValue(String.class);
                        Log.d(TAG, "Role: " + data);
                        Toast.makeText(getApplicationContext(), data, Toast.LENGTH_SHORT).show();

                        if ("admin".equals(data)) {
                            Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
                            startActivity(intent);
                            finish();
                        } else if (!"user".equals(data)) {
                            Intent intent = new Intent(getApplicationContext(), RoleActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e(TAG, "Database Error: " + error.getMessage());
                    }
                });
            } else {
                redirectToLogin();
            }
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavigationMethod =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment fragment = null;
                    int itemId = item.getItemId();

                    if (itemId == R.id.homeMenu) {
                        fragment = new HomeFragment();
                    } else if (itemId == R.id.profileMenu) {
                        fragment = new UserProfileFragment();
                    }

                    if (fragment != null) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.FragmentContainer, fragment).commit();
                    }
                    return true;
                }
            };
}
