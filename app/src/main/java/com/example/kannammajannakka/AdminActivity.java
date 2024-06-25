package com.example.kannammajannakka;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminActivity extends AppCompatActivity {

    private FrameLayout frameLayout;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        frameLayout = findViewById(R.id.FragmentContainerAdmin);
        bottomNavigationView = findViewById(R.id.BottomNavigationViewAdmin);

        getSupportFragmentManager().beginTransaction().replace(R.id.FragmentContainerAdmin, new AddMechanicFragment()).commit();
        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavigationMethod);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavigationMethod =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @SuppressLint("NonConstantResourceId")
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment fragment = null;
                    int itemId = item.getItemId();

                    if (itemId == R.id.addMechanicMenu) {
                        fragment = new AddMechanicFragment();
                    } else if (itemId == R.id.allMechanicsMenu) {
                        fragment = new ShowMechanicFragment();
                    } else if (itemId == R.id.profileMenu) {
                        fragment = new UserProfileFragment();
                    }

                    if (fragment != null) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.FragmentContainerAdmin, fragment).commit();
                    }
                    return true;
                }
            };
}
