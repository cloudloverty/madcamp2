package com.example.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;

public class TabFragment2 extends Fragment {
    static final int REQUEST_PERMISSION_KEY = 1;
    Context tab2;
    GridView galleryGridView;
    private String[] images;
    View view;

    public void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tab_fragment_2, container, false);
        tab2 = container.getContext();

        //권한 확인
        String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        if(!MainActivity.hasPermissions(tab2.getApplicationContext(), PERMISSIONS)){
            ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, REQUEST_PERMISSION_KEY);
        }

        String userEmail = MainActivity.userEmail;
        Log.d("디버그", "get userEmail from MainActivity in Tab2: "+userEmail);
        new JSONTaskUrl().execute("http://143.248.36.211:3000/urlsGet", userEmail);

        return view;

    }

    public class JSONTaskUrl extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... parms) {
            try{
                JSONObject jsonObject = new JSONObject();
                String userEmail = parms[1];
                jsonObject.accumulate("email", userEmail);

                HttpURLConnection con = null;
                BufferedReader reader = null;

                try{
                    URL url = new URL(parms[0]);                                     //url을 가져온다.
                    con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("POST");                                   //POST방식으로 보냄
                    con.setRequestProperty("Cache-Control", "no-cache");            //캐시 설정
                    con.setRequestProperty("Content-Type", "application/json");     //application JSON 형식으로 전송

                    con.setRequestProperty("Accept", "text/html");                  //서버에 response 데이터를 html로 받음
                    con.setDoOutput(true);                                          //Outstream으로 post 데이터를 넘겨주겠다는 의미
                    con.setDoInput(true);                                           //Inputstream으로 서버로부터 응답을 받겠다는 의미

                    con.connect();                                                  //연결 수행

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
                    while((line = reader.readLine()) != null){
                        buffer.append(line);
                    }

                    return buffer.toString();
                } catch (Exception e){
                    e.printStackTrace();
                } finally{
                    if (con != null)
                        con.disconnect();
                    try {
                        if(reader != null)
                            reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            } catch(Exception e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result.equals("\"No images\"")){
                Toast.makeText(tab2, ""+result, Toast.LENGTH_SHORT).show();
                return;
            }else{
                try{
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("image_urls");
                    images = new String[jsonArray.length()];
                    for(int i=0; i<jsonArray.length(); i++)
                        images[i] = jsonArray.get(i).toString();


                    galleryGridView = (GridView) view.findViewById(R.id.galleryGridView);

                    TabFragment2_ImageGridAdapter imageGridAdapter = new TabFragment2_ImageGridAdapter(tab2, images);
                    galleryGridView.setAdapter(imageGridAdapter);

                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }

    }



    ////////////////////////////////////////////////////////////////
    ///////////          Helper Functions             //////////////
    ////////////////////////////////////////////////////////////////
    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }


    /*
    //resize
    int iDisplayWidth = getResources().getDisplayMetrics().widthPixels ;
    Resources resources = tab2.getApplicationContext().getResources();
    DisplayMetrics metrics = resources.getDisplayMetrics();
    float dp = iDisplayWidth / (metrics.densityDpi / 160f);
    if(dp < 360) {
        dp = (dp - 17) / 2;
        float px = convertDpToPixel(dp, tab2.getApplicationContext());
        galleryGridView.setColumnWidth(Math.round(px));
    }
    */

}
