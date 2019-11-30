package com.example.orderspot_general;

import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.Toast;

import com.example.orderspot_general.Main.MainActivity;
import com.google.firebase.iid.FirebaseInstanceId;

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

public class LoginActivity extends AppCompatActivity {
    EditText id, pwd;
    Button btn;
    String loginId, loginPwd; // 자동로그인 아이디 비밀번호
    String sID, sPWD;    //서버에 보낼 아이디 비밀번호
    TextView z; String TAG = "값";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        id = (EditText) findViewById(R.id.logid);
        pwd = (EditText) findViewById(R.id.logpwd);
        btn = (Button) findViewById(R.id.Lbtn);
        z = findViewById(R.id.textViewz);

        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        //처음에는 SharedPreferences에 아무런 정보도 없으므로 값을 저장할 키들을 생성한다.
        // getString의 첫 번째 인자는 저장될 키, 두 번쨰 인자는 값입니다.
        // 첨엔 값이 없으므로 키값은 원하는 것으로 하시고 값을 null을 줍니다.
        loginId = auto.getString("inputId", null);   // 해당 값 불러오기
        loginPwd = auto.getString("inputPwd", null); // 해당 값 불러오기

        //MainActivity로 들어왔을 때 loginId와 loginPwd값을 가져와서 null이 아니면
        if (loginId != null && loginPwd != null) {
            // if(z.getText().toString().equals("True")) {    // 저장되어 있는 값
            Toast.makeText(LoginActivity.this, loginId + "님 자동로그인 입니다.", Toast.LENGTH_SHORT).show();
            sID = loginId;
            sPWD = loginPwd;
            new JSONTask().execute();
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            intent.putExtra("sid", loginId);
            startActivity(intent);
            finish();
            // }
        }
        //id와 pwd가 null이면 Mainactivity가 보여짐.
        else if (loginId == null && loginPwd == null) {
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {    // 버튼 클릭 했을때 로그인

                    sID = id.getText().toString();
                    sPWD = pwd.getText().toString();
                    new JSONTask().execute();
                    //new JSONTask().execute("http://52.78.114.103:3000/orderspot");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (z.getText().toString().equals("True")) { //  비교 부분
                                SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                                //아이디가 '~~'이고 비밀번호가 '~~'일 경우 SharedPreferences.Editor를 통해
                                //auto의 loginId와 loginPwd에 값을 저장해 줍니다.
                                // SharedPreferences 의 데이터를 저장/편집 하기위해 Editor 변수를 선언한다.
                                SharedPreferences.Editor autoLogin = auto.edit();
                                autoLogin.putString("inputId", id.getText().toString());
                                autoLogin.putString("inputPwd", pwd.getText().toString());
                                //꼭 commit()을 해줘야 값이 저장됩니다
                                autoLogin.commit();

                                Toast.makeText(LoginActivity.this, id.getText().toString() + "님 환영합니다.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                intent.putExtra("sid", id.getText().toString());
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "아이디를 확인해 주세요", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, 1000);

                }
            });

        }
    }

    //비동기 처리
    public class JSONTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                // 서버에 요청값 보내기
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("type", "user_login");
                jsonObject.accumulate("id", sID);
                jsonObject.accumulate("pw", sPWD);
                jsonObject.accumulate("token", FirebaseInstanceId.getInstance().getToken());
                Log.e(TAG, sID);
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
            try {
                JSONObject a = new JSONObject(result);
                Log.e(TAG, "테스트: " + a.getString("success"));
                z.setText(a.getString("success"));

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
}
