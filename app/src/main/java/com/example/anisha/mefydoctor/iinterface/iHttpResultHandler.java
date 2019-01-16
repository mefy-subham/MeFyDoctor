package com.example.anisha.mefydoctor.iinterface;

/**
 * Created by root on 18/7/18.
 */

public interface iHttpResultHandler
{
    void onSuccess(Object response, String operation_flag);
    void onCancel(Object response, String operation_flag);
    void onError(Object response, String operation_flag);
    void inProgress(Object response, String operation_flag);
}
