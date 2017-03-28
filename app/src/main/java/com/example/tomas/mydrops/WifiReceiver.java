package com.example.tomas.mydrops;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.widget.Toast;

/**
 * Created by tomas on 3/27/17.
 */

//TODO: rozbehat reciver
public class WifiReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMan.getActiveNetworkInfo();
        if (netInfo != null && netInfo.getType() == ConnectivityManager.TYPE_WIFI)
            Toast.makeText(context, "Wifi is changed", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(context, "Wifi is not changed", Toast.LENGTH_LONG).show();
    }

        }


