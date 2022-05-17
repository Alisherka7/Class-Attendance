package com.example.classattendance;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.bson.Document;

import java.io.ByteArrayOutputStream;
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
    ByteArrayOutputStream baos = new ByteArrayOutputStream();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        sharedPreferences = getSharedPreferences("currentUserId", Context.MODE_PRIVATE);
        textView = (TextView) findViewById(R.id.textView);
        logout = (Button)findViewById(R.id.logOut);
        userId = sharedPreferences.getString("userId", "");
        Document studentData = new Document().append("studentId", userId);

        Realm.init(this);
        app = new App(new AppConfiguration.Builder(AppId).build());
        app.loginAsync(Credentials.apiKey(ApiKey), new App.Callback<User>() {
            @Override
            public void onResult(App.Result<User> result) {
                user = app.currentUser();
                mongoClient = user.getMongoClient("mongodb-atlas");
                mongoDatabase = mongoClient.getDatabase("attendance");
                mongoCollection = mongoDatabase.getCollection("student");
                mongoCollection.findOne(studentData).getAsync(userD -> {
                    if(userD.isSuccess()){
                        Document student = userD.get();
                        textView.setText(student.getString("name") + "\n" + student.getString("lastName"));
                        Log.v("User", "Found");
                    }else{
                        Log.v("User", "Error" + userD.getError());
                    }
                });
                Log.v("User", "Database Connected");
                String[] projection = { MediaStore.Images.Media.DATA };
                for(String i : projection){
                    System.out.println(i);
                }
                String res = getFileToByte(String.valueOf("drawable://" + R.drawable.qwerty));
                mongoCollection.insertOne(new Document("studentId", userId).append("photo","hi")).getAsync(r -> {
                    if(r.isSuccess()){
                        Log.v("User", "Inserted done");
                    }else{
                        Log.v("User", "Error " + r.getError());
                    }
                });
            }
        });




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
    }
    // Put the image file path into this method
    public static String getFileToByte(String filePath){
        Bitmap bmp = null;
        ByteArrayOutputStream bos = null;
        byte[] bt = null;
        String encodeString = null;
        try{
            bmp = BitmapFactory.decodeFile(filePath);
            bos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bt = bos.toByteArray();
            encodeString = Base64.encodeToString(bt, Base64.DEFAULT);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return encodeString;
    }
}