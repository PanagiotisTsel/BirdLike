package com.example.flappybird;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {

    private static final String TAG = "GameActivity";
    private static final String HIGH_SCORE_KEY = "high_score";

    private GameView gameView;
    private TextView tvScore;
    private TextView tvHighScore;
    private MediaPlayer mediaPlayer;
    private boolean isPaused;
    private int currentScore;
    private int highScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Log.d(TAG, "onCreate: Started");

        tvScore = findViewById(R.id.tvScore);
        tvHighScore = findViewById(R.id.tvHighScore);
        gameView = findViewById(R.id.gameView); // Ensure gameView is defined in activity_game.xml

        highScore = getHighScore();
        tvHighScore.setText("High Score: " + highScore);

        isPaused = false;

        boolean musicEnabled = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("music_enabled", true);
        if (musicEnabled) {
            setupMediaPlayer();
        }

        Button btnPause = findViewById(R.id.btnPause);
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPaused) {
                    resumeGame();
                } else {
                    pauseGame();
                }
            }
        });

        Log.d(TAG, "onCreate: Completed");
    }

    private void setupMediaPlayer() {
        new Thread(() -> {
            mediaPlayer = MediaPlayer.create(GameActivity.this, R.raw.background_music);
            mediaPlayer.setLooping(true);
            runOnUiThread(() -> {
                if (!isPaused && mediaPlayer != null) {
                    mediaPlayer.start();
                }
            });
        }).start();
    }

    private void pauseGame() {
        Log.d(TAG, "pauseGame: Pausing game");
        isPaused = true;
        gameView.pause();
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    private void resumeGame() {
        Log.d(TAG, "resumeGame: Resuming game");
        isPaused = false;
        gameView.resume();
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: Started");
        pauseGame();
        Log.d(TAG, "onPause: Completed");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: Started");
        if (!isPaused) {
            resumeGame();
        }
        Log.d(TAG, "onResume: Completed");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: Started");
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        Log.d(TAG, "onDestroy: Completed");
    }

    private int getHighScore() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getInt(HIGH_SCORE_KEY, 0);
    }

    private void saveHighScore(int score) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(HIGH_SCORE_KEY, score);
        editor.apply();
    }

    // Call this method when the game ends or score is updated
    private void updateScore(int newScore) {
        currentScore = newScore;
        tvScore.setText("Score: " + currentScore);
        if (currentScore > highScore) {
            highScore = currentScore;
            tvHighScore.setText("High Score: " + highScore);
            saveHighScore(highScore);
        }
    }
}
