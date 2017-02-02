package com.jspark.android.musicplayer;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission();
        } else {
            init();
        }
    }

    private final int REQ_CODE = 100;

    @TargetApi(Build.VERSION_CODES.M)
    private void checkPermission() {
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            String permArr[] = {Manifest.permission.READ_EXTERNAL_STORAGE};

            requestPermissions(permArr, REQ_CODE);
        } else {
            init();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQ_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                init();
            } else {
                Toast.makeText(this, "권한 없으면 프로그램 못씀", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    public void init() {
        ArrayList<Music> data;

        DataLoader dl = new DataLoader(MainActivity.this);
        data = dl.datas;

        lv = (RecyclerView) findViewById(R.id.recyView);
        MusicAdapter adapter = new MusicAdapter(data, this);
        lv.setAdapter(adapter);
        lv.setLayoutManager(new LinearLayoutManager(this));
    }
}
