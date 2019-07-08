package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


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
        linearLayout = (LinearLayout) view.findViewById(R.id.linearLayout); // CoordinatorLayout view
        try {
            Log.d("디버그2", "start JSONTaskGetSNS...");
            final String JsonArrayBuffer = new JSONTaskGetSNS().execute("http://143.248.36.211:3000/sns").get();
            final JSONArray list = new JSONArray(JsonArrayBuffer);
            Log.d("디버그2", "jsonArray_string: "+JsonArrayBuffer);

            renderView(list);   //implement later


        } catch (Exception e) {e.printStackTrace();}

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