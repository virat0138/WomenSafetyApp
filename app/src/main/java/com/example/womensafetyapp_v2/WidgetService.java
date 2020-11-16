package com.example.womensafetyapp_v2;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;


public class WidgetService extends Service
{

    int LAYOUT_FLAG;
    float height,width;
    WindowManager wm;
    View mFloatingView;
    ImageButton alarm_btn,call_btn,phonebook_btn,sms_btn,close_btn;
    boolean onClickedAlarm=false;
    private final static String default_notification_channel_id = "default";

    //Uri notification;
    //Ringtone r ;
    MediaPlayer mPlayer;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }

        mPlayer = MediaPlayer.create(getApplicationContext(), R.raw.alarm);




        wm = (WindowManager)getSystemService(WINDOW_SERVICE);
        mFloatingView = LayoutInflater.from(this).inflate(R.layout.layout_widget, null);
        height = wm.getDefaultDisplay().getHeight();
        width = wm.getDefaultDisplay().getWidth();
        final WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, LAYOUT_FLAG, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);


        layoutParams.gravity = Gravity.TOP | Gravity.RIGHT;
        layoutParams.x = 0;
        layoutParams.y = 100;

        wm.addView(mFloatingView, layoutParams);
        mFloatingView.setBackgroundResource(R.drawable.bg_overlay);
        mFloatingView.setVisibility(View.VISIBLE);

        close_btn = (ImageButton)mFloatingView.findViewById(R.id.close_btn);
        alarm_btn = (ImageButton)mFloatingView.findViewById(R.id.alarm_btn);
        call_btn = (ImageButton)mFloatingView.findViewById(R.id.call_btn);
        phonebook_btn = (ImageButton)mFloatingView.findViewById(R.id.phone_btn);
        sms_btn = (ImageButton)mFloatingView.findViewById(R.id.sms_btn);


        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopSelf();
            }
        });

        alarm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickedAlarmFunction();

            }

            private void onClickedAlarmFunction() {
                TriggerAlarmFunction(onClickedAlarm);
                onClickedAlarm = !onClickedAlarm;
            }

            private void TriggerAlarmFunction(boolean onClickedAlarm) {
                if(!onClickedAlarm)
                {
                    mPlayer.start();
                    mPlayer.setLooping(true);
                    alarm_btn.setBackgroundResource(R.drawable.alarm_overlay_selected);
                    Toast.makeText(getApplicationContext(),"Security Alarm Enabled",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    mPlayer.pause();
                    alarm_btn.setBackgroundResource(R.drawable.alarm_overlay);
                    Toast.makeText(getApplicationContext(),"Security Alarm Disabled",Toast.LENGTH_SHORT).show();
                }
            }
        });

        call_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setData(Uri.parse("tel:100"));
                if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(getApplicationContext(), "Call Permission Denied by the User", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    startActivity(intent);
                }
                //Toast.makeText(getApplicationContext(), "Feature for Emergency Calls", Toast.LENGTH_SHORT).show();
            }
        });

        call_btn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setData(Uri.parse("tel:181"));
                if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(getApplicationContext(), "Call Permission Denied by the User", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    startActivity(intent);
                }
                return true;
            }
        });

        phonebook_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent opencontacts = new Intent(Intent.ACTION_VIEW, ContactsContract.Contacts.CONTENT_URI);
                opencontacts.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(opencontacts);
                Toast.makeText(getApplicationContext(),"Opening Contacts",Toast.LENGTH_SHORT).show();
            }
        });

        phonebook_btn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent open_emg = new Intent(getApplicationContext(),DataActivity.class);
                open_emg.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(open_emg);
                return true;
            }
        });

        sms_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent open_map = new Intent(getApplicationContext(),SMSapp.class);
                open_map.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  //9521201009
                startActivity(open_map);
                Toast.makeText(getApplicationContext(), "sending SMS with location", Toast.LENGTH_SHORT).show();
            }
        });

        mFloatingView.setOnTouchListener(new View.OnTouchListener() {
                int InitialX,InitialY;
                float InitialTouchX,InitialTouchY;
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch(event.getAction())
                    {
                        case MotionEvent.ACTION_MOVE:
                            layoutParams.x = InitialX + (int)(InitialTouchX-event.getRawX());
                            layoutParams.y = InitialY + (int)(event.getRawY()-InitialTouchY);
                            wm.updateViewLayout(mFloatingView,layoutParams);
                            return true;



                        case MotionEvent.ACTION_DOWN:
                            InitialX=layoutParams.x;
                            InitialY=layoutParams.y;
                            InitialTouchX=event.getRawX();
                            InitialTouchY=event.getRawY();
                            return true;


                    }
                    return false;
            }
        });

        return START_STICKY;
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        wm.removeView(mFloatingView);
        mPlayer.stop();
        }
    }
