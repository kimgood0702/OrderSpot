package com.example.orderspot_general;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class WaitActivity extends AppCompatActivity {
    String loginId;
    String waittext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // 사용자 아이디
        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        loginId = auto.getString("inputId", null);   // 해당 값 불러오기
        // 대기번호
        waittext = auto.getString("text",null);

        TextView tw = findViewById(R.id.textView2);
        tw.setText(tw.getText().toString() + waittext);

    }

}
