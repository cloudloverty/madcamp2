package com.example.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class TabFragment2_DetailImage  extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_fragment_2_simple_image);

        ImageView imageView =(ImageView)findViewById(R.id.imageView);
        setImage(imageView);
    }

    private void setImage(ImageView imageView){
        Intent receivedIntent = getIntent();
        Bitmap bitmap = (Bitmap) receivedIntent.getParcelableExtra("image");
        imageView.setImageBitmap(bitmap);
    }
}