package com.example.nevi_menu;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nevi_menu.ui.exercise.OnItemClick;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminExerciseEdit extends AppCompatActivity implements OnItemClick {

    String exerciseArea;
    private RecyclerView recyclerView;
    private AdminExerciseAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<AdminExerciseItem> arrayList;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    private Button admin_exercise_add_btn;
    private Button admin_exercise_complete_btn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_exercise_edit);

        Intent intent = getIntent();
        exerciseArea =  intent.getStringExtra("exerciseArea");

        admin_exercise_add_btn = findViewById(R.id.admin_exercise_add_btn);
        admin_exercise_complete_btn = findViewById(R.id.admin_exercise_complete_btn);

        recyclerView = findViewById(R.id.admin_exercise_edit);
        recyclerView.setHasFixedSize(true); // 리사이클러뷰 성능 강화
        layoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>(); // Exercise 객체를 담을 어레이 리스트(어뎁터 쪽으로 날림)

        database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동
        databaseReference = database.getReference(exerciseArea); // DB 테이블 연결
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //파이어베이스 데이터베이스의 데이터를 받아오는 곳
                arrayList.clear(); // 기존 배열리스트가 존재하지않게 초기화
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    AdminExerciseItem exercise = snapshot.getValue(AdminExerciseItem.class); // 만들어뒀던 Exercise 객체에 데이터를 담는다.
                    arrayList.add(exercise); // 담은 데이터들을 배열리스트에 넣고 리사이클러뷰로 보낼 준비
                }
                adapter.notifyDataSetChanged(); // 리스트 저장 및 새로고침
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 디비를 가져오던 중 에러 발생 시
                Log.e(exerciseArea, String.valueOf(databaseError.toException()));
            }
        });

        adapter = new AdminExerciseAdapter(arrayList,this);
        recyclerView.setAdapter(adapter);

        admin_exercise_add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminExerciseEdit.this , AdminExerciseAdd.class);
                intent.putExtra("exerciseArea",exerciseArea);
                startActivity(intent);
            }
        });

        admin_exercise_complete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminExerciseEdit.this , AdminActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(String value) {
        DatabaseReference dataRef =  databaseReference.child(value);
        dataRef.removeValue();
    }
}
