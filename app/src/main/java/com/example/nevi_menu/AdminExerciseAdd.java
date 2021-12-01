package com.example.nevi_menu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminExerciseAdd extends AppCompatActivity {

    String exerciseArea;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    EditText admin_exerciseAdd_name;
    EditText admin_exerciseAdd_uri;
    EditText admin_exerciseAdd_description;
    EditText admin_exerciseAdd_val;
    RatingBar star;
    Button admin_exerciseAdd_add_btn;
    Button admin_exerciseAdd_cancel_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_exercise_add);

        Intent intent = getIntent();
        exerciseArea =  intent.getStringExtra("exerciseArea");

        admin_exerciseAdd_name = findViewById(R.id.admin_exerciseAdd_name);
        admin_exerciseAdd_uri = findViewById(R.id.admin_exerciseAdd_uri);
        admin_exerciseAdd_description = findViewById(R.id.admin_exerciseAdd_description);
        admin_exerciseAdd_val = findViewById(R.id.admin_exerciseAdd_val);
        star = findViewById(R.id.admin_exerciseAdd_star);
        admin_exerciseAdd_add_btn = findViewById(R.id.admin_exerciseAdd_add_btn);
        admin_exerciseAdd_cancel_btn = findViewById(R.id.admin_exerciseAdd_cancel_btn);

        admin_exerciseAdd_add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(admin_exerciseAdd_name.getText().toString().isEmpty() || admin_exerciseAdd_name.getText().toString().equals("운동 이름")){
                    Toast.makeText(getApplicationContext(),"운동 이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }else if(admin_exerciseAdd_uri.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"uri 입력해주세요.", Toast.LENGTH_SHORT).show();
                }else if(admin_exerciseAdd_val.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"횟수를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }else{
                    AdminExerciseItem exercise = new AdminExerciseItem();
                    exercise.setIv_profile("profile");
                    exercise.setTv_name(admin_exerciseAdd_name.getText().toString());
                    exercise.setUri(admin_exerciseAdd_uri.getText().toString());
                    exercise.setDescription(admin_exerciseAdd_description.getText().toString());
                    exercise.setStar(Float.toString(star.getRating()));
                    exercise.setTv_time(admin_exerciseAdd_val.getText().toString());

                    database = FirebaseDatabase.getInstance();
                    databaseReference = database.getReference(exerciseArea);
                    databaseReference.child(exercise.getTv_name()).setValue(exercise);

                    Intent intent = new Intent(AdminExerciseAdd.this , AdminActivity.class);
                    startActivity(intent);
                }
            }
        });

        admin_exerciseAdd_cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminExerciseAdd.this , AdminActivity.class);
                startActivity(intent);
            }
        });


    }
}
