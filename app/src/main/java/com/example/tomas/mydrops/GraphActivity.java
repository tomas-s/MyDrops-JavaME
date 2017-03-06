package com.example.tomas.mydrops;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
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

    //bude potrebne restom sa opytat na pole
    Integer[] pole = {1,55};
    Integer[] state;
    Integer[] battery;

    @Override
    public void postponeEnterTransition() {
        super.postponeEnterTransition();
    }

    //TODO: Prezentacia parsovanie a pridavania grafovpádompádompádom
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        String sensor_id = getIntent().getStringExtra("sensor_id");
        Toast.makeText(GraphActivity.this, sensor_id, Toast.LENGTH_SHORT).show();
        getData(sensor_id);

        Button button = (Button) findViewById(R.id.button6);

        // add button listener
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                // custom dialog
                final Dialog dialog = new Dialog(GraphActivity.this,R.style.FullHeightDialog);
                dialog.setContentView(R.layout.dialog);
                //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                // set the custom dialog components - text, image and button
                //TextView text = (TextView) dialog.findViewById(R.id.text);
                //text.setText("Android custom dialog example!");
                //ImageView image = (ImageView) dialog.findViewById(R.id.image);
                //image.setImageResource(R.drawable.logo);
/*s
                Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
                // if button is clicked, close the custom dialog
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });*/

                dialog.show();
            }
        });



    }

    private void drawGraph(Integer[] pole,LineGraph lg,int range){
        Line l = new Line();
       // if (range==2){
        l.setColor(Color.GREEN);//}
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
        li.setUsingDips(true); // pridal som
        li.setRangeY(0, range);
        li.setLineToFill(0);
        li.setBackgroundColor(Color.GRAY); // pridal som
        //li.setfil
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
/*
    public void showDialog(){
        AlertDialog alertDialog = new AlertDialog.Builder(GraphActivity.this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("Alert message to be shown");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
    */


}
