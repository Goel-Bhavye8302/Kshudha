package com.ai.game.kshudha.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.ai.game.kshudha.Fragment.HomeFragment;
import com.ai.game.kshudha.Fragment.MapsFragment;
import com.ai.game.kshudha.Fragment.ProfileFragment;
import com.ai.game.kshudha.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.Map;

public class HomeActivity extends AppCompatActivity {
    BottomNavigationView navBar;
    public Fragment fragment = null;

    public void loadFragments(Fragment fragment) {
        if(fragment != null){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainerView, fragment)
                    .setReorderingAllowed(true)
                    .commit();
        }
        else{
            Toast.makeText(this, "Fragment Error!", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        navBar = findViewById(R.id.bottom_navigation);
        navBar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.page_1) fragment = new HomeFragment();
                if(item.getItemId() == R.id.page_2) fragment = new MapsFragment();
                if(item.getItemId() == R.id.page_3) fragment = new ProfileFragment();
                if(item.getItemId() == R.id.page_4) return false;

                loadFragments(fragment);
                return true;
            }
        });
    }
}