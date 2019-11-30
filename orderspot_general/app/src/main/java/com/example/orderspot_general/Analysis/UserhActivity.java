package com.example.orderspot_general.Analysis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.example.orderspot_general.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class UserhActivity extends AppCompatActivity {
    private BarChart barChart;  //sales
    private BarChart barChart2; //count
    Intent it; String sit; Boolean gg = false;

    String a[]={"",""}; String b[]={"",""};String c[]={"",""};String d[]={"",""};
    String e[]={"",""};String f[]={"",""};String g[]={"",""};String h[]={"",""};
    String i[]={"",""};String j[]={"",""};String k[]={"",""};String l[]={"0","0"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userh);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        it = getIntent();
        sit = it.getStringExtra("sidd");
        new JSONTask5().execute();

        Button bh =findViewById(R.id.buttonh);
        bh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Button cb = findViewById(R.id.changebtn);
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(gg == true){
                    h2();
                }else{
                    h1();
                }
            }
        });
    }
    // 소비액
    public void h1(){
        barChart = (BarChart) findViewById(R.id.barChart);
        BarDataSet barDataSet = new BarDataSet(getData(), "소비액(원)");
        barDataSet.setBarBorderWidth(0.9f);
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        BarData barData = new BarData(barDataSet);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        final String[] months = new String[]{"2019 01", "2019 02", "2019 03", "2019 04", "2019 05", "2019 06", "2019 07", "2019 08", "2019 09", "2019 10", "2019 11", "2019 12"};
        IndexAxisValueFormatter formatter = new IndexAxisValueFormatter(months);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(formatter);
        barChart.setData(barData);
        barChart.setFitBars(true);
        barChart.animateXY(5000, 5000);
        barChart.invalidate();
        gg = true;
    }

    private ArrayList getData() {
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, Integer.parseInt(a[0])));
        entries.add(new BarEntry(1, Integer.parseInt(b[0])));
        entries.add(new BarEntry(2, Integer.parseInt(c[0])));
        entries.add(new BarEntry(3, Integer.parseInt(d[0])));
        entries.add(new BarEntry(4, Integer.parseInt(e[0])));
        entries.add(new BarEntry(5, Integer.parseInt(f[0])));
        entries.add(new BarEntry(6, Integer.parseInt(g[0])));
        entries.add(new BarEntry(7, Integer.parseInt(h[0])));
        entries.add(new BarEntry(8, Integer.parseInt(i[0])));
        entries.add(new BarEntry(9, Integer.parseInt(j[0])));
        entries.add(new BarEntry(10, Integer.parseInt(k[0])));
        entries.add(new BarEntry(11, Integer.parseInt(l[0])));

        return entries;
    }

    //count
    public  void h2() {
        barChart2 = (BarChart) findViewById(R.id.barChart);
        BarDataSet barDataSet2 = new BarDataSet(getData2(), "주문횟수");
        barDataSet2.setBarBorderWidth(0.9f);
        barDataSet2.setColors(ColorTemplate.PASTEL_COLORS);
        BarData barData = new BarData(barDataSet2);
        XAxis xAxis2 = barChart2.getXAxis();
        xAxis2.setPosition(XAxis.XAxisPosition.BOTTOM);
        final String[] months = new String[]{"2019 01", "2019 02", "2019 03", "2019 04", "2019 05", "2019 06", "2019 07", "2019 08", "2019 09", "2019 10", "2019 11", "2019 12"};
        IndexAxisValueFormatter formatter = new IndexAxisValueFormatter(months);
        xAxis2.setGranularity(1f);
        xAxis2.setValueFormatter(formatter);
        barChart2.setData(barData);
        barChart2.setFitBars(true);
        //barChart2.animateXY(5000, 5000);    //barChart2.animateY(5000);
        barChart2.animateY(5000);
        barChart2.invalidate();
        gg = false;
    }

    private ArrayList getData2() {
        ArrayList<BarEntry> entries2 = new ArrayList<>();
        entries2.add(new BarEntry(0, Integer.parseInt(a[1])));
        entries2.add(new BarEntry(1, Integer.parseInt(b[1])));
        entries2.add(new BarEntry(2, Integer.parseInt(c[1])));
        entries2.add(new BarEntry(3, Integer.parseInt(d[1])));
        entries2.add(new BarEntry(4, Integer.parseInt(e[1])));
        entries2.add(new BarEntry(5, Integer.parseInt(f[1])));
        entries2.add(new BarEntry(6, Integer.parseInt(g[1])));
        entries2.add(new BarEntry(7, Integer.parseInt(h[1])));
        entries2.add(new BarEntry(8, Integer.parseInt(i[1])));
        entries2.add(new BarEntry(9, Integer.parseInt(j[1])));
        entries2.add(new BarEntry(10, Integer.parseInt(k[1])));
        entries2.add(new BarEntry(11, Integer.parseInt(l[1])));

        return entries2;
    }

    //비동기 처리
    public class JSONTask5 extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                // 서버에 요청값 보내기
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("type", "guser_purchase_search");
                jsonObject.accumulate("guser_ID", sit);
                jsonObject.accumulate("start_day", "2019-10-19");
                jsonObject.accumulate("term", "m");
                Log.e("로그", jsonObject.toString());
                HttpURLConnection con = null;
                BufferedReader reader = null;

                try {
                    URL url = new URL("http://52.78.114.103:3000/orderspot");
                    //URL url = new URL(urls[0]);
                    con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("POST");
                    con.setRequestProperty("Cache-Control", "no-cache");
                    con.setRequestProperty("Content-Type", "application/json");
                    con.setRequestProperty("Accept", "text/html");
                    con.setDoOutput(true);
                    con.setDoInput(true);
                    con.connect();

                    OutputStream outStream = con.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));
                    writer.write(jsonObject.toString());
                    writer.flush();
                    writer.close();

                    InputStream stream = con.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(stream));
                    StringBuffer buffer = new StringBuffer();
                    String line = "";
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line);
                    }
                    return buffer.toString();

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (con != null) {
                        con.disconnect();
                    }
                    try {
                        if (reader != null) {
                            reader.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                //con.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            String TAG = "로그2";
            try {
                JSONArray jsonArray = new JSONArray(result);
                Log.e(TAG, jsonArray.toString());
                for (int ii = 0; ii < jsonArray.length(); ii++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(ii);
                    if(jsonObject.getString("MONTH").equals("2019-01")) {
                            a[0] = jsonObject.getString("SALES");   //sales 값
                            a[1] = jsonObject.getString("COUNT");   //count 값
                    }
                    else if(jsonObject.getString("MONTH").equals("2019-02")){
                            b[0] = jsonObject.getString("SALES");   //SALES 카운트값
                            b[1] = jsonObject.getString("COUNT");   //COUNT 값
                    }
                    else if(jsonObject.getString("MONTH").equals("2019-03")){
                        c[0] = jsonObject.getString("SALES");   //SALES 카운트값
                        c[1] = jsonObject.getString("COUNT");   //COUNT 값
                    }
                    else if(jsonObject.getString("MONTH").equals("2019-04")){
                        d[0] = jsonObject.getString("SALES");   //SALES 카운트값
                        d[1] = jsonObject.getString("COUNT");   //COUNT 값
                    }
                    else if(jsonObject.getString("MONTH").equals("2019-05")){
                        e[0] = jsonObject.getString("SALES");   //SALES 카운트값
                        e[1] = jsonObject.getString("COUNT");   //COUNT 값
                    }
                    else if(jsonObject.getString("MONTH").equals("2019-06")){
                        f[0] = jsonObject.getString("SALES");   //SALES 카운트값
                        f[1] = jsonObject.getString("COUNT");   //COUNT 값
                    }
                    else if(jsonObject.getString("MONTH").equals("2019-07")){
                        g[0] = jsonObject.getString("SALES");   //SALES 카운트값
                        g[1] = jsonObject.getString("COUNT");   //COUNT 값
                    }
                    else if(jsonObject.getString("MONTH").equals("2019-08")){
                        h[0] = jsonObject.getString("SALES");   //SALES 카운트값
                        h[1] = jsonObject.getString("COUNT");   //COUNT 값
                    }
                    else if(jsonObject.getString("MONTH").equals("2019-09")){
                        i[0] = jsonObject.getString("SALES");   //SALES 카운트값
                        i[1] = jsonObject.getString("COUNT");   //COUNT 값
                    }
                    else if(jsonObject.getString("MONTH").equals("2019-10")){
                        j[0] = jsonObject.getString("SALES");   //SALES 카운트값
                        j[1] = jsonObject.getString("COUNT");   //COUNT 값
                    }
                    else if(jsonObject.getString("MONTH").equals("2019-11")){
                        k[0] = jsonObject.getString("SALES");   //SALES 카운트값
                        k[1] = jsonObject.getString("COUNT");   //COUNT 값
                    }
                    else if(jsonObject.getString("MONTH").equals("2019-12")){
                        l[0] = jsonObject.getString("SALES");   //SALES 카운트값
                        l[1] = jsonObject.getString("COUNT");   //COUNT 값
                    }

                }
                Log.e("json값", jsonArray.toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }
            h1();
        }
    }
}
