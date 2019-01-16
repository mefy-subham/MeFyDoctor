package com.example.anisha.mefydoctor.controller;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.anisha.mefydoctor.constant.APIConstant;
import com.example.anisha.mefydoctor.iinterface.iHttpController;
import com.example.anisha.mefydoctor.iinterface.iHttpResultHandler;
import com.example.anisha.mefydoctor.model.CallIdModel;
import com.example.anisha.mefydoctor.model.CallModel;


import java.util.Map;

public class HttpController implements iHttpController
{

    private static HttpController _httpController;
    private RequestQueue _requestQueue;
    private iHttpResultHandler _resultHandler;

    public static final HttpController getInstance()
    {
        if(_httpController == null)
        {
            _httpController = new HttpController();
        }

        return _httpController;
    }

    public void goGarbge()
    {
        _httpController = null;
    }


    @Override
    public void placeCall(CallModel callModel, Context context, String operationFlag) {

        if(_requestQueue == null)
        {
            _requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }

        String url = APIConstant.SEND_FCM_NOTIFICATION;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        //_videoResume.setVisibility(View.VISIBLE);

                        if(_resultHandler != null)
                        {
                            _resultHandler.onSuccess(response,operationFlag);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(_resultHandler != null)
                {
                    _resultHandler.onError(error,operationFlag);
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                //CallModel.getParamMap()

                return callModel.getParamMap(callModel);
            }
        };
        // Add the request to the RequestQueue.

        //30 seconds timeout
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);

        _requestQueue.add(stringRequest);

    }

    @Override
    public void saveCall(CallIdModel callIdModel, Context context, String operationFlag) {
        if(_requestQueue == null)
        {
            _requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }

        String url = APIConstant.CALL_HISTORY;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        //_videoResume.setVisibility(View.VISIBLE);

                        if(_resultHandler != null)
                        {
                            _resultHandler.onSuccess(response,operationFlag);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(_resultHandler != null)
                {
                    _resultHandler.onError(error,operationFlag);
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                return callIdModel.getParamMap(callIdModel);
            }
        };
        // Add the request to the RequestQueue.

        //30 seconds timeout
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);

        _requestQueue.add(stringRequest);
    }

    public void set_resultHandler(iHttpResultHandler _resultHandler) {
        this._resultHandler = _resultHandler;
    }


}
