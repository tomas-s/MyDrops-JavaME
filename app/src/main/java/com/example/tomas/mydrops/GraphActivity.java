package com.example.tomas.mydrops;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.echo.holographlibrary.Line;
import com.echo.holographlibrary.LineGraph;
import com.echo.holographlibrary.LinePoint;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GraphActivity extends AppCompatActivity {

    //bude potrebne restom sa opytat na pole
    Integer[] pole = {1,55};
    Integer[] state;
    Integer[] battery;

    @Override
    public void postponeEnterTransition() {
        super.postponeEnterTransition();
    }

    //TODO: Prezentacia parsovanie a pridavania grafov
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        String sensor_id = getIntent().getStringExtra("sensor_id");
        Toast.makeText(GraphActivity.this, sensor_id, Toast.LENGTH_SHORT).show();
        getData(sensor_id);

    }

    private void drawGraph(Integer[] pole,LineGraph lg,int range){
        Line l = new Line();
        LinePoint p = new LinePoint();
        if(pole.length<2){
            addPoint(l, p, 0, pole[0]);
            addPoint(l, p, 1, pole[0]);
        }
        else {
            for (int i = 0; i < pole.length; i++) {
                addPoint(l, p, i, pole[i]);
            }
        }




        l.setColor(Color.parseColor("#FFBB33"));

        LineGraph li = lg;
        li.addLine(l);
        li.setRangeY(0, range);
        li.setLineToFill(0);
    }
    private void addPoint(Line l,LinePoint p,int x,int y){
        p = new LinePoint();
        p.setX(x);
        p.setY(y);
        l.addPoint(p);

    }

    public void getData(String deviceID){
        JsonObject json = new JsonObject();
        json.addProperty("DeviceID", deviceID);

        Ion.with(getApplicationContext())
                .load("http://85.93.125.205:8126/api/lastTen")
                .setJsonObjectBody(json)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        JsonArrayCustom jsonArrayCustom = new JsonArrayCustom(result);
                        try {
                            battery = jsonArrayCustom.getBattery();

                        } catch (JSONException e1) {
                            e1.printStackTrace();
                            Toast.makeText(GraphActivity.this, "Chyba pri ziskavani stavu baterie", Toast.LENGTH_SHORT).show();
                        }
                        try {
                            state = jsonArrayCustom.getState();
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                            Toast.makeText(GraphActivity.this, "Chyba pri ziskavani detekcie vody", Toast.LENGTH_SHORT).show();
                        }
                        drawGraph(state,(LineGraph)findViewById(R.id.state),2);
                        drawGraph(battery,(LineGraph)findViewById(R.id.battery),100);


                    }

                });





    }
}
