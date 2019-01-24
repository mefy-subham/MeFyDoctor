package com.example.anisha.mefydoctor.controller;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.anisha.mefydoctor.constant.APIConstant;
import com.example.anisha.mefydoctor.constant.APPConstant;
import com.example.anisha.mefydoctor.handler.UserHandler;
import com.example.anisha.mefydoctor.iinterface.iHttpProfileController;
import com.example.anisha.mefydoctor.iinterface.iHttpResultHandler;
import com.example.anisha.mefydoctor.model.UserDataModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HttpProfileController implements iHttpProfileController {


    private static HttpProfileController _Instance;
    private iHttpResultHandler _iHttpResultHandler;
    private RequestQueue _requestQueue;

    public static HttpProfileController getInstance()
    {
        if(_Instance == null)
            _Instance =  new HttpProfileController();

        return _Instance;
    }

    public  void  gogarbage()
    {
        if (_Instance != null)
        {
            _Instance = null;
        }
    }

    @Override
    public void setProfileData(UserDataModel profileData, Context context) {

    }

    @Override
    public void updateProfileData(UserDataModel profileData, Context context) {
        String url = APIConstant.SEND_FCM_TOKEN;


        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(APIConstant.PARAM_SET_FCM, profileData.getFcmtoken());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (_requestQueue == null) {
            _requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.PUT, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        //System.out.println("updateProfileData response: " + response);
                        if (_iHttpResultHandler != null)
                            _iHttpResultHandler.onSuccess(profileData, APPConstant.GET_USER_UPDATED_INFO);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (_iHttpResultHandler != null)
                    _iHttpResultHandler.onError(error.getMessage(), APPConstant.GET_USER_UPDATED_INFO);
                //System.out.println("updateProfileData onErrorResponse: " + error.getMessage() + error);

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }

        };
        //30 seconds timeout
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsObjRequest.setRetryPolicy(policy);

        _requestQueue.add(jsObjRequest);
    }

    @Override
    public void getProfileData(String phoneNo, Context context) {

    }
    public void set_iHttpResultHandler(iHttpResultHandler _iHttpResultHandler) {
        this._iHttpResultHandler = _iHttpResultHandler;
    }
}


