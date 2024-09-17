package com.example.careathome;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Home_Screen extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            replaceFragment(new HomeFragment());
        }
    }

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentcontainer, fragment);
        transaction.commit();
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.navigation_home) {
            Toast.makeText(Home_Screen.this, "Home clicked", Toast.LENGTH_SHORT).show();
            replaceFragment(new HomeFragment());
            return true;
        } else if (itemId == R.id.navigation_dashboard) {
            Toast.makeText(Home_Screen.this, "dashboard clicked", Toast.LENGTH_SHORT).show();
            replaceFragment(new ProfileFragment());
            return true;
        } else if (itemId == R.id.navigation_notifications) {
            Toast.makeText(Home_Screen.this, "Profile Clicked", Toast.LENGTH_SHORT).show();
            replaceFragment(new AboutFragment());
            return true;
        }
        return false;
    }
}