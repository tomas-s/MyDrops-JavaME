package com.example.tomas.mydrops;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class SetDropThird extends AppCompatActivity {
    String sensors;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_drop_third);
        email = getIntent().getStringExtra("email");
        sensors = getIntent().getStringExtra("sensors");
    }
    @Override
    protected void onDestroy() {
        super.onStart();/*
        Intent toMenuActivity = new Intent(SetDropThird.this, ShowDrops.class);
        toMenuActivity.putExtra("sensors", sensors);
        toMenuActivity.putExtra("email", email);
        startActivity(toMenuActivity);*/
    }




    public void sendData(View view){
        Intent toMenuActivity = new Intent(SetDropThird.this, ShowDrops.class);
        toMenuActivity.putExtra("sensors", sensors);
        toMenuActivity.putExtra("email", email);
        startActivity(toMenuActivity);
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


}
