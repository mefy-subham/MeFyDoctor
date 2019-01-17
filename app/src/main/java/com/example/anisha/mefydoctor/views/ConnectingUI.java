package com.example.anisha.mefydoctor.views;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.anisha.mefydoctor.R;
import com.example.anisha.mefydoctor.constant.APPConstant;
import com.example.anisha.mefydoctor.handler.HttpHandler;
import com.example.anisha.mefydoctor.iinterface.iHttpResultHandler;
import com.example.anisha.mefydoctor.model.CallModel;
import com.example.anisha.mefydoctor.model.TokenDataModel;

public class ConnectingUI extends AppCompatActivity {

    private Button _decline;
    private String room_name,value_send,fcm,u_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connecting_ui);
        _decline=(Button)findViewById(R.id.btn_decline);
        _decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //-----------Make The Room Destroy-------------
                CallModel callModel=new CallModel();
                callModel.set_userInfo(u_name);
                callModel.set_roomId(room_name);
                callModel.set_fcmToken(fcm);
                callModel.set_status(value_send);
                callModel.set_type("decline");
                HttpHandler httpHandler = HttpHandler.getInstance();
                ServerResultHandler serverResultHandler = new ServerResultHandler(ConnectingUI.this);
                httpHandler.set_resultHandler(serverResultHandler);
                httpHandler.placeCall(callModel, ConnectingUI.this, APPConstant.SEND_FCM_NOTIFICATION_OPERATION);
                finish();
            }
        });
    }
    private class ServerResultHandler implements iHttpResultHandler {
        private Context _context;


        public ServerResultHandler(Context context) {
            this._context = context;
        }

        @Override
        public void onSuccess(Object response, String operation_flag) {

            if (operation_flag.equalsIgnoreCase(APPConstant.SEND_FCM_NOTIFICATION_OPERATION)) {

            }


        }

        @Override
        public void onCancel(Object response, String operation_flag) {

        }

        @Override
        public void onToken(TokenDataModel tokenDataModel, String operation_flag) {

        }

        @Override
        public void onError(Object response, String operation_flag) {

        }

        @Override
        public void inProgress(Object response, String operation_flag) {

        }
    }

}