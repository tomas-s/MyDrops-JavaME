package com.example.tomas.mydrops;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class SetDropThird extends AppCompatActivity {
    String sensors;
    String email;
    String sensor_id;
    String id;

    public String getResultRequest() {
        return resultRequest;
    }

    public void setResultRequest(String resultRequest) {
        this.resultRequest = resultRequest;
    }

    String resultRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_drop_third);
        email = getIntent().getStringExtra("email");
        sensors = getIntent().getStringExtra("sensors");
        sensor_id = getIntent().getStringExtra("sensor_id");
        id=getIntent().getStringExtra("id");
    }
    @Override
    protected void onDestroy() {
        super.onStart();
    }




    public void sendData(View view){

        Intent toMenuActivity = new Intent(SetDropThird.this, ShowDrops.class);
        toMenuActivity.putExtra("sensors", sensors);
        toMenuActivity.putExtra("email", email);
        toMenuActivity.putExtra("id", id);
        startActivity(toMenuActivity);
        Toast.makeText(getApplicationContext(),"Now restart your Drop device",Toast.LENGTH_LONG).show();
    }

    public void connectToOldWifi(){
        WifiManager wfMgr = (WifiManager) getSystemService(Context.WIFI_SERVICE);


        wfMgr.disconnect();     //disconect from current network
        int networkId = wfMgr.addNetwork(SetDropFirst.getOldConfiguration());
        if (networkId != -1) {
            // success, can call wfMgr.enableNetwork(networkId, true) to connect
        }
        if(wfMgr.enableNetwork(networkId,true)){
            Log.i("Aktivovana old wifi",Integer.toString(networkId));
        }
    }


}
