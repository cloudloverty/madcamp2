package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

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


public class TabFragment2_ImageGridAdapter extends BaseAdapter{
    Context context = null;
    String[] images = null;
    ImageView imageView;
    int imageID = 0;

    public TabFragment2_ImageGridAdapter(Context context, String[] images){
        this.context = context;
        this.images = images;
    }

    public int getCount() { return (null != images) ? images.length : 0; }
    public Object getItem(int position) { return (null != images) ? images[position] : 0; }
    public long getItemId(int position) { return position; }

    public View getView(final int position, View convertView, ViewGroup parent){

        final String path = images[position];

        if(convertView != null)
            imageView = (ImageView)convertView;
        else {
            try {
                final Bitmap bitmap = new JSONTaskGetImage().execute("http://143.248.36.211:3000/imageGet", path).get();

                imageView = new ImageView(context);
                imageView.setAdjustViewBounds(true);
                imageView.setImageBitmap(bitmap);

                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view){
                        Intent intent = new Intent(context, TabFragment2_DetailImage.class);
                        intent.putExtra("image", bitmap);
                        context.startActivity(intent);
                    }
                });

            } catch(Exception e){
                e.printStackTrace();
            }
        }

        return imageView;
    }

    public class JSONTaskGetImage extends AsyncTask<String, String, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... parms) {
            try{
                JSONObject jsonObject = new JSONObject();
                String imageURL = parms[1];
                jsonObject.accumulate("imageURL", imageURL);

                HttpURLConnection con = null;
                BufferedReader reader = null;

                try{
                    URL url = new URL(parms[0]);                                     //url을 가져온다.
                    //Log.d("디버그", "JSONTaskGetImage: "+url.toString());
                    con = (HttpURLConnection) url.openConnection();
                    //Log.d("디버그", "open connection");
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
                    writer.close();
                    //Log.d("디버그", "write flushed");


                    //입력 스트림 생성
                    InputStream stream = con.getInputStream();

                    Bitmap image = BitmapFactory.decodeStream(stream);

                    Bitmap imageR = Bitmap.createScaledBitmap(image, 320, 240, false);

                    return imageR;

                } catch (Exception e){
                    Log.d("디버그", "createScaledBitmap exception");
                    e.printStackTrace();
                } finally{
                    if (con != null)
                        con.disconnect();
                }

            } catch(Exception e){
                e.printStackTrace();
                Log.d("디버그", "JSONObject exception");
            }

            return null;

        }

    }

}


/*
            Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), temp[position]);
            String t = "";

            bmp = Bitmap.createScaledBitmap(bmp, 320, 240, false);

            imageView = new ImageView(context);
            imageView.setAdjustViewBounds(true);
            imageView.setImageBitmap(bmp);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view){
                    Log.d("디버그", "onClick: "+position+", "+path);
                    Intent intent = new Intent(context, TabFragment2_DetailImage.class);
                    intent.putExtra("image ID", temp[position]);
                    context.startActivity(intent);
                }

            });
 */