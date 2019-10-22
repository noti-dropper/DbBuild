package com.example.dblogic;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //db를 생성하고 객체를 가져옵니다.
        NotiDBHelper DBHelper = new NotiDBHelper(this);
        SQLiteDatabase db = DBHelper.getWritableDatabase();


/*
        // BtnOnClickListener의 객체 생성.
        BtnOnClickListener onClickListener = new BtnOnClickListener() ;

        //모든 알림 조회 버튼 눌림
        Button allButton = (Button) findViewById(R.id.AllButton) ;
        allButton.setOnClickListener(onClickListener) ;

 */
    }

    /*
    //알림을 디비에서 긁어와줄께요
    class BtnOnClickListener implements Button.OnClickListener {
        @Override
        public void onClick(View view) {
            ArrayList<ArrayList[]> noti_arraylist = new ArrayList<ArrayList[]>();

            switch (view.getId()) {
                //모든 알림을 조회하는 버튼이 눌렸을 경우
                case R.id.allButton :
                    String sql = "select * from NotiTable";

                    // 쿼리실행
                    Cursor c = db.rawQuery(sql, null);

                    // 선택된 로우를 끝까지 반복하며 데이터를 가져온다.

                    while(c.moveToNext()){
                        // 가져올 컬럼의 인덱스 번호를 추출한다.
                        int id_pos = c.getColumnIndex("id");
                        int sentence_pos = c.getColumnIndex("sentence");
                        int value_pos = c.getColumnIndex("value");

                        // 컬럼 인덱스 번호를 통해 데이터를 가져온다.
                        int id = c.getInt(id_pos);
                        String sentence = c.getString(sentence_pos);
                        int value = c.getInt(value_pos);

                        //데이터를 리스트에 넣습니다.
                        ArrayList noti = new ArrayList();
                        noti.add(id);
                        noti.add(sentence);
                        noti.add(value);

                        //2차원 리스트에 위 리스트를 넣습니다.
                        noti_arraylist.add(noti);
                    }
                    break ;
                case R.id.positiveButton :

                    break ;
                case R.id.nagativeButton :

                    break ;
            }


        }
    }
     */




}
