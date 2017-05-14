package com.example.tomas.mydrops;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.StringTokenizer;


public class ShowDrops extends AppCompatActivity {
    String sensors;
    String s;
    String [] pole;
    ArrayList<JSONObject> finalList;
    String userId;
    GridView gridview;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_drops);


        final String email = getIntent().getStringExtra("email");
        sensors = getIntent().getStringExtra("sensors");
        userId = getIntent().getStringExtra("id");
        JsonArrayCustom jsonArrayCustom = null;
        try {
            jsonArrayCustom = new JsonArrayCustom(sensors);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(ShowDrops.this,"Chyba pri vytvarani JsonArray",Toast.LENGTH_SHORT).show();
        }
        try {
            pole = jsonArrayCustom.sensorParse();   // !!!!!!!!!!!!tu je chyba vrati mi nespravne v resp pole s malym poctom prvkov
            finalList = jsonArrayCustom.getArrayList();

        } catch (JSONException e) {
            Toast.makeText(ShowDrops.this, "Chyba pri parsovani", Toast.LENGTH_SHORT).show();
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floatingActionButton4);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent toSetDropFirst = new Intent(ShowDrops.this, SetDropFirst.class);
                toSetDropFirst.putExtra("sensors", sensors);
                toSetDropFirst.putExtra("email", email);
                toSetDropFirst.putExtra("new", "true");
                toSetDropFirst.putExtra("sensor_id", "-1");
                toSetDropFirst.putExtra("id",userId);
                startActivity(toSetDropFirst);

            }
        });

        //GridView

        gridview = (GridView) findViewById(R.id.gridview);
        TextView emptyView = (TextView)findViewById(R.id.textViewEmpty);
        emptyView.setText("You don not have created any device");
        gridview.setEmptyView(emptyView);
        gridview.setAdapter(new ImageAdapter(this,pole,finalList));       //tu pridat pole s nazvom zariadenia , batery statusom, battery icony a stavom

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            String tempBattery;
            String tempState;
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                String sensorID="";
                try {
                    JsonArrayCustom json = new JsonArrayCustom(sensors);
                    sensorID = json.getSensorID(position);
                    tempBattery = json.getBattery(position);
                    tempState = json.getState(position);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ShowDrops.this, "Chyba pri ziskavani sensorID", Toast.LENGTH_SHORT).show();
                }

                if(!tempState.equals("0")) {
                    Intent toGraphActivity = new Intent(ShowDrops.this, GraphActivity.class);
                    toGraphActivity.putExtra("sensor_id", sensorID);
                    toGraphActivity.putExtra("sensors", sensors);
                    toGraphActivity.putExtra("new", "false");
                    toGraphActivity.putExtra("email", email);
                    toGraphActivity.putExtra("tempBattery", tempBattery);
                    toGraphActivity.putExtra("tempState", tempState);
                    toGraphActivity.putExtra("id", userId);
                    startActivity(toGraphActivity);
                }
                else {
                    Intent toSetDropFirst = new Intent(ShowDrops.this, ConfigDropFirst.class);
                    toSetDropFirst.putExtra("sensors", sensors);
                    toSetDropFirst.putExtra("email", email);
                    toSetDropFirst.putExtra("new", "false");
                    toSetDropFirst.putExtra("sensor_id", sensorID);
                    toSetDropFirst.putExtra("id", userId);
                    startActivity(toSetDropFirst);
                }
            }
        });
    }

    public int  plus(int x, int y){
        return x+y;
    }


    public void refreshDevice(View view){
        if(!(LoginActivity.isInternetAvailable(getApplicationContext()))){
            Toast.makeText(ShowDrops.this, "Internet is not available", Toast.LENGTH_SHORT).show();
        }
            else {
            String url = LoginActivity.getUrl()+"/api/users/" + userId + "/sensors";
            final Context context = getApplicationContext();
            Ion.with(getApplicationContext())
                    .load(url)
                    .asString()
                    .setCallback(new FutureCallback<String>() {
                        @Override
                        public void onCompleted(Exception e, String result) {
                            if (result==null){
                                Toast.makeText(ShowDrops.this, "Server is temporary offline", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                JsonArrayCustom jsonArrayCustom = null;
                                sensors = result;
                                try {
                                    jsonArrayCustom = new JsonArrayCustom(result);
                                } catch (JSONException b) {
                                    e.printStackTrace();
                                    Toast.makeText(ShowDrops.this, "Can not make a JSON", Toast.LENGTH_SHORT).show();
                                }
                                try {
                                    pole = jsonArrayCustom.sensorParse();
                                    finalList = jsonArrayCustom.getArrayList();

                                } catch (JSONException b) {
                                    Toast.makeText(ShowDrops.this, "JSON parse error", Toast.LENGTH_SHORT).show();
                                }
                                gridview.setAdapter(new ImageAdapter(context, pole, finalList));
                            }
                        }
                    });
        }

    }


    private JSONArray sensorParse(String sensros){
        JSONArray jsonarray=null;
        try {
         jsonarray = new JSONArray(sensros);
        for (int i = 0; i < jsonarray.length(); i++) {
            JSONObject jsonobject = null;

                jsonobject = jsonarray.getJSONObject(i);

            String name = jsonobject.getString("name");
            String url = jsonobject.getString("url");
        }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(ShowDrops.this, "Chyba pri parsovanie Jsonov", Toast.LENGTH_SHORT).show();
        }
        return jsonarray;
    }



    public class ImageAdapter extends BaseAdapter {
        private Context mContext;
        private Integer[] mThumbIds;
        ArrayList<JSONObject> finalList;
        String state;
        String name;
        int battery;



        public ImageAdapter(Context c, String[] pole, ArrayList<JSONObject> finalList) {
            mThumbIds = new Integer[pole.length];
            mContext = c;
            this.finalList = finalList;
            int i;
            for(i=0;i<pole.length;i++){
                int resID = getResources().getIdentifier(pole[i], "drawable", getPackageName());
                this.mThumbIds[i] = resID;
            }
        }

        public void setmThumbIds(String[] pole) {
            int i;
            for(i=0;i<pole.length;i++){
                int resID = getResources().getIdentifier(pole[i], "drawable", getPackageName());
                this.mThumbIds[i] = resID;
            }

        }

        public int getCount() {
            return finalList.size();
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }






        public View getView(int position, View convertView, ViewGroup parent) {
            View gridView;

            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            gridView = new View(mContext);
            if(convertView==null) {
                gridView = inflater.inflate(R.layout.gridview_row, null);
            }
            else
            {
                gridView = (View) convertView;
            }



                 ImageView imageViewStatus = (ImageView) gridView.findViewById(R.id.imageViewStatus);
                 ImageView imageViewBattery=(ImageView) gridView.findViewById(R.id.imageViewBattery);
                 TextView textViewEspName = (TextView) gridView.findViewById(R.id.textViewEspName);
                 TextView textViewBatteryPercentage = (TextView) gridView.findViewById(R.id.textViewBatteryPercentage);
                 TextView textViewStatus = (TextView) gridView.findViewById(R.id.textViewStatus);


                JSONObject jsonobject;
                jsonobject = finalList.get(position);
                try {
                    state = jsonobject.getString("state");
                    battery = Integer.parseInt(jsonobject.getString("battery"));
                    name = jsonobject.getString("name");
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ShowDrops.this,"Chyba pri parsovani array listu v triede show drops - metoda getView",Toast.LENGTH_SHORT).show();
                }

                textViewEspName.setText(name);
                if (state.equals("0")) {
                    imageViewStatus.setImageResource(R.drawable.sm_icon_red);
                    textViewStatus.setText("Contact was lost");
                }
                if (state.equals("1")){
                    imageViewStatus.setImageResource(R.drawable.sm_icon_green);
                    textViewStatus.setText("Water is not detect");
                }
                if (state.equals("2")) {
                    imageViewStatus.setImageResource(R.drawable.sm_icon_red);
                    textViewStatus.setText("Water was detect!");
                }


                if (battery<10){
                    textViewBatteryPercentage.setText("Battery: "+Integer.toString(battery)+"%");
                    imageViewBattery.setImageResource(R.drawable.battery_0_bars);
                }if (battery>=10&&battery<25){
                textViewBatteryPercentage.setText("Battery: "+Integer.toString(battery)+"%");
                imageViewBattery.setImageResource(R.drawable.battery_1_bar);
            }
                if (battery>=25&&battery<50){
                    textViewBatteryPercentage.setText("Battery: "+Integer.toString(battery)+"%");
                    imageViewBattery.setImageResource(R.drawable.battery_2_bars);
                }
                if (battery>=50&&battery<75){
                    textViewBatteryPercentage.setText("Battery: "+Integer.toString(battery)+"%");
                    imageViewBattery.setImageResource(R.drawable.battery_3_bars);
                }
                if (battery>=75) {
                    textViewBatteryPercentage.setText("Battery: "+Integer.toString(battery)+"%");
                    imageViewBattery.setImageResource(R.drawable.battery_4_bars);
                }



                return gridView;


            /*
            else
            {
                gridView = (View) convertView;
            }
*/


        }

    }



}
