package com.example.nevi_menu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Login extends AppCompatActivity {
    private FirebaseAuth mFirebaseAuth; //파이어베이스 인증
    private DatabaseReference mDatabaseRef; //실시간 데이터베이스
    private EditText mEtEmail, mEtPwd; //로그인 입력필드

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); //xml이랑 연동

        mFirebaseAuth = FirebaseAuth.getInstance(); //파이어베이스 auth 사용준비
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("FirebaseRegister"); //리얼타임db 선언

        mEtEmail = findViewById(R.id.et_email); //login xml에서 id로 연결
        mEtPwd = findViewById(R.id.et_pwd);

        Button btn_login = findViewById(R.id.btn_login); //로그인 버튼
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //버튼 클릭했을 때
                //로그인 요청
                String strEmail = mEtEmail.getText().toString(); //로그인 입력필드의 값을 가져와서 문자열로 변환
                String strPwd = mEtPwd.getText().toString();

                mFirebaseAuth.signInWithEmailAndPassword(strEmail, strPwd).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            //로그인 성공
                            if(strEmail.equals("root@root.com")) {
                                Intent intent = new Intent(Login.this, AdminActivity.class); //관리자화면으로 이동
                                startActivity(intent); //로그인 처리 완료
                                finish(); //현재 액티비티 파괴
                            }
                            else{
                                Intent intent = new Intent(Login.this, MainActivity.class); //메인화면으로 이동
                                startActivity(intent); //로그인 처리 완료
                                finish(); //현재 액티비티 파괴
                            }
                        } else {
                        Toast.makeText(Login.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        Button btn_register = findViewById(R.id.btn_register); //회원가입 버튼
        btn_register.setOnClickListener(new View.OnClickListener() { //회원가입 버튼 클릭 시
            @Override
            public void onClick(View view) {
                //회원가입 화면으로 이동
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });
    }



}