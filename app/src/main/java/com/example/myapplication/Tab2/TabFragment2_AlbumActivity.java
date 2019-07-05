package com.example.myapplication.Tab2;

import android.app.Activity;
import android.content.Intent;
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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class TabFragment2_AlbumActivity extends AppCompatActivity {
    GridView galleryGridView;
    ArrayList<HashMap<String, String>> imageList = new ArrayList< HashMap<String, String> > (); // 여기
    String album_name = "";
    LoadAlbumImages loadAlbumTask;                                                                  // 앨범 읽어오기 인스턴스

    final int PICTURE_REQUEST_CODE = 100;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_fragment_2_activity_album);                                     // tab_fragment_2_activity_album에 뷰 올리기

                                                                                                    // intent : activity간 데이터 교환
        Intent intent = getIntent();                                                                // TabFragment2_MainActivity : class LoadAlbum : func onPostExecute의 intent
        album_name = intent.getStringExtra("name");                                          // intent에서 앨범이름 읽어오기
        setTitle(album_name);                                                                       // 앨범이름 상단 바에 보이기



        galleryGridView = (GridView) findViewById(R.id.galleryGridView);                            // 선택된 앨범에 저장된 사진들의 이미지 미리보기
        int iDisplayWidth = getResources().getDisplayMetrics().widthPixels ;
        Resources resources = getApplicationContext().getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = iDisplayWidth / (metrics.densityDpi / 160f);

                                                                                                    // 사진의 가로세로 비율 조절
        if(dp < 360)
        {
            dp = (dp - 17) / 2;
            float px = TabFragment2_Function.convertDpToPixel(dp, getApplicationContext());
            galleryGridView.setColumnWidth(Math.round(px));
        }


        loadAlbumTask = new LoadAlbumImages();
        loadAlbumTask.execute();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tab_fragment_2_menu_album, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.cam:
                Intent cameraApp = new Intent(TabFragment2_AlbumActivity.this, TabFragment2_TakePictureActivity.class);
                cameraApp.putExtra("location", album_name);
                startActivity(cameraApp);
                return true;
            case R.id.Delete:
                Toast.makeText(TabFragment2_AlbumActivity.this,"소중한 추억입니다. 다시 생각해보세요.",Toast.LENGTH_SHORT).show();
/*
                Intent selectToDelete = new Intent(TabFragment2_AlbumActivity.this, TabFragment2_Selection.class);
                selectToDelete.putExtra("location", album_name);
                startActivity(selectToDelete);
*/
                return true;

        }

        return super.onOptionsItemSelected(item);
    }



    // 앨범 속 이미지들의 정보 불러오기
    class LoadAlbumImages extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            imageList.clear();
        }

        protected String doInBackground(String... args) {
            String xml = "";

            String path = null;                                                                     // 사진의 위치 정보 초기화
            String album = null;                                                                    // 사진이 저장된 앨범 정보 초기화
            String timestamp = null;                                                                // 사진이 단말기에 저장된 시간 초기화
            Uri uriExternal = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            Uri uriInternal = MediaStore.Images.Media.INTERNAL_CONTENT_URI;

                                                                                                    // 커서를 옮겨가며 선택된 앨범에 저장된 사진 정보 읽어오고 각 변수에 저장
            String[] projection = { MediaStore.MediaColumns.DATA,
                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.MediaColumns.DATE_MODIFIED };

            Cursor cursorExternal = getContentResolver().query(uriExternal, projection, "bucket_display_name = \""+album_name+"\"", null, null);
            Cursor cursorInternal = getContentResolver().query(uriInternal, projection, "bucket_display_name = \""+album_name+"\"", null, null);
            Cursor cursor = new MergeCursor(new Cursor[]{cursorExternal,cursorInternal});
            while (cursor.moveToNext()) {

                path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA));
                album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
                timestamp = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_MODIFIED));

                imageList.add(TabFragment2_Function.mappingInbox(album, path, timestamp, TabFragment2_Function.convertToTime(timestamp), null));
                                                                                                    // 사진정보를 사진리스트에 추가
            }
            cursor.close();
          // Collections.sort(imageList, new TabFragment2_AlbumActivity(TabFragment2_Function.KEY_TIMESTAMP, "dsc")); // Arranging photo album by timestamp decending // 여기
            return xml;
        }

        @Override
        protected void onPostExecute(String xml) {
                                                                                                    // 현재 Activity에 사진들의 이미지 붙이기
            SingleAlbumAdapter adapter = new SingleAlbumAdapter(TabFragment2_AlbumActivity.this, imageList);
            galleryGridView.setAdapter(adapter);

            // 특정 사진 클릭 시, 해당 사진의 이미지 열기
            galleryGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view,
                                        final int position, long id) {
                    Intent intent = new Intent(TabFragment2_AlbumActivity.this, TabFragment2_GalleryPreview.class);
                    intent.putExtra("path", imageList.get(+position).get(TabFragment2_Function.KEY_PATH));
                    startActivity(intent);                                                          // TabFragment2_GalleryPreview 액티비티 실행
                }
            });
        }
    }
}


// 앨범의 모든 사진 붙이기
class SingleAlbumAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList<HashMap< String, String >> data;
    public SingleAlbumAdapter(TabFragment2_AlbumActivity a, ArrayList < HashMap < String, String >> d) {
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

    // class SingleAlbumViewHolder로 class TabFragment2_AlbumActivity : class LoadAlbumImages에서 읽어온 앨범들 보이기
    public View getView(int position, View convertView, ViewGroup parent) {
        SingleAlbumViewHolder holder = null;
        if (convertView == null) {
            holder = new SingleAlbumViewHolder();
            convertView = LayoutInflater.from(activity).inflate(
                    R.layout.tab_fragment_2_single_album_row, parent, false);

            holder.galleryImage = (ImageView) convertView.findViewById(R.id.galleryImage);

            convertView.setTag(holder);
        } else {
            holder = (SingleAlbumViewHolder) convertView.getTag();
        }
        holder.galleryImage.setId(position);

        HashMap< String, String > song = new HashMap<String, String>();
        song = data.get(position);
        try {

            Glide.with(activity)
                    .load(new File(song.get(TabFragment2_Function.KEY_PATH))).into(holder.galleryImage); // Uri of the picture


        } catch (Exception e) {}
        return convertView;
    }
}

// TabFragment2_AlbumActivity 액티비티에 사진이미지 보이기
class SingleAlbumViewHolder {
    ImageView galleryImage;
}

