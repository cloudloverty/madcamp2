package com.example.myapplication;
//https://gist.github.com/srayhunter/47ab2816b01f0b00b79150150feb2eb2
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class TabFragment1 extends Fragment {

    Context cont;
    Context tab1;
    RecyclerView recyclerView;
    View v;
    static final int REQUEST_PERMISSION_KEY = 1;
    private ArrayList<ContactItem> mResult = new ArrayList<>();     //연락처 정보를 담은 object로 이루어진 array
    private ArrayList<String> nameList = new ArrayList<>();             //연락처 정보에서 이름만 가져온 array
    private ArrayList<String> numberList = new ArrayList<>();             //연락처 정보에서 이름만 가져온 array
    private ArrayList<String> nameNumberList = new ArrayList<>();   //연락처 정보에서 이름+번호만 가져온 array
    private ArrayList<Uri> photoUriList = new ArrayList<>();        //연락처 정보에서 photo uri만 가져온 array

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.tab_fragment_1, container, false);
        cont = inflater.getContext();
        tab1 = container.getContext();

        //실제 화면에 띄우는 어댑터 연결은, 아래에 onResume()에서 실행됨

        //이 fab를 통해서 연락처 추가.
        //ACTION_INSERT라는 intent를 실행시키고 startActivityForResult를 통해서 추가가 끝난 뒤에 onActivityResult에서 view를 update
        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                Intent addContactIntent = new Intent(Intent.ACTION_INSERT);
                addContactIntent.setType(ContactsContract.Contacts.CONTENT_TYPE);
                addContactIntent.putExtra(ContactsContract.Intents.Insert.NAME,"");
                addContactIntent.putExtra(ContactsContract.Intents.Insert.PHONE,"");

                startActivityForResult(addContactIntent,1000);
            }
        });

        return v;
    }

    private void initRecyclerView(View v){
        Context c = getActivity();

        //recyclerView에 recyclerv_view layout 바인딩
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerv_view);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter( c, mResult);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(c));
    }

    /////////////////////helper function//////////////////////
    public ArrayList<ContactItem> phoneBook() {

        //ContactItem class는 아래 정의되어 있음
        ArrayList<ContactItem> contactList = new ArrayList<ContactItem>();

        //phone의 content를 받아오는 uri, 순서대로 number, name, photo, contact_id, last update date를 받아옴
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection = new String[]{
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.Contacts.PHOTO_ID,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                ContactsContract.CommonDataKinds.Phone.CONTACT_LAST_UPDATED_TIMESTAMP};
        String[] selectionArgs = null;
        String sortOrder = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " COLLATE LOCALIZED ASC";
        Cursor cursor = cont.getContentResolver().query(uri, projection, null, selectionArgs, sortOrder);

        //커서를 움직이며 필요한 정보를 contactItem에 세팅
        if (cursor.moveToFirst()) {
            do{
                long photo_id = cursor.getLong(2);
                String name = cursor.getString(1);
                String number = cursor.getString(0);
                String PhoneContactID = cursor.getString(3);
                String TimeStamp = getDate(cursor.getString(4));
                Log.d("이메일", "TimeStamp: " + TimeStamp);

                ContactItem contactItem = new ContactItem();
                contactItem.setPhoto_id(photo_id);
                contactItem.setUser_Name(name);
                contactItem.setUser_Number(number);
                contactItem.setLast_update(TimeStamp);

                //위에서 얻은 contact ID를 통해서, 똑같은 ID를 가진 contact의 이메일을 받아올 거임
                //get Email
                Uri curi =  ContactsContract.CommonDataKinds.Email.CONTENT_URI;
                Cursor ce = cont.getContentResolver().query(
                        curi,
                        null,
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                        new String[] {PhoneContactID},
                        null);
                String email = "No Email Address!";         //default
                if (ce != null && ce.moveToFirst()) {       //이메일 정보가 있을 경우 가져오기
                    email = ce.getString(ce.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                    ce.close();
                }

                contactItem.setUser_Email(email);           //contactItem에 추가

                contactList.add(contactItem);               //완성된 contactItem 오브젝트를 contactList에 추가
            } while (cursor.moveToNext());                  //모든 연락처에 대해 반복
        }
        cursor.close();
        return contactList;
    }

    public String getDate(String TimeStamp){                //time stamp를 보기 쉬운 스트링으로 변환
        long dataValue = Long.parseLong(TimeStamp);
        Date date = new Date(dataValue);
        SimpleDateFormat datef = new SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.getDefault());
        return datef.format(date);
    }


    public class ContactItem {
        private String user_Name;
        private String user_Number;
        private String user_Email;
        private String last_update;
        private long photo_id;

        public ContactItem() {}
        public long getPhoto_id() {return photo_id;}
        public void setPhoto_id(long id) {this.photo_id = id;}
        public String getUser_Email() {return user_Email;}
        public void setUser_Email(String address) {this.user_Email = address;}
        public String getUser_Name() {return user_Name;}
        public void setUser_Name(String name) {this.user_Name = name;}
        public String getUser_Number() {return user_Number;}
        public void setUser_Number(String number) {this.user_Number = number;}
        public String getLast_Update() {return last_update;}
        public void setLast_update(String date) {this.last_update = date;}
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case REQUEST_PERMISSION_KEY: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    mResult = phoneBook();
                    initRecyclerView(v);
                } else
                {
                    Toast.makeText(tab1.getApplicationContext(), "You must accept permissions.", Toast.LENGTH_LONG).show();
                }
            }
        }

    }
    @Override
    public void onResume() {
        super.onResume();

        String[] PERMISSIONS = {Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_CONTACTS};
        if(!MainActivity.hasPermissions(tab1.getApplicationContext(), PERMISSIONS)){
            ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, REQUEST_PERMISSION_KEY);
        }else{
            mResult = phoneBook();
            initRecyclerView(v);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        Context c = getActivity();

        //연락처 추가하는 activity가 끝나고 돌아올 때 recyclerView의 adapter을 새로 설정해서 새 화면을 띄움
        RecyclerViewAdapter adapter = new RecyclerViewAdapter( c, phoneBook());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(c));

    }

}
