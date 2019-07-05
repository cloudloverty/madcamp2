package com.example.myapplication.Tab2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class TabFragment2_Delete extends Activity {
    Intent intent = getIntent();
    ArrayList<String> selected = new ArrayList<String>();



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        selected = intent.getStringArrayListExtra("selected");

        deleteSelected();
    }


    public void deleteSelected(){
        for(int i=0;i<selected.size();i++){
            File tempFile = new File(selected.get(i));

            Log.e("Log", "file exists = " + tempFile.exists());



            boolean delete = tempFile.delete();

            Log.e("Log", "delete = " + delete);
        }
    }
}
