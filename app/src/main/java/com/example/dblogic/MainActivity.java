package com.example.dblogic;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {


    SQLiteDatabase db;

    JSONObject similarityAPIResult;
    JSONObject analyzeAPIResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createDatabase();

        try {
//            analyzeAPIResult = new JSONObject("{\"result\": [\"하늘\"]}");
            analyzeAPIResult = new JSONObject("{\"result\": [\"오늘\", \"치킨\"]}");
//            similarityAPIResult = new JSONObject("{\"하늘\": {\"하늘\": 1.0}}");
            similarityAPIResult = new JSONObject("{\"오늘\": {\"오늘\": 0.14069609344005585,\"치킨\": 0.9999999403953552,\"하늘\": 0.018994173035025597},\"치킨\": {\"오늘\": 0.14069609344005585,\"치킨\": 0.9999999403953552,\"하늘\": 0.018994173035025597}}");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            whenGetNewNoti("하늘이다.");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    public void createDatabase(){

        db = openOrCreateDatabase("data.db", MODE_PRIVATE, null);

        if(db == null){
            Log.e("=====", "DB was not opened");
            return;
        }

        try{
            db.execSQL("create table noun( id integer primary key autoincrement, noun text not null unique, weight integer not null default 0, views integer not null default 0);");
        } catch (SQLiteException e){
            e.printStackTrace();
        }

        try{
            db.execSQL("create table notification( id integer primary key autoincrement, title text, msg text not null, nlink text, isread integer default 0 );");
        } catch (SQLiteException e){
            e.printStackTrace();
        }
        Log.e("=====", "DB created");
    }



    public void whenGetNewNoti(String noti) throws JSONException {

        Cursor cursor;
        int latestNotification = 0;
        ArrayList<String> similarSample = new ArrayList<>();


        // 받은 노티를 디비에 저장
        db.execSQL("insert into notification(msg) values (\""+ noti +"\")");
        cursor = db.rawQuery("select id from notification", null);
        latestNotification = cursor.getCount();


        // 백엔드 API에서 명사 정보를 받아옴
        JSONArray compare = analyzeAPIResult.getJSONArray("result");

        for(int i = 0; i < compare.length(); i++){
            // compare에 있는 명사가 데이터베이스에 있는지 확인
            cursor = db.rawQuery("select id from noun where noun = \"" + compare.getString(i) + "\"", null);
            cursor.moveToFirst();

            Log.e("===44===", ""+cursor.getCount()  );

            // 명사가 존재하는 경우
            if(cursor.getCount() != 0){
                Log.e("==Add nLink==", " ");

                int targetNounId = cursor.getInt(0);

                // 명사id 가져옴 -> 가장 최신으로 받은 노티 커서를 가져옴 -> nlink 값 가져오기 -> 명사의 id값 더해서 업데이트
                cursor = db.rawQuery("select nlink from notification where id="+latestNotification, null);
                cursor.moveToFirst();

                String appendNLink = cursor.getString(0);

                if(appendNLink == null)
                    appendNLink = targetNounId + "";
                else
                    appendNLink = appendNLink + "," + targetNounId;

                db.execSQL("update notification set nlink = \"" + appendNLink + "\" where id=" + latestNotification);

            }
            else { // 명사가 존재하지 않는 경우
                Log.e("==Add similarSample==",  " ");
                similarSample.add(compare.getString(i));

                // Noun DB에 추가하고 id값을 가져옴
                db.execSQL("insert into noun(noun) values (\""+ compare.getString(i) +"\")");
                cursor = db.rawQuery("select id from noun", null);

                int targetNounId = cursor.getCount();

                // 마지막으로 추가된 Notification DB의 데이터에 nLink 업데이트
                cursor = db.rawQuery("select nlink from notification where id="+latestNotification, null);
                cursor.moveToFirst();

                String appendNLink = cursor.getString(0);

                if(appendNLink == null)
                    appendNLink = targetNounId + "";
                else
                    appendNLink = appendNLink + "," + targetNounId;

                db.execSQL("update notification set nlink = \"" + appendNLink + "\" where id=" + latestNotification);
            }
        }

        // API에 보낼 JSON data 생성
        ArrayList<String> nounDBTotal = new ArrayList<>();
        ArrayList<Integer> nounDBTotalWeight = new ArrayList<>();

        String makeJSONTmp = "{\"request_noun\" : [";
        for(int i = 0; i < similarSample.size(); i++){
            makeJSONTmp += "\"" + similarSample.get(i) + "\",";
            if(i+1 == similarSample.size())
                makeJSONTmp = makeJSONTmp.substring(0,makeJSONTmp.length()-1);
        }
        makeJSONTmp += "],\"total_nouns\" : [";

        cursor = db.rawQuery("select noun, weight from noun", null);
        cursor.moveToFirst();
        for(int i = 0; i < cursor.getCount(); i++){
            nounDBTotal.add(cursor.getString(0));
            nounDBTotalWeight.add(cursor.getInt(1));

            makeJSONTmp += "\"" + cursor.getString(0) + "\",";
            if(i+1 == cursor.getCount())
                makeJSONTmp = makeJSONTmp.substring(0,makeJSONTmp.length()-1);
            cursor.moveToNext();
        }
        makeJSONTmp += "]}";

        Log.e("====22====", makeJSONTmp);


        JSONObject apiSimilarSend = new JSONObject(makeJSONTmp);

        // API로 유사도를 받아옴
        // apiSimilarSend를 보냄
        // similarityAPIResult를 받음
        // request_noun -> similarSample
        // total_nouns -> nounDBTotal, nounDBTotalWeight

        double totalWeight = 0.0;

        for(int i = 0; i < similarSample.size(); i++){
            JSONObject simJson = similarityAPIResult.getJSONObject(similarSample.get(i));
            for(int j=0; j < simJson.length(); j++){
                double value = simJson.getDouble(nounDBTotal.get(j));
                int weight = nounDBTotalWeight.get(i);

                totalWeight = totalWeight + (value*weight);
            }

            db.execSQL("update noun set weight = " + (int)totalWeight + " where noun=\"" + similarSample.get(i) + "\"");
            totalWeight = 0.0;
        }
    }
}



//        // Algorithm Pseudo Code
//        for(String compare : input){
//
//            compare를 DB 조회;
//            if(있음){
//                Notif DB에 nlink 정보 추가 (concat);
//            } else {
//                sample에 추가;
//                Noun DB 에 명사 정보 추가
//                Notif DB에 nlink 정보 추가 (concat);  /// noun의 weight 값은 뒤에서 처리해줌!!
//            }
//        }
//
//        sample에 있는 내용을 유사도검사 돌림; (sample -> request_noun)
//        JSONObject로 옴;
//
//        for(sample 사이즈 만큼){ // sample과 JSONObj
//            JSONObject.getString(sample[index]);
//            if(유사도 값이 없을 경우 {"null", "null"}){
//                weight값 = 0
//            }else{
//                weight값 계산
//            }
//        }