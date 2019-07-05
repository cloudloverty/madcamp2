package com.example.myapplication.Tab2;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.MergeCursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

// fragment2 위에서 동작하는 activity
public class TabFragment2_MainActivity extends AppCompatActivity {

    static final int REQUEST_PERMISSION_KEY = 1;

    TextView tvName;
    Button confirm;
    EditText new_album;
    TextView confirm_creation;
    View addView, toastView;
    Intent intent;



    LoadAlbum loadAlbumTask;                                                                        // 갤러리 읽어오기 인스턴스
    GridView galleryGridView;
    ArrayList<HashMap<String, String>> albumList = new ArrayList<HashMap<String, String>>();
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_fragment_2_activity_main);                                      // tab_fragment_2_activity_main에 뷰 올리기


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addView = (View) View.inflate(TabFragment2_MainActivity.this, R.layout.tab_fragment_2_create_album, null);
                AlertDialog.Builder dlg = new AlertDialog.Builder(TabFragment2_MainActivity.this);
                dlg.setTitle("앨범 생성");
                dlg.setView(addView);
                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent selectForNewAlbum = new Intent(TabFragment2_MainActivity.this, TabFragment2_Selection.class);
                        new_album = (EditText) addView.findViewById(R.id.album_name);
                        tvName.setText(new_album.getText().toString());
                        selectForNewAlbum.putExtra("newAlbum", new_album.getText().toString());
                        startActivity(selectForNewAlbum);
                    }
                });
                dlg.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast toast = new Toast(TabFragment2_MainActivity.this);
                        toastView = (View) View.inflate(TabFragment2_MainActivity.this, R.layout.tab_fragment_2_creation_confirm, null);
                        confirm_creation.setText("취소했습니다.");
                        toast.setView(toastView);
                        toast.show();
                    }
                });
                dlg.show();

            }

        });
        galleryGridView = (GridView) findViewById(R.id.galleryGridView);
                                                                                                    // 앨범 미리보기 가로세로 비율 조절
        int iDisplayWidth = getResources().getDisplayMetrics().widthPixels ;                        // context의 가로픽셀 정보 저장
        Resources resources = getApplicationContext().getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = iDisplayWidth / (metrics.densityDpi / 160f);                                     // dp : pixels/1inch

        if(dp < 360)                                                                                // 가로를 기준으로 세로 비율 조절
        {
            dp = (dp - 17) / 2;
            float px = TabFragment2_Function.convertDpToPixel(dp, getApplicationContext());
            galleryGridView.setColumnWidth(Math.round(px));
        }
                                                                                                    // 앨범 권한 물어보기
        String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        if(!TabFragment2_Function.hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST_PERMISSION_KEY);
        }


    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tab_fragment_2_menu_album, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.Delete:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), 1);
                return true;
            case R.id.Move:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }




    // 갤러리 읽어오기
    class LoadAlbum extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            albumList.clear();
        }

        protected String doInBackground(String... args) {
            String xml = "";

            String path = null;                                                                     // 앨범의 위치 정보 초기화
            String album = null;                                                                    // 앨범 이름 초기화
            String timestamp = null;                                                                // 앨범이 단말기에 저장된 시간 초기화
            String countPhoto = null;                                                               // 앨범에 저장된 사진 수
            Uri uriExternal = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            Uri uriInternal = MediaStore.Images.Media.INTERNAL_CONTENT_URI;

            String[] projection = { MediaStore.MediaColumns.DATA,
                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.MediaColumns.DATE_MODIFIED };
            Cursor cursorExternal = getContentResolver().query(uriExternal, projection, "_data IS NOT NULL) GROUP BY (bucket_display_name",
                    null, null);
            Cursor cursorInternal = getContentResolver().query(uriInternal, projection, "_data IS NOT NULL) GROUP BY (bucket_display_name",
                    null, null);
            Cursor cursor = new MergeCursor(new Cursor[]{cursorExternal,cursorInternal});

                                                                                                    // 커서를 옮겨가며 단말에 저장된 앨범 정보
            while (cursor.moveToNext()) {

                path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA));
                album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
                timestamp = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_MODIFIED));
                countPhoto = TabFragment2_Function.getCount(getApplicationContext(), album);

                albumList.add(TabFragment2_Function.mappingInbox(album, path, timestamp, TabFragment2_Function.convertToTime(timestamp), countPhoto));
                                                                                                    // 앨범정보를 앨범리스트에 추가
            }
            cursor.close();
            Collections.sort(albumList, new TabFragment2_MapComparator(TabFragment2_Function.KEY_TIMESTAMP, "dsc")); // Arranging photo album by timestamp decending
            return xml;
        }

        // 갤러리 붙이기
        @Override
        protected void onPostExecute(String xml) {

            AlbumAdapter adapter = new AlbumAdapter(TabFragment2_MainActivity.this, albumList); // 현재 Activity에 앨범 붙이기
            galleryGridView.setAdapter(adapter);

            // 특정 앨범 클릭 시, 해당 앨범 열기
            galleryGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view,
                                        final int position, long id) {
                    Intent intent = new Intent(TabFragment2_MainActivity.this, TabFragment2_AlbumActivity.class);
                    intent.putExtra("name", albumList.get(+position).get(TabFragment2_Function.KEY_ALBUM));
                    startActivity(intent);                                                          // TabFragment2_AlbumActivity 액티비티 실행
                }
            });
        }
    }


    // 권한 여부 물어보기
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSION_KEY: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    loadAlbumTask = new LoadAlbum();
                    loadAlbumTask.execute();
                }
                else {
                    Toast.makeText(TabFragment2_MainActivity.this, "You must accept permissions.", Toast.LENGTH_LONG).show();
                }
            }
        }

    }



    // 첫 실행이 아니며 권한 없이 시작 할 때, 권한 설정 여부 확인
    @Override
    protected void onResume() {
        super.onResume();

        String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        if(!TabFragment2_Function.hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST_PERMISSION_KEY);
        }else{
            loadAlbumTask = new LoadAlbum();
            loadAlbumTask.execute();
        }

    }
}

// 갤러리의 모든 앨범 붙이기
class AlbumAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList<HashMap< String, String >> data;
    public AlbumAdapter(Activity a, ArrayList < HashMap < String, String >> d) {
        activity = a;
        data = d;
    }
    public int getCount() {
        return data.size();
    }
    public Object getItem(int position) {
        return position;
    }
    public long getItemId(int position) {
        return position;
    }

    // class AlbumViewHolder로 class TabFragment2_MainActivity : class LoadAlbum에서 읽어온 앨범들 보이기
    public View getView(int position, View convertView, ViewGroup parent) {
        AlbumViewHolder holder = null;
        if (convertView == null) {
            holder = new AlbumViewHolder();
            convertView = LayoutInflater.from(activity).inflate(
                    R.layout.tab_fragment_2_album_row, parent, false);

            holder.galleryImage = (ImageView) convertView.findViewById(R.id.galleryImage);          // 앨범 이미지 뷰 저장
            holder.gallery_count = (TextView) convertView.findViewById(R.id.gallery_count);         // 앨범 사진 수 뷰 저장
            holder.gallery_title = (TextView) convertView.findViewById(R.id.gallery_title);         // 앨범 이름 뷰 저장

            convertView.setTag(holder);
        } else {
            holder = (AlbumViewHolder) convertView.getTag();
        }

                                                                                                    // 뷰에 저장된 정보를 position에 setting
        holder.galleryImage.setId(position);
        holder.gallery_count.setId(position);
        holder.gallery_title.setId(position);

        HashMap<String, String> song = data.get(position);
        try {
            holder.gallery_title.setText(song.get(TabFragment2_Function.KEY_ALBUM));
            holder.gallery_count.setText(song.get(TabFragment2_Function.KEY_COUNT));

            Glide.with(activity)
                    .load(new File(song.get(TabFragment2_Function.KEY_PATH))) // Uri of the picture
                    .into(holder.galleryImage);


        } catch (Exception e) {}
        return convertView;
    }
}

// TabFragment2_MainActivity 액티비티에 앨범이미지, 앨범에 저장된 사진 수, 앨범 이름 보이기
class AlbumViewHolder {
    ImageView galleryImage;
    TextView gallery_count, gallery_title;
}

