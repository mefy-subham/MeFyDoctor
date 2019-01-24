package com.example.anisha.mefydoctor.views;

import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.PowerManager;
import android.os.Vibrator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.text.format.Time;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;


import com.example.anisha.mefydoctor.R;
import com.example.anisha.mefydoctor.constant.APPConstant;
import com.example.anisha.mefydoctor.controller.UiController;
import com.example.anisha.mefydoctor.handler.HttpHandler;
import com.example.anisha.mefydoctor.iinterface.iHttpResultHandler;
import com.example.anisha.mefydoctor.iinterface.iObserver;
import com.example.anisha.mefydoctor.model.CallIdModel;
import com.example.anisha.mefydoctor.model.CallModel;
import com.example.anisha.mefydoctor.model.TokenDataModel;


public class CallingUI extends AppCompatActivity implements iObserver {

    private Button _ans,_end;
    private TextView _name;
    private Vibrator vib;
    private final static int INTERVAL = 42000; //42 milliseconds
    private String room_name,u_name,status,caller_fcmToken,caller_image_url,recording_url,type;
    private String accessToken;
    private String userName = "Doc";

    private String callId;
    private Time startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calling_ui);

        UiController uiController=UiController.getInstance();
        uiController.registerObserver(this);



        //no backgrounded process
         //this.finishAffinity();

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // to wake up screen
        PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
        @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
        wakeLock.acquire();



        // to release screen lock
        KeyguardManager keyguardManager = (KeyguardManager) getApplicationContext().getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("TAG");
        keyguardLock.disableKeyguard();

        Uri ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        Ringtone ringtoneSound = RingtoneManager.getRingtone(getApplicationContext(), ringtoneUri);

        System.out.println("Main | Play.onClick | ringtone:" +ringtoneSound);
        if (ringtoneSound != null) {
            ringtoneSound.play();
            vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vib.vibrate(3000);
        }


            Handler handler = new Handler();

            handler.postDelayed(new Runnable(){
                public void run(){
                    //do something
                    //finishAffinity();
                    //System.exit(0);
                    /*HttpHandler httpHandler = HttpHandler.getInstance();
                    ServerResultHandler serverResultHandler = new ServerResultHandler(CallingUI.this);
                    httpHandler.set_resultHandler(serverResultHandler);
                    CallModel callModel = new CallModel();
                    callModel.setUserInfo(u_name);
                    callModel.setRoomId(room_name);
                    callModel.setCallee_fcmToken(caller_fcmToken);
                    callModel.setStatus(status);
                    callModel.setType("decline");
                    callModel.setRecording_url("Support");
                    callModel.setCaller_image_url("ABC");
                    callModel.setCaller_fcmToken("callee_fcm");
                    httpHandler.placeCall(callModel,CallingUI.this,APPConstant.SEND_FCM_NOTIFICATION_OPERATION);*/
                    finish();
                    handler.postDelayed(this, INTERVAL);
                }
            }, INTERVAL);







        _ans=(Button)findViewById(R.id.btn_answer);
        _end=(Button)findViewById(R.id.btn_decline);

        Intent i = getIntent();

        caller_fcmToken=i.getExtras().getString(APPConstant.caller_fcmToken);
        caller_image_url=i.getExtras().getString(APPConstant.caller_image_url);
        recording_url=i.getExtras().getString(APPConstant.recording_url);
        u_name = i.getExtras().getString(APPConstant.userInfo);
        room_name = i.getExtras().getString(APPConstant.roomId);
        type = i.getExtras().getString(APPConstant.type);
        status=i.getExtras().getString(APPConstant.status);
        accessToken=i.getExtras().getString(APPConstant.ACCESS_TOKEN);

        System.out.println("VideoActivity | onCreate | getString | caller_fcmToken:" + caller_fcmToken);
        /*System.out.println("VideoActivity | onCreate | getString | callee_fcm:" + callee_fcm);*/
        System.out.println("VideoActivity | onCreate | getString | caller_image_url:" + caller_image_url);
        System.out.println("VideoActivity | onCreate | getString | recording_url:" + recording_url);
        System.out.println("VideoActivity | onCreate | getString | u_name:" + u_name);
        System.out.println("VideoActivity | onCreate | getString | room_name:" + room_name);
        System.out.println("VideoActivity | onCreate | getString | type:" + type);
        System.out.println("VideoActivity | onCreate | getString | status:" + status);
        System.out.println("VideoActivity | onCreate | getString | accessToken:" + accessToken);

        _name=(TextView)findViewById(R.id.name);
        _name.setText(u_name);


        _ans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //---------Call Answer------------------


                if (ringtoneSound != null) {
                    ringtoneSound.stop();
                }
                //Save Call History
                callHistoryCreate();



                finish();

            }
        });


        _end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //----------------Call Decline------------

                if (ringtoneSound != null) {
                    ringtoneSound.stop();
                }
                HttpHandler httpHandler = HttpHandler.getInstance();
                ServerResultHandler serverResultHandler = new ServerResultHandler(CallingUI.this);
                httpHandler.set_resultHandler(serverResultHandler);
                CallModel callModel = new CallModel();
                callModel.setUserInfo(u_name);
                callModel.setRoomId(room_name);
                callModel.setCallee_fcmToken(caller_fcmToken);
                callModel.setStatus(status);
                callModel.setType("decline");
                callModel.setRecording_url("Support");
                callModel.setCaller_image_url("ABC");
                callModel.setCaller_fcmToken("callee_fcm");
                httpHandler.placeCall(callModel,CallingUI.this,APPConstant.SEND_FCM_NOTIFICATION_OPERATION);

            }
        });
    }

    private void createAccessToken(String userName,String room) {
        HttpHandler httpHandler = HttpHandler.getInstance();
        ServerResultHandler serverResultHandler = new ServerResultHandler(CallingUI.this);
        httpHandler.set_resultHandler(serverResultHandler);
        httpHandler.twilioToken(CallingUI.this,APPConstant.TWILIO_TOKEN_OPERATION,userName,room);

    }

    private void callHistoryCreate() {
        CallIdModel callIdModel= new CallIdModel();
        callIdModel.setDoctor_Id(userName);
        startTime = new Time();
        startTime.setToNow();
        callIdModel.setStartTime(startTime.toString());
        System.out.println("CallingUI | callHistoryCreate | startTime.toString() :" +startTime.toString());
        callIdModel.setIndividual_Id(u_name);
        callIdModel.setFile(recording_url);
        HttpHandler httpHandler = HttpHandler.getInstance();
        ServerResultHandler serverResultHandler = new ServerResultHandler(CallingUI.this);
        httpHandler.set_resultHandler(serverResultHandler);
        httpHandler.saveCall(callIdModel, CallingUI.this, APPConstant.CALL_HISTORY_SAVE_CALL);

    }

    @Override
    public void onDecline(String msg) {
        System.out.println("CallingUI | OnDecline | Msg :" +msg);
        finishAffinity();
        System.exit(0);

    }

    @Override
    protected void onDestroy() {
        System.out.println("CallingUI | onDestroy | Destroyed ");
        UiController.getInstance().unregisterObserver(this);
        super.onDestroy();
    }
    private class ServerResultHandler implements iHttpResultHandler {
        private Context _context;


        public ServerResultHandler(Context context) {
            this._context = context;
        }

        @Override
        public void onSuccess(Object response, String operation_flag) {

            if(operation_flag.equalsIgnoreCase(APPConstant.SEND_FCM_NOTIFICATION_OPERATION)){
                finish();
            }

            if (operation_flag.equalsIgnoreCase(APPConstant.CALL_HISTORY_SAVE_CALL)) {
                System.out.println("CallingUI | ServerResultHandler | CALL_HISTORY_SAVE_CALL | response : " +response);

            }

        }

        @Override
        public void onCancel(Object response, String operation_flag) {

        }

        @Override
        public void onToken(TokenDataModel tokenDataModel, String operation_flag) {
            if (operation_flag.equalsIgnoreCase(APPConstant.TWILIO_TOKEN_OPERATION)) {

                accessToken = tokenDataModel.get_twilioToken();
                Intent intent = new Intent(CallingUI.this, VideoActivity.class);
                intent.putExtra(APPConstant.roomId, room_name);
                intent.putExtra(APPConstant.ACCESS_TOKEN,accessToken);
                intent.putExtra(APPConstant.CALL_ID,callId);
                /*intent.putExtra(APPConstant.callee_fcmToken, callee_fcm);*/
                intent.putExtra(APPConstant.caller_fcmToken, caller_fcmToken);
                intent.putExtra(APPConstant.caller_image_url, caller_image_url);
                intent.putExtra(APPConstant.startTime,startTime.toString());
                intent.putExtra(APPConstant.recording_url, recording_url);
                intent.putExtra(APPConstant.userInfo, u_name);
                intent.putExtra(APPConstant.type, type);
                intent.putExtra(APPConstant.status, status);
                startActivity(intent);
                System.out.println("AppointmentActivity | ServerResultHandler | onSuccess | TWILIO_TOKEN_OPERATION | onToken: "+accessToken);

            }

        }

        @Override
        public void onCallId(CallIdModel callIdModel, String operation_flag) {

            callId = callIdModel.getCallId();
            System.out.println("ServerResultHandler | onCallId | callIdModel | getCallId : " +callId);
            //Create Access Token
            createAccessToken(userName,room_name);
        }

        @Override
        public void onError(Object response, String operation_flag) {

        }

        @Override
        public void inProgress(Object response, String operation_flag) {

        }
    }
}
