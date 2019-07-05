package com.example.myapplication.Tab2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.MergeCursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.example.myapplication.R;

import java.io.File;


public class TabFragment2_CreateAlbum extends DialogFragment {
    //TabFragment2_TakePictureActivity forNew;
    Button confirm;
    EditText newAlbum;
    TextView result;
    View creationView, toastView;

    String newAlbumName;
    File newAlbumPath;

    Intent intent;

    private CreationListener cListener;


    public interface CreationListener {

        public void myCallback(String new_album);

    }


    public TabFragment2_CreateAlbum() {

    }
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

/*
        try {

            cListener = (CreationListener) getTargetFragment();

        } catch (ClassCastException e) {

            throw new ClassCastException();

        }
*/
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        creationView = (View) View.inflate(getActivity(), R.layout.tab_fragment_2_create_album, null);
        toastView = (View) View.inflate(getActivity(), R.layout.tab_fragment_2_creation_confirm, null);
        final Toast toast = new Toast(getActivity());

        AlertDialog.Builder dlg = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        dlg.setTitle("앨범 생성");
        dlg.setView(creationView);

        dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                result = (TextView) toastView.findViewById(R.id.result);
                newAlbum = (EditText) creationView.findViewById(R.id.album_name);
                newAlbumName = newAlbum.getText().toString();
                newAlbumPath = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+newAlbumName);
                Uri uriExternal = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                Uri uriInternal = MediaStore.Images.Media.INTERNAL_CONTENT_URI;

                if (newAlbumPath.exists()) {
                    toast.setView(toastView);
                    result.setText("앨범 이름을 확인해주세요");
                    toast.show();
                }
                else{
                    //Intent intent = new Intent(Intent.ACTION_PICK);
                    //intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                    //startActivityForResult(intent, 1);
                    //File tmp = new File(Environment.getExternalStorageDirectory(), "forNewAlbum.jpg");
                    newAlbumPath.mkdir();

                    /*
                    String defaultPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/DCIM";
                    String[] projection = { MediaStore.MediaColumns.DATA,
                            MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.MediaColumns.DATE_MODIFIED };
                    Cursor cursorExternal = getContext().getContentResolver().query(uriExternal, projection, "_data IS NOT NULL) GROUP BY (bucket_display_name",
                            null, null);
                    Cursor cursorInternal = getContext().getContentResolver().query(uriInternal, projection, "_data IS NOT NULL) GROUP BY (bucket_display_name",
                            null, null);
                    Cursor cursor = new MergeCursor(new Cursor[]{cursorExternal,cursorInternal});
                    cursor.moveToNext();

                    defaultPath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA));

                    File file = new File(defaultPath);

                    file.renameTo(new File(newAlbumPath.getAbsolutePath()));

                    cursor.close();

*/




                    toast.setView(toastView);
                    result.setText("앨범 "+newAlbumName+" 생성");
                    //toast.makeText(getContext(),"생성",Toast.LENGTH_LONG);
                    toast.show();
                }


                // tvName.setText(new_album.getText().toString());
                //cListener.myCallback(newAlbum.getText().toString());
            }
        });

        dlg.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //confirm_creation.setText("취소했습니다.");
                toast.setView(toastView);
                toast.show();
            }
        });

        return dlg.create();

    }



}

