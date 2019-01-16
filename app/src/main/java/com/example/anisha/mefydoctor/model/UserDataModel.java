package com.example.anisha.mefydoctor.model;

import com.example.anisha.mefydoctor.constant.APPConstant;


public class UserDataModel {


    private String fcmtoken;


    public UserDataModel() {

    }

    public void setFcmtoken(String fcmtoken) {
        System.out.println("UserDataModel | setFcmtoken | fcmtoken:" + fcmtoken);
        this.fcmtoken = fcmtoken;
    }

    public String getFcmtoken() {
        System.out.println("UserDataModel | getFcmtoken | fcmtoken:" + fcmtoken);
        return fcmtoken;
    }
}
