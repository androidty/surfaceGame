package com.ty.android.recyclergame.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.SystemClock;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by android_1 on 2016/11/23.
 */

public class Tool {

    public static void forceStopRecyclerViewScroll(RecyclerView mRecyclerView) {
        mRecyclerView.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_CANCEL, 0, 0, 0));
    }


    public static Bitmap createRepeater(int width, int height,Bitmap src){
        int wCount = (width + src.getWidth() - 1) / src.getWidth();
        int hCount = (height + src.getHeight() - 1) / src.getHeight();
        Log.d("createRepeater", "createRepeater:      "+src.getWidth()+"   "+src.getHeight());
        Bitmap bitmap = Bitmap.createBitmap(width*wCount, height*hCount, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        for (int idy = 0;idy<hCount;++idy) {
            for (int idx = 0; idx < wCount; ++idx) {
                canvas.drawBitmap(src, idx * src.getWidth(), idy*src.getHeight(), null);
            }
        }
        Log.d("createRepeater", "createRepeater: "+bitmap.getWidth()+"   "+bitmap.getHeight());
        return bitmap;
    }
}
