package com.example.flappybird;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            findViewById(R.id.btnStart).setOnClickListener(v -> {
                try {
                    startActivity(new Intent(MainActivity.this, GameActivity.class));
                } catch (Exception e) {
                    Log.e(TAG, "Error starting GameActivity", e);
                }
            });

            findViewById(R.id.btnSettings).setOnClickListener(v -> {
                try {
                    startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                } catch (Exception e) {
                    Log.e(TAG, "Error starting SettingsActivity", e);
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Error setting click listeners", e);
        }
    }
}
