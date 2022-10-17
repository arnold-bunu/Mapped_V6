package com.example.mapped_v6;



import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.mapped_v6.R;
import com.example.mapped_v6.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;

import ui.MapsFragment;

public class MainActivity extends AppCompatActivity {
    Button btnTest;
    MapsFragment MapsFragment;
    private ActivityMainBinding binding;
    private AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (1==2){

        } else {
            binding = ActivityMainBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());
            setSupportActionBar(binding.appBar.toolbar);
            DrawerLayout drawerLayout = binding.drawerLayout;
            NavigationView navigationView = binding.navView;
            appBarConfiguration = new AppBarConfiguration.Builder(R.id.navHome, R.id.navFavourites, R.id.navSettings)
                    .setDrawerLayout(drawerLayout).build();
            NavController navController = Navigation.findNavController(this, R.id.nav_content_main);
            NavigationUI.setupActionBarWithNavController(this,navController, appBarConfiguration);
            NavigationUI.setupWithNavController(navigationView, navController);

            navigationView.getMenu().findItem(R.id.navLogout).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    logOut();
                    return true;
                }
            });
        }
    }

    private void logOut() {
    }
}