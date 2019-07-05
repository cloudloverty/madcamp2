package com.example.myapplication;

import android.content.ContentUris;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class TabFragment1_DetailInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_fragment_1_detail_info);

        //RecyclerViewAdapter에서 건네받은 intent에 들어있는 정보들을 꺼내서 저장
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String number = intent.getStringExtra("number");
        String email = intent.getStringExtra("email");
        String date = intent.getStringExtra("date");
        Long photoID = intent.getLongExtra("photoID", 0);

        //위에서 얻은 정보를 layout에 출력해주기 위해 layout을 id를 통해 가져옴
        ImageView photoImage = (ImageView) findViewById(R.id.UserPhoto);
        TextView nameText = (TextView) findViewById(R.id.UserName);
        TextView numberText = (TextView) findViewById(R.id.UserNumber);
        TextView emailText = (TextView) findViewById(R.id.UserEmail);
        TextView dateText = (TextView) findViewById(R.id.LastUpdate);

        //layout에다가 정보 채워넣기
        photoImage.setImageURI(ContentUris.withAppendedId(ContactsContract.Data.CONTENT_URI, photoID));
        nameText.setText(name);
        numberText.setText(number);
        emailText.setText(email);
        dateText.setText(date);

        //통화버튼
        final String tel = "tel:" + number;
        ImageButton btn = (ImageButton) findViewById(R.id.CallButton);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent call_intent = new Intent();
                call_intent.setAction(Intent.ACTION_DIAL);
                call_intent.setData(Uri.parse(tel));
                startActivity(call_intent);
            }
        });

        setTitle(name);

    }
}
