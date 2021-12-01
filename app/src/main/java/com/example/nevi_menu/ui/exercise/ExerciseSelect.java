package com.example.nevi_menu.ui.exercise;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nevi_menu.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ExerciseSelect extends Fragment implements OnItemClick {

    private int exerciseArea;
    private String exerciseAreaName;
    private String dayName;

    public ExerciseSelect(int exerciseArea, String exerciseAreaName, String dayName) {
        this.exerciseArea = exerciseArea;
        this.exerciseAreaName = exerciseAreaName;
        this.dayName = dayName;
    }

    private RecyclerView recyclerView;
    private ExerciseSelectItemAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<ExerciseSelectItem> arrayList;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private int containerView;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(exerciseArea, container, false);

        recyclerView = view.findViewById(R.id.exercise_select);
        recyclerView.setHasFixedSize(true); // 리사이클러뷰 성능 강화
        layoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>(); // Exercise 객체를 담을 어레이 리스트(어뎁터 쪽으로 날림)
        containerView = container.getId();

        database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동
        databaseReference = database.getReference(exerciseAreaName); // DB 테이블 연결
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //파이어베이스 데이터베이스의 데이터를 받아오는 곳
                arrayList.clear(); // 기존 배열리스트가 존재하지않게 초기화
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ExerciseSelectItem exercise = snapshot.getValue(ExerciseSelectItem.class); // 만들어뒀던 Exercise 객체에 데이터를 담는다.
                    arrayList.add(exercise); // 담은 데이터들을 배열리스트에 넣고 리사이클러뷰로 보낼 준비
                }
                adapter.notifyDataSetChanged(); // 리스트 저장 및 새로고침
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 디비를 가져오던 중 에러 발생 시
                Log.e(exerciseAreaName, String.valueOf(databaseError.toException()));
            }
        });

        adapter = new ExerciseSelectItemAdapter(arrayList, recyclerView.getContext(), this);
        recyclerView.setAdapter(adapter);

        return view;
    }


    @Override
    public void onClick(String value) {
        Intent intent = new Intent(getActivity(), ExerciseScene.class);
        intent.putExtra("name",value);
        intent.putExtra("exerciseAreaName",exerciseAreaName);
        intent.putExtra("dayName",dayName);
        startActivity(intent);

    }
}
