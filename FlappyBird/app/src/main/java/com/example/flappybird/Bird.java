package com.example.flappybird;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Bird {
    private static final int RADIUS = 40;
    private static final int GRAVITY = 2;
    private static final int JUMP_STRENGTH = -30;
    private int x, y, yVelocity;
    private Bitmap birdImage;

    public Bird(Context context) {
        this.x = 200;
        this.y = 400;
        this.yVelocity = 0;

        Bitmap originalBirdImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.bird);
        birdImage = Bitmap.createScaledBitmap(originalBirdImage, RADIUS * 2, RADIUS * 2, true);
    }

    public void update() {
        yVelocity += GRAVITY;
        y += yVelocity;
        if (y > 800) {
            y = 800;
            yVelocity = 0;
        }
        if (y < 0) {
            y = 0;
            yVelocity = 0;
        }
    }

    public void jump() {
        yVelocity = JUMP_STRENGTH;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(birdImage, x - RADIUS, y - RADIUS, null);
    }

    public Rect getBounds() {
        return new Rect(x - RADIUS, y - RADIUS, x + RADIUS, y + RADIUS);
    }
}
