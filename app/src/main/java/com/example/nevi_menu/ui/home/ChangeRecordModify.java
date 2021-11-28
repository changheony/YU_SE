package com.example.nevi_menu.ui.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nevi_menu.R;
import com.example.nevi_menu.UserAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ChangeRecordModify extends AppCompatActivity {

    private Button mBtnSave;  //저장버튼
    private CalendarView calendarview; //날짜 선택 캘린더뷰
    private EditText mEtCurrentWeight, mEtTargetWeight, mEtHeight; //현재체중, 목표체중, 키 EditText
    private String currentWeight, targetWeight, height; //EditText의 값을 저장하기 위한 변수
    private DatabaseReference mDBRefUser, mDBRefBody; //디비 참조. UserAccount, BodyRecord
    private FirebaseAuth mFirebaseAuth; //디비 인증
    private FirebaseUser firebaseUser;
    private boolean blankflag, heightblank=false; //공백검사, 키 공백검사
    private HashMap<String, Object> upload = new HashMap<>(); //현재체중, 목표체중, 키 저장
    private String date; //데이터를 기록할 날짜
    private String lastdate; //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_record_modify);

        mBtnSave = findViewById(R.id.btn_saveRecord);
        mEtCurrentWeight = findViewById((R.id.tv_cwContent));
        mEtTargetWeight = findViewById((R.id.tv_twContent));
        mEtHeight = findViewById((R.id.et_height));
        calendarview = findViewById(R.id.cv_calendar);

        mFirebaseAuth = FirebaseAuth.getInstance(); //파이어베이스 인증 얻어오기
        firebaseUser = mFirebaseAuth.getCurrentUser(); //현재 유저 정보 가져오기

        mDBRefUser = FirebaseDatabase.getInstance().getReference("UserAccount"); //리얼타임디비 UserAccount 참조
        mDBRefBody = FirebaseDatabase.getInstance().getReference("BodyRecord"); //리얼타임디비 BodyRecord 참조

        //캘린더 오늘 날짜 받아오기
        Calendar cal = Calendar.getInstance(); //캘린더 객체 생성
        Date today = new Date(calendarview.getDate()); //캘린더뷰에서 날짜값 읽어오기
        cal.setTime(today); //캘린더 객체에 캘린더뷰 값을 넣음
        date = Integer.toString(cal.get(Calendar.YEAR))+ "-" + Integer.toString((cal.get(Calendar.MONTH))+1) + "-" +Integer.toString(cal.get(Calendar.DATE));

        //캘린더 날짜 클릭했을 때
        calendarview.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendar, int year, int month, int day) {
                date = year+"-"+(month+1)+"-"+day; //클릭한 날짜 저장
            }
        });

        //저장버튼 눌렀을 때
        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentWeight = mEtCurrentWeight.getText().toString();
                targetWeight = mEtTargetWeight.getText().toString();
                height = mEtHeight.getText().toString();

                //공백검사
                blankflag = checkBlank(); //통과못하면 false 반환
                if(!blankflag)
                    Toast.makeText(ChangeRecordModify.this, "체중을 입력해주세요", Toast.LENGTH_SHORT).show();
                else{
                    if(heightblank) { // 키 공백
                        //디비에서 키 가져오기
                        DatabaseReference mDBReference = mDBRefUser.child(firebaseUser.getUid()); //현재 로그인한 유저의 UserAccount 디비 경로
                        mDBReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String firemessage = dataSnapshot.getValue().toString();   //디비의 데이터를 문자열로 받기
                                String segments[] = firemessage.split(","); //각 key별로 자르기

                                int idx = segments[6].indexOf("="); //height
                                height = segments[6].substring(idx + 1); //키의 데이터만 뽑아서 저장
                                height = height.substring(0, height.length()-1); //마지막 문자 제거
                                int idx2 = segments[3].indexOf("="); //update
                                lastdate = segments[3].substring(idx2 + 1);

                                upload.put("currentWeight", currentWeight);
                                upload.put("targetWeight", targetWeight);
                                upload.put("height", height);

                                mDBRefBody.child(firebaseUser.getUid()).child(date).setValue(upload); //체중, 키가 저장된 맵을 BodyRecord 디비에 저장
                                Toast.makeText(ChangeRecordModify.this, "저장완료", Toast.LENGTH_SHORT).show();

                                //가장 최근의 기록으로 UserAccount 정보 갱신
                                boolean recent = checkRecent(); //마지막으로 유저정보가 업데이트 된 날짜 알아내기
                                System.out.println("최근인가?" + recent);
                                if(recent) {
                                    lastdate = date;
                                    int idx3 = segments[0].indexOf("="); //password
                                    String pwd = segments[0].substring(idx3+1);

                                    int idx4 = segments[4].indexOf("="); //email
                                    String email = segments[4].substring(idx4+1);

                                    int idx5 = segments[2].indexOf("="); //nickname
                                    String nickname = segments[2].substring(idx5+1);

                                    //데이터베이스 위에서 받아온걸로 다 저장해두기.
                                    UserAccount ua = new UserAccount();
                                    ua.setEmailId(email);
                                    ua.setPassword(pwd);
                                    ua.setNickname(nickname);
                                    ua.setHeight(height);
                                    ua.setCurrentWeight(currentWeight);
                                    ua.setTargetWeight(targetWeight);
                                    ua.setUpdate(lastdate);

                                    Map<String, Object> userinfo = ua.toMap();  //user account 정보를 map으로 만들기
                                    mDBRefUser.child(firebaseUser.getUid()).setValue(userinfo);  //db에 userAccount정보 업데이트
                                }
                                //이전페이지로 이동
                                Intent intent = new Intent(ChangeRecordModify.this, ChangeRecord.class);
                                startActivity(intent);
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                // Getting Post failed, log a message
                                Log.w("test", "loadPost:onCancelled", databaseError.toException()); //에러처리
                            }
                        });
                    }
                    else { // 키 공백 아니면
                        upload.put("currentWeight", currentWeight);
                        upload.put("targetWeight", targetWeight);
                        upload.put("height", height); //EditText에 입력한 키 저장

                        mDBRefBody.child(firebaseUser.getUid()).child(date).setValue(upload); //체중, 키가 저장된 맵을 디비에 저장
                        Toast.makeText(ChangeRecordModify.this, "저장완료", Toast.LENGTH_SHORT).show();

                        //가장 최근의 기록으로 UserAccount 정보 갱신
                        DatabaseReference mDBReference = mDBRefUser.child(firebaseUser.getUid()); //현재 로그인한 유저의 UserAccount 디비 경로
                        mDBReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String firemessage = dataSnapshot.getValue().toString();   //디비의 데이터를 문자열로 받기
                                String segments[] = firemessage.split(","); //각 key별로 자르기

                                int idx2 = segments[3].indexOf("="); //update
                                lastdate = segments[3].substring(idx2 + 1);

                                boolean recent = checkRecent(); //마지막으로 유저정보가 업데이트 된 날짜 알아내기
                                if(recent) { //가장 최근의 데이터를 업데이트할 경우
                                    lastdate = date;
                                    int idx3 = segments[0].indexOf("="); //password
                                    String pwd = segments[0].substring(idx3+1);

                                    int idx4 = segments[4].indexOf("="); //email
                                    String email = segments[4].substring(idx4+1);

                                    int idx5 = segments[2].indexOf("="); //nickname
                                    String nickname = segments[2].substring(idx5+1);

                                    //데이터베이스 정보 갱신
                                    UserAccount ua = new UserAccount();
                                    ua.setEmailId(email);
                                    ua.setPassword(pwd);
                                    ua.setNickname(nickname);
                                    ua.setHeight(height);
                                    ua.setCurrentWeight(currentWeight);
                                    ua.setTargetWeight(targetWeight);
                                    ua.setUpdate(lastdate);

                                    Map<String, Object> userinfo = ua.toMap();  //user account 정보를 map으로 만들기
                                    mDBRefUser.child(firebaseUser.getUid()).setValue(userinfo);  //db에 userAccount정보 업데이트
                                }
                                //이전페이지로 이동
                                Intent intent = new Intent(ChangeRecordModify.this, ChangeRecord.class);
                                startActivity(intent);
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                // Getting Post failed, log a message
                                Log.w("test", "loadPost:onCancelled", databaseError.toException()); //에러처리
                            }
                        });
                    }
                }
            }
        });
    }

    public boolean checkBlank() {
        //공백 입력 검사
        boolean res = true;
        if(currentWeight.replaceAll(" ", "").equals(""))
            res = false;
        else if(targetWeight.replaceAll(" ", "").equals(""))
            res = false;
        if(height.replaceAll(" ", "").equals("")) {
            heightblank = true; //키 공백
        }
        return res;
    }

    public boolean checkRecent() {
        //어떤 날짜가 더 최근인지 비교 (lastdate, date)
        int y1,m1,d1; //lastdate. 기존날짜
        int y2,m2,d2; //date. 이 페이지에서 정보 갱신한 날짜
        String tmplastdate[] = lastdate.split("-");
        String tmpdate[] = date.split("-");

        y1 = Integer.parseInt(tmplastdate[0]);
        System.out.println(y1);
        m1 = Integer.parseInt(tmplastdate[1]);
        System.out.println(m1);
        d1 = Integer.parseInt(tmplastdate[2]);
        System.out.println(d1);
        y2 = Integer.parseInt(tmpdate[0]);
        System.out.println(y2);
        m2 = Integer.parseInt(tmpdate[1]);
        System.out.println(m2);
        d2 = Integer.parseInt(tmpdate[2]);
        System.out.println(d2);

        if(y1<=y2) //년도 비교
            return true;
        else if(y1>y2)
            return false;
        else if(m1<=m2) //월 비교
            return true;
        else if(m1>m2)
            return false;
        else if(d1<=d2) //일 비교
            return true;
        else
            return false;
    }
}