package com.example.nevi_menu.ui.exercise;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nevi_menu.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Day extends Fragment implements OnItemClick,OnItemViewClick{

    private int day;
    private String dayName;

    public Day(int day,String dayName) {

        this.day = day;
        this.dayName = dayName;
    }

    private RecyclerView recyclerView;
    private ExerciseAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Exercise> arrayList;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference mDBRefUser;
    private Button btn_edit, btn_add, btn_complete;




    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(day, container, false);

        btn_add = view.findViewById(R.id.btn_add);
        btn_complete = view.findViewById(R.id.btn_complete);
        btn_edit = view.findViewById(R.id.btn_edit);
        recyclerView = view.findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true); // 리사이클러뷰 성능 강화
        layoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>(); // Exercise 객체를 담을 어레이 리스트(어뎁터 쪽으로 날림)


        firebaseAuth = FirebaseAuth.getInstance(); // 파이어베이스 인증 얻어오기
        firebaseUser = firebaseAuth.getCurrentUser(); // 현재 유저 정보 가져오기

        mDBRefUser = FirebaseDatabase.getInstance().getReference("UserAccount"); // DB 테이블 연결
        DatabaseReference mdatabaseRef = mDBRefUser.child(firebaseUser.getUid()).child("Exercise"+dayName);

        mdatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //파이어베이스 데이터베이스의 데이터를 받아오는 곳
                arrayList.clear(); // 기존 배열리스트가 존재하지않게 초기화
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Exercise exercise = snapshot.getValue(Exercise.class); // 만들어뒀던 Exercise 객체에 데이터를 담는다.
                    arrayList.add(exercise); // 담은 데이터들을 배열리스트에 넣고 리사이클러뷰로 보낼 준비
                }
                adapter.notifyDataSetChanged(); // 리스트 저장 및 새로고침
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 디비를 가져오던 중 에러 발생 시
                Log.e(dayName, String.valueOf(databaseError.toException()));
            }
        });

        adapter = new ExerciseAdapter(arrayList, recyclerView.getContext(),this,this);
        recyclerView.setAdapter(adapter);


        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.setVisibility();
                btn_edit.setVisibility(View.INVISIBLE);
                btn_add.setVisibility(View.VISIBLE);
                btn_complete.setVisibility(View.VISIBLE);
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_edit.setVisibility(View.VISIBLE);
                btn_add.setVisibility(View.INVISIBLE);
                btn_complete.setVisibility(View.INVISIBLE);


                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                ExerciseArea exerciseArea = new ExerciseArea(R.layout.fragment_exercise_area,dayName);//R.layout.fragment_exercise_area
                transaction.replace(container.getId(),exerciseArea);
                transaction.addToBackStack(null);
                transaction.commit();


            }
        });

        btn_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.setVisibility();
                btn_edit.setVisibility(View.VISIBLE);
                btn_add.setVisibility(View.INVISIBLE);
                btn_complete.setVisibility(View.INVISIBLE);
            }
        });

        return view;
    }


    @Override
    public void onClick(String value) {
        DatabaseReference dataRef =  mDBRefUser.child(firebaseUser.getUid()).child("Exercise"+dayName).child(value);
        dataRef.removeValue();
    }

    @Override
    public void onItemClick(String value) {
        Intent intent = new Intent(getActivity(), ExerciseVideo.class);
        intent.putExtra("name",value);
        startActivity(intent);
    }
}
