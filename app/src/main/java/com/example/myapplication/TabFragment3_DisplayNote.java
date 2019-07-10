package com.example.myapplication;


import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

public class TabFragment3_DisplayNote extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_fragment_3_detail_info);
        try {
            JSONObject jsonObject = new JSONObject(getIntent().getStringExtra("json"));
            String title = jsonObject.getString("title");
            String writer = jsonObject.getString("writer");
            String date = jsonObject.getString("date");
            String body = jsonObject.getString("body");

            setTitle("SNS");

            TextView t = (TextView) findViewById(R.id.txtTitle);
            TextView w = (TextView) findViewById(R.id.txtWriter);
            TextView d = (TextView) findViewById(R.id.txtDate);
            TextView b = (TextView) findViewById(R.id.txtBody);

            t.setText(title);
            w.setText(writer);
            d.setText(date);
            b.setText(body);

        } catch (Exception e) {}


    }


    @Override
    public void onBackPressed() {
        finish();
        return;
    }
}