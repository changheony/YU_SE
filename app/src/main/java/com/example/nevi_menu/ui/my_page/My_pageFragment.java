package com.example.nevi_menu.ui.my_page;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.nevi_menu.databinding.FragmentMyPageBinding;

public class My_pageFragment extends Fragment {

    private My_pageViewModel my_pageViewModel;
    private FragmentMyPageBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        my_pageViewModel =
                new ViewModelProvider(this).get(My_pageViewModel.class);

        binding = FragmentMyPageBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textMyPage;
        my_pageViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
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