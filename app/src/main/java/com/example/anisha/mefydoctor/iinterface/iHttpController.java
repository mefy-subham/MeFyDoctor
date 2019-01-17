package com.example.anisha.mefydoctor.iinterface;

import android.content.Context;

import com.example.anisha.mefydoctor.model.CallIdModel;
import com.example.anisha.mefydoctor.model.CallModel;
import com.example.anisha.mefydoctor.model.RoomModel;


public interface iHttpController {
    void placeCall(CallModel callModel, Context context, String operationFlag);
    void saveCall(CallIdModel callIdModel, Context context, String operationFlag);
    void roomCreation(RoomModel roomModel, Context context, String operationFlag);
    void twilioToken(Context context,String operationFlag,String userName,String roomName);
}
