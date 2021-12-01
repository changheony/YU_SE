package com.example.nevi_menu.ui.exercise;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.nevi_menu.R;
import com.example.nevi_menu.databinding.FragmentExerciseBinding;

public class ExerciseFragment extends Fragment {

    private FragmentExerciseBinding binding;

    Button isClicked;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentExerciseBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.monday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isClicked != null){
                    isClicked.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffffff")));
                }
                isClicked = binding.monday;
                isClicked.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4dc0f3")));
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                Day mon = new Day(R.layout.fragment_exercise_day,"Monday");
                transaction.replace(R.id.frame,mon);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        binding.tuesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isClicked != null){
                    isClicked.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffffff")));
                }
                isClicked = binding.tuesday;
                isClicked.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4dc0f3")));
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                Day tues = new Day(R.layout.fragment_exercise_day,"Tuesday");
                transaction.replace(R.id.frame,tues);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        binding.wednesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isClicked != null){
                    isClicked.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffffff")));
                }
                isClicked = binding.wednesday;
                isClicked.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4dc0f3")));
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                Day weds = new Day(R.layout.fragment_exercise_day,"Wednesday");
                transaction.replace(R.id.frame,weds);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        binding.thursday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isClicked != null){
                    isClicked.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffffff")));
                }
                isClicked = binding.thursday;
                isClicked.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4dc0f3")));
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                Day thurs = new Day(R.layout.fragment_exercise_day,"Thursday");
                transaction.replace(R.id.frame,thurs);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        binding.friday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isClicked != null){
                    isClicked.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffffff")));
                }
                isClicked = binding.friday;
                isClicked.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4dc0f3")));
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                Day fri = new Day(R.layout.fragment_exercise_day,"Friday");
                transaction.replace(R.id.frame,fri);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        binding.saturday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isClicked != null){
                    isClicked.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffffff")));
                }
                isClicked = binding.saturday;
                isClicked.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4dc0f3")));
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                Day sat = new Day(R.layout.fragment_exercise_day,"Saturday");
                transaction.replace(R.id.frame,sat);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        binding.sunday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isClicked != null){
                    isClicked.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffffff")));
                }
                isClicked = binding.sunday;
                isClicked.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4dc0f3")));
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                Day sun = new Day(R.layout.fragment_exercise_day,"Sunday");
                transaction.replace(R.id.frame,sun);
                transaction.addToBackStack(null);
                transaction.commit();
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