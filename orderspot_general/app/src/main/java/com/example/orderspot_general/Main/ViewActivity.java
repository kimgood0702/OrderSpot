package com.example.orderspot_general.Main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.orderspot_general.R;
import com.squareup.picasso.Picasso;

public class ViewActivity extends AppCompatActivity {
    ImageView okBtn,cImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        okBtn = findViewById(R.id.okBtn);
        cImage = findViewById(R.id.cImage);
        Intent it = getIntent();
        //cImage.setImageResource(it.getIntExtra("img",0));   // 이 부분 오류뜬다.
        String getImageUrl = it.getStringExtra("img");
        Picasso.get().load(getImageUrl).into(cImage);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
