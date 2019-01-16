package com.example.anisha.mefydoctor.handler;

import android.content.Context;

import com.example.anisha.mefydoctor.controller.HttpProfileController;
import com.example.anisha.mefydoctor.iinterface.iHttpResultHandler;
import com.example.anisha.mefydoctor.iinterface.iUserInfo;
import com.example.anisha.mefydoctor.model.UserDataModel;

public class UserHandler implements iUserInfo {

    private static UserHandler _userHandler;
    private UserDataModel userData ;
    private iHttpResultHandler _iHttpResultHandler;
    private HttpProfileController _httpProfileController;

    public static UserHandler getInstance()
    {
        if(_userHandler == null)
            _userHandler =  new UserHandler();

        return _userHandler;
    }

    public  void  gogarbage()
    {
        if (_userHandler != null)
        {
            _userHandler = null;
        }
    }
    public UserDataModel getUserData() {
        return userData;
    }
    @Override
    public void setUserInfo(UserDataModel userDataModel, Context context) {

    }

    @Override
    public void getUserInfo(String id, Context context) {

    }

    @Override
    public void updateUserInfo(UserDataModel userDataModel, Context context) {
        _httpProfileController = HttpProfileController.getInstance();
        _httpProfileController.set_iHttpResultHandler(_iHttpResultHandler);
        _httpProfileController.updateProfileData(userDataModel,context);
    }

    public void setUserData(UserDataModel userData) {
        //System.out.println("setUserData in userhandler:  "+userData.getEmail());
        this.userData = userData;
    }
}
