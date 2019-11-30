package com.example.orderspot_general.Main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.orderspot_general.CountD.Main2Activity;
import com.example.orderspot_general.HomeActivity;
import com.example.orderspot_general.LoginActivity;
import com.example.orderspot_general.R;
import com.example.orderspot_general.RecommandD.RecommandActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

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

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "FCM";

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    TextView tv;    // 토큰
    TextView ri;    // 유저 아이디\
    Button pb;  // 확인버튼
    Intent it;
    String data = "";
    String data2 = "";  String loginId;
    String zxc ; // 좌표
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // 아이디 값 가져오기.
        ri = findViewById(R.id.recieveid);
        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        loginId = auto.getString("inputId", null);   // 해당 값 불러오기
        ri.setText("" + loginId);
        //NFC 좌표값
        it = getIntent();
        zxc =it.getStringExtra("zxc");

        //토큰 값
        tv = findViewById(R.id.textView);

        //로그아웃 버튼.
        Button logout = (Button) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //SharedPreferences에 저장된 값들을 로그아웃 버튼을 누르면 삭제하기 위해
                //SharedPreferences를 불러옵니다. 메인에서 만든 이름으로
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = auto.edit();
                //editor.clear()는 auto에 들어있는 모든 정보를 기기에서 지웁니다.
                editor.clear();
                editor.commit();
                Toast.makeText(MainActivity.this, "로그아웃.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        // 토큰이 등록되는 시점에 호출되는 메소드 입니다.
        FirebaseInstanceId.getInstance().getInstanceId().
                addOnSuccessListener(this, new OnSuccessListener<InstanceIdResult>() {
                            @Override
                            public void onSuccess(InstanceIdResult instanceIdResult) {
                                String newToken = instanceIdResult.getToken();
                                Log.d(TAG, "새토큰" + newToken);
                            }
                        }
                );

        // 확인 버튼 + 체크박스 확인
        pb = findViewById(R.id.purchasebtn);
        pb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             //   String data = "";
             //   String data2 = "";
                int m = 0;
                List<Menulist> mList =((MenulistAdapter) adapter).getMenulist();
                JSONObject jsonObject = new JSONObject();
                for (int i = 0; i < mList.size(); i++) {
                    Menulist ml = mList.get(i);
                    if (ml.isSelected() == true) {
                        data = data + "\n" + ml.getMenuName();
                        m = m + 1;

                        JSONArray jsonArray = new JSONArray();
                        try {
                            jsonObject.put("productName", ml.getMenuName());
                            jsonObject.put("productPrice", ml.getMenuPrice());
                            jsonObject.put("product_ID", ml.getProductid());
                            jsonObject.put("MerchantUser_muser_ID", ml.getMerchantUser_muser_ID());
                            jsonArray.put(jsonObject);
                            if (data2.equals("")) {
                                data2 = jsonObject.toString();
                            } else {
                                data2 = data2 + ',' + jsonObject.toString();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                data2 = "[" + data2 + ']';
                Log.e("오오", data2);
                Intent it2 = new Intent(getApplicationContext(), Main2Activity.class);
                it2.putExtra("name", data2);
                // + 아이디 또 보내기.
                it2.putExtra("sid", it.getStringExtra("sid"));
                startActivity(it2);
                finish();
                Toast.makeText(getApplicationContext(),"Selected : \n" + data, Toast.LENGTH_SHORT).show();

            }
        });

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        new JSONTask2().execute();


    }

    public class JSONTask2 extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {

            try {
                // 서버에 요청값 보내기
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("type", "products_information");
                jsonObject.accumulate("GPS", zxc);   //위치값 변화

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
            ArrayList<String> list = new ArrayList<>();
            List<Menulist> list2 = new ArrayList<>();
            try {
                JSONArray jsonArray = new JSONArray(result);
                //
                //List<String> list = new ArrayList<>();
                list.add(jsonArray.getString(0));   // 상호명
                list.add(jsonArray.getString(1));   //출력하는 부분 (메뉴판)
                //tv2.setText(list.get(0) + "\n" + list.get(1));

                JSONArray menulistArray = new JSONArray(jsonArray.getString(1));
                Log.e(TAG, "menulistArray" + menulistArray);
                Log.e(TAG, "test :" + menulistArray.length());
                for (int i = 0; i < menulistArray.length(); i++) {
                    JSONObject jsonObject = menulistArray.getJSONObject(i);
                    //tv2.setText(jsonObject.getString("productImage"));

                    String image = "http://52.78.114.103:3000/orderspot_image?image_name=" + jsonObject.getString("productImage");
                    Menulist menulist = new Menulist(
                            image,
                            jsonObject.getString("productName"),
                            jsonObject.getString("productPrice") + "원",
                            jsonObject.getString("product_ID"),
                            jsonObject.getString("MerchantUser_muser_ID"),
                            false);//체크박스??
                    list2.add(menulist);

                    Log.e(TAG, "for test \n" + jsonObject.getString("productImage") + "\n" + jsonObject.getString("productName") + "\n"
                            + jsonObject.getString("MerchantUser_muser_ID"));
                }
                adapter = new MenulistAdapter(list2);
                recyclerView.setAdapter(adapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

}
