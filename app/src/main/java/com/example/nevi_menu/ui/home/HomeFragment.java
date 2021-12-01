package com.example.nevi_menu.ui.home;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.nevi_menu.R;
import com.example.nevi_menu.databinding.FragmentHomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import im.dacer.androidcharts.LineView;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private String dbdate; //디비에서 받아온 날짜 저장
    private DatabaseReference mDBRefBody; //BodyRecord 디비 참조
    private FirebaseAuth mFirebaseAuth; //디비 인증
    private FirebaseUser firebaseUser;
    private ArrayList<BodyRecord> item = new ArrayList<>(); //일자별 신체기록. 옛날 날짜가 인덱스 작음
    private ArrayList<Integer> datalistCur = new ArrayList<>(); //현재 체중 리스트
    private ArrayList<Integer> datalistTar = new ArrayList<>(); //목표 체중 리스트
    private ArrayList<String> xAxis = new ArrayList<>(); //x축 데이터 저장
    private ArrayList<ArrayList<Integer>> datalists = new ArrayList<>(); //데이터리스트들을 합치는 리스트?
    private BodyRecord bodyrecord;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.checkbodybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //신체변화기록으로 이동
                Intent intent = new Intent(getActivity(), ChangeRecord.class);
                startActivity(intent);
            }
        });

        //그래프 작업
        setxAxis(); //그래프의 x축 데이터 셋팅
        final LineView lineView = (LineView) root.findViewById(R.id.line_view);

        mFirebaseAuth = FirebaseAuth.getInstance(); //파이어베이스 인증 얻어오기
        firebaseUser = mFirebaseAuth.getCurrentUser(); //현재 유저 정보 가져오기

        //디비에서 데이터 가져오기
        mDBRefBody = FirebaseDatabase.getInstance().getReference("BodyRecord").child(firebaseUser.getUid()); //리얼타임디비 path설정
        mDBRefBody.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //디비에서 데이터 가져오기
                    dbdate = snapshot.getKey();
                    bodyrecord = snapshot.getValue(BodyRecord.class);

                    bodyrecord.setDate(dbdate);
                    item.add(bodyrecord);
                }

                //디비에서 받아온 신체기록 있을 때 그래프 그리기
                if (bodyrecord != null) {
                    setxAxis(); //x축 셋팅
                    setweight(); //체중 그래프에 사용할 데이터 리스트 셋팅
                    datalists.add(datalistCur); //체중리스트들을 하나의 리스트로 합치기
                    datalists.add(datalistTar);

                    //그래프 그리기
                    lineView.setDrawDotLine(true); //보조선
                    lineView.setShowPopup(LineView.SHOW_POPUPS_NONE); //각 점마다 숫자 안뜨게 설정. 클릭하면 나옴
                    lineView.setColorArray(new int[]{
                            Color.parseColor("#000000"), Color.parseColor("#2980b9") //블랙, 파랑
                    });
                    lineView.setBottomTextList(xAxis); //가로축 레이블
                    lineView.setDataList(datalists); //그래프 데이터
                }
                //데이터 없어서 그래프 안그려짐. 범례 숨기고 텍스트 띄우기
                else {
                    binding.legend1.setVisibility(View.GONE);
                    binding.legend2.setVisibility(View.GONE);
                    binding.nomessage.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("test", "loadPost:onCancelled", databaseError.toException()); //에러처리
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    void setxAxis() { //datalist 만큼만 x축 셋팅
        String tmp;
        for (int i = 0; i < item.size(); i++) {
            tmp = item.get(i).getDate();
            tmp = tmp.substring(5);
            xAxis.add(tmp);
        }
    }

    void setweight() { // 체중 그래프에 사용할 데이터 리스트 셋팅
        int tmp1, tmp2;
        for (int i = 0; i < item.size(); i++) {
            tmp1 = Integer.parseInt(item.get(i).getCurrentWeight());
            datalistCur.add(tmp1);
            tmp2 = Integer.parseInt(item.get(i).getTargetWeight());
            datalistTar.add(tmp2);
        }
    }

}