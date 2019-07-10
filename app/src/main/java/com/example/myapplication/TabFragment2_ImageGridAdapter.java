package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
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

        if(convertView != null){
            imageView = (ImageView)convertView;
        } else {
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
                    URL url = new URL(parms[0]);
                    con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("POST");
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
                    Bitmap image = BitmapFactory.decodeStream(stream);
                    Bitmap imageR = Bitmap.createScaledBitmap(image, 320, 240, false);
                    return imageR;

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
