package com.example.nevi_menu;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity {
    //회원가입 클래스
    private FirebaseAuth mFirebaseAuth;  //파이어베이스 인증
    private DatabaseReference mDatabaseRef, mDBReference;  //실시간 데이터베이스
    private EditText mEtEmail, mEtPwd, mEtPwdCheck, mEtNickname, mEtHeight, mEtCurweight, mEtTarweight;  //회원가입 입력필드
    private Button mBtnEmail, mBtnNickname;  //이메일, 닉네임 중복체크 버튼
    private Button mBtnRegister, mBtnPrev;  //회원가입 버튼, 이전버튼
    private String strEmail, strPwd, strPwdCheck, strNickname, strHeight, strCurweight, strTarweight, strUpdate; //입력필드의 내용을 String으로 변환할 때 사용하는 변수. update는 제외
    private Boolean lengthFlag, emailoverlapFlag, nnoverlapFlag, emailformFlag, nicknameformFlag, pwdCheckFlag; //길이체크, 이메일/닉네임 중복체크, 이메일 포맷체크, 닉네임 포맷체크, 비밀번호 확인 플래그
    private final ArrayList<String> emailList = new ArrayList<>(); //디비에서 이메일 저장
    private final ArrayList<String> nicknameList = new ArrayList<>(); //디비에서 닉네임 저장
    private boolean chk = false; //플래그들 검사
    private UserAccount ua;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFirebaseAuth = FirebaseAuth.getInstance(); //파이어베이스 인증 얻어오기
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("UserAccount"); //파이어베이스 실시간 디비 설정. path 아래에 데이터 들어가게됨

        connectId();  //xml의 id와 위에 선언한 EditText or Button과 연결

        mBtnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register.this, Login.class);  //로그인페이지로 이동
                startActivity(intent);
            }
        });

        //이메일 중복체크 버튼을 클릭했을 때 db에서 데이터 받아와서 비교
        mBtnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strEmail = mEtEmail.getText().toString();
                //파이어베이스에서 UserAccount의 데이터 읽어오기
                mDBReference = FirebaseDatabase.getInstance().getReference().child("UserAccount");
                mDBReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            ua = snapshot.getValue(UserAccount.class);
                            String dbemail = ua.getEmailId();
                            emailList.add(dbemail); //리스트에 이메일 추가
                            System.out.println("dbemail: " + dbemail);
                        }
                        emailoverlapFlag = emailList.contains(strEmail); //중복되면 true

                        //이메일 포맷 체크
                        emailformFlag = checkEmail();

                        if (emailoverlapFlag==Boolean.TRUE || emailformFlag==Boolean.FALSE) //이메일 중복이거나 포맷체크 통과 못하면
                            Toast.makeText(Register.this, "사용할 수 없는 이메일입니다.", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(Register.this, "사용할 수 있는 이메일입니다.", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Getting Post failed, log a message
                        Log.w("test", "loadPost:onCancelled", databaseError.toException()); //에러처리
                    }
                });
            }
        });

        //닉네임 중복체크 버튼을 클릭했을 때
        mBtnNickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strNickname = mEtNickname.getText().toString();
                //파이어베이스에서 UserAccount의 데이터 읽어오기
                mDBReference = FirebaseDatabase.getInstance().getReference().child("UserAccount");
                mDBReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            ua = snapshot.getValue(UserAccount.class);
                            String dbnickname = ua.getNickname();
                            System.out.println("dbnickname: " + dbnickname);
                            nicknameList.add(dbnickname); //리스트에 닉네임 추가
                        }
                        nnoverlapFlag = nicknameList.contains(strNickname); //중복되면 true

                        //닉네임 포맷 체크
                        nicknameformFlag = checkNickname();
                        if (nnoverlapFlag == Boolean.TRUE || nicknameformFlag == Boolean.FALSE) //닉네임이 중복되거나 포맷체크 통과못하면
                            Toast.makeText(Register.this, "사용할 수 없는 닉네임입니다.", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(Register.this, "사용할 수 있는 닉네임입니다.", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Getting Post failed, log a message
                        Log.w("test", "loadPost:onCancelled", databaseError.toException()); //에러처리
                    }
                });
            }
        });

        //가입 버튼 클릭했을 때
        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //회원가입 처리 시작
                etToString();  //입력한 회원가입정보를 문자열로 변환해서 저장

                //입력값 포맷체크(공백 및 특수문자)
                lengthFlag = checkFormat();

                //비밀번호, 비밀번호 확인 일치 여부 체크
                pwdCheckFlag = checkPwd();

                //회원가입 조건 충족하였다면
                if(lengthFlag == Boolean.TRUE && emailformFlag == Boolean.TRUE &&
                        pwdCheckFlag==Boolean.TRUE && emailoverlapFlag==Boolean.FALSE && nnoverlapFlag == Boolean.FALSE) {
                    //Firebase Auth에 계정 생성됨
                    mFirebaseAuth.createUserWithEmailAndPassword(strEmail, strPwd).addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //회원가입 성공
                            if(task.isSuccessful()) {
                                FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();  //현재 회원가입 된 유저 가져오기
                                SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd"); //오늘 날짜 구하기
                                Date time = new Date();
                                strUpdate = form.format(time);
                                UserAccount account = new UserAccount(strEmail, strPwd, strNickname, strHeight, strCurweight, strTarweight, strUpdate); //user account 생성

                                account.setIdToken(firebaseUser.getUid());  //토큰 가져오기
                                Map<String, Object> userinfo = account.toMap();  //user account 정보를 map으로 만들기
                                mDatabaseRef.child(firebaseUser.getUid()).setValue(userinfo);  //db에 정보 insert. child 적는대로 트리 생성됨

                                Toast.makeText(Register.this, "회원가입을 완료하였습니다.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Register.this, Login.class);  //로그인페이지로 이동
                                startActivity(intent);
                            } else { // 회원가입 실패
                                    Toast.makeText(Register.this, "회원가입을 실패하였습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else { //회원가입 조건 충족 못했다면
                    if(!lengthFlag)
                        Toast.makeText(Register.this, "공백이나 특수문자는 허용되지 않습니다.", Toast.LENGTH_SHORT).show();
                    if(!emailformFlag)
                        Toast.makeText(Register.this, "이메일 형식을 올바로 입력해주세요.", Toast.LENGTH_SHORT).show();
                    if(emailoverlapFlag)
                        Toast.makeText(Register.this, "이메일 중복체크를 완료해주세요.", Toast.LENGTH_SHORT).show();
                    if(nnoverlapFlag)
                        Toast.makeText(Register.this, "닉네임 중복체크를 완료해주세요.", Toast.LENGTH_SHORT).show();
                    if(!pwdCheckFlag)
                        Toast.makeText(Register.this, "비밀번호가 일치하지않습니다.", Toast.LENGTH_SHORT).show();
                    if(strPwd.length()<6)
                        Toast.makeText(Register.this, "비밀번호는 최소 6자 이상 입력해야합니다.", Toast.LENGTH_SHORT).show();
                }
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
        strPwdCheck = mEtPwdCheck.getText().toString();
        strNickname = mEtNickname.getText().toString();
        strHeight = mEtHeight.getText().toString();
        strCurweight = mEtCurweight.getText().toString();
        strTarweight = mEtTarweight.getText().toString();
    }

    public Boolean checkFormat() {
        //입력값에 공백이나 특수문자 있는지 체크
        String[] userinfo = new String[] {strPwd, strPwdCheck, strHeight, strCurweight, strTarweight};
        String pattern = "^[0-9|a-z|A-Z|ㄱ-ㅎ|ㅏ-ㅣ|가-힣|]*$"; //특수문자 체크
        for(int i=0; i<userinfo.length; i++) {
            if(userinfo[i].replaceAll(" ", "").equals("")) //공백 입력
                return Boolean.FALSE;
            else if(!Pattern.matches(pattern, userinfo[i])) //공백 혹은 특수문자 입력
                return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    public Boolean checkEmail() {
        //이메일 포맷 체크
        String regex = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
        boolean blank=true;
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(strEmail);
        boolean result = m.matches();
        if(strEmail.replaceAll(" ", "").equals("")) //공백만 입력
            blank = false;
        return blank & result; //둘 중 하나라도 통과못하면 false
    }

    public Boolean checkNickname() {
        //닉네임 포맷 체크
        String pattern = "^[0-9|a-z|A-Z|ㄱ-ㅎ|ㅏ-ㅣ|가-힣|]*$"; //특수문자 체크
        boolean blank=true;
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(strNickname);
        boolean result = m.matches();
        if(strNickname.replaceAll(" ", "").equals("")) //공백 입력
            blank = false;
        return blank & result; //공백검사, 특수문자 체크 둘 중 하나라도 통과못하면 false
    }

    public Boolean checkPwd() {
        //비밀번호와 비밀번호 확인에 입력한 데이터가 일치한지 비교
        if(strPwd.equals(strPwdCheck)) //일치하면
            return Boolean.TRUE;
        else
            return Boolean.FALSE;
    }
}