package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
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
    FloatingActionButton btnadd;
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
            Log.d("디버그2", "start JSONTaskGetSNS...");
            final String JsonArrayBuffer = new JSONTaskGetSNS().execute("http://143.248.36.211:3000/sns").get();
            final JSONArray list = new JSONArray(JsonArrayBuffer);
            Log.d("디버그2", "jsonArray_string: "+JsonArrayBuffer);

            renderView(list);   //implement later

        } catch (Exception e) {e.printStackTrace();}


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

        FloatingActionButton btnRefresh = (FloatingActionButton) view.findViewById(R.id.btn_refresh);
        btnRefresh.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                try {
                    Log.d("디버그2", "restart JSONTaskGetSNS...");
                    final String JsonArrayBuffer = new JSONTaskGetSNS().execute("http://143.248.36.211:3000/sns").get();
                    final JSONArray list = new JSONArray(JsonArrayBuffer);
                    Log.d("디버그2", "jsonArray_string: "+JsonArrayBuffer);

                    renderView(list);   //implement later

                } catch (Exception e) {e.printStackTrace();}
            }
        });


        return view;
    }


    public void renderView(JSONArray list){
        final JSONArray listf = list;
        SNSlistView = (ListView) view.findViewById(R.id.listView);
        TabFragment3_CustomAdapter customAdapter = new TabFragment3_CustomAdapter(tab3, list);
        SNSlistView.setAdapter(customAdapter);


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
                //JSONObject를 만들고 key value 형식으로 값을 저장해준다.
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
                    URL url = new URL(parms[0]);                                     //url을 가져온다.
                    con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("POST");//POST방식으로 보냄
                    con.setRequestProperty("Cache-Control", "no-cache");            //캐시 설정
                    con.setRequestProperty("Content-Type", "application/json");     //application JSON 형식으로 전송

                    con.setRequestProperty("Accept", "text/html");                  //서버에 response 데이터를 html로 받음
                    con.setDoOutput(true);                                          //Outstream으로 post 데이터를 넘겨주겠다는 의미
                    con.setDoInput(true);                                           //Inputstream으로 서버로부터 응답을 받겠다는 의미

                    con.connect();          //연결 수행

                    //서버로 보내기위해서 스트림 만듬
                    OutputStream outStream = con.getOutputStream();
                    //버퍼를 생성하고 넣음
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));
                    writer.write(jsonObject.toString());
                    writer.flush();
                    writer.close();//버퍼를 받아줌


                    //입력 스트림 생성
                    InputStream stream = con.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(stream));    //속도를 향상시키고 부하를 줄이기 위한 버퍼를 선언한다
                    StringBuffer buffer = new StringBuffer();                      //실제 데이터를 받는곳

                    //line별 스트링을 받기 위한 temp 변수
                    String line = "";
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line);
                    }

                    return buffer.toString();

                    //아래는 예외처리 부분이다.
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    //종료가 되면 disconnect메소드를 호출한다.
                    if (con != null) {
                        con.disconnect();
                    }
                    try {
                        //버퍼를 닫아준다.
                        if (reader != null) {
                            reader.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }//finally 부분
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    public class JSONTaskGetSNS extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... parms) {
            try{
                JSONObject jsonObject = new JSONObject();
                Log.d("디버그2", "making useless jsonobject");

                HttpURLConnection con = null;
                BufferedReader reader = null;

                try{
                    URL url = new URL(parms[0]);                                     //url을 가져온다.
                    con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");                                   //GET방식으로 보냄

                    con.setRequestProperty("Accept", "text/html");                  //서버에 response 데이터를 html로 받음
                    con.setDoInput(true);                                           //Inputstream으로 서버로부터 응답을 받겠다는 의미

                    Log.d("디버그2", "connecting...");
                    con.connect();                                                  //연결 수행
                    Log.d("디버그2", "connection success");

                    //입력 스트림 생성
                    InputStream stream = con.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(stream));    //속도를 향상시키고 부하를 줄이기 위한 버퍼를 선언한다
                    StringBuffer buffer = new StringBuffer();                      //실제 데이터를 받는곳

                    //line별 스트링을 받기 위한 temp 변수
                    String line = "";
                    while((line = reader.readLine()) != null){
                        buffer.append(line);
                    }


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


/*
*     public class SNSdata {
        private String writer;
        private String title;
        private String body;
        private String date;

        public SNSdata(String writer, String title, String body, String date){
            this.writer = writer;
            this.title = title;
            this.body = body;
            this.date = date;
        }

        public String getWriter() {return writer;}
        public String getTitle() {return title;}
        public String getBody() {return body;}
        public String getDate() {return date;}

        public ArrayList getSNSdata(JSONArray list) {
            ArrayList snsDataArray = new ArrayList();

            for(int i=0; i<list.length(); i++){
                try {
                    JSONObject j = list.getJSONObject(i);
                    String writerS = j.getString("writer");
                    String titleS = j.getString("title");
                    String bodyS = j.getString("body");
                    String dateS = j.getString("date");
                    snsDataArray.add(new SNSdata(writerS, titleS, bodyS, dateS));
                }catch(Exception e) {e.printStackTrace();}
            }
            return snsDataArray;
        }


    }
* */