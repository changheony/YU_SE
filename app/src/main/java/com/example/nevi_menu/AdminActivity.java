package com.example.nevi_menu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AdminActivity extends AppCompatActivity {

    private Button btn_mvad_shopping;
    private Button btn_mvad_food;
    private Button btn_mvad_exercise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);


        btn_mvad_food = findViewById(R.id.btn_mvad_food); //식단관리자 이동버튼 선언
        btn_mvad_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminActivity.this , AdminFood.class); //쇼핑몰 관리자 화면으로 변경
                startActivity(intent); //객체이동
            }
        });

        btn_mvad_exercise = findViewById(R.id.btn_mvad_exercise); //운동관리자 이동버튼 선언
        btn_mvad_exercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminActivity.this , AdminExercise.class); //쇼핑몰 관리자 화면으로 변경
                startActivity(intent); //객체이동
            }
        });

        btn_mvad_shopping = findViewById(R.id.btn_mvad_shopping); //쇼핑관리자 이동버튼 선언
        btn_mvad_shopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminActivity.this , AdminShopping.class); //쇼핑몰 관리자 화면으로 변경
                startActivity(intent); //객체이동
            }
        });
    }
}