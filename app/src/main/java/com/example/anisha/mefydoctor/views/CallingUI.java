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
    private String docId,startTime,indId;
    private String userName= "Doc";
    private String twilio_token;
    private String room;

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
                finishAffinity();
                System.exit(0);

                handler.postDelayed(this, INTERVAL);
            }
        }, INTERVAL);




        _ans=(Button)findViewById(R.id.btn_answer);
        _end=(Button)findViewById(R.id.btn_decline);

        Intent i=getIntent();
        String name=i.getExtras().getString("name");
        room=i.getExtras().getString("room");
        String pic=i.getExtras().getString("photos");
        System.out.println("CallingUI | Intent | Room: "+room);
        System.out.println("CallingUI | Intent | Name: "+name);
        System.out.println("CallingUI | Intent | Photo: "+pic);

        _name=(TextView)findViewById(R.id.name);
        _name.setText(name);


        _ans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //---------Call Answer------------------


                if (ringtoneSound != null) {
                    ringtoneSound.stop();
                }
                //Create Access Token
                createAccessToken(userName,room);

                //Save Call History
                callHistoryCreate();
                Intent intent = new Intent();
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

                finish();
            }
        });
    }

    private void createAccessToken(String userName,String room) {
        HttpHandler httpHandler = HttpHandler.getInstance();
        ServerResultHandler serverResultHandler = new ServerResultHandler(CallingUI.this);
        httpHandler.twilioToken(CallingUI.this,APPConstant.TWILIO_TOKEN_OPERATION,userName,room);
        httpHandler.set_resultHandler(serverResultHandler);
    }

    private void callHistoryCreate() {
        CallIdModel callIdModel= new CallIdModel();
        callIdModel.setDoctor_Id(docId);
        callIdModel.setStartTime(startTime);
        callIdModel.setIndividual_Id(indId);
        HttpHandler httpHandler = HttpHandler.getInstance();
        ServerResultHandler serverResultHandler = new ServerResultHandler(CallingUI.this);
        httpHandler.saveCall(callIdModel, CallingUI.this, APPConstant.CALL_HISTORY_SAVE_CALL);
        httpHandler.set_resultHandler(serverResultHandler);
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

            if (operation_flag.equalsIgnoreCase(APPConstant.CALL_HISTORY_SAVE_CALL)) {

            }

        }

        @Override
        public void onCancel(Object response, String operation_flag) {

        }

        @Override
        public void onToken(TokenDataModel tokenDataModel, String operation_flag) {
            if (operation_flag.equalsIgnoreCase(APPConstant.TWILIO_TOKEN_OPERATION)) {

                twilio_token = tokenDataModel.get_twilioToken();
                Intent intent = new Intent(CallingUI.this, VideoActivity.class);
                intent.putExtra("room", room);
                intent.putExtra("token",twilio_token);
                startActivity(intent);
                System.out.println("AppointmentActivity | ServerResultHandler | onSuccess | TWILIO_TOKEN_OPERATION | onToken: "+twilio_token);

            }

        }

        @Override
        public void onError(Object response, String operation_flag) {

        }

        @Override
        public void inProgress(Object response, String operation_flag) {

        }
    }
}
