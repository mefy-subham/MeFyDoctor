package com.example.anisha.mefydoctor;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

public class RegistrationActivity extends AppCompatActivity {
    public static SharedPreferences.Editor bundle;
    public static Bundle myBundle = new Bundle();
    ImageView slide_image;
    Button regFirstbtn,regSecbtn,regThirdbtn,regOtpbtn;
    AutoCompleteTextView regLangedt,regEduedt,regSpecedt;
    EditText regNameedt,regEmailedt,regCityedt,regPractedt,regAuthedt
            ,regPhnedt,regIdedt,regStateedt,regOtpedt;
    TextView intro,regGenderTv,regDobTv;
    LinearLayout Mainlayout,regFirstLayout,regSecLayout,regThirdLayout,regFourthLayout;
    RadioGroup gender;
    RadioButton radiosex;
    private String getName;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private static final String TAG = "RegistrationActivity";
    boolean otpres=false;
    String deviceid;
    ProgressDialog progress;
    public static final String MyPREFERENCES = "MyPrefs" ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        Bundle myBundle = new Bundle();
        slide_image = (ImageView)findViewById(R.id.slide_image);

        regFirstbtn = (Button) findViewById(R.id.regFirstbtn);
        regSecbtn = (Button)findViewById(R.id.regSecbtn);
        regThirdbtn = (Button)findViewById(R.id.regThirdbtn);
        regOtpbtn = (Button)findViewById(R.id.regOtpbtn);

        regNameedt = (EditText) findViewById(R.id.regNameedt);
        regEmailedt = (EditText) findViewById(R.id.regEmailedt);
        regCityedt = (EditText)findViewById(R.id.regCityedt);
        regPractedt =(EditText)findViewById(R.id.regPractedt);
        regAuthedt = (EditText)findViewById(R.id.regAuthedt);
        regPhnedt = (EditText)findViewById(R.id.regPhnedt);
        regIdedt = (EditText)findViewById(R.id.regIdedt);
        regStateedt = (EditText)findViewById(R.id.regStateedt);
        regOtpedt = (EditText)findViewById(R.id.regOtpedt);

        regLangedt = (AutoCompleteTextView) findViewById(R.id.regLangedt);
        regEduedt = (AutoCompleteTextView) findViewById(R.id.regEduedt);
        regSpecedt = (AutoCompleteTextView) findViewById(R.id.regSpecedt);

        regFirstLayout = (LinearLayout) findViewById(R.id.regFirstLayout);
        Mainlayout = (LinearLayout)findViewById(R.id.Mainlayout);
        regSecLayout = (LinearLayout)findViewById(R.id.regSecLayout);
        regThirdLayout = (LinearLayout)findViewById(R.id.regThirdLayout);
        regFourthLayout = (LinearLayout)findViewById(R.id.regFourthLayout);

        intro = (TextView)findViewById(R.id.intro);
        regGenderTv = (TextView)findViewById(R.id.regGenderTv);
        regDobTv = (TextView)findViewById(R.id.regDobTv);

        gender = (RadioGroup) findViewById(R.id.gender);

        deviceid = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        //System.out.println("entered 3rd activity---------->>>>>>>>>>>>>>>>");



//        +++++++++++++++++++++(FIRST VISIBILITY PAGE VALIDATION)+++++++++++++++


//        NAME ON CLICK VALIDATION

        regNameedt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validates());
                //System.out.println("1st data--------->>>>>>" +regNameedt);
            }
        });

//        EMAIL ON CLICK VALIDATION
        regEmailedt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (emailvalidates());
                //System.out.println("2nd data--------->>>>>>" +regEmailedt);
            }
        });

//        secbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if(radiovalidate());
//                String getNames = ((EditText) findViewById(R.id.textfirst)).getText().toString();
//                text9.setText(getNames);
//                String radiosex = RegistrationActivity.myBundle.getString("sex").toString();
//                //System.out.println("2nd data--------->>>>>>" +radiosex);
//
//            }
//        });


//        BIRTH  ON CLICK VALIDATION
        regDobTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //System.out.println("2nd data--------->>>>>>" );
                if(datevalidate());
                //System.out.println("3rd data--------->>>>>>" +regDobTv);
            }
        });
        regDobTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(RegistrationActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month+1;
                Log.d(TAG, "onDateSet: mm/dd/yyy:" + month + "/" + day + "/" + year);
                String date = month + "/" + day + "/" + year;
                regDobTv.setText(date);
            }
        };

        regFirstbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              if (validates() && emailvalidates() && datevalidate()){
                  regFirstLayout.setVisibility(View.GONE);
                  regSecLayout.setVisibility(View.VISIBLE);
              }

            }
        });


    //   +++++++++++++++++++++(SECOND LAYOUT VISIBILITY VALIDATION)+++++++++++++++

//      CITY VALIDATION

        regCityedt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             cityvalidate();
            }
        });

        regLangedt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                langValidate();
            }
        });

        regPractedt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pracValidate();
            }
        });

        regAuthedt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authValidate();
            }
        });

        regSecbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (cityvalidate() && langValidate() && pracValidate() && authValidate()){
                    regSecLayout.setVisibility(View.GONE);
                    regThirdLayout.setVisibility(View.VISIBLE);
                }

            }
        });
//++++++++++++++++++++++++THIRD LAYOUT VISIBILITY VALIDATION+++++++++++++++

        //      EDUCATION VALIDATE

        regEduedt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eduValidate();
            }
        });

//        SPECIALITY VALIDATE

        regSpecedt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                specValidate();
            }
        });

//        PHONE VALIDATE
        regPhnedt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneValidate();
            }
        });

//        STATE VALIDATE

        regStateedt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stateValidate();
            }
        });
//        REGISTRATION ID VALIDATE

        regIdedt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regValidate();
            }
        });

        regThirdbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                docPreRegistration();
                if (eduValidate() && specValidate() && stateValidate() && phoneValidate()
                        && regValidate()){
                    regThirdLayout.setVisibility(View.GONE);
                    regFourthLayout.setVisibility(View.VISIBLE);
                }
            }
        });
        regOtpbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otpres = true;
                docPreRegistration();
            }
        });
    }

//    *****************NAME FIELD VALIDATION********************

    private  boolean validates(){
        boolean result = false;

        String name = regNameedt.getText().toString();
        //System.out.println("captured data----------->>>>>>" +name);
        if(name.isEmpty()){
            Toast.makeText(this,"please enter the " +
                    "name",Toast.LENGTH_SHORT ).show();
        }
        else if(name.length()>50){
            Toast.makeText(this,"Name length is" +
                    " too big",Toast.LENGTH_LONG ).show();
        }
        else {
            RegistrationActivity.myBundle.putString("Name" ,name);
            result = true;
        }
        return result;
    }

//    *******************EMAIL FIELD VALIDATION******************

    private  boolean emailvalidates(){
        boolean emailrusult = false;

        String email = regEmailedt.getText().toString();
        //System.out.println("captured data----------->>>>>>" +email);
        if(email.isEmpty()){
            Toast.makeText(this,"please enter the " +
                    "email",Toast.LENGTH_SHORT ).show();
        }
        else {
            RegistrationActivity.myBundle.putString("Email" ,email);
            emailrusult = true;
        }
        return emailrusult;
    }

//      *****************GENDER FIELD VALIDATION*********************

    private boolean radiovalidate(){
        boolean radio = false;
        int radiogender = gender.getCheckedRadioButtonId();
        radiosex = (RadioButton) findViewById(radiogender);
        Toast.makeText(RegistrationActivity.this,
                radiosex.getText().toString(), Toast.LENGTH_SHORT).show();


        if(radiosex.isDirty()){
            Toast.makeText(this,"Please click one of the" +
                    " option",Toast.LENGTH_SHORT ).show();
        }
        else {
            RegistrationActivity.myBundle.putString("sex" , String.valueOf(radio).toString());
            radio = true;
        }
        return radio;
    }

//      *********************BIRTH FIELD VALIDATION**********************
    private boolean datevalidate(){
        boolean birth = false;
        String dateOfbirth = regDobTv.getText().toString();
        if(dateOfbirth.isEmpty()){
            Toast.makeText(this,"please enter the " +
                    "date of birth",Toast.LENGTH_SHORT ).show();
        }
        else{
            RegistrationActivity.myBundle.putString("Birth" , String.valueOf(dateOfbirth));
            birth = true;
        }
        return birth;
    }

//    **************************CITY FIELD VALIDATION****************************

    private boolean cityvalidate(){
    boolean city = false;
        String place = regCityedt.getText().toString();
        if(place.isEmpty()){
            Toast.makeText(this,"please enter the " +
                    "city",Toast.LENGTH_SHORT ).show();
        }
        else{
            RegistrationActivity.myBundle.putString("City" , String.valueOf(place));
            city= true;
        }
        return city;
    }

//    ******************LANGUAGE FIELD VALIDATION***********************

    private boolean langValidate(){
        boolean lang = false;
        String language = regLangedt.getText().toString();
        if (language.isEmpty()){
            Toast.makeText(this,"please provide the " +
                    "language",Toast.LENGTH_SHORT ).show();
        }
        else {
            RegistrationActivity.myBundle.putString("Language" ,language);
            lang = true;
        }
        return lang;
    }

//    ******************PRACTICE FIELD VALIDATION***********************

    private boolean pracValidate(){
        boolean prac = false;
        String practice = regPractedt.getText().toString();
        if (practice.isEmpty()){
            Toast.makeText(this,"please provide the " +
                    "practice details",Toast.LENGTH_SHORT ).show();
        }
        else {
            RegistrationActivity.myBundle.putString("Practice" ,practice);
            prac = true;
        }
        return prac;
    }

//    ******************ISSUING AUTHORITY FIELD VALIDATION***********************

    private boolean authValidate(){
        boolean auth = false;
        String authr = regAuthedt.getText().toString();
        if (authr.isEmpty()){
            Toast.makeText(this,"please provide the " +
                    "issuing authority",Toast.LENGTH_SHORT ).show();
        }
        else {
            RegistrationActivity.myBundle.putString("Authority" ,authr);
            auth = true;
        }
        return auth;
    }

//    *********************EDUCATION FIELD VALIDATION***********************

    private boolean eduValidate(){
        boolean edu = false;
      String education = regEduedt.getText().toString();
      if (education.isEmpty()){
          Toast.makeText(this,"please provide the " +
                  "education details",Toast.LENGTH_SHORT ).show();
      }
      else {
          RegistrationActivity.myBundle.putString("Education" ,education);
          edu = true;
      }
      return  edu;
    }

//    *********************SPECIALITY FIELD VALIDATION***********************

    private boolean specValidate(){
        boolean spec = false;
        String speciality = regSpecedt.getText().toString();
        if (speciality.isEmpty()){
            Toast.makeText(this,"please provide the " +
                    "speciality",Toast.LENGTH_SHORT ).show();
        }
        else {
            RegistrationActivity.myBundle.putString("Speciality" ,speciality);
            spec = true;
        }
        return  spec;
    }

//    *********************PHONE NUMBER FIELD VALIDATION***********************

    private boolean phoneValidate(){
        boolean phn = false;
        String phone = regPhnedt.getText().toString();
        if (phone.isEmpty()){
            Toast.makeText(this,"please provide the " +
                    "phone number",Toast.LENGTH_SHORT ).show();
        }
        else {
            RegistrationActivity.myBundle.putString("Phone" ,phone);
            phn = true;
        }
        return  phn;
    }

//    *********************STATE FIELD VALIDATION***********************

    private boolean stateValidate(){
        boolean state = false;
        String stateaddr = regStateedt.getText().toString();
        if (stateaddr.isEmpty()){
            Toast.makeText(this,"please provide which " +
                    "state you belong",Toast.LENGTH_SHORT ).show();
        }
        else {
            RegistrationActivity.myBundle.putString("State" ,stateaddr);
            state = true;
        }
        return  state;
    }

//    *********************Registration Id FIELD VALIDATION***********************

    private boolean regValidate(){
        boolean id = false;
        String regid = regIdedt.getText().toString();
        if (regid.isEmpty()){
            Toast.makeText(this,"please provide which " +
                    "registration Id",Toast.LENGTH_SHORT ).show();
        }
        else {
            RegistrationActivity.myBundle.putString("RegId" ,regid);
            id = true;
        }
        return  id;
    }


//    +++++++++++++++++++++++++DOCTOR PRE REGISTRATION+++++++++++++++++++++++

    public void docPreRegistration(){
        progress = new ProgressDialog(this);
        progress.setMessage("please wait...");
        progress.setCancelable(false);
        progress.show();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JSONObject json = new JSONObject();
        try{
            if (otpres == false){
                json.accumulate("phoneNumber", regPhnedt.getText().toString());
                json.accumulate("role","doctor");
                //System.out.println(json+"otp is false---------------->>>>>>>>>>>>>>");
            }
            else if (otpres == true){
                json.accumulate("name",regNameedt.getText().toString());
//                json.accumulate("gender",radiosex.getText().toString());
                json.accumulate("dob",regDobTv.getText().toString());
                json.accumulate("city",regCityedt.getText().toString());
                json.accumulate("phoneNumber", regPhnedt.getText().toString());
                json.accumulate("otp",regOtpedt.getText().toString());
                json.accumulate("deviceId",deviceid);
                json.accumulate("role","doctor");
                //System.out.println(json+"otp is true---------------->>>>>>>>>>>>>>");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        String url = getResources().getString(R.string.json_get_url);
        String result = "";
        String url = "http://ec2-13-232-207-92.ap-south-1.compute.amazonaws.com:5023/api/User/";
        if (otpres == false){
            url = "http://ec2-13-232-207-92.ap-south-1.compute.amazonaws.com:5023/api/User/preregistration";
        }
        else {
            url = "http://ec2-13-232-207-92.ap-south-1.compute.amazonaws.com:5023/api/User/registration";
        }

        //System.out.println("url---------------->>>>>>>>>>"+ url);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url,json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        progress.dismiss();

                        //System.out.println("response-------------------------------->>>>>>>>>>>>>>>"+response.toString());

   try{
                            JSONObject obj = response.getJSONObject("result");
                            if (otpres == false){
//                                JSONObject obj = response.getJSONObject("result");
                                String msg = obj.getString("message");
                                //System.out.println("msg--------------->>>"+msg.toString());
                                if (msg.equals("OTP sent to registered number")){
                                    regThirdLayout.setVisibility(View.GONE);
                                    regFourthLayout.setVisibility(View.VISIBLE);
                                }
                                else if (msg.equals("User already registered")){
                                    Toast toast = Toast.makeText(getApplicationContext(),
                                            "Number already registered.",
                                            Toast.LENGTH_SHORT);

                                    toast.show();
                                }
                            }

                            else{
                                String msg = obj.getString("message");
                                if ( msg.equals("User created Successfully")){
                                    //System.out.println("dashboard activity--------------->>>");
                                    Intent intentdashboard = new Intent(getBaseContext(),   DashboardActivity.class);
                                    startActivity(intentdashboard);
                                    JSONObject userObject = obj.getJSONObject("result");

                                    String username = userObject.getString("name");
                                    String doctorId = userObject.getString("doctorId");
                                    String userId = userObject.getString("userId");
//                                    String myUserId = userId.substring(userId.lastIndexOf("#")+1);

                                    //System.out.println("msg--------------->>>"+msg.toString());
//                                    //System.out.println("seperated--------->>>"+myUserId);
                                    //System.out.println("username----------------"+username);
                                    //System.out.println("doctorId-----------------"+doctorId);
                                    //System.out.println("userId------------->>>>>>>>>>>"+userId);

                                    SharedPreferences.Editor editor = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE).edit();
                                    editor.putString("doctorId",userObject.getString("doctorId"));
                                    editor.putString("userId",userObject.getString("userId"));
//                                    editor.putString("myUserId",userId.substring(userId.lastIndexOf("#")+1));
                                    editor.putString("username",userObject.getString("name"));
                                    //System.out.println("editor------------>>>>>>>>>>"+userObject.getString("doctorId"));
                                    editor.commit();
                                }
                                else if (msg.equals("Otp verification failed")){
                                    regFourthLayout.setVisibility(View.VISIBLE);
                                    Toast toast = Toast.makeText(getApplicationContext(),
                                            "Plese enter the correct OTP",
                                            Toast.LENGTH_SHORT);

                                    toast.show();
                                }

                            }

                        }
                        catch (JSONException e) {
                            // If an error occurs, this prints the error to the log
                            e.printStackTrace();
                        }
                        try {
                            JSONObject serverResp = new JSONObject(response.toString());

                        } catch (JSONException e) {
// TODO Auto-generated catch block
                            progress.dismiss();
                            //System.out.println("cache------------------------------------------>>>>>>>>>>>>>>");
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progress.dismiss();
                //System.out.println("Error getting response------------------------");
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

}
