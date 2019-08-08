package dev.malikkurosaki.crispypizza;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import me.ithebk.barchart.BarChart;
import me.ithebk.barchart.BarChartModel;

public class Main2Activity extends AppCompatActivity {

    private BarChart chart;
    private BarChart chart1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        chart = findViewById(R.id.barChart);
        chart1 = findViewById(R.id.barChart1);


        chart.setBarMaxValue(100);
        chart1.setBarMaxValue(100);

        int[] nilai = {10,20,40,60,90,100,10,50,60,50,20,60,30,70,10,20,40,60,90,100,10,50,60,50,20,60,30,70};

        List<BarChartModel> barChartModelList = new ArrayList<>();
        for (int nil : nilai){
            BarChartModel barChartModel = new BarChartModel();
            barChartModel.setBarValue(nil);
            barChartModel.setBarColor(Color.parseColor("#9C27B0"));
            barChartModel.setBarTag("halo"); //You can set your own tag to bar model
            barChartModel.setBarText("kuartal "+ nil);
            barChartModelList.add(barChartModel);
        }


        chart.addBar(barChartModelList);

//Add mutliple bar at once as list;
        //

//populate bar array list and add to barchart as a list.
        //chart.addBar(barChartModelList);
    }
}
