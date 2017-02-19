package com.ty.android.recyclergame.model;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

/**
 * Created by android_1 on 2016/12/10.
 */

public class Bullet{
    private float offsetX ;
    private float offsetY;
    private float bX;
    private float by;
    private float bR;
    private float speed;
    private int bColor;
    private boolean isLive;
    private boolean isShoot;
    private Paint paint;


    public Bullet() {
        init();
    }

    public Bullet(float offsetX,float offsetY, float bX, float by, float speed, int bColor, boolean isLive) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.bX = bX;
        this.by = by;
        this.speed = speed;
        this.bColor = bColor;
        this.isLive = isLive;
        init();
    }
    private void init(){
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(bColor);
    }

    public float getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(float offsetX) {
        this.offsetX = offsetX;
    }

    public float getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(float offsetY) {
        this.offsetY = offsetY;
    }

    public float getbX() {
        return bX;
    }

    public void setbX(float bX) {
        this.bX = bX;
    }

    public float getBy() {
        return by;
    }

    public void setBy(float by) {
        this.by = by;
    }

    public float getbR() {
        return bR;
    }

    public void setbR(float bR) {
        this.bR = bR;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public int getbColor() {
        return bColor;
    }

    public void setbColor(int bColor) {
        this.bColor = bColor;
    }

    public boolean isLive() {
        return isLive;
    }

    public void setLive(boolean live) {
        isLive = live;
    }

    public boolean isShoot() {
        return isShoot;
    }

    public void setShoot(boolean shoot) {
        isShoot = shoot;
    }

    public void drawItSelf(Canvas canvas){

        if(isLive()){
            paint.setColor(getbColor());
            Log.d("drawItSelf", "drawItSelf: "+getbX()+"    "+getBy());
            canvas.drawCircle(getbX(),getBy(),getbR(),paint);
        }
    }
}
