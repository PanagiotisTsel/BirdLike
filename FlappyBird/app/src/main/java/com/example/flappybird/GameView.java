package com.example.flappybird;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private MainThread thread;
    private Bird bird;
    private List<Pipe> pipes;
    private int score;
    private int highScore;
    private TextView tvScore;
    private TextView tvHighScore;
    private SharedPreferences prefs;
    private Context context;

    public GameView(Context context, TextView tvScore, TextView tvHighScore) {
        super(context);
        init(context, tvScore, tvHighScore);
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, null, null);
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, null, null);
    }

    private void init(Context context, TextView tvScore, TextView tvHighScore) {
        this.context = context;
        this.tvScore = tvScore;
        this.tvHighScore = tvHighScore;
        getHolder().addCallback(this);
        bird = new Bird(context);
        pipes = new ArrayList<>();
        score = 0;
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        highScore = prefs.getInt("high_score", 0);
        setFocusable(true);
        thread = new MainThread(getHolder(), this);
        if (this.tvHighScore != null) {
            this.tvHighScore.setText("High Score: " + highScore);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (canvas != null) {
            canvas.drawColor(Color.CYAN);
            bird.draw(canvas);
            for (Pipe pipe : pipes) {
                pipe.draw(canvas);
            }
            Paint paint = new Paint();
            paint.setColor(Color.WHITE);
            paint.setTextSize(50);
            canvas.drawText("Score: " + score, 50, 50, paint);
        }
    }

    public void update() {
        bird.update();
        for (Pipe pipe : pipes) {
            pipe.update();
            if (pipe.isOffScreen()) {
                pipes.remove(pipe);
                score++;
                if (tvScore != null) {
                    tvScore.setText("Score: " + score);
                }
                break;
            }
            if (pipe.collides(bird)) {
                if (score > highScore) {
                    highScore = score;
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putInt("high_score", highScore);
                    editor.apply();
                    if (tvHighScore != null) {
                        tvHighScore.setText("High Score: " + highScore);
                    }
                }
                resetGame();
            }
        }
        if (pipes.isEmpty() || pipes.get(pipes.size() - 1).getX() < 400) {
            pipes.add(new Pipe(context, 800));
        }
    }

    private void resetGame() {
        bird = new Bird(context);
        pipes.clear();
        score = 0;
        if (tvScore != null) {
            tvScore.setText("Score: 0");
        }
    }




    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!thread.isRunning()) {
            thread = new MainThread(getHolder(), this);
            thread.setRunning(true);
            thread.start();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                thread.setRunning(false);
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            bird.jump();
            return true;
        }
        return super.onTouchEvent(event);
    }


    public void pause() {
        if (thread != null) {
            thread.setRunning(false);
        }
    }

    public void resume() {
        if (thread != null) {
            thread = new MainThread(getHolder(), this);
            thread.setRunning(true);
            thread.start();
        }
    }


    }

