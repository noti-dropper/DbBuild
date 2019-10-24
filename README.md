# App-DB
 > 데이터베이스를 다루고 관리하는 기능 개발



## Schema

 - **Notification**
    ```sqlite
    create table notification( 
        id integer primary key autoincrement, 
        title text, 
        msg text not null, 
        nlink text, 
        isread integer default 0, 
        pkgname text 
        );
    ```
 - **Noun**
   
    ```sqlite
    create table noun(
        id integer primary key autoincrement, 
        noun text not null unique, 
        weight integer not null default 0, 
        views integer not null default 0
        );
    ```



## API

 - public DBManager(SQLiteDatabase getDB) `constructor`
   
- 생성자
  
   
  
 - public ArrayList<NotiDataDTO> getNotificationList()
   
- `Notification` DB 리스트를 가져옴 
  
   
  
 - public ArrayList<NotiDataDTO> getNotificationList(String query)
   
- `Notification` DB 리스트를 가져옴 
   - 쿼리를 전달할 수 있음
   
   
   
 - public void insertNoun(String nounValue)
   
- `Noun` DB에 INSERT
  
   
  
 - public void updateNotiWeight(int notiId, boolean isPositive)
   
- `Noun` DB에 weight값을 `NOTIFICATION` DB를 통해 UPDATE
  
   - 검색 시 `id`를 활용
   
   

 - public void updateNotiWeight(String noun, boolean isPositive)
   
- `Noun` DB에 weight값을 `NOTIFICATION` DB를 통해 UPDATE
   
   - 검색 시 `noun`을 활용
   
   
   
 - public void updateNounWeight(String noun, boolean isPositive)
   
   - `Noun` DB에 weight값을 직접 UPDATE
   - 검색 시 `noun`을 활용

   
   
 - public void updateNounWeight(int nounId, boolean isPositive)
   
   - `Noun` DB에 weight값을 직접 UPDATE
   - 검색 시 `id`를 활용
   
   
   
 - public void updateNewNoti(String msg) throws JSONException

   - 새로운 알림이 왔을 때, `NOTIFICATION` DB와 `NOUN` DB에 업데이트




## 참고

### void updateNewNoti(String msg)

```pseudocode
// Algorithm Pseudo Code
for(String compare : input){

    compare를 DB 조회;
    if(있음){
        Notif DB에 nlink 정보 추가 (concat);
    } else {
        sample에 추가;
        Noun DB 에 명사 정보 추가
        Notif DB에 nlink 정보 추가 (concat);  /// noun의 weight 값은 뒤에서 처리해줌!!
    }
}

sample에 있는 내용을 유사도검사 돌림; (sample -> request_noun)
JSONObject로 옴;

for(sample 사이즈 만큼){ // sample과 JSONObj
    JSONObject.getString(sample[index]);
    if(유사도 값이 없을 경우 {"null", "null"}){
weight값 = 0
    }else{
weight값 계산
    }
}
```

