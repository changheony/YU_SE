package com.example.nevi_menu.ui.exercise;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.nevi_menu.R;

public class ExerciseArea extends Fragment {

    private int layout;
    private String dayName;
    Button chest;
    Button back;
    Button shoulder;
    Button arm;
    Button leg;
    Button abs;
    Button etc_exercise;


    public ExerciseArea(int layout,String dayName){
        this.layout = layout;
        this.dayName = dayName;
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,@Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(layout, container, false);




        chest = view.findViewById(R.id.btn_chest);
        back = view.findViewById(R.id.btn_back);
        shoulder = view.findViewById(R.id.btn_shoulder);
        arm = view.findViewById(R.id.btn_arm);
        leg = view.findViewById(R.id.btn_leg);
        abs = view.findViewById(R.id.btn_abs);
        etc_exercise = view.findViewById(R.id.btn_etc_exercise_add);

        chest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                ExerciseSelect chest = new ExerciseSelect(R.layout.fragment_exercise_select,"Chest",dayName);
                transaction.replace(container.getId(),chest);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                ExerciseSelect back = new ExerciseSelect(R.layout.fragment_exercise_select,"Back",dayName);
                transaction.replace(container.getId(),back);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        shoulder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                ExerciseSelect shoulder = new ExerciseSelect(R.layout.fragment_exercise_select,"Shoulder",dayName);
                transaction.replace(container.getId(),shoulder);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        arm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                ExerciseSelect arm = new ExerciseSelect(R.layout.fragment_exercise_select,"Arm",dayName);
                transaction.replace(container.getId(),arm);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        leg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                ExerciseSelect leg = new ExerciseSelect(R.layout.fragment_exercise_select,"Leg",dayName);
                transaction.replace(container.getId(),leg);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        abs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                ExerciseSelect abs = new ExerciseSelect(R.layout.fragment_exercise_select,"Abs",dayName);
                transaction.replace(container.getId(),abs);
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });

        etc_exercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                EtcExerciseScene etc_exercise_scene = new EtcExerciseScene(R.layout.fragment_exercise_etcaddscene,dayName);
                transaction.replace(container.getId(),etc_exercise_scene);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });


        return view;
    }

}