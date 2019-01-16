package com.example.anisha.mefydoctor.iinterface;

import android.content.Context;

import com.example.anisha.mefydoctor.model.CallIdModel;
import com.example.anisha.mefydoctor.model.CallModel;


public interface iHttpController {
    void placeCall(CallModel callModel, Context context, String operationFlag);
    void saveCall(CallIdModel callIdModel, Context context, String operationFlag);
}
