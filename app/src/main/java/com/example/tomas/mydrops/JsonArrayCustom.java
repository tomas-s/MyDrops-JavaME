package com.example.tomas.mydrops;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tomas on 3/5/17.
 */




public class JsonArrayCustom {
    String sensrosString;
    JSONArray sensorArray;

    public ArrayList<JSONObject> getArrayList() {
        return arrayList;
    }

    ArrayList<JSONObject> arrayList;


    public JsonArrayCustom(){}

    public JsonArrayCustom(String sensros) throws JSONException {
        this.sensrosString = sensros;
        try {
            sensorArray = new JSONArray(sensros);
        } catch (JSONException e) {
            e.getMessage();
        }
        arrayList= new ArrayList<JSONObject>();

        for (int i = 0; i < sensorArray.length(); i++) {
            //JSONObject jsonobject = new JSONObject();
             sensorArray.getJSONObject(i);
            if(sensorArray.getJSONObject(i).getString("state")!=null){
                if(Integer.parseInt(sensorArray.getJSONObject(i).getString("battery"))!=0){
                     arrayList.add(sensorArray.getJSONObject(i));
                }
                }
            }

    }


    public int getSensorCount(){
        return  arrayList.size();
    }

    //TODO:Ostretit ak sa poslu nevalidne data bez state a battery
    public String[] sensorParse() throws JSONException {
        String[] pole = new String[getSensorCount()];
        
        String state;
        int battery;

            for (int i = 0; i < sensorArray.length(); i++) {
                JSONObject jsonobject = new JSONObject();


                    jsonobject = sensorArray.getJSONObject(i);


                     state = jsonobject.getString("state");
                     battery = Integer.parseInt(jsonobject.getString("battery"));


                    if (state.equals("0")){
                        if (battery<20){
                            pole[i]= "sensor_0";
                        }if (battery>20&&battery>40){
                            pole[i]= "sensor_1";
                            }
                        if (battery>40&&battery>60){
                            pole[i]= "sensor_2";
                        }
                        if (battery>60&&battery>80){
                            pole[i]= "sensor_3";
                        }
                        if (battery>80) {
                            pole[i] = "sensor_4";
                        }
                    }

                    if (state.equals("1")){
                        if (battery<20){
                            pole[i]= "sensor_10";
                        }if (battery>20&&battery>40){
                            pole[i]= "sensor_11";
                        }
                        if (battery>40&&battery>60){
                            pole[i]= "sensor_12";
                        }
                        if (battery>60&&battery>80){
                            pole[i]= "sensor_13";
                        }
                        if (battery>80){
                            pole[i]= "sensor_14";
                        }
                    }


    }
        return pole;
}


    public String getSensorID(int i) throws JSONException {
        JSONObject jsonObject = sensorArray.getJSONObject(i);
        return jsonObject.getString("sensor_id");
    }


    public Integer[] getBattery() throws JSONException {
            List<Integer> list = new ArrayList<Integer>();

            for( int i = 0 ; i < sensorArray.length() ; i++ ) {
                JSONObject jsonobject = null;
                jsonobject = sensorArray.getJSONObject(i);
                list.add( Integer.parseInt(jsonobject.getString("battery")) );
            }

            // convert it to array
            Integer [] array = list.toArray( new Integer[ list.size() ] );
        return array;


/*
//original
            Integer[] battery;

        for (int i = 0; i < sensorArray.length(); i++) {
            JSONObject jsonobject = null;
             jsonobject = sensorArray.getJSONObject(i);
                 battery[i] = Integer.parseInt(jsonobject.getString("battery"));
        }
        return battery;*/
    }

    public Integer[] getState() throws JSONException {
        List<Integer> list = new ArrayList<Integer>();

        for( int i = 0 ; i < sensorArray.length() ; i++ ) {
            JSONObject jsonobject = null;
            jsonobject = sensorArray.getJSONObject(i);
            list.add( Integer.parseInt(jsonobject.getString("state")) );
        }

        // convert it to array
        Integer [] array = list.toArray( new Integer[ list.size() ] );
        return array;
    }



}
