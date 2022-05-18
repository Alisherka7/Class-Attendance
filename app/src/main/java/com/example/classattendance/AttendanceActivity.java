package com.example.classattendance;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AttendanceActivity extends AppCompatActivity {

    LinearLayout attendanceLinearLayout;
    Button backButton;
    final int n = 4;
    final TextView[] classTextView = new TextView[n];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        String[] str = {"first", "second","third","four"};
        attendanceLinearLayout = (LinearLayout) findViewById(R.id.attendanceLinearLayout);
        for(int i =0; i<n; i++){
            TextView classTextView = new TextView(this);
            classTextView.setHeight(200);
            classTextView.setText(str[i]);
            classTextView.setTextSize(25);
            classTextView.setPadding(50,0,0,0);

            classTextView.setGravity(Gravity.CENTER_VERTICAL);
            if(i%2 == 0){
                int color = Color.parseColor("#ACC8E5");
                classTextView.setBackgroundColor(color);
            }
            attendanceLinearLayout.addView(classTextView);
        }
    }
}