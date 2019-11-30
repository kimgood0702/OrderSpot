package com.example.orderspot_general;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.orderspot_general.Analysis.UserhActivity;
import com.example.orderspot_general.Analysis.UserpActivity;
import com.example.orderspot_general.Main.MainActivity;
import com.example.orderspot_general.RecommandD.RecommandActivity;

public class HomeActivity extends AppCompatActivity {
    TextView rn;    String loginId;// 유저 아이디\
    Intent it;

    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    private static String tagNum = null;
    private TextView tagDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //NFC
        tagDesc = findViewById(R.id.tagDesc);
        nfcAdapter = nfcAdapter.getDefaultAdapter(this);
        Intent intent = new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        // 아이디 값 가져오기
        // 사용자 아이디
        rn = findViewById(R.id.recieveid1);
        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        loginId = auto.getString("inputId", null);   // 해당 값 불러오기
        rn.setText("" + loginId);

        //로그아웃 버튼.
        Button logout = (Button) findViewById(R.id.logout1);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //SharedPreferences에 저장된 값들을 로그아웃 버튼을 누르면 삭제하기 위해
                //SharedPreferences를 불러옵니다. 메인에서 만든 이름으로
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intent);
                SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = auto.edit();
                //editor.clear()는 auto에 들어있는 모든 정보를 기기에서 지웁니다.
                editor.clear();
                editor.commit();
                Toast.makeText(HomeActivity.this, "로그아웃.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        Button userp = findViewById(R.id.userp);    // 사용자 편향분석 그래프
        userp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it5 = new Intent(getApplicationContext(), UserpActivity.class);
                it5.putExtra("sidd",rn.getText().toString());
                startActivity(it5);
            }
        });

        Button userh = findViewById(R.id.userh);    // 순위 지출 내역 검색
        userh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it5 = new Intent(getApplicationContext(), UserhActivity.class);
                it5.putExtra("sidd",rn.getText().toString());
                startActivity(it5);
            }
        });

        Button recobtn = findViewById(R.id.recommandbtn);   // 메뉴 추천
        recobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it5 = new Intent(getApplicationContext(), RecommandActivity.class);
                it5.putExtra("sidd",rn.getText().toString());
                startActivity(it5);
            }
        });

    }

    // nfc
    @Override
    protected void onPause() {
        if (nfcAdapter != null) {
            nfcAdapter.disableForegroundDispatch(this);
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (nfcAdapter != null) {
            nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if (tag != null) {
            byte[] tagId = tag.getId();
            tagDesc.setText("TAGID: " + toHexString(tagId));
            tagNum = toHexString(tagId);
            if(tagNum.equals("04707032575E80")){
                Intent it = new Intent(getApplicationContext(),MainActivity.class);
                it.putExtra("zxc","37.560478,126.984574");
                tagDesc.setText(" ");   // 해제시 초기화
                startActivity(it);
                Toast.makeText(getApplicationContext(),"주문페이지로 이동합니다.",Toast.LENGTH_SHORT).show();
            }else if(tagNum.equals("0466C532575E80")){
                Intent it = new Intent(getApplicationContext(),MainActivity.class);
                it.putExtra("zxc","37.573646,126.974185");
                tagDesc.setText(" ");   // 해제시 초기화
                startActivity(it);
                Toast.makeText(getApplicationContext(),"주문페이지로 이동합니다.",Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getApplicationContext(),"등록되지 않은 매장입니다",Toast.LENGTH_SHORT).show();
                tagDesc.setText(" ");
            }
        }
    }

    public static final String CHARS = "0123456789ABCEDEF";

    public static String toHexString(byte[] data) {
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<data.length; ++i){
            sb.append(CHARS.charAt((data[i] >> 4)& 0x0F)).append(CHARS.charAt(data[i] & 0x0F));

        }return sb.toString();
    }

}
