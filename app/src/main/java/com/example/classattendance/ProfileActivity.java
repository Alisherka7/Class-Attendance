package com.example.classattendance;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import org.bson.Document;
import java.io.ByteArrayOutputStream;
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
    TextView studentName, studentLastName, studentMajor;
    String userId = "";
    String AppId = "application-0-zzrvt";
    String ApiKey = "FAzsYOpI23DJjV5hIawVdoelTgpOq8KyTDoUvfdxDHqNWJdehVuaGViJMW2qHkxY";
    Button logout, classAttandanceButton, scheduleButton;
    ImageView studentImg;
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
        studentName = (TextView) findViewById(R.id.studentName);
        studentLastName = (TextView) findViewById(R.id.studentLastName);
        studentImg = (ImageView)findViewById(R.id.studentImg);
        studentMajor = (TextView)findViewById(R.id.studentMajor);
        classAttandanceButton = (Button)findViewById(R.id.classAttendanceButton);
        scheduleButton = (Button)findViewById(R.id.scheduleButton);
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
                        studentName.setText(student.getString("name"));
                        studentLastName.setText(student.getString("lastName"));
                        studentMajor.setText(student.getString("major"));
                        Bitmap bMap = base64ToImg(student.getString("studentImg"));
                        studentImg.setImageBitmap(bMap);
                        Log.v("User", "Found");
                    }else{
                        Log.v("User", "Error" + userD.getError());
                    }
                });
                Log.v("User", "Database Connected");

//                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.qwerty);
//                String stringImg = imageToBase64(bitmap);
//                mongoCollection.insertOne(new Document("studentId", userId).append("photo",stringImg)).getAsync(r -> {
//                    if(r.isSuccess()){
//                        Log.v("User", "Inserted done");
//                    }else{
//                        Log.v("User", "Error " + r.getError());
//                    }
//                });
            }
        });

        // LogOut button
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

        // Check Attendance
        classAttandanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, AttendanceActivity.class);
                startActivity(intent);
            }
        });

        // Schedule Button
        scheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, ScheduleActivity.class);
                startActivity(intent);
            }
        });
    }
    // Put the image bitmap
    public static String imageToBase64(Bitmap bitmap){
        //encode image to base64 string
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return imageString;
    }

    // Base64 to Image
    public static Bitmap base64ToImg(String base64){
        byte[] imageBytes = Base64.decode(base64, Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        return decodedImage;
    }


}