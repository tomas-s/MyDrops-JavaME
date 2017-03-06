package com.example.tomas.mydrops;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ShowDrops extends AppCompatActivity {
    String sensors;
    String s;
    String [] pole;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_drops);


        final String sensors = getIntent().getStringExtra("sensors");
        JsonArrayCustom jsonArrayCustom = new JsonArrayCustom(sensors);
        try {
             pole = jsonArrayCustom.sensorParse();
        } catch (JSONException e) {
            Toast.makeText(ShowDrops.this, "Chyba pri parsovani", Toast.LENGTH_SHORT).show();
        }
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floatingActionButton4);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ShowDrops.this,"Vytvaram zariadenie",Toast.LENGTH_SHORT).show();
            }
        });

        //GridView

        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this,pole));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                String sensorID="";
                try {
                     sensorID = new JsonArrayCustom(sensors).getSensorID(position);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ShowDrops.this, "Chyba pri ziskavani sensorID", Toast.LENGTH_SHORT).show();
                }

                Intent toGraphActivity = new Intent(ShowDrops.this, GraphActivity.class);
                toGraphActivity.putExtra("sensor_id", sensorID);
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



        public ImageAdapter(Context c,String[] pole) {
            mThumbIds = new Integer[pole.length];
            mContext = c;
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
            return mThumbIds.length;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        // create a new ImageView for each item referenced by the Adapter
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
        }




        // references to our images



    }



}
