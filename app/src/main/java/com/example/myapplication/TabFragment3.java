package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
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
import java.text.SimpleDateFormat;
import java.util.Date;


public class TabFragment3 extends Fragment {
    ListView SNSlistView;
    View view;
    LinearLayout linearLayout;
    Context tab3;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tab_fragment_3, container, false);
        tab3 = container.getContext();
        linearLayout = (LinearLayout) view.findViewById(R.id.linearLayout);

        try {
            final String JsonArrayBuffer = new JSONTaskGetSNS().execute("http://143.248.36.211:3000/sns").get();
            final JSONArray list = new JSONArray(JsonArrayBuffer);

            renderView(list);   //first rendering

        } catch (Exception e) {e.printStackTrace();}


        ///////////////////////////SNS post Btn/////////////////////////////////////////////////////
        FloatingActionButton btnAdd = (FloatingActionButton) view.findViewById(R.id.btn_add) ;
        btnAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                final View layout_sns = LayoutInflater.from(tab3).inflate(R.layout.tab_fragment_3_add_sns, null);

                new MaterialStyledDialog.Builder(tab3)
                        .setIcon(R.drawable.ic_launcher_foreground)
                        .setTitle("SHARE")
                        .setCustomView(layout_sns)
                        .setNegativeText("CANCEL")
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveText("SHARE")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                MaterialEditText edt_register_title = (MaterialEditText) layout_sns.findViewById(R.id.edit_title);
                                MaterialEditText edt_register_body = (MaterialEditText) layout_sns.findViewById(R.id.edit_body);

                                if(TextUtils.isEmpty(edt_register_title.getText().toString())){
                                    Toast.makeText(tab3, "Fill title", Toast.LENGTH_SHORT).show();
                                    return;
                                }if(TextUtils.isEmpty(edt_register_body.getText().toString())){
                                    Toast.makeText(tab3, "Fill main text", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                addSNS(edt_register_title.getText().toString(), edt_register_body.getText().toString());

                            }
                        }).show();

            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////


        //////////////////////////////////SNS refresh Btn///////////////////////////////////////////
        FloatingActionButton btnRefresh = (FloatingActionButton) view.findViewById(R.id.btn_refresh);
        btnRefresh.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                try {
                    final String JsonArrayBuffer = new JSONTaskGetSNS().execute("http://143.248.36.211:3000/sns").get();
                    final JSONArray list = new JSONArray(JsonArrayBuffer);

                    renderView(list);   //implement later

                } catch (Exception e) {e.printStackTrace();}
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////


        return view;
    }


    public void renderView(JSONArray list){
        final JSONArray listf = list;
        SNSlistView = (ListView) view.findViewById(R.id.listView);
        TabFragment3_CustomAdapter customAdapter = new TabFragment3_CustomAdapter(tab3, list);
        SNSlistView.setAdapter(customAdapter);

        //////////////////////////////////Detail info/////////////////////////////////////////////
        SNSlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    JSONObject j = listf.getJSONObject(position);
                    Intent intent = new Intent(tab3, TabFragment3_DisplayNote.class);
                    intent.putExtra("json", j.toString());

                    startActivityForResult(intent, 3000);
                } catch (Exception e) {}
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////

    }


    public void addSNS(String title_edt, String body_edt){
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdfNow = new SimpleDateFormat("MM/dd HH:mm");
        String date_edt = sdfNow.format(date);
        String writer_edt = MainActivity.userEmail;
        new JSONTaskPostSNS().execute("http://143.248.36.211:3000/snsPost", title_edt, writer_edt, date_edt, body_edt);
    }


    public class JSONTaskPostSNS extends AsyncTask<String, String, String>{
        @Override
        protected String doInBackground(String... parms) {
            try {
                JSONObject jsonObject = new JSONObject();
                String title = parms[1];
                String writerp = parms[2];
                String date = parms[3];
                String body = parms[4];

                jsonObject.accumulate("title", title);
                jsonObject.accumulate("writer", writerp);
                jsonObject.accumulate("date", date);
                jsonObject.accumulate("body", body);

                HttpURLConnection con = null;
                BufferedReader reader = null;

                try {
                    URL url = new URL(parms[0]);
                    con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("POST");//POST방식으로 보냄
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
                    while ((line = reader.readLine()) != null)
                        buffer.append(line);

                    return buffer.toString();

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (con != null)
                        con.disconnect();
                    try {
                        if (reader != null)
                            reader.close();
                    } catch (IOException e) { e.printStackTrace(); }
                }
            } catch (Exception e) { e.printStackTrace(); }

            return null;
        }
    }

    public class JSONTaskGetSNS extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... parms) {
            try{
                JSONObject jsonObject = new JSONObject();

                HttpURLConnection con = null;
                BufferedReader reader = null;

                try{
                    URL url = new URL(parms[0]);
                    con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");                                    //GET방식으로 보냄
                    con.setRequestProperty("Accept", "text/html");
                    con.setDoInput(true);
                    con.connect();

                    InputStream stream = con.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(stream));
                    StringBuffer buffer = new StringBuffer();
                    String line = "";
                    while((line = reader.readLine()) != null)
                        buffer.append(line);

                    return buffer.toString();

                } catch (Exception e){
                    e.printStackTrace();
                } finally{
                    if (con != null)
                        con.disconnect();
                }
            } catch(Exception e){ e.printStackTrace(); }

        return null;
        }
    }


}

