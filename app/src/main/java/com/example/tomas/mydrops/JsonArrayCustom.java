package com.example.tomas.mydrops;

import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by tomas on 3/5/17.
 */




public class JsonArrayCustom {
    String sensrosString;
    JSONArray sensorArray;


    public JsonArrayCustom(String sensros) {
        this.sensrosString = sensros;
        try {
            sensorArray = new JSONArray(sensros);
        } catch (JSONException e) {
            e.getMessage();
        }

    }


    public int getSensorCount(){
        return  sensorArray.length();
    }

    //TODO:Ostretit ak sa poslu nevalidne data bez state a battery
    public String[] sensorParse() throws JSONException {
        String[] pole = new String[getSensorCount()];
        
        String state;
        int battery;

            for (int i = 0; i < sensorArray.length(); i++) {
                JSONObject jsonobject = null;


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



}
