package com.example.msi.geolocationapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import io.skygear.skygear.AuthResponseHandler;
import io.skygear.skygear.Container;
import io.skygear.skygear.Error;
import io.skygear.skygear.User;

public class MainActivity extends AppCompatActivity {

    private EditText loginname;
    private EditText loginpw;
    private EditText signupname;
    private EditText signuppw;
    private Button loginbutton;
    private Button signupbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Sign Up / Log In");

        // get Skygear Container
        final Container skygear = Container.defaultContainer(this);

        loginname = (EditText) findViewById(R.id.inputloginname);
        loginpw = (EditText) findViewById(R.id.inputloginPW);
        signupname = (EditText) findViewById(R.id.inputsignupname);
        signuppw = (EditText) findViewById(R.id.inputsignupPW);

        loginbutton = (Button) findViewById(R.id.loginbutton);
        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = loginname.getText().toString();
                final String password = loginpw.getText().toString();
                skygear.loginWithUsername(username, password, new AuthResponseHandler() {
                    @Override
                    public void onAuthSuccess(User user) {
                        nextPage();
                    }

                    @Override
                    public void onAuthFail(Error error) {

                    }
                });
            }
        });

        signupbutton = (Button) findViewById(R.id.signupbutton);
        signupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = signupname.getText().toString();
                final String password = signuppw.getText().toString();
                skygear.signupWithUsername(username, password, new AuthResponseHandler() {
                    @Override
                    public void onAuthSuccess(User user) {
                        nextPage();
                    }

                    @Override
                    public void onAuthFail(Error error) {

                    }
                });
            }
        });
    }

    //Function for going to the next page
    private void nextPage(){
        Intent i = new Intent();
        i.setClass(this, MapPage.class);
        startActivity(i);
    }
}
