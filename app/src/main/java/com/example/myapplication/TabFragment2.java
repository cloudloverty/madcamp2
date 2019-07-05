package com.example.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

public class TabFragment2 extends Fragment {
    static final int REQUEST_PERMISSION_KEY = 1;
    Context tab2;
    GridView galleryGridView;
    private int[] imageIDs = new int[]{
            R.drawable.gallery_image_01,
            R.drawable.gallery_image_02,
            R.drawable.gallery_image_03,
            R.drawable.gallery_image_04,
            R.drawable.gallery_image_05,
            R.drawable.gallery_image_06,
            R.drawable.gallery_image_07,
    };

    public void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.tab_fragment_2, container, false);
        tab2 = container.getContext();

        //권한 확인
        String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        if(!MainActivity.hasPermissions(tab2.getApplicationContext(), PERMISSIONS)){
            ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, REQUEST_PERMISSION_KEY);
        }


        galleryGridView = (GridView) view.findViewById(R.id.galleryGridView);
        TabFragment2_ImageGridAdapter imageGridAdapter = new TabFragment2_ImageGridAdapter(tab2, imageIDs);
        galleryGridView.setAdapter(imageGridAdapter);

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

        return view;

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


}
