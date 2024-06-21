package com.example.flappybird;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;

public class Pipe {
    private static final int WIDTH = 100;
    private static final int GAP = 450;
    private static final int SPEED = 10;
    private int x, height;
    private boolean passed;
    private Bitmap pipeImage;
    private Bitmap upperPipeImage;
    private Bitmap lowerPipeImage;

    public Pipe(Context context, int startX) {
        this.x = startX;
        this.height = (int) (Math.random() * 400) + 100;
        this.passed = false;

        pipeImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.pipe);

        upperPipeImage = Bitmap.createScaledBitmap(pipeImage, WIDTH, height, true);

        int lowerPipeHeight = 1200 - (height + GAP);
        lowerPipeImage = Bitmap.createScaledBitmap(pipeImage, WIDTH, lowerPipeHeight, true);
        lowerPipeImage = flipBitmap(lowerPipeImage);
    }

    private Bitmap flipBitmap(Bitmap original) {
        Matrix matrix = new Matrix();
        matrix.preScale(1.0f, -1.0f);
        return Bitmap.createBitmap(original, 0, 0, original.getWidth(), original.getHeight(), matrix, true);
    }

    public void update() {
        x -= SPEED;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(upperPipeImage, x, 0, null);
        canvas.drawBitmap(lowerPipeImage, x, height + GAP, null);
    }

    public boolean collides(Bird bird) {
        Rect birdBounds = bird.getBounds();
        Rect upperPipe = new Rect(x, 0, x + WIDTH, height);
        Rect lowerPipe = new Rect(x, height + GAP, x + WIDTH, 1200);
        return birdBounds.intersect(upperPipe) || birdBounds.intersect(lowerPipe);
    }

    public boolean isOffScreen() {
        return x < -WIDTH;
    }

    public int getX() {
        return x;
    }
}
