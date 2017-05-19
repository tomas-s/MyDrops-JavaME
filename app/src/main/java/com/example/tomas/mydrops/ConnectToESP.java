package com.example.tomas.mydrops;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Iterator;
import java.util.List;

public class ConnectToESP extends AppCompatActivity {

    private Spinner spinner;
    WifiConfiguration oldConfiguration=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_connect_to_esp);
        addItems();
        spinner = (Spinner) findViewById(R.id.spinnerSendingTime);
        spinner.setSelection(8);
       // connect();
    }

    @Override
    protected void onDestroy() {
        super.onStart();
        connectToOldWifi();
    }



    public void setSendingTime(View view){
        spinner = (Spinner) findViewById(R.id.spinnerSendingTime);
        String pom = String.valueOf(spinner.getSelectedItem());
        int i = getResult(pom);
        Toast.makeText(ConnectToESP.this, "Je "+i+" integer: " ,
                Toast.LENGTH_SHORT).show();
    }











    public void addItems() {
        spinner = (Spinner) findViewById(R.id.spinnerSendingTime);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinnerSendingTime, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
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
        }
        if(wfMgr.enableNetwork(networkId,true)){
            Log.i("Aktivovana new wifi",Integer.toString(networkId));
        }

    }

    public void connectToOldWifi(){
        WifiManager wfMgr = (WifiManager) getSystemService(Context.WIFI_SERVICE);


        wfMgr.disconnect();     //disconect from current network
        int networkId = wfMgr.addNetwork(oldConfiguration);
        if (networkId != -1) {
            // success, can call wfMgr.enableNetwork(networkId, true) to connect
        }
        if(wfMgr.enableNetwork(networkId,true)){
            //Toast.makeText(contex,"Aktivnova stara wifi",Toast.LENGTH_LONG).show();
            Log.i("Aktivovana old wifi",Integer.toString(networkId));
        }


    }


    public void callRest(){
       // final TextView mTextView = (TextView) findViewById(R.id.textStatus);

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://192.168.4.1/interval/2";
        //  String url ="http://www.google.com";

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

}
