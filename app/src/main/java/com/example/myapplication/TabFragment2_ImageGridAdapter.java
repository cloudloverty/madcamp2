package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;


public class TabFragment2_ImageGridAdapter extends BaseAdapter{
    Context context = null;
    int[] imageIDs = null;
    int imageID = 0;

    public TabFragment2_ImageGridAdapter(Context context, int[] imageIDs){
        this.context = context;
        this.imageIDs = imageIDs;
    }

    public int getCount() { return (null != imageIDs) ? imageIDs.length : 0; }
    public Object getItem(int position) { return (null != imageIDs) ? imageIDs[position] : 0; }
    public long getItemId(int position) { return position; }

    public View getView(final int position, View convertView, ViewGroup parent){
        ImageView imageView = null;
        imageID = imageIDs[position];

        if(convertView != null)
            imageView = (ImageView)convertView;
        else{
            Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), imageIDs[position]);
            bmp = Bitmap.createScaledBitmap(bmp, 320, 240, false);

            imageView = new ImageView(context);
            imageView.setAdjustViewBounds(true);
            imageView.setImageBitmap(bmp);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view){
                    Intent intent = new Intent(context, TabFragment2_DetailImage.class);
                    intent.putExtra("image ID", imageID);
                    context.startActivity(intent);
                }

            });
        }
        return imageView;
    }
}
