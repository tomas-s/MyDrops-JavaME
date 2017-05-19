package com.example.tomas.mydrops;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

//http://sourcey.com/beautiful-android-login-and-signup-screens-with-material-design/
public class LoginActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    String sensors=null;
    Intent toMenuActivity;



    private  static String url = "http://drops.sde.cz/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if(!(isInternetAvailable(getApplicationContext()))){
            Toast.makeText(LoginActivity.this, "Internet is not available", Toast.LENGTH_SHORT).show();
        }
    }

    public static String getUrl() {
        return url;
    }

    /**
     * Overi dostpnost internetoveho pripojenia
     * @param context
     * @return
     */
    public static boolean isInternetAvailable(Context context) {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;
        boolean connectionavailable = false;
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        NetworkInfo informationabtnet = cm.getActiveNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            try {
                if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                    if (ni.isConnected()) haveConnectedWifi = true;
                if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                    if (ni.isConnected()) haveConnectedMobile = true;
                if (informationabtnet.isAvailable()
                        && informationabtnet.isConnected())
                    connectionavailable = true;
                Log.i("ConnectionAvailable", "" + connectionavailable);
            } catch (Exception e) {
                System.out.println("Inside utils catch clause , exception is"
                        + e.toString());
                e.printStackTrace();
            }
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    public boolean test() {
        EditText editText = (EditText) findViewById(R.id.login);
        if (editText.length()>0) {
            return true;
        } else return false;
    }

    /**
     * Metoda Autentifikuje uzivatela  v priapde, ze autentifkacia je prejde vytvory novu aktivitu
     * @param view
     */
    public void authentificaion(View view){
        if(!(isInternetAvailable(getApplicationContext()))){
            Toast.makeText(LoginActivity.this, "Internet is not available", Toast.LENGTH_SHORT).show();
        }
        else {
            //View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
            EditText eLogin = (EditText) findViewById(R.id.login);
            String login = eLogin.getText().toString();
            EditText ePassword = (EditText) findViewById(R.id.passwprd);
            String password = ePassword.getText().toString();
            final Button btnLogin = (Button) findViewById(R.id.btn_login);


            if (validate(view, login, password)) {
                JsonObject json = new JsonObject();
                json.addProperty("email", login);
                json.addProperty("password", password);
                //btnLogin.setEnabled(false);
                //add progress Dialog
                progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setMessage("Authenticating...");
                progressDialog.show();

                Ion.with(getApplicationContext())
                        .load(getUrl()+"/api/login")
                        .setJsonObjectBody(json)
                        .asJsonObject()
                        .setCallback(new FutureCallback<JsonObject>() {
                            @Override
                            public void onCompleted(Exception e, JsonObject result) {
                                if (result != null) {
                                    toMenuActivity = new Intent(LoginActivity.this, ShowDrops.class);


                                    String id = result.get("id").toString();
                                    String email = result.get("email").getAsString();
                                    int confirmed = result.get("confirmed").getAsInt();
                                    toMenuActivity.putExtra("id", id);
                                    toMenuActivity.putExtra("email", email);
                                    String url = getUrl()+"/api/users/" + id + "/sensors";
                                    if (confirmed == 1) {

                                        Ion.with(getApplicationContext())
                                                .load(url)
                                                .asString()
                                                .setCallback(new FutureCallback<String>() {
                                                    @Override
                                                    public void onCompleted(Exception e, String result) {
                                                        sensors = result;
                                                        toMenuActivity.putExtra("sensors", sensors);
                                                        toMenuActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                        startActivity(toMenuActivity);
                                                        finish();
                                                    }
                                                });

                                    } else {
                                        Toast.makeText(LoginActivity.this, "Confirm you email", Toast.LENGTH_SHORT).show();
                                    }

                                } else {
                                    Toast.makeText(LoginActivity.this, "Incorect username or password", Toast.LENGTH_SHORT).show();


                                }
                                progressDialog.dismiss();
                            }
                        });
            }
        }
    }


    public boolean validate(View view,String login, String email) {
        boolean valid = true;
        EditText eLogin =(EditText) findViewById(R.id.login);
        EditText ePassword =(EditText) findViewById(R.id.passwprd);

        if (email.isEmpty() ) {
            eLogin.setError("Email is not valid");
            valid = false;
            return valid;
        } else {
            eLogin.setError(null);
        }

        if (ePassword.length() < 4 || ePassword.length() > 25) {
            ePassword.setError("Password is not valid");
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





}
