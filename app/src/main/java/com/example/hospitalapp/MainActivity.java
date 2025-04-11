package com.example.hospitalapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    private static final String PREFS_NAME = "AppPrefs";
    private static final String THEME_KEY = "AppTheme";
    private static final int THEME_MORNING_FROST = 0;
    private static final int THEME_MIDNIGHT_FOREST = 1;
    private static final int THEME_GOLDEN_HOUR = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        applyTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager viewPager = findViewById(R.id.viewPager);
        TabLayout tabLayout = findViewById(R.id.tabLayout);

        TabAdapter adapter = new TabAdapter(getSupportFragmentManager());
        adapter.addFragment(new PatientsFragment(), "Пациенты");
        adapter.addFragment(new DoctorsFragment(), "Врачи");
        adapter.addFragment(new ArchiveFragment(), "Архив");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        ImageButton themeButton = findViewById(R.id.themeButton);
        themeButton.setOnClickListener(v -> showThemeSelectionDialog());
    }

    private void applyTheme() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int theme = prefs.getInt(THEME_KEY, THEME_MORNING_FROST);
        switch (theme) {
            case THEME_MORNING_FROST:
                setTheme(R.style.Theme_MorningFrost);
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case THEME_MIDNIGHT_FOREST:
                setTheme(R.style.Theme_MidnightForest);
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case THEME_GOLDEN_HOUR:
                setTheme(R.style.Theme_GoldenHour);
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
        }
    }

    private void showThemeSelectionDialog() {
        String[] themes = {"Утренний Мороз", "Полуночный лес", "Золотой час"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Выберите тему");
        builder.setItems(themes, (dialog, which) -> {
            SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt(THEME_KEY, which);
            editor.apply();
            recreate();
        });
        builder.setNegativeButton("Отмена", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }
}