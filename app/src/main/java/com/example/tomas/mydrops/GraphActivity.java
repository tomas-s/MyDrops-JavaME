package com.example.tomas.mydrops;


import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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

    //TODO: skulturnit a poriesit nastavenie mena
    Integer[] state;
    Integer[] battery;
    String sensor_id;
    String email;
    String sensors;
    String id;
    int x;

    @Override
    public void postponeEnterTransition() {
        super.postponeEnterTransition();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        sensor_id = getIntent().getStringExtra("sensor_id");
        email = getIntent().getStringExtra("email");
        sensors = getIntent().getStringExtra("sensors");
        final String tempBattery = getIntent().getStringExtra("tempBattery");
        final String tempState = getIntent().getStringExtra("tempState");
        id = getIntent().getStringExtra("id");
        getData(sensor_id);
        TextView dropState = (TextView) findViewById(R.id.textViewState);
        dropState.setText("Current state: " + tempState);
        TextView batteryState = (TextView) findViewById(R.id.textViewBatteryState);
        batteryState.setText("Current battery state: " + tempBattery + "%");
        TextView textViewEsp = (TextView) findViewById(R.id.textViewESP);
        String espName = "ESP " + sensor_id.substring(sensor_id.length() - 6);
        textViewEsp.setText(espName);
    }


    public void toNextActivity(View view) {
        Intent toSetDropFirst = new Intent(GraphActivity.this, ConfigDropFirst.class);
        toSetDropFirst.putExtra("sensors", sensors);
        toSetDropFirst.putExtra("email", email);
        toSetDropFirst.putExtra("new", "false");
        toSetDropFirst.putExtra("sensor_id", sensor_id);
        toSetDropFirst.putExtra("id", id);
        startActivity(toSetDropFirst);

    }

    /**
     * Vykresli graf na zaklade parametrov
     * @param pole - informacie o nameranych hodnotach
     * @param lg
     * @param range
     */
    private void drawGraph(Integer[] pole, LineGraph lg, int range) {
        Line l = new Line();
        l.setColor(Color.BLACK);
        LinePoint p = new LinePoint();
        if (pole.length < 2) {
            addPoint(l, p, 0, pole[0]);
            addPoint(l, p, 1, pole[0]);
        } else {
            x = 0;
            for (int i = pole.length - 1; i >= 0; i--) {
                addPoint(l, p, x, pole[i]);
                x++;
            }
        }


        LineGraph li = lg;
        li.addLine(l);
        li.setUsingDips(true);
        li.setRangeY(0, range);
        li.setLineToFill(0);
        li.setBackgroundColor(Color.WHITE);

    }

    /**
     * Metoda prida novy bod na grafe
     * @param l
     * @param p
     * @param x - xova suradnica
     * @param y - Yonova suradnica
     */
    private void addPoint(Line l, LinePoint p, int x, int y) {
        p = new LinePoint();
        p.setX(x);
        p.setY(y);
        l.addPoint(p);

    }

    /**
     * Metoda zisti data zo serveru v priapde ze telefon je pripojeny do WiFi siete alebo na mobilnych datach
     * @param deviceID
     */
    public void getData(String deviceID) {
        JsonObject json = new JsonObject();
        json.addProperty("DeviceID", deviceID);
        if (!(LoginActivity.isInternetAvailable(getApplicationContext()))) {
            Toast.makeText(GraphActivity.this, "Internet is not available", Toast.LENGTH_SHORT).show();
        } else{
            Ion.with(getApplicationContext())
                    .load(LoginActivity.getUrl() + "/api/lastTen")
                    .setJsonObjectBody(json)
                    .asString()
                    .setCallback(new FutureCallback<String>() {
                        @Override
                        public void onCompleted(Exception e, String result) {
                            JsonArrayCustom jsonArrayCustom = null;
                            try {
                                jsonArrayCustom = new JsonArrayCustom(result);
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                                Toast.makeText(GraphActivity.this, "Chyba pri vytvarani Json array custom", Toast.LENGTH_SHORT).show();
                            }
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
                            drawGraph(state, (LineGraph) findViewById(R.id.state), 2);
                            drawGraph(battery, (LineGraph) findViewById(R.id.battery), 100);


                        }

                    });
    }
}



}
