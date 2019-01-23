package com.example.anisha.mefydoctor.constant;

/**
 * Created by root on 17/7/18.
 */

public class APIConstant {
    public static final String SEND_FCM_NOTIFICATION = "https://us-central1-mefy-1c490.cloudfunctions.net/sendFCMNotification";
    public static final String CALL_HISTORY = "http://ec2-13-232-207-92.ap-south-1.compute.amazonaws.com:5023/api/callhistory/savecall";
    public static final String UPDATE_CALL_HISTORY = "http://ec2-13-232-207-92.ap-south-1.compute.amazonaws.com:5023/api/callhistory/updatecallhistory/";
    public static final String SEND_FCM_TOKEN = "http://ec2-13-232-207-92.ap-south-1.compute.amazonaws.com:5023/api/doctor/profile/8fbf8ec1-5f96-4b63-b139-a08a9fd1478e";
    public static final String PARAM_SET_FCM = "mobiletoken" ;
    public static final String TWILIO_TOKEN = "http://ec2-13-232-207-92.ap-south-1.compute.amazonaws.com:5023/api/User/twilloToken?username=";
    public static final String CREATE_ROOM = "http://ec2-13-232-207-92.ap-south-1.compute.amazonaws.com:5023/api/User/joinroom";

    //Param Model For Call
    public static final String doctorId = "doctorId";
    public static final String individualId = "individualId";
    public static final String file = "file";
    public static final String startTime = "startTime";
    public static final String endTime = "endTime";

}
