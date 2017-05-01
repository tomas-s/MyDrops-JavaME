package com.example.tomas.mydrops;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.StringTokenizer;


//TODO: dorobit refresh zoznamu
public class ShowDrops extends AppCompatActivity {
    String sensors;
    String s;
    String [] pole;
    ArrayList<JSONObject> finalList;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_drops);


        final String email = getIntent().getStringExtra("email");
        final String sensors = getIntent().getStringExtra("sensors");
        JsonArrayCustom jsonArrayCustom = null;
        try {
            jsonArrayCustom = new JsonArrayCustom(sensors);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(ShowDrops.this,"Chyba pri vytvarani JsonArray",Toast.LENGTH_SHORT).show();
        }
        try {
            pole = jsonArrayCustom.sensorParse();
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
                startActivity(toSetDropFirst);

            }
        });

        //GridView

        GridView gridview = (GridView) findViewById(R.id.gridview);
        TextView emptyView = (TextView)findViewById(R.id.textViewEmpty);
        emptyView.setText("There is no item");
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

                Intent toGraphActivity = new Intent(ShowDrops.this, GraphActivity.class);
                toGraphActivity.putExtra("sensor_id", sensorID);
                toGraphActivity.putExtra("sensors", sensors);
                toGraphActivity.putExtra("new", "false");
                toGraphActivity.putExtra("email", email);
                toGraphActivity.putExtra("tempBattery", tempBattery);
                toGraphActivity.putExtra("tempState", tempState);
                startActivity(toGraphActivity);

                //Toast.makeText(ShowDrops.this,"" + sensoorID,Toast.LENGTH_SHORT).show();
            }
        });
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

        // create a new ImageView for each item referenced by the Adapter
        /*
        public View getView(int position, View convertView, ViewGroup parent) {
           // setmThumbIds(pole);
            ImageView imageView;
            if (convertView == null) {
                // if it's not recycled, initialize some attributes
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(285, 285));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(8, 8, 8, 8);
            } else {
                imageView = (ImageView) convertView;
            }

            imageView.setImageResource(mThumbIds[position]);
            return imageView;
        }*/





        public View getView(int position, View convertView, ViewGroup parent) {
            View gridView;

            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if(convertView==null)
            {
                gridView = new View(mContext);
                gridView = inflater.inflate( R.layout.gridview_row , null);


                 ImageView imageViewStatus = (ImageView) gridView.findViewById(R.id.imageViewStatus);
                 ImageView imageViewBattery=(ImageView) gridView.findViewById(R.id.imageViewBattery);
                 TextView textViewEspName = (TextView) gridView.findViewById(R.id.textViewEspName);
                 TextView textViewBatteryPercentage = (TextView) gridView.findViewById(R.id.textViewBatteryPercentage);

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
                if (state.equals("0")){
                    imageViewStatus.setImageResource(R.drawable.sm_icon_red);
                }
                else {
                    imageViewStatus.setImageResource(R.drawable.sm_icon_green);
                }

                if (battery<20){
                    textViewBatteryPercentage.setText("Battery: "+Integer.toString(battery)+"%");
                    imageViewBattery.setImageResource(R.drawable.battery_0_bars);
                }if (battery>20&&battery<40){
                textViewBatteryPercentage.setText("Battery: "+Integer.toString(battery)+"%");
                imageViewBattery.setImageResource(R.drawable.battery_1_bar);
            }
                if (battery>40&&battery<60){
                    textViewBatteryPercentage.setText("Battery: "+Integer.toString(battery)+"%");
                    imageViewBattery.setImageResource(R.drawable.battery_2_bars);
                }
                if (battery>60&&battery<80){
                    textViewBatteryPercentage.setText("Battery: "+Integer.toString(battery)+"%");
                    imageViewBattery.setImageResource(R.drawable.battery_3_bars);
                }
                if (battery>80) {
                    textViewBatteryPercentage.setText("Battery: "+Integer.toString(battery)+"%");
                    imageViewBattery.setImageResource(R.drawable.battery_4_bars);
                }





            }
            else
            {
                gridView = (View) convertView;
            }


            return gridView;
        }

    }



}
