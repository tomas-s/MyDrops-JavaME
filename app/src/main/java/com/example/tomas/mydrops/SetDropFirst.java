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
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.Iterator;
import java.util.List;

public class SetDropFirst extends AppCompatActivity {
    private  static WifiConfiguration oldConfiguration=null;
    public static WifiConfiguration getOldConfiguration() {
        return oldConfiguration;
    }

    public String getSensor_id() {
        return sensor_id;
    }

    public void setSensor_id(String sensor_id) {
        this.sensor_id = sensor_id;
    }

    String sensor_id;

    String sensors;
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_drop_first);
         email = getIntent().getStringExtra("email");
         sensors = getIntent().getStringExtra("sensors");
/*
        //pridanie do SetDropSecond
        JsonObject json = new JsonObject();
        json.addProperty("email", email);

        //Generovanie senzor ID funguje, zakomentoval som aby sa nevytvorilo milion senzorov
        Ion.with(getApplicationContext())
                .load("http://85.93.125.205:8126/api/generateSN")
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        setSensor_id(result.get("sensor_id").getAsString());
                    }
                });
                */
        connect(getApplicationContext());
    }

    @Override
    protected void onDestroy() {
        super.onStart();
        connectToOldWifi();
    }

    public void toNextActivity(View view){
        sensor_id = "$2y$10$2SdhktPrmZTRpJC0EzCpJ./PnXoX.K3ZOf8sHPOhUIG8fi.23S7TK";
        Intent toSetDropSecond = new Intent(SetDropFirst.this, SetDropSecond.class);
        toSetDropSecond.putExtra("sensor_id", getSensor_id());
        toSetDropSecond.putExtra("sensors", sensors);
        toSetDropSecond.putExtra("email", email);
        toSetDropSecond.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(toSetDropSecond);
    }





    //TODO: zistit ci neni aktivna wifi ESP ak nie je az potom volat novu metodu
    //TODO:
    public void connect(Context context)
    {



        String ssid= "ESP";
        String password="123123123";
        WifiConfiguration wfc = new WifiConfiguration();

        wfc.SSID = "\"".concat(ssid).concat("\"");
        wfc.status = WifiConfiguration.Status.DISABLED;
        wfc.priority = 40;


        wfc.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
        wfc.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
        wfc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
        wfc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
        wfc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
        wfc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
        wfc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
        wfc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
        wfc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);

        wfc.preSharedKey = "\"".concat(password).concat("\"");

        WifiManager wfMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        int networkId = wfMgr.addNetwork(wfc);

        /*if(!wfMgr.isWifiEnabled())
        {
            wfMgr.setWifiEnabled(true);
        }
*/


        WifiInfo oldWifi = wfMgr.getConnectionInfo();   //pouzit get configured info

        int intOldConfiguration=-1;
        int index=0;
        List<WifiConfiguration> configuredNetworks = wfMgr.getConfiguredNetworks();
        for(Iterator<WifiConfiguration> i = configuredNetworks.iterator(); i.hasNext(); ) {
            if(i.next().networkId ==oldWifi.getNetworkId()){
                oldConfiguration = configuredNetworks.get(index);
                break;
            }
            index++;
        }

        wfMgr.disconnect();     //disconect from current network

        //int networkId = wfMgr.addNetwork(wfc);
        if (networkId != -1) {
            // success, can call wfMgr.enab
            // leNetwork(networkId, true) to connect
        }
        if(wfMgr.enableNetwork(networkId,true)){
            Toast.makeText(context,"Aktivnova nova wifi",Toast.LENGTH_LONG).show();
            Log.i("Aktivovana new wifi",Integer.toString(networkId));
        }

/*
//TODO: dorobit nespravny login heslo
        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiInfo w = wifiManager.getConnectionInfo();
        String networkSSID = "ESP8622";
        String networkPass = "123123123";

        WifiConfiguration conf = new WifiConfiguration();
        conf.SSID = "\"" + networkSSID + "\"";   // Please note the quotes. String should contain ssid in quotes
        conf.wepKeys[0] = "\"" + networkPass + "\"";
        conf.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
        conf.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
        conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
        conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
        conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
        conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
        conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
        conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
        conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
        WifiInfo wifiInfi = wifiManager.getConnectionInfo();
         networkId = wifiManager.addNetwork(conf);  // tu som zmazal deklaracia int
        if (networkId >= 0) {
            // Try to disable the current network and start a new one.
        wifiManager.enableNetwork(networkId, true);


        }

*/

        /*
        Toast.makeText(MainActivity.this, "Je "+i+" integer: " ,
                Toast.LENGTH_SHORT).show();
        wifiManager.disconnect();
        wifiManager.enableNetwork(i,true);
        wifiManager.reconnect();
        wifiManager.saveConfiguration();*/
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
