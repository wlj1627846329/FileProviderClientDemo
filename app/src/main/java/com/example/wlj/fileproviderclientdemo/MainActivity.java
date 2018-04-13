package com.example.wlj.fileproviderclientdemo;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.OpenableColumns;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv = ((ImageView) findViewById(R.id.main_vi));
        String serviceStr = "com.example.wlj.fileproviderdemo.testContentProvider";
        Uri uri = Uri.parse("content://" + serviceStr + "/wallpaper/small/"
                + getPackageName());
        String uriString = getContentResolver().getType(uri);

        Log.e("AAA-->", "onCreate: " + uriString );
        readFile(Uri.parse(uriString));
    }

    private void readFile(Uri returnUri) {
        ParcelFileDescriptor inputPFD;
        //获取文件句柄
        try {
            inputPFD = getContentResolver().openFileDescriptor(returnUri, "r");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
        //获取文件名字和大小
        Cursor returnCursor =
                getContentResolver().query(returnUri, null, null, null, null);
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
        returnCursor.moveToFirst();
        Log.e("AAA-->", "readFile: " + "文件名:" + returnCursor.getString(nameIndex) + ", 大小:" +
                Long.toString(returnCursor.getLong(sizeIndex)) + " B");
        returnCursor.close();

        Bitmap bitmap = BitmapFactory.decodeFileDescriptor(inputPFD.getFileDescriptor());
        iv.setImageBitmap(bitmap);

        try {
            inputPFD.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
