package com.example.tomas.mydrops;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.echo.holographlibrary.Line;
import com.echo.holographlibrary.LineGraph;
import com.echo.holographlibrary.LinePoint;

public class GraphActivity extends AppCompatActivity {

    //bude potrebne restom sa opytat na pole
    int[] pole = {1,0,0,1,2,3,4,5,};

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

        drawGraph();
    }

    private void drawGraph(){
        Line l = new Line();
        LinePoint p = new LinePoint();

        for (int i = 0; i < pole.length; i++) {
            addPoint(l,p,i,pole[i]);
        }

        /*addPoint(l,p,0,5);
        addPoint(l,p,8,8);
        addPoint(l,p,10,4);*/


        l.setColor(Color.parseColor("#FFBB33"));

        LineGraph li = (LineGraph)findViewById(R.id.graph);
        li.addLine(l);
        li.setRangeY(0, 10);
        li.setLineToFill(0);
    }
    private void addPoint(Line l,LinePoint p,int x,int y){
        p = new LinePoint();
        p.setX(x);
        p.setY(y);
        l.addPoint(p);

    }
}
