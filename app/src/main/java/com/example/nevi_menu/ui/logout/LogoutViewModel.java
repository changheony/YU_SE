package com.example.nevi_menu.ui.logout;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LogoutViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public LogoutViewModel() {
        //mText = new MutableLiveData<>();
        //mText.setValue("This is Logout fragment^^");
    }

    public LiveData<String> getText() {
        return mText;
    }
}