package com.bili.control;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.Window;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.kongqw.rockerlibrary.view.RockerView;

public class surface extends Activity implements Runnable{
    private SurfaceHolder holder;
    private Thread mythread;
    private Canvas canvas;
    URL videoUrl;
    //private String url="http://192.168.1.104:8083/?action=snapshot";
    private int w;
    private int h;
    HttpURLConnection conn;
    Bitmap bmp;
    Integer ang=0;
    Integer speed=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surface);

        SeekBar seekBar=findViewById(R.id.seekbar_speed);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                speed=progress;
                refre();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBar.setProgress(30);


        RockerView rockerView = (RockerView) findViewById(R.id.rockerView);
        // 设置回调模式
        rockerView.setCallBackMode(RockerView.CallBackMode.CALL_BACK_MODE_STATE_CHANGE);
        // 监听摇动角度


        rockerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction()==0) {
                    Log.e("send", "按下");
                }
                if(event.getAction()==1) {
                    Log.e("send", "松开");
                }
                if(event.getAction()==2) {
                    Log.e("send", "拖动");
                }


                return false;
            }
        });
        rockerView.setOnAngleChangeListener(new RockerView.OnAngleChangeListener() {
            @Override
            public void onStart() {
                //mLogRight.setText(null);
            }

            @Override
            public void angle(double angle) {
                //mLogRight.setText("摇动角度 : " + angle);
                Double dou=angle;
                ang=dou.intValue();
                //refre();

            }

            @Override
            public void onFinish() {
                //mLogRight.setText(null);
                ang=0;
                //refre();
            }
        });



        //url = getIntent().getExtras().getString("CameraIp");

        w = getWindowManager().getDefaultDisplay().getWidth();
        //h = getWindowManager().getDefaultDisplay().getHeight();

        SurfaceView surface = findViewById(R.id.surface);



        surface.setKeepScreenOn(true);// 保持屏幕常亮
        mythread = new Thread(this);
        holder = surface.getHolder();
        holder.addCallback(new Callback() {

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                // TODO Auto-generated method stub

            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                // TODO Auto-generated method stub
                mythread.start();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                       int height) {
                // TODO Auto-generated method stub

            }
        });

    }

    private void refre(){
        TextView textView=findViewById(R.id.text_rock_log);
        textView.setText("angle:"+ang+"   speed:"+speed);
    }



    private void draw() {
        // TODO Auto-generated method stub
        try {
            InputStream inputstream = null;
            //创建一个URL对象
            String url = "http://192.168.1.104:8083/?action=snapshot";
            videoUrl=new URL(url);
            //利用HttpURLConnection对象从网络中获取网页数据
            conn = (HttpURLConnection)videoUrl.openConnection();
            //设置输入流
            conn.setDoInput(true);
            //连接
            conn.connect();
            //得到网络返回的输入流
            inputstream = conn.getInputStream();
            //创建出一个bitmap

            bmp = BitmapFactory.decodeStream(inputstream);

            canvas = holder.lockCanvas();
            canvas.drawColor(Color.WHITE);
            RectF rectf = new RectF(0, 0, w, w*3/4);
            canvas.drawBitmap(bmp, null, rectf, null);
            holder.unlockCanvasAndPost(canvas);
            //关闭HttpURLConnection连接
            conn.disconnect();
        } catch (Exception ex) {
        } finally {
        }
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        while(true){
            draw();
        }
    }



}