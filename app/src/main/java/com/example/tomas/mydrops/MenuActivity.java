package com.example.tomas.mydrops;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.echo.holographlibrary.Line;
import com.echo.holographlibrary.LineGraph;
import com.echo.holographlibrary.LinePoint;

public class MenuActivity extends AppCompatActivity {

    //// TODO: 1/31/17 convert activity to fragment https://github.com/OmniDebt/OmniDebt-Android/wiki/Convert-Activity-to-Fragment

    //showGraph
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Intent intent = getIntent();
        String value = intent.getStringExtra("key");

        Line l = new Line();
        LinePoint p = new LinePoint();
        p.setX(0);
        p.setY(5);
        l.addPoint(p);

        p = new LinePoint();
        p.setX(8);
        p.setY(8);
        l.addPoint(p);
        p = new LinePoint();
        p.setX(10);
        p.setY(4);
        l.addPoint(p);

        l.setColor(Color.parseColor("#FFBB33"));

        LineGraph li = (LineGraph)findViewById(R.id.graph);
        li.addLine(l);
        li.setRangeY(0, 10);
        li.setLineToFill(0);
    }


    public void connectToESP(View view){
        Intent intent = new Intent(this,connectToESP.class);
        WifiManager wfMgr = (WifiManager) getSystemService(Context.WIFI_SERVICE);

        if(!wfMgr.isWifiEnabled())
        {
            if(wfMgr.setWifiEnabled(true)) {
                Toast.makeText(MenuActivity.this, "Changing wifi to ESP device" ,Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        }
        else
            startActivity(intent);

    }

    public void showGraph(View view){
        Intent intent = new Intent(this,GraphActivity.class);
        startActivity(intent);

    }


}
