package com.example.nevi_menu.ui.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import com.example.nevi_menu.R; //폴더안에 소스 넣을 때 경로 알려주는 역할
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ChangeRecord extends AppCompatActivity {

    private Button mBtnModify;  //수정버튼
    private CalendarView calendarview; //날짜 선택 캘린더뷰
    private TextView mTvCurrentWeight, mTvTargetWeight, mTvHeight; //현재체중, 목표체중, 키 TextView
    private TextView mTvcwContent, mTvtwContent, mTvheightContent; //실제 데이터 표시
    private String dbdate; //디비에서 받아온 날짜 저장
    private DatabaseReference mDBRefBody; //BodyRecord 디비 참조
    private FirebaseAuth mFirebaseAuth; //디비 인증
    private FirebaseUser firebaseUser;
    private String date; //데이터를 기록할 날짜
    private ArrayList<BodyRecord> datelist = new ArrayList<>();
    private BodyRecord bodyrecord, tmprecord;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_record);

        mBtnModify = findViewById(R.id.btn_modifyRecord);
        calendarview = findViewById(R.id.cv_calendar);
        mTvCurrentWeight = findViewById(R.id.tv_currentWeight);
        mTvTargetWeight = findViewById(R.id.tv_targetWeight);
        mTvHeight = findViewById(R.id.tv_height);
        mTvcwContent = findViewById(R.id.tv_cwContent);
        mTvtwContent = findViewById(R.id.et_curpwd);
        mTvheightContent = findViewById(R.id.tv_heightContent);

        mFirebaseAuth = FirebaseAuth.getInstance(); //파이어베이스 인증 얻어오기
        firebaseUser = mFirebaseAuth.getCurrentUser(); //현재 유저 정보 가져오기

        //캘린더 오늘 날짜 받아오기
        Calendar cal = Calendar.getInstance(); //캘린더 객체 생성
        Date today = new Date(calendarview.getDate()); //캘린더뷰에서 날짜값 읽어오기
        cal.setTime(today); //캘린더 객체에 캘린더뷰 값을 넣음
        date = Integer.toString(cal.get(Calendar.YEAR))+ "-" + Integer.toString((cal.get(Calendar.MONTH))+1) + "-" +Integer.toString(cal.get(Calendar.DATE));

        //디비에서 데이터 가져오기 (addValueEventListener? addListenerForSingleValueEvent?)
        mDBRefBody = FirebaseDatabase.getInstance().getReference("BodyRecord").child(firebaseUser.getUid()); //리얼타임디비 path설정
        mDBRefBody.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //디비에서 데이터 가져오기
                    dbdate = snapshot.getKey();
                    bodyrecord = snapshot.getValue(BodyRecord.class);

                    //디비에서 받아온 신체기록 저장해서 리스트로 만들기
                    bodyrecord.setDate(dbdate);
                    datelist.add(bodyrecord);
                }
                //선택한 날짜에 존재하는 데이터를 찾아서 텍스트뷰로 뿌려주기
                for(int i=0; i<datelist.size(); i++) {
                    tmprecord = datelist.get(i);
                    if(date.equals(tmprecord.getDate())) {
                        mTvcwContent.setText(tmprecord.getCurrentWeight());
                        mTvtwContent.setText(tmprecord.getTargetWeight());
                        mTvheightContent.setText(tmprecord.getHeight());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("test", "loadPost:onCancelled", databaseError.toException()); //에러처리
            }
        });

        //캘린더 날짜 클릭했을 때
        calendarview.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendar, int year, int month, int day) {
                date = year+"-"+(month+1)+"-"+day; //클릭한 날짜 저장
                mTvcwContent.setText("");
                mTvtwContent.setText("");
                mTvheightContent.setText("");

                mDBRefBody.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            //디비에서 데이터 가져오기
                            dbdate = snapshot.getKey();
                            bodyrecord = snapshot.getValue(BodyRecord.class);

                            //디비에서 받아온 신체기록 저장
                            bodyrecord.setDate(dbdate);
                            datelist.add(bodyrecord);
                        }
                        //선택한 날짜에 존재하는 데이터를 텍스트뷰로 뿌려주기
                        for(int i=0; i<datelist.size(); i++) {
                            tmprecord = datelist.get(i);
                            if(date.equals(tmprecord.getDate())) {
                                mTvcwContent.setText(tmprecord.getCurrentWeight());
                                mTvtwContent.setText(tmprecord.getTargetWeight());
                                mTvheightContent.setText(tmprecord.getHeight());
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.w("test", "loadPost:onCancelled", databaseError.toException()); //에러처리
                    }
                });
            }
        });

        mBtnModify.setOnClickListener(new View.OnClickListener() { //수정버튼 눌렀을 때
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChangeRecord.this, ChangeRecordModify.class);
                startActivity(intent);
            }
        });
    }
}