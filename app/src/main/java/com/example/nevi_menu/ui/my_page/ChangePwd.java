package com.example.nevi_menu.ui.my_page;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.nevi_menu.R;
import com.example.nevi_menu.UserAccount;
import com.example.nevi_menu.databinding.ActivityChangePwdBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.view.Change;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class ChangePwd extends AppCompatActivity {

    private String strCurpwd, strNewpwd, strNewpwdcheck, dbCurpwd;
    private DatabaseReference mDBReference; //UserAccount 디비 참조
    private FirebaseAuth mFirebaseAuth; //디비 인증
    private FirebaseUser firebaseUser;
    private UserAccount useraccount;
    private boolean checkCurpwd, checkNewpwd, checkPwdLength=true;

    private ActivityChangePwdBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pwd);

        mBinding = ActivityChangePwdBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);

        mBinding.btnChangepwdDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //변경완료 버튼을 누를 시
                strCurpwd = mBinding.etCurpwd.getText().toString();
                strNewpwd = mBinding.etNewpwd.getText().toString();
                strNewpwdcheck = mBinding.etNewpwdcheck.getText().toString();

                //공백검사
                boolean blankflag = checkBlank(); //통과못하면 false 반환
                if (!blankflag)
                    Toast.makeText(ChangePwd.this, "정보를 모두 입력해주세요", Toast.LENGTH_SHORT).show();
                else {
                    mFirebaseAuth = FirebaseAuth.getInstance(); //파이어베이스 인증 얻어오기
                    firebaseUser = mFirebaseAuth.getCurrentUser(); //현재 유저 정보 가져오기

                    //디비에서 데이터 가져오기
                    mDBReference = FirebaseDatabase.getInstance().getReference("UserAccount").child(firebaseUser.getUid()); //리얼타임디비 path설정
                    mDBReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            useraccount = dataSnapshot.getValue(UserAccount.class);
                            dbCurpwd = useraccount.getPassword();

                            if (strCurpwd.equals(dbCurpwd)) //현재 비밀번호가 디비에 입력된 정보와 동일한지 확인
                                checkCurpwd = true;
                            if (strNewpwd.equals(strNewpwdcheck)) //새 비밀번호와 새 비밀번호 확인이 동일한지 확인
                                checkNewpwd = true;
                            if (strNewpwd.length() < 6 || strNewpwdcheck.length() < 6) //새 비밀번호와 새 비밀번호 확인이 6글자 이상인지 확인
                                checkPwdLength = false;

                            if (checkCurpwd && checkNewpwd && checkPwdLength) { //입력값이 유효할 경우
                               //디비 갱신
                                useraccount.setPassword(strNewpwd);
                                mDBReference.setValue(useraccount); //비밀번호가 변경된 UserAccount 정보를 디비에 업로드
                                //Auth 정보 변경
                                firebaseUser.updatePassword(strNewpwd).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            //toast 출력해주고 마이페이지로 화면 이동
                                            Toast.makeText(ChangePwd.this, "비밀번호 변경 완료", Toast.LENGTH_SHORT).show();
                                            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                                            ft.replace(R.id.changepwd_frame, new My_pageFragment());
                                            ft.commit();
                                            finish(); //이거안하면 화면 겹침현상 생김
                                        }
                                        else{
                                            Toast.makeText(ChangePwd.this, "비밀번호 변경 실패", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } else { // 유효하지 않은 입력값 처리
                                if (!checkCurpwd)
                                    Toast.makeText(ChangePwd.this, "현재 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                                if (!checkNewpwd)
                                    Toast.makeText(ChangePwd.this, "새 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                                if (!checkPwdLength)
                                    Toast.makeText(ChangePwd.this, "비밀번호는 최소 6자 이상 입력해야합니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.w("test", "loadPost:onCancelled", databaseError.toException()); //에러처리
                        }
                    });
                }
            }
        });
    }

    public boolean checkBlank() {
        //공백 입력 검사
        if(strCurpwd.replaceAll(" ", "").equals(""))
            return false;
        else if(strNewpwd.replaceAll(" ", "").equals(""))
            return false;
        else if(strNewpwdcheck.replaceAll(" ", "").equals("")) {
            return false;
        }
        return true;
    }
}