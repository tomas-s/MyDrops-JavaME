package com.example.tomas.mydrops;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

//http://sourcey.com/beautiful-android-login-and-signup-screens-with-material-design/
public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void authentificaion(View view){
        Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
        String value="Vytaj v druje aktivite";
        intent.putExtra("key",value);
        startActivity(intent);
    }

}
