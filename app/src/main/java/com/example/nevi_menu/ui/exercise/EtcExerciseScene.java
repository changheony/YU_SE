package com.example.nevi_menu.ui.exercise;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.nevi_menu.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EtcExerciseScene extends Fragment {

    int layout;
    String dayName;

    public EtcExerciseScene(int layout,String dayName){
        this.layout = layout;
        this.dayName = dayName;
    }

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference mDBRefUser;

    EditText etcaddscene_name_text;
    EditText etcaddscene_num_val;
    Button etcaddscene_complete_btn;
    Button etcaddscene_cancel_btn;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(layout, container, false);

        etcaddscene_name_text = view.findViewById(R.id.etcaddscene_name_text);
        etcaddscene_num_val = view.findViewById(R.id.etcaddscene_num_val);
        etcaddscene_complete_btn = view.findViewById(R.id.etcaddscene_complete_btn);
        etcaddscene_cancel_btn = view.findViewById(R.id.etcaddscene_cancel_btn);


        firebaseAuth = FirebaseAuth.getInstance(); // 파이어베이스 인증 얻어오기
        firebaseUser = firebaseAuth.getCurrentUser(); // 현재 유저 정보 가져오기

        mDBRefUser = FirebaseDatabase.getInstance().getReference("UserAccount"); // DB 테이블 연결
        DatabaseReference mdatabaseRef = mDBRefUser.child(firebaseUser.getUid()).child("Exercise"+dayName);

        etcaddscene_complete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name_text = etcaddscene_name_text.getText().toString();
                String num_val = etcaddscene_num_val.getText().toString();

                Exercise exercise = new Exercise();
                exercise.setIv_profile("0");
                exercise.setTv_name(name_text);
                exercise.setTv_time(num_val);

                if(!exercise.getTv_name().isEmpty() && !exercise.getTv_time().isEmpty()){
                    mdatabaseRef.child(name_text).setValue(exercise);
                }



                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                ExerciseFragment exerciseFragment = new ExerciseFragment();
                transaction.replace(container.getId(),exerciseFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        etcaddscene_cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                ExerciseFragment exerciseFragment = new ExerciseFragment();
                transaction.replace(container.getId(),exerciseFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return view;
    }



}
