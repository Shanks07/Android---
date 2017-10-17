package com.shanks.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class SecActivity extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sec_activity);

        textView = (TextView) findViewById(R.id.txt);
        Intent intent = getIntent();
        String data = intent.getStringExtra("data");
        textView.setText(data);
    }
}
