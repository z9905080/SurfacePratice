package com.example.shouting.surfacepratice;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Shouting on 2017/10/23.
 */

public class TakeView extends SurfaceView implements SurfaceHolder.Callback {

    SurfaceHolder holder;
    Bitmap takebp;
    Paint paint;
    Rect takerect;  //矩形容器
    int x,y;
    ShowThread st;
    int touchX=0,touchY=0;
    boolean chk = false;
    int tempwidth=0;
    int tempheight=0;

    //constructor
    public TakeView(MainActivity context) {
        super(context);
        // TODO Auto-generated constructor stub
        getHolder().addCallback(this);
        takebp = BitmapFactory.decodeResource(getResources(), R.drawable.a);
        //指定矩形容器的大小等於圖片takebp的大小
        takerect = new Rect(x,y,takebp.getWidth(),takebp.getHeight());
        holder = getHolder();
        st = new ShowThread();
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(80);
    }

    public void DoDraw(Canvas canvas) {
        canvas.drawColor(Color.BLACK);
        canvas.drawBitmap(takebp, x, y, null);
        canvas.drawText("Tx = " + touchX + " Ty = " + touchY ,10, 100, paint);
        canvas.drawText("takerect(Left,Top):" + takerect.left + "," + takerect.top ,10, 200, paint);
        canvas.drawText("chk : " + chk, 10, 300, paint);
        canvas.drawText("tempX,Y : " + tempwidth + "," + tempheight, 10, 400, paint);
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN &&
                takerect.contains((int)event.getX(),(int)event.getY())) {
            touchX = (int)event.getX();
            touchY = (int)event.getY();
            tempwidth = touchX - x;
            tempheight = touchY -y;
            chk = true;
        }
        else if ( (event.getAction() == MotionEvent.ACTION_MOVE) && chk) {
            touchX = (int)event.getX();
            touchY = (int)event.getY();
            //讓點擊的座標成為圖片的中心點
            //takebp.getWidth()/2;
            //takebp.getHeight()/2;
            x = touchX - tempwidth;
            y = touchY - tempheight;
            takerect.offsetTo(x, y);
        }
        else if (event.getAction() == MotionEvent.ACTION_UP) {
            chk = false;
        }
        return true;
    }

    public class ShowThread extends Thread {

        Canvas canvas;
        boolean flag = false;
        int span = 20;

        //constructor
        public ShowThread() {
            flag = true;
        }

        public void run() {
            while(flag) {
                try {
                    synchronized(holder) {
                        canvas = holder.lockCanvas();
                        DoDraw(canvas);
                        holder.unlockCanvasAndPost(canvas);
                        Thread.sleep(span);
                    }
                } catch (Exception e) { e.printStackTrace(); }
            }
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        // TODO Auto-generated method stub
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        st.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        st.interrupt();
    }
}
