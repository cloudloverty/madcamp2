package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class TabFragment3_CustomAdapter extends BaseAdapter {
    Context context = null;
    JSONArray list = null;


    public TabFragment3_CustomAdapter(Context context, JSONArray list){
        this.context = context;
        this.list = list;
    }

    public int getCount() { return (null != list) ? list.length() : 0;}
    public Object getItem(int position) {
        if (null != list){
            try{
                return list.getJSONObject(position);
            }catch (Exception e) {e.printStackTrace();}
        }
        return 0;
    }
    public long getItemId(int position) { return position;}

    public View getView(int position, View convertView, ViewGroup parent){
        TextView txtTitle = null;
        TextView txtWriter = null;
        TextView txtDate = null;
        String writerS;
        String titleS;
        String bodyS;
        String dateS;

        try {
            JSONObject j = list.getJSONObject(position);
            writerS = j.getString("writer");
            titleS = j.getString("title");
            bodyS = j.getString("body");
            dateS = j.getString("date");

            if (convertView == null){
                convertView = LayoutInflater.from(context).inflate(R.layout.tab_fragment_3_list_template, null);
                txtTitle = (TextView)convertView.findViewById(R.id.txtTitle);
                txtWriter = (TextView)convertView.findViewById(R.id.txtWriter);
                txtDate = (TextView)convertView.findViewById(R.id.txtDate);
            }
            txtTitle.setText(titleS);
            txtWriter.setText(writerS);
            txtDate.setText(dateS);
        } catch (Exception e) {}

        return convertView;
    }

}
