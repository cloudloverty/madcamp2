package com.example.myapplication;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TabFragment3_DisplayNote extends AppCompatActivity {

    String dateString;
    int id_To_Update = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_fragment_3_view_notepad);
        try {
            JSONObject jsonObject = new JSONObject(getIntent().getStringExtra("json"));
            String title = jsonObject.getString("title");
            String writer = jsonObject.getString("writer");
            String date = jsonObject.getString("date");
            String body = jsonObject.getString("body");

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