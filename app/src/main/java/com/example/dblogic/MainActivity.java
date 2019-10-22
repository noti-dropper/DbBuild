package com.example.dblogic;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //NotiTable을 생성하고 객체를 가져옵니다.
        NotiDBHelper NotiDBHelper = new NotiDBHelper(this);
        SQLiteDatabase NotiDB = NotiDBHelper.getWritableDatabase();

        //NounTable을 생성하고 객체를 가져옵니다.
        NounDBHelper NounDBHelper = new NounDBHelper(this);
        SQLiteDatabase NounDB = NounDBHelper.getWritableDatabase();
    }
}
