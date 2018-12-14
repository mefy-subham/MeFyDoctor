package com.example.anisha.mefydoctor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


import org.json.JSONArray;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {
    private EditText edtphnmbr,edtOtp;
    private Button loginbtn,otpbtn;
    private TextView textreg,tvOtp,tvOtpSec;
    private LinearLayout layoutLoginMain,layoutOtp;
    boolean otpres=false;
    String deviceid;
    ProgressDialog progress;
    public static final String MyPREFERENCES = "MyPrefs" ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        layoutLoginMain = (LinearLayout)findViewById(R.id.layoutLoginMain) ;
        layoutOtp = (LinearLayout)findViewById(R.id.layoutOtp);
        edtphnmbr = (EditText)findViewById(R.id.edtphnmbr);
        edtOtp = (EditText)findViewById(R.id.edtOtp);
        loginbtn = (Button) findViewById(R.id.loginbtn);
        otpbtn = (Button)findViewById(R.id.otpbtn);
        textreg = (TextView) findViewById(R.id.textreg);
        tvOtp = (TextView)findViewById(R.id.tvOtp);
        tvOtpSec = (TextView)findViewById(R.id.tvOtpSec);
deviceid = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        textreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(),   RegistrationActivity.class);
                startActivity(intent);
                System.out.println("moving to 3rd activity-------------->>>>>>>>>>");
            }
        });
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckConnectionStatus();
//                layoutLoginMain.setVisibility(View.GONE);
//                layoutOtp.setVisibility(View.VISIBLE);
            }
        });
        otpbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                otpres = true;
                CheckConnectionStatus();
//                Intent intentdashboard = new Intent(getBaseContext(),   DashboardActivity.class);
//                startActivity(intentdashboard);
            }
        });
    }

    public void CheckConnectionStatus(){
        progress = new ProgressDialog(this);
        progress.setMessage("please wait...");
        progress.setCancelable(false);
        progress.show();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JSONObject json = new JSONObject();
        try{
            if (otpres == false){
                json.accumulate("phoneNumber", edtphnmbr.getText().toString());
                json.accumulate("role","doctor");
                json.accumulate("deviceId",deviceid);
            }
            else if (otpres == true){
                json.accumulate("phoneNumber", edtphnmbr.getText().toString());
                json.accumulate("role","doctor");
                json.accumulate("otp",edtOtp.getText().toString());
            }

            System.out.println(json+"otp is true---------------->>>>>>>>>>>>>>");

        } catch (JSONException e) {
            e.printStackTrace();
        }
//        String url = getResources().getString(R.string.json_get_url);
        final String result = "";
        String url = "http://ec2-13-232-207-92.ap-south-1.compute.amazonaws.com:5023/api/User/";
        if (otpres == false){
            url = "http://ec2-13-232-207-92.ap-south-1.compute.amazonaws.com:5023/api/User/login";
        }
        else if (otpres == true){
            url = "http://ec2-13-232-207-92.ap-south-1.compute.amazonaws.com:5023/api/User/verifyotp";
        }

        System.out.println("url---------------->>>>>>>>>>"+ url);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url,json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            JSONObject obj = response.getJSONObject("result");
                            if (otpres == false){
                                String msg = obj.getString("message");
                                System.out.println("msg--------------->>>"+msg.toString());
                                if (msg.equals("OTP sent to registered number")){

                                    layoutLoginMain.setVisibility(View.GONE);
                                    layoutOtp.setVisibility(View.VISIBLE);
                                    otpres= true;
                                }
                                else if (msg.equals("User not Registered")){
                                    Toast toast = Toast.makeText(getApplicationContext(),
                                            "Number not registered.\n Please Register",
                                            Toast.LENGTH_SHORT);

                                    toast.show();
                                }
                              else if ( msg.equals("Doctor loggedIn successfully")){
                                    System.out.println("dashboard activity--------------->>>");
                                    Intent intentdashboard = new Intent(getBaseContext(),   DashboardActivity.class);
                                    startActivity(intentdashboard);
                                    JSONObject userObject = obj.getJSONObject("user");
                                    String username = userObject.getString("name");
                                    String doctorId = userObject.getString("doctorId");
                                    String userId = userObject.getString("userId");
                                    String myUserId = userId.substring(userId.lastIndexOf("#")+1);

                                    System.out.println("msg--------------->>>"+msg.toString());
                                    System.out.println("seperated--------->>>"+myUserId);
                                    System.out.println("username----------------"+username);
                                    System.out.println("doctorId-----------------"+doctorId);
                                    System.out.println("userId------------->>>>>>>>>>>"+userId);

                                    SharedPreferences.Editor editor = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE).edit();
                                    editor.putString("doctorId",userObject.getString("doctorId"));
                                    editor.putString("userId",userObject.getString("userId"));
                                    editor.putString("myUserId",userId.substring(userId.lastIndexOf("#")+1));
                                    editor.putString("username",userObject.getString("name"));
                                    System.out.println("editor------------>>>>>>>>>>"+userObject.getString("doctorId"));
                                    editor.commit();
                                }

                            }
                            else{
                                String msg = obj.getString("message");

                            if (msg.equals("doctor profile detail")){
                                    System.out.println("dashboard activity--------------->>>");
                                    Intent intentdashboard = new Intent(getBaseContext(),   DashboardActivity.class);
                                    startActivity(intentdashboard);
                                    JSONObject userObject = obj.getJSONObject("result");
                                    String username = userObject.getString("name");
                                    String doctorId = userObject.getString("doctorId");
                                    String userId = userObject.getString("userId");
                                    String myUserId = userId.substring(userId.lastIndexOf("#")+1);

                                    System.out.println("msg--------------->>>"+msg.toString());
                                    System.out.println("seperated--------->>>"+myUserId);
                                    System.out.println("username----------------"+username);
                                    System.out.println("doctorId-----------------"+doctorId);
                                    System.out.println("userId------------->>>>>>>>>>>"+userId);

                                    SharedPreferences.Editor editor = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE).edit();
                                    editor.putString("doctorId",userObject.getString("doctorId"));
                                    editor.putString("userId",userObject.getString("userId"));
                                    editor.putString("myUserId",userId.substring(userId.lastIndexOf("#")+1));
                                    editor.putString("username",userObject.getString("name"));
                                    System.out.println("editor------------>>>>>>>>>>"+userObject.getString("doctorId"));
                                    editor.commit();
                                }
                                else if (msg.equals("Otp verification failed")){
                                    layoutOtp.setVisibility(View.VISIBLE);
                                    Toast toast = Toast.makeText(getApplicationContext(),
                                            "Plese enter the correct OTP",
                                            Toast.LENGTH_SHORT);

                                    toast.show();
                                }

                            }

//                            else{
//                                String msg = obj.getString("message");
//                                System.out.println("msg--------------->>>"+msg.toString());
//                                if (msg.equals("OTP sent to registered number")){
//                                    otpres = true;
//                                    layoutLoginMain.setVisibility(View.GONE);
//                                    layoutOtp.setVisibility(View.VISIBLE);
//                                }
//                                else if (msg.equals("User not Registered")){
//                                    Toast toast = Toast.makeText(getApplicationContext(),
//                                            "Number not registered.\n Please Register",
//                                            Toast.LENGTH_SHORT);
//
//                                    toast.show();
//                                }
//                            }


                        }
                        catch (JSONException e) {
                            // If an error occurs, this prints the error to the log
                            e.printStackTrace();
                        }
                        System.out.println("resp------------------->>>>>>>>>>>>>>>>"+result);
                        progress.dismiss();
                        System.out.println("response-------------------------------->>>>>>>>>>>>>>>"+response.toString());
                        try {
                            JSONObject serverResp = new JSONObject(response.toString());
                            System.out.println("success result:------------------------->>>>>>>>>>>>> " + serverResp);

                        } catch (JSONException e) {
// TODO Auto-generated catch block
                            progress.dismiss();
                            System.out.println("cache------------------------------------------>>>>>>>>>>>>>>");
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progress.dismiss();
                System.out.println("Error getting response------------------------");
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

}
