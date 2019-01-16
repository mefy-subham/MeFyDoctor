package com.example.anisha.mefydoctor.model;

import java.util.HashMap;
import java.util.Map;

public class CallModel
{
    private String _fcmToken;
    private String _roomId;
    private String _userInfo;

    public String get_userInfo() {
        return _userInfo;
    }

    public void set_userInfo(String _userInfo) {
        this._userInfo = _userInfo;
    }

    public String get_type() {
        return _type;
    }

    public void set_type(String _type) {
        this._type = _type;
    }

    public String get_status() {
        return _status;
    }

    public void set_status(String _status) {
        this._status = _status;
    }

    private String _type;
    private String _status;

    public String get_fcmToken() {
        return _fcmToken;
    }

    public void set_fcmToken(String _fcmToken) {
        this._fcmToken = _fcmToken;
    }

    public String get_roomId() {
        return _roomId;
    }

    public void set_roomId(String _roomId) {
        this._roomId = _roomId;
    }

    public Map<String,String> getParamMap(CallModel callModel)
    {
        Map<String, String> params = new HashMap<String, String>();
        params.put("fcmToken", callModel._fcmToken);
        //params.put("roomId", "Me");
        params.put("roomId", callModel._roomId);
        params.put("userInfo", callModel._userInfo);
        params.put("type", callModel._type);
        params.put("status", callModel._status);
        return params;
    }

}
