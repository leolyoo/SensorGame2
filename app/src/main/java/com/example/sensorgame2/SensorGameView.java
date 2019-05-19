package com.example.sensorgame2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;

public class SensorGameView extends View {
    Rect dst, bgDst;
    int viewCenterX, viewCenterY;
    int dstRadius, bgDstRadius;
    double dstX;
    double dstY;
    Bitmap bitmap;
    Bitmap bgBitmap;
    GameResultListener gameResultListener;

    public SensorGameView(Context context) {
        super(context);
        dst = new Rect();
        bgDst = new Rect();
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bottle);
        bgBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.table);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        viewCenterX = getMeasuredWidth() / 2;
        viewCenterY = getMeasuredHeight() / 2;
        dstRadius = getMeasuredWidth() / 6;
        bgDstRadius = getMeasuredWidth() / 3;
        int bgDstLeft = viewCenterX - bgDstRadius;
        int bgDstTop = viewCenterY - bgDstRadius;
        int bgDstRight = viewCenterX + bgDstRadius;
        int bgDstBottom = viewCenterY + bgDstRadius;
        bgDst.set(bgDstLeft, bgDstTop, bgDstRight, bgDstBottom);
        dstX = viewCenterX;
        dstY = viewCenterY;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        double dstLeft = dstX - dstRadius;
        double dstTop = dstY - dstRadius;
        double dstRight = dstX + dstRadius;
        double dstBottom = dstY + dstRadius;
        dst.set((int) dstLeft, (int) dstTop, (int) dstRight, (int) dstBottom);
        canvas.drawBitmap(bgBitmap, null, bgDst, null);
        canvas.drawBitmap(bitmap, null, dst, null);
    }

    public void updateDst(double pitch, double roll) {
        int distanceFromCenter = (int) Math.sqrt(Math.pow(Math.abs(viewCenterX - dstX), 2) + Math.pow(Math.abs(viewCenterY - dstY), 2));
        if (distanceFromCenter <= bgDstRadius) {
            dstX = dstX + roll;
            dstY = dstY - pitch;
            invalidate();
        } else {
            gameResultListener.onGameOver();
        }
    }

    void setGameResultListener(GameResultListener gameResultListener) {
        this.gameResultListener = gameResultListener;
    }
}
