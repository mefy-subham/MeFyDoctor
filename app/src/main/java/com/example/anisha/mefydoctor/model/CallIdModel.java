package com.example.anisha.mefydoctor.model;

import java.util.HashMap;
import java.util.Map;

public class CallIdModel {
    private String doctor_Id;
    private String individual_Id;
    private String File;
    private String startTime;
    private String endTime;
    private String callId;

    public String getCallId() {
        return callId;
    }

    public void setCallId(String callId) {
        this.callId = callId;
    }

    public String getDoctor_Id() {
        return doctor_Id;
    }

    public void setDoctor_Id(String doctor_Id) {
        this.doctor_Id = doctor_Id;
    }

    public String getIndividual_Id() {
        return individual_Id;
    }

    public void setIndividual_Id(String individual_Id) {
        this.individual_Id = individual_Id;
    }

    public String getFile() {
        return File;
    }

    public void setFile(String file) {
        File = file;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
    public Map<String,String> getParamMap(CallIdModel callIdModel)
    {
        Map<String, String> params = new HashMap<String, String>();
        params.put("doctorId", callIdModel.doctor_Id);
        //params.put("roomId", "Me");
        params.put("individualId", callIdModel.individual_Id);
        params.put("file", callIdModel.File);
        params.put("startTime", callIdModel.startTime);
        params.put("endTime", callIdModel.endTime);
        return params;
    }
}
