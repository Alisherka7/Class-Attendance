package com.example.classattendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;

public class MainActivity extends AppCompatActivity {
    App app;
    String AppId = "application-0-zzrvt";
    String currentUserId = "currentUserId";
    String userId = "userId";
    EditText login, password;
    Button loginButton;
    TextView signup;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login = (EditText)findViewById(R.id.login);
        password = (EditText)findViewById(R.id.password);
        loginButton = (Button)findViewById(R.id.loginButton);
        signup = (TextView)findViewById(R.id.signup);


        sharedPreferences = getSharedPreferences(currentUserId, Context.MODE_PRIVATE);

        // Realm Initialize
        Realm.init(this);
        app = new App(new AppConfiguration.Builder(AppId).build());

        //로그인 경우
        if(sharedPreferences.contains(userId)){
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            String i = sharedPreferences.getString(userId, "");
            System.out.println("main Activity-------->" + i);
            startActivity(intent);
        }
        
 //     로그인 버튼 클릭 경우
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 로그인 및 비밀번호 비어 있는지 확인
                if(!(login.getText().toString().trim().length() > 0) || !(password.getText().toString().trim().length() > 0)){
                    Log.v("User", "Failed");
                }else{
                    // MongoDB Realm 사용자 계정 확인
                    Credentials credentials = Credentials.emailPassword(login.getText().toString(), password.getText().toString());
                    app.loginAsync(credentials, new App.Callback<User>() {
                        @Override
                        public void onResult(App.Result<User> result) {
                            if(result.isSuccess()) {
                                // 로그인 경우
                                User user = app.currentUser();

                                // Shared Preferences 이용하여 사용자의 계정 ID를 저장한다.
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(userId, user.getId().toString());
                                editor.commit();
                                Log.v("User", "Logged success");
                                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                                startActivity(intent);
                                finish();
                            }else{
                                // 로그인 실패 경우
                                Log.v("User", "Failed");
                            }
                        }
                    });
                }
            }
        });

        // 회원 가입 버튼
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
