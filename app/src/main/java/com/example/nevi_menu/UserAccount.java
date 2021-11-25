package com.example.nevi_menu;

import java.util.HashMap;
import java.util.Map;

/**
 * 사용자 계정 정보 모델 클래스
 */

public class UserAccount {
    private String idToken; //Firebase Uid (고유 토큰정보)
    private String emailId; //이메일 아이디
    private String password; //비밀번호
    private String nickname; //닉네임
    private String height; //키
    private String currentWeight; //현재 체중
    private String targetWeight; //목표 체중

    public UserAccount() { } // Default constructor required for calls to DataSnapshot.getValue(User.class). 이거안하면 에러남

    public UserAccount(String emailId, String password, String nickname, String height, String currentWeight, String targetWeight) {
        this.emailId = emailId;
        this.password = password;
        this.nickname = nickname;
        this.height = height;
        this.currentWeight = currentWeight;
        this.targetWeight = targetWeight;
    }

    public Map<String, Object> toMap() { //DB에 insert 할 때 Map 사용
        HashMap<String, Object> result = new HashMap<>();
        result.put("emailId", emailId);
        result.put("password", password);
        result.put("nickname", nickname);
        result.put("height", height);
        result.put("currentWeight", currentWeight);
        result.put("targetWeight", targetWeight);

        return result;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getCurrentWeight() {
        return currentWeight;
    }

    public void setCurrentWeight(String currentWeight) {
        this.currentWeight = currentWeight;
    }

    public String getTargetWeight() {
        return targetWeight;
    }

    public void setTargetWeight(String targetWeight) {
        this.targetWeight = targetWeight;
    }
}
