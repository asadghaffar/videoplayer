package com.fcodex.videoplayer.Activities;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.fcodex.videoplayer.BuildConfig;
import com.fcodex.videoplayer.Fragment.HomeFragment;
import com.fcodex.videoplayer.R;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar navigationToolBar;
    private InterstitialAd interstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer_layout);

        id();

        initializeDefaultFragment(savedInstanceState, 0);
        setSupportActionBar(navigationToolBar);
        // Apply navigation drawer icon
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutFragmentReplacement, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }

    }

    private void id() {
        navigationToolBar = findViewById(R.id.navigationToolBar);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
    }

    private void initializeDefaultFragment(Bundle savedInstanceState, int itemIndex) {
        if (savedInstanceState == null) {
            MenuItem menuItem = navigationView.getMenu().getItem(itemIndex).setChecked(true);
            onNavigationItemSelected(menuItem);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            new MaterialAlertDialogBuilder(MainActivity.this).setIcon(R.drawable.ic_launcher_background)
                    .setTitle("Exit")
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialogInterface, i) -> finish())
                    .setNegativeButton("No", (dialogInterface, i) -> dialogInterface.cancel()).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Navigation Onclick Listener
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        switch (item.getItemId()) {
            case R.id.nav_home:
                ft.replace(R.id.frameLayoutFragmentReplacement, new HomeFragment())
                        .commit();
                break;
            case R.id.nav_share_app:
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, R.string.app_name);
                    String stringDescription = "\nLet me recommend you this application\n\n";
                    stringDescription = stringDescription + "https://play.google.com/store/apps/details?id=" +
                            BuildConfig.APPLICATION_ID + "\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, stringDescription);
                    startActivity(Intent.createChooser(shareIntent, "Choose One"));
                } catch (Exception e) {
                    Log.d("e_", String.valueOf(e));
                }
                break;
            case R.id.nav_exit:
                new MaterialAlertDialogBuilder(MainActivity.this).setIcon(R.drawable.ic_launcher_background)
                        .setTitle("Exit")
                        .setCancelable(false)
                        .setPositiveButton("Yes", (dialogInterface, i) -> {
                            finishAffinity();
                            System.exit(0);
                        })
                        .setNegativeButton("No", (dialogInterface, i) -> dialogInterface.cancel()).show();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    private void showInterstitial() {
        if (interstitialAd.isLoaded()) {
            interstitialAd.show();
        }
    }
    // Interstitial ad end
    
}