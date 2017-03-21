package com.example.tomas.mydrops;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.Iterator;
import java.util.List;

public class SetDropSecond extends AppCompatActivity {
    String sensors;
    String email;
    Spinner spinner;
    boolean isSuccessResponse=false;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        spinner = (Spinner) findViewById(R.id.spinnerSendingTime);
        //spinner.setSelection(7);
        setContentView(R.layout.activity_set_drop_second);
        addItems();
        final String sensor_id = getIntent().getStringExtra("sensor_id");
        email = getIntent().getStringExtra("email");
        sensors = getIntent().getStringExtra("sensors");
        //if sensor_id.equals("");
        Toast.makeText(SetDropSecond.this, sensor_id, Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onDestroy() {
        super.onStart();
        //connectToOldWifi();
        /*Intent toMenuActivity = new Intent(SetDropSecond.this, ShowDrops.class);
        toMenuActivity.putExtra("sensors", sensors);
        toMenuActivity.putExtra("email", email);
        startActivity(toMenuActivity);*/
    }


    public boolean validate() {
        boolean valid = true;
        EditText eSSID =(EditText) findViewById(R.id.e_SSID);
        EditText ePassword =(EditText) findViewById(R.id.editTextPassword);
        EditText eDevicePassword =(EditText) findViewById(R.id.editTextDevicePassword);


        if (eSSID.length() < 2 || ePassword.length() > 14) {
            valid = false;
            eSSID.setError("SSID is not valid");
            return valid;
        } else {
            ePassword.setError(null);
        }

        if (ePassword.length() < 6 || ePassword.length() > 25) {
            valid = false;
            ePassword.setError("Password is not valid");
            return valid;
        } else {
            ePassword.setError(null);
        }
        if (eDevicePassword.length() < 6 || ePassword.length() > 25) {
            valid = false;
            eDevicePassword.setError("Password for device is not valid");
            return valid;
        } else {
            eDevicePassword.setError(null);
        }

        return valid;
    }

    public void toSetDropThird(View view){
        if(validate()&&isSuccessResponse) {
            connectToOldWifi();
            Intent toSetDropThird = new Intent(SetDropSecond.this, SetDropThird.class);
            toSetDropThird.putExtra("sensors", sensors);
            toSetDropThird.putExtra("email", email);
            //toSetDropThird.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(toSetDropThird);
        }
    }






    public void connectToOldWifi(){
        WifiManager wfMgr = (WifiManager) getSystemService(Context.WIFI_SERVICE);


        wfMgr.disconnect();     //disconect from current network
        int networkId = wfMgr.addNetwork(SetDropFirst.getOldConfiguration());
        if (networkId != -1) {
            // success, can call wfMgr.enableNetwork(networkId, true) to connect
        }
        if(wfMgr.enableNetwork(networkId,true)){
            //Toast.makeText(contex,"Aktivnova stara wifi",Toast.LENGTH_LONG).show();
            Log.i("Aktivovana old wifi",Integer.toString(networkId));
        }
    }
    public void connectToEsp(View view){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://192.168.4.1/interval/2";

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String r = response;
                        Log.d("Response:",response);
                        // Display the first 500 characters of the response string.
                        //mTextView.setText("Response is: "+ response.substring(0,500));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  mTextView.setText("That didn't work!");
            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }


    public void sendJsonToESP(View view){
        JsonObject json = new JsonObject();

        json.addProperty("SN", "$2y$10$2SdhktPrmZTRpJC0EzCpJ./PnXoX.K3ZOf8sHPOhUIG8fi.23S7TK");
        json.addProperty("ssidWifi", getSSID());
        json.addProperty("passwordWifi", getPass());
        json.addProperty("passwordAP", getDevicePassword());
        //json.addProperty("interval", getInterval());

        Ion.with(getApplicationContext())
                .load("http://192.168.4.1/")
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        isSuccessResponse = true;
                    }
                });
    }


    public void addItems() {
        spinner = (Spinner) findViewById(R.id.spinnerSendingTime);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinnerSendingTime, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(6);
    }

    private int getResult(String s){
        int result=0;
        switch (s){
            case "1 hour":
                result=1;
                break;
            case "3 hours":
                result=2;
                break;
            case "6 hours":
                result=3;
                break;
            case "12 hours":
                result=4;
                break;
            case "1 day":
                result=5;
                break;
            case "3 days":
                result=6;
                break;
            case "1 week (recommended)":
                result=7;
                break;
            case "2 weeks":
                result=8;
                break;

            default:
                throw new IllegalArgumentException("Invalid argument of spinner:  "+s);
        }
        return result;
    }

    public String getSSID(){
        EditText eSSID =(EditText) findViewById(R.id.e_SSID);
        return eSSID.getText().toString();
    }

    public String getPass(){
        EditText ePassword =(EditText) findViewById(R.id.editTextPassword);
        return ePassword.getText().toString();
    }

    public String getDevicePassword(){
        EditText eDevicePassword =(EditText) findViewById(R.id.editTextDevicePassword);
        return eDevicePassword.getText().toString();
    }

    public String getInterval(){
        spinner = (Spinner) findViewById(R.id.spinnerSendingTime);
        String pom = String.valueOf(spinner.getSelectedItem());
        int i = getResult(pom);
        return String.valueOf(i);
    }


}
