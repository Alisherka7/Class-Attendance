package com.example.classattendance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.User;

public class RegistrationActivity extends AppCompatActivity {


    SharedPreferences sharedPreferences;
    String AppId = "application-0-zzrvt";
    EditText login, password, confirmPassword;
    TextView backToLoginButton;
    Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // SharedPreferences 초기화
        sharedPreferences = getSharedPreferences("currentUserId", Context.MODE_PRIVATE);

        login = (EditText)findViewById(R.id.login);
        password = (EditText)findViewById(R.id.password);
        confirmPassword = (EditText)findViewById(R.id.passwordConfirm);
        signUpButton = (Button)findViewById(R.id.signupButton);
        backToLoginButton = (TextView)findViewById(R.id.backToLoginButton);

        // Realm Initialize
        Realm.init(this);
        App app = new App(new AppConfiguration.Builder(AppId).build());

        //휘원가입 버튼
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 비어 있는지 확인
                if(!TextUtils.isEmpty(login.getText().toString()) && !TextUtils.isEmpty(password.getText().toString()) && !TextUtils.isEmpty(confirmPassword.getText().toString())){
                    if(password.getText().toString().equals(confirmPassword.getText().toString())){
                        // mongodb에게 회원 가입 요청 제공
                        app.getEmailPassword().registerUserAsync(login.getText().toString(), password.getText().toString(), it->{
                            if(it.isSuccess()){
                                Log.v("User", "Registered with email success");
                                User user = app.currentUser();
                                // 회원가입 성공 경우에는 SharedPreferences에서 로그인 데이터를 저장하고 사용자 계정으로 들어가기
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("userId", user.getId().toString());
                                editor.commit();
                                Intent intent = new Intent(RegistrationActivity.this, ProfileActivity.class);
                                startActivity(intent);
                                finish();
                            }else{
                                Log.v("user", "Registration Failed ->" + it.getError());
                                Toast.makeText(RegistrationActivity.this, "사용할 수 없는 로그인입니다.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else {
                        Log.v("User", "Password confirm is not correct");
                        Toast.makeText(RegistrationActivity.this, "비밀번호가 맞지 않습니다.", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Log.v("User", "필수 입력란이 비었습니다. 확인해 주세요.");
                }
            }
        });

        // 로그인 액티비티로 돌아가기
        backToLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

}