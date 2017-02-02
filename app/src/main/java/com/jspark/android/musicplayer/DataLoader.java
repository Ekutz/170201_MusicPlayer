package com.jspark.android.musicplayer;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by jsPark on 2017. 2. 1..
 */

public class DataLoader {

    private static ArrayList<Music> datas = new ArrayList<>();
    Context context;

    public static ArrayList<Music> load(Context context) {
        if(datas==null|| datas.size()==0) {
            getContacts(context);
        }
        return datas;
    }

    public static void getContacts(Context context) {

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        String projections[] = new String[] {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DURATION
        };

        ContentResolver resolver = context.getContentResolver();

        String sortOrder =  MediaStore.Audio.Media.ALBUM_ID+ " ASC";

        Cursor cursor = resolver.query(uri, projections, null, null, sortOrder);

        if(cursor.moveToFirst()) {
            while(cursor.moveToNext()) {
                Music mMusic = new Music();

                int idx = cursor.getColumnIndex(projections[0]);
                mMusic.id = (cursor.getString(idx));
                idx = cursor.getColumnIndex(projections[1]);
                mMusic.album_id = (cursor.getInt(idx));
                idx = cursor.getColumnIndex(projections[2]);
                mMusic.title = (cursor.getString(idx));
                idx = cursor.getColumnIndex(projections[3]);
                mMusic.artist = (cursor.getString(idx));
                idx = cursor.getColumnIndex(projections[4]);
                mMusic.length = (cursor.getInt(idx));

                mMusic.album_img = getAlbumImgSimple(""+mMusic.album_id);

                mMusic.uri = getMusicUri(mMusic.id);
                //Log.d("uri", String.valueOf(mMusic.uri));
                // 가장 간단하고 무식한 방법
                // 이미지가 무거워짐
                //mMusic.album_art = Uri.parse("content://media/external/audio/albumart/"+mMusic.album_id);

                // Bitmap 형식으로 가져오는 방법
                // 500개 정도 가져오면 앱 터짐
                //mMusic.album_art = getAlbumImgBitmap(""+mMusic.album_id);

                datas.add(mMusic);
            }
            cursor.close();
        }
    }

    private static Uri getMusicUri(String music_id) {
        Uri contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        return Uri.withAppendedPath(contentUri, music_id);
    }


    // 경로에서 이미지를 가져다가 Bitmap 형태로 가져오는 방법
    private Bitmap getAlbumImgBitmap(String album_id) {
        Bitmap img = null;

        // album_id로 uri 생성
        Uri uri = Uri.parse("content://media/external/audio/albumart/"+album_id);

        // ContentResolver 생성
        ContentResolver resolver = context.getContentResolver();

        // resolver로 inputStream 열기
        try {
            InputStream is = resolver.openInputStream(uri);

            // BitmapFactory를 통해 이미지를 복호화하여 가져온다
            img = BitmapFactory.decodeStream(is);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return img;
    }


    // 가장 간단하고 무식한 방법
    // 이미지가 안뜨는 경우도 생기고 view가 생길때 마다 가져오니 앱이 무거워짐
    private static Uri getAlbumImgSimple(String album_id) {
        return Uri.parse("content://media/external/audio/albumart/"+album_id);
    }
}
