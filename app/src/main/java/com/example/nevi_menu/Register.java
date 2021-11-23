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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;  //파이어베이스 인증
    private DatabaseReference mDatabaseRef, tmpDatabaseRef;  //실시간 데이터베이스
    private EditText mEtEmail, mEtPwd, mEtPwdCheck, mEtNickname, mEtHeight, mEtCurweight, mEtTarweight;  //회원가입 입력필드
    private Button mBtnEmail, mBtnNickname;  //이메일, 닉네임 중복체크 버튼
    private Button mBtnRegister;  //회원가입 버튼
    private String strEmail, strPwd, strNickname, strHeight, strCurweight, strTarweight; //입력필드의 내용을 String으로 변환할 때 사용하는 변수
    private Boolean lengthFlag=Boolean.TRUE, overlapFlag, emailformFlag=Boolean.TRUE; //길이체크, 중복체크, 이메일 포맷체크 플래그. 지금은 테스트 한다고 true 넣어둠

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFirebaseAuth = FirebaseAuth.getInstance(); //파이어베이스 인증 얻어오기
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("UserAccount"); //파이어베이스 실시간 디비 설정. path 아래에 데이터 들어가게됨

        connectId();  //xml의 id와 위에 선언한 EditText or Button과 연결

        //이메일 중복체크 버튼을 클릭했을 때 db에서 데이터 받아오기
//        mBtnEmail.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });

        //가입 버튼 클릭했을 때
        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //회원가입 처리 시작
                etToString();  //입력한 회원가입정보를 문자열로 변환해서 저장

                //글자수체크
                lengthFlag = checkOverlap();
                //이메일 포맷 체크
                emailformFlag = checkEmail();

                //Firebase Auth 진행
                mFirebaseAuth.createUserWithEmailAndPassword(strEmail, strPwd).addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //인증처리 완료되었을 때 (회원가입 성공) && 글자수 체크 통과 && 중복 체크 통과
                        if(task.isSuccessful() && lengthFlag == Boolean.TRUE && emailformFlag == Boolean.TRUE) {
                            FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();  //현재 회원가입 된 유저 가져오기
                            UserAccount account = new UserAccount(strEmail, strPwd, strNickname, strHeight, strCurweight, strTarweight); //user account 생성

                            account.setIdToken(firebaseUser.getUid());  //토큰 가져오기
                            Map<String, Object> userinfo = account.toMap();  //user account 정보를 map으로 만들기
                            mDatabaseRef.child(firebaseUser.getUid()).setValue(userinfo);  //db에 정보 insert. child 적는대로 트리 생성됨

                            tmpDatabaseRef = FirebaseDatabase.getInstance().getReference("EmailList");
                            tmpDatabaseRef.child("email").setValue(strEmail);  //디비에 이메일 리스트 저장

                            tmpDatabaseRef = FirebaseDatabase.getInstance().getReference("NicknameList");
                            tmpDatabaseRef.child("nickname").setValue(strNickname);  //디비에 닉네임 리스트 저장

                            Toast.makeText(Register.this, "회원가입을 완료하였습니다.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Register.this, Login.class);  //로그인페이지로 이동
                            startActivity(intent);
                            finish();//회원가입 돌아가지 않게하기
                        } else { // 회원가입 실패. 여기서도 경우별로 Toast 메시지 나누자
                            if(!lengthFlag)
                                Toast.makeText(Register.this, "정보는 1~30자 이내로 입력해주세요.", Toast.LENGTH_SHORT).show();
                            if(!emailformFlag)
                                Toast.makeText(Register.this, "이메일 형식을 올바로 입력해주세요.", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(Register.this, "회원가입을 실패하였습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
    public void connectId() {
        //xml의 id와 클래스의 EditText나 Button과 연결
        mEtEmail = findViewById(R.id.et_email);
        mEtPwd = findViewById(R.id.et_pwd);
        mEtPwdCheck = findViewById(R.id.et_pwdcheck);
        mEtNickname = findViewById(R.id.et_nickname);
        mEtHeight = findViewById(R.id.et_height);
        mEtCurweight = findViewById(R.id.et_curweight);
        mEtTarweight = findViewById(R.id.et_tarweight);
        mBtnEmail = findViewById(R.id.btn_email);
        mBtnNickname = findViewById(R.id.btn_nickname);
        mBtnRegister = findViewById(R.id.btn_register);
    }

    public void etToString() {
        //입력필드의 값을 String으로 바꾸기
        strEmail = mEtEmail.getText().toString();
        strPwd = mEtPwd.getText().toString();
        strNickname = mEtNickname.getText().toString();
        strHeight = mEtHeight.getText().toString();
        strCurweight = mEtCurweight.getText().toString();
        strTarweight = mEtTarweight.getText().toString();
    }

    public Boolean checkOverlap() {
        //회원가입 시 입력한 값이 1~30자 이내인지 검사. 공백x
        if(strEmail.length()>30 || strEmail.length()==0)
            return Boolean.FALSE;
        else if(strPwd.length()>30 || strEmail.length()==0)
            return Boolean.FALSE;
        else if(strNickname.length()>30 || strEmail.length()==0)
            return Boolean.FALSE;
        else if(strHeight.length()>30 || strEmail.length()==0)
            return Boolean.FALSE;
        else if(strCurweight.length()>30 || strEmail.length()==0)
            return Boolean.FALSE;
        else if(strTarweight.length()>30 || strEmail.length()==0)
            return Boolean.FALSE;

        return Boolean.TRUE;
    }

    public Boolean checkEmail() {
        //이메일 포맷 체크
        String regex = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(strEmail);
        boolean result = m.matches();
        return result;
    }
}