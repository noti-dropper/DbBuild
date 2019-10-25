package com.example.dblogic;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        데이터베이스 매니저 생성
//        데이터베이스 파일 실제 위치 : /data/data/앱패키지주소/databases/data.db
        DBManager database = new DBManager(openOrCreateDatabase("data.db", MODE_PRIVATE, null));  // 데이터베이스 생성, 열기


//        새로운 노티를 데이터베이스에 반영하는 예시
//        try {
//            database.updateNewNoti("오늘 날씨가 좋군요!!");  // 노티 메시지를 테이블(Notification, Noun)에 업데이트
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        명사 테이블 전부 가져옴
//        ArrayList<NounDTO> list = database.getNounList();
//        for(int i =0 ; i < list.size(); i++){
//            Log.e("========", list.get(i).noun + "/" + list.get(i).weight);
//        }

//        NOTIFICATION 데이터베이스에 대한 weight값 업데이트
//        database.updateNotiWeight(2,true);
//
//        NOUN 데이터베이스에 대한 weight값 업데이트
//        database.insertNoun("바다");
//        database.updateNounWeight(2, true);
//        database.updateNounWeight("하늘", false);
//
//        NOTIFICATION 데이터베이스 정보를 가져옴
//        database.getNotificationList();
    }
}



