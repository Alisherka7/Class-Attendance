package com.example.classattendance;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;

public class MainActivity extends AppCompatActivity {

    String AppId = "application-0-zzrvt";
    EditText login, password;
    Button loginButton;
    TextView signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login = (EditText)findViewById(R.id.login);
        password = (EditText)findViewById(R.id.password);
        loginButton = (Button)findViewById(R.id.loginButton);
        signup = (TextView)findViewById(R.id.signup);

        Realm.init(this);
        App app = new App(new AppConfiguration.Builder(AppId).build());
        
 //     MongoDB Realm Login Rrequest
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Credentials credentials = Credentials.emailPassword(login.getText().toString(), password.getText().toString());
                app.loginAsync(credentials, new App.Callback<User>() {
                    @Override
                    public void onResult(App.Result<User> result) {
                        if(result.isSuccess()) {
                            Log.v("User", "Logged success");
                            MongoDBRealm i = new MongoDBRealm();
                        }else{
                            Log.v("User", "Failed");
                        }
                    }
                });
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
