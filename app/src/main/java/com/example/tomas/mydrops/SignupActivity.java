package com.example.tomas.mydrops;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class SignupActivity extends AppCompatActivity {
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
    }

    public void toLogin(View view){
        Intent SignupActivity = new Intent(SignupActivity.this, LoginActivity.class);
        startActivity(SignupActivity);
    }


    public void createAccount(View view){
        EditText input_email =(EditText) findViewById(R.id.input_email);
        String email = input_email.getText().toString();
        EditText input_password =(EditText) findViewById(R.id.input_password);
        String password = input_password.getText().toString();
        EditText input_name =(EditText) findViewById(R.id.input_name);
        String name = input_name.getText().toString();

        final Button btnSignUp = (Button) findViewById(R.id.btn_signup);


        if(validate(view,email)) {
            JsonObject json = new JsonObject();
            json.addProperty("email", email);
            json.addProperty("password", password);
            json.addProperty("name", name);
            btnSignUp.setEnabled(false);
            //add progress Dialog
            progressDialog = new ProgressDialog(SignupActivity.this);
            progressDialog.setMessage("Registering...");
            progressDialog.show();

            Ion.with(getApplicationContext())
                    .load("http://85.93.125.205:8126/api/register")
                    .setJsonObjectBody(json)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {

                            if (result != null) {
                                Intent toLoginActivity = new Intent(SignupActivity.this, LoginActivity.class);
                                startActivity(toLoginActivity);
                                Toast.makeText(SignupActivity.this, "Verify your email", Toast.LENGTH_SHORT).show();


                            } else {
                                Toast.makeText(SignupActivity.this, "Bad insert data", Toast.LENGTH_SHORT).show();

                            }
                            String bp = result.toString();
                            progressDialog.dismiss();
                            btnSignUp.setEnabled(true);


                        }
                    });
        }

    }



    public boolean validate(View view, String email) {
        boolean valid = true;
        EditText input_email =(EditText) findViewById(R.id.input_email);
        EditText input_password =(EditText) findViewById(R.id.input_password);
        EditText input_name =(EditText) findViewById(R.id.input_name);

        if (email.isEmpty() ) {
            Toast.makeText(SignupActivity.this, "Email is not valid", Toast.LENGTH_SHORT).show();
            valid = false;
            return valid;
        } else {
            input_email.setError(null);
        }

        if (input_password.length() < 4 || input_password.length() > 10) {
            Toast.makeText(SignupActivity.this, "Password is not valid", Toast.LENGTH_SHORT).show();
            valid = false;
            return valid;
        } else {
            input_password.setError(null);
        }

        if (input_name.length() < 1 || input_name.length() > 20) {
            Toast.makeText(SignupActivity.this, "Name is not valid", Toast.LENGTH_SHORT).show();
            valid = false;
            return valid;
        } else {
            input_name.setError(null);
        }

        return valid;
    }
}
