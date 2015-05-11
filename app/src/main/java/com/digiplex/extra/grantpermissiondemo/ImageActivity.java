package com.digiplex.extra.grantpermissiondemo;

import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class ImageActivity extends ListActivity {
    public static final String CAMERA_IMAGE_BUCKET_NAME =
            Environment.getExternalStorageDirectory().toString()
                    + "/DCIM/Camera";
    public static final String CAMERA_IMAGE_BUCKET_ID =
            getBucketId(CAMERA_IMAGE_BUCKET_NAME);
    int THUMBNAIL_SIZE = 100000;
    private List<ImageData> values;

    /**
     * Matches code in MediaProvider.computeBucketValues. Should be a common
     * function.
     */
    public static String getBucketId(String path) {
        return String.valueOf(path.toLowerCase().hashCode());
    }

    public static List<ImageData> getCameraImages(Context context) {
        final String[] projection = {MediaStore.Images.ImageColumns._ID, MediaStore.Images.Media.DATA};
        final String selection = MediaStore.Images.Media.BUCKET_ID + " = ?";
        final String[] selectionArgs = {CAMERA_IMAGE_BUCKET_ID};
        final Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null);
        ArrayList<ImageData> result = new ArrayList<ImageData>(cursor.getCount());
        if (cursor.moveToFirst()) {
            final int dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            final int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns._ID);
            do {
                final String data = cursor.getString(dataColumn);
                final String id = cursor.getString(idColumn);


                result.add(new ImageData(data, id));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return result;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        values = getCameraImages(this);
        ArrayAdapter<ImageData> adapter = new ArrayAdapter<ImageData>(this,
                android.R.layout.simple_list_item_1, values);
        setListAdapter(adapter);

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        ImageData temp = values.get(position);
    }

    private static class ImageData {
        String name;
        Uri path;

        ImageData(String mName, String id) {
            name = mName;
            path = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + id);

        }

        @Override
        public String toString() {

            File f = new File(name);
            return f.getName();
        }
    }

}