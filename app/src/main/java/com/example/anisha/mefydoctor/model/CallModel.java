package com.example.anisha.mefydoctor.model;

import com.example.anisha.mefydoctor.constant.APPConstant;

import java.util.HashMap;
import java.util.Map;

public class CallModel
{
    private String roomId;
    private String userInfo;
    private String type;
    private String status;
    private String caller_fcmToken;
    private String callee_fcmToken;
    private String caller_image_url;
    private String recording_url;


    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(String userInfo) {
        this.userInfo = userInfo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCaller_fcmToken() {
        return caller_fcmToken;
    }

    public void setCaller_fcmToken(String caller_fcmToken) {
        this.caller_fcmToken = caller_fcmToken;
    }

    public String getCallee_fcmToken() {
        return callee_fcmToken;
    }

    public void setCallee_fcmToken(String callee_fcmToken) {
        this.callee_fcmToken = callee_fcmToken;
    }

    public String getCaller_image_url() {
        return caller_image_url;
    }

    public void setCaller_image_url(String caller_image_url) {
        this.caller_image_url = caller_image_url;
    }

    public String getRecording_url() {
        return recording_url;
    }

    public void setRecording_url(String recording_url) {
        this.recording_url = recording_url;
    }
    public Map<String,String> getParamMap(CallModel callModel)
    {
        Map<String, String> params = new HashMap<String, String>();
        params.put(APPConstant.callee_fcmToken, callModel.getCallee_fcmToken());
        params.put(APPConstant.caller_fcmToken, callModel.getCaller_fcmToken());
        params.put(APPConstant.caller_image_url, callModel.getCaller_image_url());
        params.put(APPConstant.recording_url, callModel.getRecording_url());
        params.put(APPConstant.userInfo, callModel.getUserInfo());
        params.put(APPConstant.roomId, callModel.getRoomId());
        params.put(APPConstant.type, callModel.getType());
        params.put(APPConstant.status, callModel.getStatus());
        return params;
    }

}
