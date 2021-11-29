package com.example.nevi_menu.ui.my_page;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class My_pageViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public My_pageViewModel() {
        //mText = new MutableLiveData<>();
        //mText.setValue("mypage in dea han gul an de no...");
    }

    public LiveData<String> getText() {
        return mText;
    }
}