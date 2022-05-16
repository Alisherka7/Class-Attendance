package com.example.classattendance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.sql.SQLOutput;

public class ProfileActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    TextView textView;
    Button logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        sharedPreferences = getSharedPreferences("currentUserId", Context.MODE_PRIVATE);
        textView = (TextView) findViewById(R.id.textView);
        logout = (Button)findViewById(R.id.logOut);
        String i = sharedPreferences.getString("userId", "");
        textView.setText(i);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("userId");
                editor.apply();
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        System.out.println("Profile activity ->" + i);
    }
}