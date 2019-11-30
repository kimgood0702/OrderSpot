package com.example.orderspot_general.Analysis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;


import com.example.orderspot_general.R;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

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

public class UserpActivity extends AppCompatActivity {
    private LineChart lineChart;
    Intent it; String sit;
    String a[] = {"",""};
    String b[] = {"",""};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userp);
        lineChart = (LineChart) findViewById(R.id.chart);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        it = getIntent();
        sit = it.getStringExtra("sidd");
        Log.e("sit는?",it.getStringExtra("sidd"));
        new JSONTask4().execute();

        Button bp =findViewById(R.id.buttonp);
        bp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    //비동기 처리
    public class JSONTask4 extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                // 서버에 요청값 보내기
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("type", "guser_bias_analysis");
                jsonObject.accumulate("guser_ID", sit);
                jsonObject.accumulate("start_day", "2019-10-19");
                jsonObject.accumulate("term", "m");
                Log.e("로그",jsonObject.toString());
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
                Log.e(TAG,jsonArray.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    //tv2.setText(jsonObject.getString("productImage"));

                    if(jsonObject.getString("DAY").equals("2019-10")) {
                        if(jsonObject.getString("CATEGORY").equals("fastfood")){
                            a[0] = jsonObject.getString("COUNT");   //fastfood 카운트값
                        }else{
                            a[1] = jsonObject.getString("COUNT");   //beverage 카운트 값
                        }
                    }
                    else if(jsonObject.getString("DAY").equals("2019-11")){
                        if(jsonObject.getString("CATEGORY").equals("fastfood")){
                            b[0] = jsonObject.getString("COUNT");   //fastfood 카운트값
                        }else{
                            b[1] = jsonObject.getString("COUNT");   //beverage 카운트 값
                        }
                    }
                }
                Log.e("로그확인",a[0] + "\n" + a[1] + "\n" + b[0] + "\n" + b[1]);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            g1();
        }
    }

    public void g1() {
        List<Entry> entries = new ArrayList<>();
        entries.add(new Entry(10, Integer.parseInt(a[0])));
        entries.add(new Entry(11, Integer.parseInt(b[0])));

        List<Entry> entries2 = new ArrayList<>();
        entries2.add(new Entry(10, Integer.parseInt(a[1])));
        entries2.add(new Entry(11, Integer.parseInt(b[1])));

        ////1
        LineDataSet lineDataSet = new LineDataSet(entries, "fast-food");
        lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);

        lineDataSet.setLineWidth(2);    // 선굵기
        lineDataSet.setCircleRadius(6); //곡률
        lineDataSet.setCircleColor(Color.parseColor("#FFA1B4DC"));
        lineDataSet.setCircleColorHole(Color.BLUE);
        lineDataSet.setColor(Color.parseColor("#FFA1B4DC"));
        lineDataSet.setDrawCircleHole(true);
        lineDataSet.setDrawCircles(true);
        lineDataSet.setDrawHorizontalHighlightIndicator(false);
        lineDataSet.setDrawHighlightIndicators(false);
        lineDataSet.setDrawValues(false);

        //   LineData lineData = new LineData(lineDataSet);
        //라인 데이터의 텍스트 컬러 / 사이즈를 설정 가능

        /////2
        LineDataSet lineDataSet2 = new LineDataSet(entries2, "beverage");
        lineDataSet2.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineDataSet2.setLineWidth(2);    // 선굵기
        lineDataSet2.setCircleRadius(6); //곡률
        lineDataSet2.setCircleColor(Color.parseColor("#FF0000"));
        lineDataSet2.setCircleColorHole(Color.RED);
        lineDataSet2.setColor(Color.parseColor("#FF0000"));
        lineDataSet2.setDrawCircleHole(true);
        lineDataSet2.setDrawCircles(true);
        lineDataSet2.setDrawHorizontalHighlightIndicator(false);
        lineDataSet2.setDrawHighlightIndicators(false);
        lineDataSet2.setDrawValues(false);

        //라인 데이터의 텍스트 컬러 / 사이즈를 설정 가능
        List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(lineDataSet);
        dataSets.add(lineDataSet2);

        LineData data = new LineData(dataSets);
        //////////////////////////////////////////////////////////////////////////

        lineChart.setData(data);
        lineChart.invalidate();

        // x 축
        XAxis xAxis = lineChart.getXAxis();
        // x축에 대한 정보를 lineChart 로부터 받아옴
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        // x축 표시에 대한 위치 설정으로 아래쪽에 위치시킨다.
        xAxis.setTextColor(Color.BLACK);
        // x축 텍스트 컬러 설정
        xAxis.enableGridDashedLine(8, 24, 0);

        // y축
        YAxis yLAxis = lineChart.getAxisLeft();
        // y축 왼쪽 데이터 가져오기
        yLAxis.setTextColor(Color.BLACK);
        // y푹 텍스트 컬러 설정

        //y축 오른쪽 비활성화
        YAxis yRAxis = lineChart.getAxisRight();
        yRAxis.setDrawLabels(false);
        yRAxis.setDrawAxisLine(false);
        yRAxis.setDrawGridLines(false);

        Description description = new Description();
        description.setText("");

        lineChart.setDoubleTapToZoomEnabled(false);
        lineChart.setDrawGridBackground(false);
        lineChart.setDescription(description);
        lineChart.animateY(2000, Easing.EasingOption.EaseInCubic);
        lineChart.invalidate();

    }

}
