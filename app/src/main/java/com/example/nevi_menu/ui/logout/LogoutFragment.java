package com.example.nevi_menu.ui.logout;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.nevi_menu.Login;
import com.example.nevi_menu.MainActivity;
import com.example.nevi_menu.databinding.FragmentLogoutBinding;
import com.google.firebase.auth.FirebaseAuth;

public class LogoutFragment extends Fragment {

    private LogoutViewModel logoutViewModel;
    private FragmentLogoutBinding binding;
    private FirebaseAuth mFirebaseAuth;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        logoutViewModel =
                new ViewModelProvider(this).get(LogoutViewModel.class);

        binding = FragmentLogoutBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mFirebaseAuth = FirebaseAuth.getInstance();
        binding.btnLogoutYes.setOnClickListener(new View.OnClickListener() { //로그아웃 하는경우
            @Override
            public void onClick(View view) {
                //로그아웃하고 로그인창으로 이동
                mFirebaseAuth.signOut();
                Intent intent = new Intent(getActivity(), Login.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        binding.btnLogoutNo.setOnClickListener(new View.OnClickListener() { //로그아웃 안하는 경우
            @Override
            public void onClick(View view) {
                //홈 화면으로 이동
                Intent intent = new Intent(getActivity(), MainActivity.class);
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