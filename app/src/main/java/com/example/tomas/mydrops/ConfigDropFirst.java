package com.example.tomas.mydrops;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.Iterator;
import java.util.List;
//TODO: niekedy stratime sensor ID
public class ConfigDropFirst extends AppCompatActivity {

    NetworkInfo mWifi;
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
    String Ssid;
    WifiManager wifiManager;
    String sensors;
    String email;
    String newDevice;
    String sensor_id;
    String id;
    ProgressDialog progress;
    ConnectivityManager connManager;
    boolean isConnected= false;
    EditText eDevicePassword;
    Thread t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_drop_first);
        email = getIntent().getStringExtra("email");
        sensors = getIntent().getStringExtra("sensors");
        newDevice = getIntent().getStringExtra("new");
        sensor_id= getIntent().getStringExtra("sensor_id");
        id=getIntent().getStringExtra("id");
        eDevicePassword =(EditText) findViewById(R.id.editTextDropPassword);
    }



    @Override
    protected void onDestroy() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isConnected=false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * Vyvola novu aktivitu
     * @param view
     */
    public void toNextActivity(View view){
        if(validate(view)) {
                final EditText editTextDropPassword = (EditText) findViewById(R.id.editTextDropPassword);
                String password = editTextDropPassword.getText().toString();
                String SSID;
                SSID = "ESP " + sensor_id.substring(sensor_id.length() - 6);

                connect(getApplicationContext(), SSID, password);
                try {
                    t.join();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Intent toSetDropSecond = new Intent(ConfigDropFirst.this, ConfigDropSecond.class);
                toSetDropSecond.putExtra("sensor_id", getSensor_id());
                toSetDropSecond.putExtra("sensors", sensors);
                toSetDropSecond.putExtra("email", email);
                toSetDropSecond.putExtra("id", id);
                toSetDropSecond.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                if (isConnected) {
                    startActivity(toSetDropSecond);
                } else {
                    eDevicePassword.setError("Password is not valid");
                }
        }
    }

    /**
     * Metoda overi ci su vstupne textove polia vyplnene podla pozadaovanych kriterii
     * @return false  - ak su validne , true ak nevalidne
     */
    public boolean validate(View view) {
        boolean valid = true;
        EditText ePassword =(EditText) findViewById(R.id.editTextDropPassword);


        if (ePassword.length() > 25) {
            ePassword.setError("The password is too long.");
            valid = false;
        }

        if (ePassword.length() < 4) {
            ePassword.setError("The password is too short.");
            valid = false;
        }
        return valid;
    }
    /**
     * Meoda sluziaca na pripojenie zariadenia na WiFi
     */
    public void connect(Context context,String ssid,String password)
    {


        WifiConfiguration wfc = new WifiConfiguration();
      //  WifiConfiguration wfc = new WifiConfiguration();

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
            if(wfMgr.enableNetwork(networkId,true)){
                //Toast.makeText(context,"Aktivnova nova wifi",Toast.LENGTH_LONG).show();
                Log.i("Aktivovana new wifi",Integer.toString(networkId));
            }
        }


        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        Ssid = wifiManager.getConnectionInfo().getSSID().substring(1,4);
        connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);


        t = new Thread(new Runnable() {

            @Override
            public void run() {

                try {
                    int i=0;
                    boolean wasBreak = false;
                    while(!(mWifi.isConnected() && Ssid.equals("ESP"))){
                        i++;
                        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                        Ssid = wifiManager.getConnectionInfo().getSSID().substring(1,4);
                        connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                        mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                        Thread.sleep(1000);
                        if(i>17){
                            wasBreak = true;
                            break;
                        }

                    }

                    if(!wasBreak) {
                        isConnected = true;
                    }
                } catch (Exception e) {
                   // Toast.makeText(getApplicationContext(),"Error in Thread t",Toast.LENGTH_LONG).show();
                }
            }
        });
        t.start();


    }

    /**
     * Pripoj do stare WiFi siete
     */
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

    /**
     * Overi ci je zariadenie pripojen
     * @param context
     * @return
     */
    public static boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connectivityManager != null) {
            networkInfo = connectivityManager.getActiveNetworkInfo();
        }

        return networkInfo != null && networkInfo.getState() == NetworkInfo.State.CONNECTED;
    }




}
