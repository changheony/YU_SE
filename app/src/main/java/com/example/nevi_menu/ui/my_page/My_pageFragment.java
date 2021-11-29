package com.example.nevi_menu.ui.my_page;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.nevi_menu.Login;
import com.example.nevi_menu.UserAccount;
import com.example.nevi_menu.databinding.FragmentMyPageBinding;
import com.example.nevi_menu.ui.home.BodyRecord;
import com.example.nevi_menu.ui.home.ChangeRecord;
import com.example.nevi_menu.ui.home.ChangeRecordModify;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class My_pageFragment extends Fragment {

    private My_pageViewModel my_pageViewModel;
    private FragmentMyPageBinding binding;
    private DatabaseReference mDBReference, mDBRefBody; //UserAccount, BodyRecord 디비 참조
    private FirebaseAuth mFirebaseAuth; //디비 인증
    private FirebaseUser firebaseUser;
    private UserAccount useraccount;
    private BodyRecord bodyrecord;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        my_pageViewModel =
                new ViewModelProvider(this).get(My_pageViewModel.class);

        binding = FragmentMyPageBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mFirebaseAuth = FirebaseAuth.getInstance(); //파이어베이스 인증 얻어오기
        firebaseUser = mFirebaseAuth.getCurrentUser(); //현재 유저 정보 가져오기

        //디비에서 데이터 가져오기
        mDBReference = FirebaseDatabase.getInstance().getReference("UserAccount").child(firebaseUser.getUid()); //리얼타임디비 path설정
        mDBRefBody = FirebaseDatabase.getInstance().getReference("BodyRecord").child(firebaseUser.getUid());

        mDBReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                useraccount = dataSnapshot.getValue(UserAccount.class);
                //데이터를 텍스트뷰로 뿌려주기
                binding.tvEmailcontent.setText(useraccount.getEmailId());
                binding.tvNncontent.setText(useraccount.getNickname());
                binding.tvCwcontent.setText(useraccount.getCurrentWeight());
                binding.tvTwcontent.setText(useraccount.getTargetWeight());
                binding.tvHeightcontent.setText(useraccount.getHeight());

                //회원탈퇴
                binding.btnDeleteuser.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder ad = new AlertDialog.Builder(getActivity());
                        ad.setTitle("회원탈퇴");
                        ad.setMessage("회원탈퇴를 원하시면 비밀번호를 입력하세요\n모든 데이터는 삭제되며 복구할 수 없습니다.");

                        final EditText et = new EditText(getActivity());
                        ad.setView(et); //다이어로그안에 뷰로 EditText 객체 추가
                        et.setInputType(InputType.TYPE_CLASS_TEXT | InputType. TYPE_TEXT_VARIATION_PASSWORD); //inputtype=textpassword

                        ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String strpwd = et.getText().toString();
                                if(strpwd.equals(useraccount.getPassword())) {
                                    mDBRefBody.addValueEventListener(new ValueEventListener() { //BodyRecord 디비 가져오기
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            bodyrecord = dataSnapshot.getValue(BodyRecord.class);
                                            if(bodyrecord != null) { //체중기록 안한 데이터 null인거 가져오면 에러나는듯
                                                mDBRefBody.setValue(null);
                                                mDBReference.setValue(null); //UserAccount 리얼타임디비 데이터 삭제
                                                mFirebaseAuth.getCurrentUser().delete(); //Auth 정보 삭제
                                                dialogInterface.dismiss(); //현재 다이얼로그 닫기
                                                Intent intent = new Intent(getActivity(), Login.class);
                                                startActivity(intent); //로그인으로 이동
                                                getActivity().finish();
                                            }
                                            else {
                                                mDBReference.setValue(null); //UserAccount 리얼타임디비 데이터 삭제
                                                mFirebaseAuth.getCurrentUser().delete(); //Auth 정보 삭제
                                                dialogInterface.dismiss(); //현재 다이얼로그 닫기
                                                Intent intent = new Intent(getActivity(), Login.class);
                                                startActivity(intent);
                                                getActivity().finish();
                                            }
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                            Log.w("test", "loadPost:onCancelled", databaseError.toException()); //에러처리
                                        }
                                    });
                                }
                                else {
                                    dialogInterface.dismiss(); //현재 다이얼로그 닫기
                                    Toast.makeText(getActivity(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        ad.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss(); //현재 다이얼로그 닫기
                            }
                        });
                        ad.show();
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("test", "loadPost:onCancelled", databaseError.toException()); //에러처리
            }
        });

        binding.btnChangepwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //비밀번호 변경버튼을 눌렀을 때
                Intent intent = new Intent(getActivity(), ChangePwd.class);
                startActivity(intent);
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}