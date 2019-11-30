package com.example.orderspot_general;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

public class ConfirmActivity extends AppCompatActivity {
    String loginId;
    String waittext;
    String reviewText;
    EditText editText;  //리뷰입력칸
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);

        // 사용자 아이디
        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        loginId = auto.getString("inputId", null);   // 해당 값 불러오기
        // 대기번호
        waittext = auto.getString("text",null);

        Button b2 = findViewById(R.id.button2);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //리뷰입력
                editText =findViewById(R.id.editText);
                reviewText = editText.getText().toString();

                new JSONTask6().execute();  // 서버에게 수령완료 누르기.
                new JSONTask7().execute();  // 리뷰쓰기
                Log.e("머야",reviewText);


                Toast.makeText(getApplicationContext(), "감사합니다.다음에도 이용해주세요~", Toast.LENGTH_SHORT).show();
                Intent it = new Intent(getApplicationContext(),HomeActivity.class);
                startActivity(it);
                finish();
            }
        });


    }

    //비동기 처리
    public class JSONTask6 extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                // 서버에 요청값 보내기
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("type", "general_complete_message_check");
                jsonObject.accumulate("guser_ID", loginId);
                jsonObject.accumulate("order_ID", waittext);

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
            Log.e("json값12", result);
            try {
                JSONArray jsonArray = new JSONArray(result);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                }
                Log.e("json값", jsonArray.toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


    //비동기 처리
    public class JSONTask7 extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                // 서버에 요청값 보내기
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("type", "user_review");
                jsonObject.accumulate("guser_ID", loginId);
                jsonObject.accumulate("order_ID", waittext);
                jsonObject.accumulate("review",reviewText);

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
            Log.e("json값13", result);
            try {
                JSONArray jsonArray = new JSONArray(result);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                }
                Log.e("json값", jsonArray.toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    //서비스로 부터 인텐트를 받았을 때의 처리.
    @Override
    protected void onNewIntent(Intent intent) {

        //인텐트를 받은 경우만, 값을 Activity로 전달하도록 함.
        if(intent != null){
            processIntent(intent);
        }
        super.onNewIntent(intent);
    }

    //인텐트를 처리하도록 합니다.
    private void processIntent(Intent intent){
        String from = intent.getStringExtra("from");
        if(from == null) {
            //from값이 없는경우 ,값을 전달하지 않음. (푸쉬 노티메시지가 아닌것을 판단하고 처리하지 않는듯)
            Log.d("택택이","보낸 곳이 없습니다.");
            return;
        }
        //메세지를 받은경우 처리를 함.
        Log.d("탱탱이","여기서 메세지 응답처리를 하면 됩니다.");
    }


}


