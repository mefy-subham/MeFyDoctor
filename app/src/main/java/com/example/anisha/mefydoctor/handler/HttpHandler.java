package com.example.anisha.mefydoctor.handler;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.example.anisha.mefydoctor.controller.HttpController;
import com.example.anisha.mefydoctor.iinterface.iHttpController;
import com.example.anisha.mefydoctor.iinterface.iHttpResultHandler;
import com.example.anisha.mefydoctor.model.CallIdModel;
import com.example.anisha.mefydoctor.model.CallModel;
import com.example.anisha.mefydoctor.model.RoomModel;


public class HttpHandler implements iHttpController
{

    private static HttpHandler _httpHandler;
    private RequestQueue _requestQueue;
    private HttpController _httpController;
    private iHttpResultHandler _resultHandler;

    public static final HttpHandler getInstance()
    {
        if(_httpHandler == null)
        {
            _httpHandler = new HttpHandler();
        }

        return _httpHandler;
    }

    public void goGarbge()
    {
        _httpController = null;
    }


    @Override
    public void placeCall(CallModel callModel, Context context, String operationFlag) {

        _httpController = HttpController.getInstance();
        _httpController.set_resultHandler(_resultHandler);
        _httpController.placeCall(callModel,context, operationFlag);
    }

    public void set_resultHandler(iHttpResultHandler _resultHandler) {
        this._resultHandler = _resultHandler;
    }
    @Override
    public void saveCall(CallIdModel callIdModel, Context context, String operationFlag) {

        _httpController = HttpController.getInstance();
        _httpController.set_resultHandler(_resultHandler);
        _httpController.saveCall(callIdModel,context, operationFlag);
    }

    @Override
    public void roomCreation(RoomModel roomModel, Context context, String operationFlag) {
        _httpController = HttpController.getInstance();
        _httpController.set_resultHandler(_resultHandler);
        _httpController.roomCreation(roomModel,context, operationFlag);
    }

    @Override
    public void twilioToken(Context context,String operationFlag,String userName,String roomName) {
        _httpController = HttpController.getInstance();
        _httpController.set_resultHandler(_resultHandler);
        _httpController.twilioToken(context,operationFlag,userName,roomName);
    }
}
