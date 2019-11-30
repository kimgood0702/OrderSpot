package com.example.orderspot_general.CountD;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.orderspot_general.R;
import com.example.orderspot_general.WaitActivity;

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

public class Main2Activity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    Button cb;  // 구매버튼
    TextView tvid, test;
    String data = ""; // 서버전송 데이터
    String wt; // 대기번호
    String loginId;
    EditText require;
    String requ = "";   //요청사항
    static JSONObject amount = new JSONObject();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // 사용자 아이디
        tvid = findViewById(R.id.recieveid2);
        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        loginId = auto.getString("inputId", null);   // 해당 값 불러오기
        tvid.setText("" + loginId+"님");

        final Intent it = getIntent();
        test = findViewById(R.id.textViewTEST);

        recyclerView = findViewById(R.id.recyclerview2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // 선택한 메뉴들 가져오기.
        recyclerView.setAdapter(adapter);

        ///////////////////////////////////////체크한 메뉴들 리사이클러뷰 적용/////////////////////////////////////////
        JSONObject jsonObject = new JSONObject();
        final List<CheckAdapter.Checklist> checklists = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(it.getStringExtra("name"));
            //
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);

                CheckAdapter.Checklist checklist = new CheckAdapter.Checklist(
                        jsonObject.getString("productName"), //guser_ID
                        jsonObject.getString("productPrice"), //
                        jsonObject.getString("product_ID"),
                        jsonObject.getString("MerchantUser_muser_ID"));// muser_ID
                checklists.add(checklist);

                try {
                    //수량을 넣으면 됨
                    amount.put(jsonObject.getString("product_ID"), 1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            adapter = new CheckAdapter(checklists);
            recyclerView.setAdapter(adapter);
            Log.e("과연2", amount.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        // 구매 버튼
        cb = findViewById(R.id.checkbtn);
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                require = findViewById(R.id.editTextre); // 요청사항
                requ = require.getText().toString();
                /////////////////////////////////////////////////////////////////서버전송데이터/////////////////////////////////////////
                JSONObject jsonObject2 = new JSONObject();
                CheckAdapter.Checklist ch = checklists.get(0);
                JSONArray jsonArray2 = new JSONArray();
                try {
                    jsonObject2.put("muser_ID", ch.getMerchant());
                    jsonObject2.put("guser_ID", loginId);
                    jsonObject2.put("requirement", requ);   //요청사항
                    jsonObject2.put("amount", amount);
                    jsonArray2.put(jsonObject2);
                    if (data.equals("")) {
                        data = jsonObject2.toString();
                    } else {
                        data = data + ',' + jsonObject2.toString();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("과연", data);
                ///////////////////////////////////////////////////////////////////////////////////////////////////////

                new JSONTask3().execute();
                //조건문 true일때 대기화면으로 넘어가게끔
                //개수
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (test.getText().toString().equals("True")) {
                            Intent it = new Intent(getApplicationContext(), WaitActivity.class);
                            //it.putExtra("wt",wt);
                            startActivity(it);
                            finish();
                        }
                    }
                }, 1000);
            }
        });


    }

    public void setProductAmount(String productID, int count) {
        try {
            amount.put(productID, count);
            Log.e("amount_test", amount.toString());
        } catch (Exception e) {
            Log.e("amount_put", e.toString());
        }
    }

    public class JSONTask3 extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {

            try {
                // 서버에 요청값 보내기
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("type", "user_order");
                jsonObject.accumulate("order_information", data);

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

        //doinBackground 메소드가 끝나면 여기로 와서 텍스트 뷰의 값을 바꿔준다.
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                JSONObject a = new JSONObject(result);
                test.setText(a.getString("success"));   //true
                wt = a.getString("order");  //order id 주문번호
                SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                //저장을 하기위해 editor를 이용하여 값을 저장시켜준다.
                SharedPreferences.Editor editor = auto.edit();
                editor.putString("text",wt);
                //꼭 commit()을 해줘야 값이 저장됩니다
                editor.commit();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


}