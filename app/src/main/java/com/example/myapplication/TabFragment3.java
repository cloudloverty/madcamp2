package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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


public class TabFragment3 extends Fragment {
    private ListView obj;
    TabFragment3_NDb mydb;
    FloatingActionButton btnadd;
    ListView mylist;
    CoordinatorLayout coordinatorLayout;
    SimpleCursorAdapter adapter;
    Context tab3;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.tab_fragment_3, container, false);

        tab3 = container.getContext();

        coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinatorLayout); // CoordinatorLayout view
        mydb = new TabFragment3_NDb(tab3);

        //할 일을 추가하는 버튼. id를 지정해준 뒤에 TabFragment3_DisplayNote를 실행시킴
        //추가하는 액티비티가 끝나면 startActivityForResult를 통해서 onActivityResult에 적어놓은 코드를 실행(listView 업데이트)
        btnadd = (FloatingActionButton) view.findViewById(R.id.btnadd);
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle dataBundle = new Bundle();
                dataBundle.putInt("id", 0);
                Intent intent = new Intent(tab3, TabFragment3_DisplayNote.class);
                intent.putExtras(dataBundle);
                startActivityForResult(intent, 3000);
            }
        });

        //tab3가 처음 불려졌을 때, 현재 db에서 데이터를 fetch해서 listView에 표시해줌
        ///////////////////////first fetch when tab3 is is activated///////////////////////////////
        Cursor c = mydb.fetchAll();
        String[] fieldNames = new String[] { TabFragment3_NDb.name, TabFragment3_NDb._id, TabFragment3_NDb.dates, TabFragment3_NDb.remark };
        int[] display = new int[] { R.id.txtnamerow, R.id.txtidrow, R.id.txtdate,R.id.txtremark };
        adapter = new SimpleCursorAdapter(tab3, R.layout.tab_fragment_3_list_template, c, fieldNames, display, 0);
        mylist = (ListView) view.findViewById(R.id.listView1);
        mylist.setAdapter(adapter);
        ///////////////////////////////////////////////////////////////////////////////////////////

        //listView가 클릭됐을 때, intent에 정보를 넣어서 startActivityForResult
        //액티비티가 끝나면 onActivityResult에 적힌 코드를 실행(listView 업데이트)
        mylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                LinearLayout linearLayoutParent = (LinearLayout) arg1;
                LinearLayout linearLayoutChild = (LinearLayout) linearLayoutParent.getChildAt(0);
                TextView m = (TextView) linearLayoutChild.getChildAt(1);
                Bundle dataBundle = new Bundle();
                dataBundle.putInt("id", Integer.parseInt(m.getText().toString()));
                Intent intent = new Intent(tab3, TabFragment3_DisplayNote.class);
                intent.putExtras(dataBundle);

                startActivityForResult(intent, 3000);

            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        Cursor c = mydb.fetchAll();
        String[] fieldNames = new String[] { TabFragment3_NDb.name, TabFragment3_NDb._id, TabFragment3_NDb.dates, TabFragment3_NDb.remark };
        int[] display = new int[] { R.id.txtnamerow, R.id.txtidrow, R.id.txtdate,R.id.txtremark };
        adapter = new SimpleCursorAdapter(tab3, R.layout.tab_fragment_3_list_template, c, fieldNames, display, 0);
        mylist.setAdapter(adapter);

    }
}