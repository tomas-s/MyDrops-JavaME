package com.example.tomas.mydrops;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONException;
import org.json.JSONObject;

//http://sourcey.com/beautiful-android-login-and-signup-screens-with-material-design/
public class LoginActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    String sensors=null;
    Intent toMenuActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void authentificaion(View view){

        EditText eLogin =(EditText) findViewById(R.id.login);
        String login = eLogin.getText().toString();
        EditText ePassword =(EditText) findViewById(R.id.passwprd);
        String password = ePassword.getText().toString();

        final Button btnLogin = (Button) findViewById(R.id.btn_login);


        if(validate(view,login,password)) {
            JsonObject json = new JsonObject();
            json.addProperty("email", login);
            json.addProperty("password", password);
            btnLogin.setEnabled(false);
            //add progress Dialog
            progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setMessage("Authenticating...");
            progressDialog.show();

            Ion.with(getApplicationContext())
                    .load("http://85.93.125.205:8126/api/login")
                    .setJsonObjectBody(json)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            if (result != null) {
                                 toMenuActivity = new Intent(LoginActivity.this, ShowDrops.class);


                                String id = result.get("id").toString();
                                String email = result.get("email").toString();
                                int confirmed = result.get("confirmed").getAsInt();
                                toMenuActivity.putExtra("id", id);
                                toMenuActivity.putExtra("email", email);
                                String url = "http://85.93.125.205:8126/api/users/"+id+"/sensors";
                                if(confirmed ==1){

                                    Ion.with(getApplicationContext())
                                            .load(url)
                                            .asString()
                                            .setCallback(new FutureCallback<String>() {
                                                @Override
                                                public void onCompleted(Exception e, String result) {
                                                    sensors = result;
                                                     toMenuActivity.putExtra("sensors", sensors);
                                                     startActivity(toMenuActivity);
                                                }
                                            });

                                }
                                else {
                                    Toast.makeText(LoginActivity.this, "Confirm you email", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                Toast.makeText(LoginActivity.this, "Bad email or password", Toast.LENGTH_SHORT).show();
                            }
                            progressDialog.dismiss();
                            btnLogin.setEnabled(true);


                        }
                    });
        }

    }


    public boolean validate(View view,String login, String email) {
        boolean valid = true;
        EditText eLogin =(EditText) findViewById(R.id.login);
        EditText ePassword =(EditText) findViewById(R.id.passwprd);

        if (email.isEmpty() ) {
            Toast.makeText(LoginActivity.this, "Email is not valid", Toast.LENGTH_SHORT).show();
            valid = false;
            return valid;
        } else {
            eLogin.setError(null);
        }

        if (ePassword.length() < 4 || ePassword.length() > 10) {
            Toast.makeText(LoginActivity.this, "Password is not valid", Toast.LENGTH_SHORT).show();
            valid = false;
            return valid;
        } else {
            ePassword.setError(null);
        }

        return valid;
    }

    public void signIpForm(View view){
        Intent SignupActivity = new Intent(LoginActivity.this, SignupActivity.class);
       startActivity(SignupActivity);
    }



    public void fakeauthentificaion(View view){
        Intent toMennu = new Intent(LoginActivity.this, MenuActivity.class);
        startActivity(toMennu);
    }


}
