package com.example.classattendance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.bson.Document;

import java.lang.annotation.Documented;
import java.sql.SQLOutput;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;

public class ProfileActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    TextView textView;
    String userId = "";
    String AppId = "application-0-zzrvt";
    String ApiKey = "FAzsYOpI23DJjV5hIawVdoelTgpOq8KyTDoUvfdxDHqNWJdehVuaGViJMW2qHkxY";
    Button logout;
    App app;
    User user;
    MongoClient mongoClient;
    MongoDatabase mongoDatabase;
    MongoCollection<Document> mongoCollection;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        sharedPreferences = getSharedPreferences("currentUserId", Context.MODE_PRIVATE);
        textView = (TextView) findViewById(R.id.textView);
        logout = (Button)findViewById(R.id.logOut);
        userId = sharedPreferences.getString("userId", "");


        Realm.init(this);
        app = new App(new AppConfiguration.Builder(AppId).build());
        app.loginAsync(Credentials.apiKey(ApiKey), new App.Callback<User>() {
            @Override
            public void onResult(App.Result<User> result) {
                user = app.currentUser();
                mongoClient = user.getMongoClient("mongodb-atlas");
                mongoDatabase = mongoClient.getDatabase("attendace");
                mongoCollection = mongoDatabase.getCollection("student");
                Log.v("User", "Database Connected");
            }
        })




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