package com.example.womensafetyapp_v2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_PHONE_CALL=1;
    private static final int REQUEST_LOCATION=1;
    Button startApp,emergency_btn,helpline_btn;
    ImageButton exit_btn,about_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startApp = (Button)findViewById(R.id.start_button);
        emergency_btn = (Button)findViewById(R.id.emergency_button);
        helpline_btn = (Button)findViewById(R.id.helpline_button);
        exit_btn = (ImageButton)findViewById(R.id.exit_btn);
        about_btn = (ImageButton)findViewById(R.id.about_btn);
        getPermission();

        startApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!Settings.canDrawOverlays(MainActivity.this))
                {
                    getPermission();
                }
                else
                {
                    Intent intent = new Intent(MainActivity.this,WidgetService.class);
                    startService(intent);
                    finish();
                }

            }
        });

        emergency_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,DataActivity.class);
                startActivity(intent);
            }
        });

        exit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });

        helpline_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        });

        about_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),AboutusActivity.class);
                startActivity(intent);
            }
        });


    }

    public void getPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, 1);

        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);


   }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1)
        {
            if(!Settings.canDrawOverlays(MainActivity.this))
            {
                Toast.makeText(this,"Permission denied by User",Toast.LENGTH_LONG).show();
            }
        }
    }
}