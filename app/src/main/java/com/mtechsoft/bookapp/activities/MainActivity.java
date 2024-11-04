package com.mtechsoft.bookapp.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.multidex.BuildConfig;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.mtechsoft.bookapp.R;

public class MainActivity extends BaseActivity {
    public NavController navController;
    ImageView ivDrawer;
    private DrawerLayout drawer;
    NavigationView navigationView;
    RelativeLayout rlToolbar;
    TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        ivDrawer = findViewById(R.id.ivDrawer);
        rlToolbar = findViewById(R.id.rlToolbar);
        tvTitle = findViewById(R.id.tvTitle);
        initNavigation();

    }

    private void initNavigation() {
        ivDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START, true);
            }
        });
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navigationView, navController);
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                if (destination.getLabel() != null) {
                    if (destination.getLabel().equals("fragment_chapter_detail") || destination.getLabel().equals("Mcqs")) {
                        rlToolbar.setVisibility(View.GONE);
                    } else {
                        rlToolbar.setVisibility(View.VISIBLE);
                        tvTitle.setText(destination.getLabel());
                    }
                }

            }
        });

        navigationView.getMenu().findItem(R.id.nav_share).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                shareApp();
                return true;
            }
        });
        navigationView.getMenu().findItem(R.id.logout).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                showExitAppAlert();
                return true;
            }
        });
    }

    private void showExitAppAlert() {
        new AlertDialog.Builder(this)
                .setTitle("Quit")
                .setMessage("Are you sure you want to exit application?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        System.exit(0);
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public void shareApp() {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
            String shareMessage = "\nLet me recommend you this application\n\n";
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "choose one"));
        } catch (Exception e) {
            //e.toString();
        }
    }

    private boolean flagDoubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawers();

        if (!navController.getCurrentDestination().getLabel().toString().equals("Dashboard")) {
            super.onBackPressed();
        } else {
            if (flagDoubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.flagDoubleBackToExitPressedOnce = true;
            showToast(getString(R.string.warn_press_back_to_exit));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    flagDoubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }

}
