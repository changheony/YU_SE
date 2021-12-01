package com.example.nevi_menu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class AdminExercise extends AppCompatActivity {

    Button chest;
    Button back;
    Button shoulder;
    Button arm;
    Button leg;
    Button abs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_exercise);

        chest = findViewById(R.id.btn_admin_chest);
        back = findViewById(R.id.btn_admin_back);
        shoulder = findViewById(R.id.btn_admin_shoulder);
        arm = findViewById(R.id.btn_admin_arm);
        leg = findViewById(R.id.btn_admin_leg);
        abs = findViewById(R.id.btn_admin_abs);

        chest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminExercise.this , AdminExerciseEdit.class);
                intent.putExtra("exerciseArea","Chest");
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminExercise.this , AdminExerciseEdit.class);
                intent.putExtra("exerciseArea","Back");
                startActivity(intent);
            }
        });

        shoulder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminExercise.this , AdminExerciseEdit.class);
                intent.putExtra("exerciseArea","Shoulder");
                startActivity(intent);
            }
        });

        arm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminExercise.this , AdminExerciseEdit.class);
                intent.putExtra("exerciseArea","Arm");
                startActivity(intent);
            }
        });

        leg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminExercise.this , AdminExerciseEdit.class);
                intent.putExtra("exerciseArea","Leg");
                startActivity(intent);
            }
        });

        abs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminExercise.this , AdminExerciseEdit.class);
                intent.putExtra("exerciseArea","Abs");
                startActivity(intent);
            }
        });
    }
}