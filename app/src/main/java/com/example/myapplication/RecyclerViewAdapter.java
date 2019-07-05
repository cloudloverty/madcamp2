package com.example.myapplication;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<TabFragment1.ContactItem> mResult;      //tab1에 띄울 스트링을 저장할 array
    private Context mContext;

    public RecyclerViewAdapter(Context context, ArrayList<TabFragment1.ContactItem> result) {
        mResult = result;
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);  //사진+이름+전화번호 레이아웃을 가져와서 ViewHolder object로 변환
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");
        String nameNumber = "[이름]  " + mResult.get(position).getUser_Name() + "\n[번호]  " + mResult.get(position).getUser_Number();
        holder.resultString.setText(nameNumber);       //스트링 저장되있는 array에서 스트링을 가져와서 holder의 result에 넣어줌. result는 저 아래에 정의되어있음
        holder.resultImage.setImageURI(ContentUris.withAppendedId(ContactsContract.Data.CONTENT_URI, mResult.get(position).getPhoto_id()));

        //연락처를 클릭할 시, intent에 각 정보를 넣어서 TabFragment1_DetailInfo에 전달해줌
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, TabFragment1_DetailInfo.class);
                intent.putExtra("name", mResult.get(position).getUser_Name());
                intent.putExtra("number", mResult.get(position).getUser_Number());
                intent.putExtra("email", mResult.get(position).getUser_Email());
                intent.putExtra("photoID", mResult.get(position).getPhoto_id());
                intent.putExtra("date", mResult.get(position).getLast_Update());

                Toast.makeText(mContext, "Open " + mResult.get(position).getUser_Name() +"'s detail information", Toast.LENGTH_SHORT).show();
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mResult.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{        //tab1에 띄울 내용을 저장하는 ViewHolder object

        TextView resultString;
        ImageView resultImage;
        RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            resultString = itemView.findViewById(R.id.result_string);         //layout_listitem에 있는 result_string과 result를 바인드
            resultImage = itemView.findViewById(R.id.image);                  //layout_listitem에 있는 image와 resultImage를 바인드
            parentLayout = itemView.findViewById(R.id.parent_layout);         //layout_listitem에 있는 parent_layout과 parentLayout을 바인드
        }
    }
}
