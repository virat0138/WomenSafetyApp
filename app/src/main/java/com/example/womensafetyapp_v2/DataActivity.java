package com.example.womensafetyapp_v2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class DataActivity extends AppCompatActivity {

    Button save_btn;
    ImageButton back;
    TextView phone1,phone2,phone3;
    public String text1,text2,text3;
    TextView view_phone1,view_phone2,view_phone3;
    public static final String SHARED_PREFS = "sharedPrefs";

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        return;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);


        save_btn = (Button)findViewById(R.id.save_button);
        back = (ImageButton)findViewById(R.id.back_btn);
        phone1 = (TextView)findViewById(R.id.ph1);
        phone2 = (TextView)findViewById(R.id.ph2);
        phone3 = (TextView)findViewById(R.id.ph3);
        view_phone1 = (TextView)findViewById(R.id.contact1);
        view_phone2 = (TextView)findViewById(R.id.contact2);
        view_phone3 = (TextView)findViewById(R.id.contact3);

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(phone1.length()!=10)
                {
                    phone1.setError("Phone Number 1 needs to be 10 digits");
                }
                else if(phone2.length()!=10)
                {
                    phone2.setError("Phone Number 2 needs to be 10 digits");
                }
                else if(phone3.length()!=10)
                {
                    phone3.setError("Phone Number 3 needs to be 10 digits");
                }
                else {
                    saveData();
                    loadData();
                    updateViews();
                }
        }
        });

        loadData();
        updateViews();
    }

    public void onClick_Back(View v)
    {
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }

    public void saveData()
    {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Num 1",phone1.getText().toString());
        editor.putString("Num 2",phone2.getText().toString());
        editor.putString("Num 3",phone3.getText().toString());
        editor.commit();


        Toast.makeText(getApplicationContext(),"Phone Number Saved",Toast.LENGTH_SHORT).show();
    }

    public void loadData()
    {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        text1 = sharedPreferences.getString("Num 1","");
        text2 = sharedPreferences.getString("Num 2","");
        text3 = sharedPreferences.getString("Num 3","");
    }

    public void updateViews()
    {
        view_phone1.setText(text1);
        view_phone2.setText(text2);
        view_phone3.setText(text3);
    }

}