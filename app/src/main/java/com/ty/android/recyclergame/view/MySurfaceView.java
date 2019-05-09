package com.ty.android.recyclergame.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.ty.android.recyclergame.R;
import com.ty.android.recyclergame.model.Bullet;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by android_1 on 2016/11/24.
 */

public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private float speed = (float) Math.sqrt(64);
    private float speedY = 4;
    private float speedX = 4;

    private Context context;
    private SurfaceHolder surfaceHolder;
    private Thread thread;
    private Paint mPaint;

    private int mWidth;
    private int mHeight;

    private boolean startThread;


    private boolean touchInCircle;

    private float outCircleR;
    private float outCircleX;
    private float outCircleY;


    private float inCircleR = 45f;

    private float inCircleX;
    private float inCircleY;

    //加速按钮属性
    private float accelerateX;
    private float accelerateY;
    private float accelerateR;

    //发射子弹按钮属性
    private float shootX;
    private float shootY;
    private float shootR;


    //角色的圆心位置
    private float actorX;
    private float actorY;


    private float xK;
    private float yK;

    //方向初始角度
    private float mK;
    private float mAngle = 60;

    private Bitmap mBgBitmap;
    int wCount;
    int hCount;

    private Bullet mBullet;

    private List<PointF> xxPoints = new ArrayList<>();
    private float rbgX;


    private int bgBitmapW;
    private int bgBitmapH;


    private RectF rectf;

    private Bitmap actorBitmap1, actorBitmap2, actorBitmap3, actorBitmap4;


    public MySurfaceView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    private void init() {
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setDither(true);
        mPaint.setStrokeWidth(2.5f);
        setFocusableInTouchMode(true);


        mBgBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.bg45);
        bgBitmapW = mBgBitmap.getWidth();
        bgBitmapH = mBgBitmap.getHeight();

        actorBitmap1 = BitmapFactory.decodeResource(getResources(), R.mipmap.a1);
        actorBitmap2 = BitmapFactory.decodeResource(getResources(), R.mipmap.a2);
        actorBitmap3 = BitmapFactory.decodeResource(getResources(), R.mipmap.a3);
        actorBitmap4 = BitmapFactory.decodeResource(getResources(), R.mipmap.a4);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        startThread = true;
        thread = new Thread(this);
        thread.start();

        dealxxPoints();


    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        startThread = false;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;

        actorX = w / 2;
        actorY = h / 2;


        outCircleR = mHeight * 0.135f;

        outCircleX = mWidth * 0.15f;
        outCircleY = mHeight * 0.75f;

        accelerateX = mWidth * 0.8f;
        accelerateY = mHeight * 0.8f;
        accelerateR = mHeight * 0.08f;

        shootX = mWidth * 0.9f;
        shootY = mHeight * 0.8f;
        shootR = mHeight * 0.08f;


        inCircleX = outCircleX;
        inCircleY = outCircleY;
//        mBgRepeater = Tool.createRepeater(w, h, mBgBitmap);

        rbgX = -mWidth;

        rectf = new RectF(actorX - actorBitmap1.getWidth() / 2 - 15, actorY - actorBitmap1.getHeight() / 2 - 15, actorX + actorBitmap1.getWidth() / 2 + 15, actorY + actorBitmap1.getHeight() / 2 + 15);

        wCount = ((w + bgBitmapW - 1) / bgBitmapW) + 1;
        hCount = ((h + bgBitmapH - 1) / bgBitmapH) + 1;

        mBullet = new Bullet();

    }

    @Override
    public void run() {
        while (startThread) {
            logic();
            draw();
        }
    }

    public void draw() {

        Canvas canvas = null;
        try {


            canvas = surfaceHolder.lockCanvas();

            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(2.5f);
            mPaint.setColor(Color.WHITE);
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
//            drawBg(canvas);
            drawBgMap(canvas);
            canvas.drawCircle(outCircleX, outCircleY, outCircleR, mPaint);


            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(Color.RED);
            canvas.drawCircle(inCircleX, inCircleY, 45, mPaint);
            canvas.drawCircle(accelerateX, accelerateY, accelerateR, mPaint);

            mPaint.setColor(Color.YELLOW);
            canvas.drawCircle(shootX, shootY, shootR, mPaint);
//            canvas.drawCircle(actorX, actorY, 45, mPaint);

            drawxx(canvas);
            mBullet.drawItSelf(canvas);
            drawActor(canvas);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (canvas != null) {
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }


    }


    private void drawActor(Canvas canvas) {

        Matrix matrix = new Matrix();
        matrix.setTranslate(actorX - actorBitmap1.getWidth() / 2, actorY - actorBitmap1.getHeight() / 2);     //设置图片的旋转中心，即绕（X,Y）这点进行中心旋转
        matrix.preRotate(mAngle, (float) actorBitmap1.getWidth() / 2, (float) actorBitmap1.getHeight() / 2);  //要旋转的角度
        canvas.drawBitmap(actorBitmap1, matrix, mPaint);
        canvas.drawBitmap(actorBitmap1,actorX-actorBitmap1.getWidth()/2,actorY-actorBitmap1.getHeight()/2,mPaint);
        canvas.drawBitmap(actorBitmap2, matrix, mPaint);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(rectf,mPaint);
        canvas.drawArc(rectf,90,135,false,mPaint);
        mPaint.setStrokeWidth(5);
        canvas.drawOval(rectf, mPaint);


    }

    private void dealxxPoints() {
        for (int i = 0; i < 100; i++) {
            float x = new Random().nextInt(2 * bgBitmapW + mWidth) - bgBitmapW;
            float y = new Random().nextInt(2 * bgBitmapH + mHeight) - bgBitmapH;
            PointF p = new PointF(x, y);
            xxPoints.add(p);
        }
    }

    public void drawxx(Canvas canvas) {
        mPaint.setColor(Color.BLUE);
        for (PointF p : xxPoints) {
            canvas.drawCircle(p.x + speedX, p.y + speedY, 10, mPaint);
        }
    }


    public void drawBgMap(Canvas canvas) {
        for (int idy = -1; idy < hCount; idy++) {
            for (int idx = -1; idx < wCount; idx++) {
                canvas.drawBitmap(mBgBitmap, bgBitmapW * idx + speedX, bgBitmapH * idy + speedY, mPaint);
            }
        }

    }

    float offsetX;
    float offsetY;

    public void logic() {


        if (mAngle > 180 && mAngle <= 270) {//一
            offsetX = (float) Math.abs(speed * Math.cos((mAngle - 180) * Math.PI / 180));
            offsetY = (float) Math.abs(speed * Math.sin((mAngle - 180) * Math.PI / 180));

        } else if (mAngle > 270 && mAngle <= 360) {//二
            offsetX = (float) Math.abs(speed * Math.cos((mAngle - 180) * Math.PI / 180));
            offsetY = (float) Math.abs(speed * Math.sin((mAngle - 180) * Math.PI / 180));
        } else if (mAngle > 0 && mAngle <= 90) {//三
            offsetX = (float) Math.abs(speed * Math.cos(mAngle * Math.PI / 180));
            offsetY = (float) Math.abs(speed * Math.sin(mAngle * Math.PI / 180));
        } else if (mAngle > 90 && mAngle <= 180) {//四
            offsetX = (float) Math.abs(speed * Math.cos((180 - mAngle) * Math.PI / 180));
            offsetY = (float) Math.abs(speed * Math.sin((180 - mAngle) * Math.PI / 180));
        }

//        else {
//            offsetX = (float) Math.abs(speed * Math.cos(mAngle * Math.PI / 180));
//            offsetY = (float) Math.abs(speed * Math.sin(mAngle * Math.PI / 180));
//
//        }

        if (xK < 0) {
            speedX += offsetX;
        } else {
            speedX -= offsetX;
        }
        if (yK < 0) {
            speedY += offsetY;
        } else {
            speedY -= offsetY;
        }

        if (mBullet.isLive()) {
            if (mBullet.isShoot()) {
                mBullet.setShoot(false);
                if (xK > 0) {
                    mBullet.setOffsetX(-offsetX);
                } else {
                    mBullet.setOffsetX(offsetX);
                }
                if (yK > 0) {
                    mBullet.setOffsetY(-offsetY);
                } else {
                    mBullet.setOffsetY(offsetY);
                }
            }
            mBullet.setbX(mBullet.getbX() - mBullet.getOffsetX()*1.7f);
            mBullet.setBy(mBullet.getBy() - mBullet.getOffsetY()*1.7f);
        }

    }


    //判断小球是不是还在圆内
    public boolean isInCircle(float x, float y) {
        if (Math.sqrt((x - outCircleX) * (x - outCircleX) + (y - outCircleY) * (y - outCircleY)) > outCircleR) {
            return false;
        }
        return true;
    }

    //判断手指是不是在圆内
    public boolean pointerInCircle(float x, float y) {
        if (Math.sqrt((x - outCircleX) * (x - outCircleX) + (y - outCircleY) * (y - outCircleY)) > outCircleR) {
            return false;
        }
        return true;
    }

    //判断手指是不是点击了加速
    public boolean clickAccelerate(float x, float y) {
        if (Math.sqrt((x - accelerateX) * (x - accelerateX) + (y - accelerateY) * (y - accelerateY)) > accelerateR) {
            return false;
        }
        return true;
    }

    //判断手指是不是点击了子弹
    private boolean clickShoot(float x, float y) {
        if (Math.sqrt((x - shootX) * (x - shootX) + (y - shootY) * (y - shootY)) > shootR) {
            return false;
        }
        return true;
    }


    private void calculateK(float x, float y) {
        xK = x - outCircleX;
        yK = y - outCircleY;

        if (xK > 0 && yK > 0) {//一
            mK = xK / yK;
            mAngle = (float) (180 - (Math.atan(mK) / Math.PI * 180));


        } else if (xK < 0 && yK > 0) {//二
            mK = (-xK) / yK;
            mAngle = (float) (Math.atan(mK) / Math.PI * 180 + 180);
        } else if (xK < 0 && yK < 0) {//三
            mK = xK / yK;
            mAngle = (float) (-Math.atan(mK) / Math.PI * 180);
        } else if (xK > 0 && yK < 0) {//四
            mK = xK / (-yK);
            mAngle = (float) (Math.atan(mK) / Math.PI * 180);
        }
        mAngle = mAngle + 90;
    }


    float[] id = new float[10];
    float[] x = new float[10];
    float[] y = new float[10];
    boolean[] touched = new boolean[10];

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d("pointcount", "onTouchEvent: " + event.getPointerCount());
        int action = event.getAction() & MotionEvent.ACTION_MASK;

        int pointerIndex = (event.getAction() & MotionEvent.ACTION_POINTER_ID_MASK) >>
                MotionEvent.ACTION_POINTER_ID_SHIFT;
        int pointerCount = event.getPointerCount();
        for (int i = 0; i < 10; i++) {
            if (i >= pointerCount) {
                touched[i] = false;
                id[i] = -1;
                continue;
            }
            if (event.getAction() != MotionEvent.ACTION_MOVE && i != pointerIndex) {
                /* If it's an up/down/cancel/out event, mask the id to see if
                we should process it for this touch point */
                continue;
            }
            int pointerId = event.getPointerId(i);
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_POINTER_DOWN:
                    touched[i] = true;
                    id[i] = pointerId;
                    x[i] = (int) event.getX(i);
                    y[i] = (int) event.getY(i);

//                    if (pointerInCircle(x[i], y[i])) {
//                        inCircleX = x[i];
//                        inCircleY = y[i];
//                        touchInCircle = true;
//                    } else {
//                        touchInCircle = false;
//                    }
                    if (clickAccelerate(x[i], y[i])) {
                        speed = (float) Math.sqrt(96);
                    }

                    if (clickShoot(x[i], y[i])) {

                            mBullet.setbX(actorX);
                            mBullet.setBy(actorY);
                            mBullet.setLive(true);
                            mBullet.setbR(15);
                            mBullet.setShoot(true);
                            mBullet.setbColor(Color.GREEN);

                    }
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_POINTER_UP:
                case MotionEvent.ACTION_OUTSIDE:
                case MotionEvent.ACTION_CANCEL:
                    touched[i] = false;
                    id[i] = -1;
                    x[i] = (int) event.getX(i);
                    y[i] = (int) event.getY(i);
                    if (pointerInCircle(x[i], y[i])) {
                        inCircleX = outCircleX;
                        inCircleY = outCircleY;
                    }
                    if (clickAccelerate(x[i], y[i])) {
                        speed = (float) Math.sqrt(64);
                    }

                    break;
                case MotionEvent.ACTION_MOVE:
                    touched[i] = true;
                    id[i] = pointerId;
                    x[i] = (int) event.getX(i);
                    y[i] = (int) event.getY(i);

                    if (pointerInCircle(x[i], y[i])) {
                        if (isInCircle(x[i], y[i])) {
                            inCircleX = x[i];
                            inCircleY = y[i];
                            //计算K值
                            calculateK(x[i], y[i]);
                        } else {
                            inCircleX = outCircleX;
                            inCircleY = outCircleY;
                        }
                    }
                    if (clickAccelerate(x[i], y[i])) {
                        speed = (float) Math.sqrt(96);
                    }
                    break;
            }
        }


//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                float dX = event.getX();
//                float dY = event.getY();
//                if (pointerInCircle(dX, dY)) {
//                    inCircleX = dX;
//                    inCircleY = dY;
//                    touchInCircle = true;
//                } else {
//                    touchInCircle = false;
//                }
//
//
//            case MotionEvent.ACTION_MOVE:
//                float mX = event.getX();
//                float mY = event.getY();
//                if (touchInCircle) {
//                    if (isInCircle(mX, mY)) {
//                        inCircleX = mX;
//                        inCircleY = mY;
//                        //计算K值
//                        calculateK(mX, mY);
//                    } else {
//                        inCircleX = outCircleX;
//                        inCircleY = outCircleY;
//                    }
//                }
//                if(clickAccelerate(mX,mY)){
//                    speed = (float) Math.sqrt(64);
//                }
//                break;
//            case MotionEvent.ACTION_UP:
//                inCircleX = outCircleX;
//                inCircleY = outCircleY;
//                speed = (float) Math.sqrt(16);
//                break;
//        }

        return true;
    }

}
