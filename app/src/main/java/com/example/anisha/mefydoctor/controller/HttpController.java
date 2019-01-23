package com.example.anisha.mefydoctor.controller;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.anisha.mefydoctor.constant.APIConstant;
import com.example.anisha.mefydoctor.constant.APPConstant;
import com.example.anisha.mefydoctor.iinterface.iHttpController;
import com.example.anisha.mefydoctor.iinterface.iHttpResultHandler;
import com.example.anisha.mefydoctor.model.CallIdModel;
import com.example.anisha.mefydoctor.model.CallModel;
import com.example.anisha.mefydoctor.model.RoomModel;
import com.example.anisha.mefydoctor.model.TokenDataModel;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
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
        System.out.println("HttpController | placeCall | getStatus: "+callModel.getStatus());
        System.out.println("HttpController | placeCall | getCallee_fcmToken: "+callModel.getCallee_fcmToken());
        System.out.println("HttpController | placeCall | getCaller_fcmToken: "+callModel.getCaller_fcmToken());
        System.out.println("HttpController | placeCall | getCaller_image_url: "+callModel.getCaller_image_url());
        System.out.println("HttpController | placeCall | getRecording_url: "+callModel.getRecording_url());
        System.out.println("HttpController | placeCall | getRoomId: "+callModel.getRoomId());
        System.out.println("HttpController | placeCall | getType: "+callModel.getType());
        System.out.println("HttpController | placeCall | getUserInfo: "+callModel.getUserInfo());

        String url = APIConstant.SEND_FCM_NOTIFICATION;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        System.out.println("HttpController | placeCall | callModel: "+callModel.getStatus());
                        System.out.println("HttpController | placeCall | onResponse: "+response);
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
                    System.out.println("HttpController | placeCall | VolleyError: "+error);
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                //CallModel.getParamMap()
                System.out.println("HttpController | placeCall | callModel.getParamMap(callModel): "+callModel.getParamMap(callModel));
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
        JSONObject call_Idmodel = new JSONObject();
        try {
            call_Idmodel.put(APIConstant.doctorId, callIdModel.getDoctor_Id());
            call_Idmodel.put(APIConstant.startTime, callIdModel.getStartTime());
            call_Idmodel.put(APIConstant.individualId, callIdModel.getIndividual_Id());
            call_Idmodel.put(APIConstant.file, callIdModel.getFile());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, call_Idmodel,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("HttpController | CALL_HISTORY"+response);
                        CallIdModel resultCallID = new CallIdModel();

                        try {
                            if(response.getString("error").equalsIgnoreCase("false")){
                                response=response.getJSONObject("result");
                                resultCallID.setCallId(response.getString("callId"));
                                System.out.println("HttpController | CALL_HISTORY | callId :"+response.getString("callId"));
                            }



                        } catch (JSONException e) {
                            System.out.println("HttpController | JSONException :"+e);
                            e.printStackTrace();
                        }



                        if ( _resultHandler!= null)
                            _resultHandler.onCallId(resultCallID, APPConstant.CALL_HISTORY_SAVE_CALL);
                    }

                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(this.toString(), "Error: " + error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        // Add the request to the RequestQueue.

        //30 seconds timeout
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsObjRequest.setRetryPolicy(policy);

        _requestQueue.add(jsObjRequest);
    }

    @Override
    public void roomCreation(RoomModel roomModel, Context context,String operationFlag) {

        if(_requestQueue == null)
        {
            _requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }

        String url = APIConstant.CREATE_ROOM;

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

                return null;
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
    public void twilioToken(Context context,String operationFlag,String userName,String roomName) {


        if(_requestQueue == null)
        {
            _requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }


        String url = APIConstant.TWILIO_TOKEN+userName+"&roomname="+roomName;



        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("HttpController | twilioToken"+response);
                        TokenDataModel tokenDataModel = new TokenDataModel();

                        try {

                            tokenDataModel.set_twilioToken(response.getString("token"));
                            System.out.println("HttpController | twilioToken :"+response.getString("token"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                        if ( _resultHandler!= null)
                            _resultHandler.onToken(tokenDataModel, APPConstant.TWILIO_TOKEN_OPERATION);
                    }

                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(this.toString(), "Error: " + error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                //headers.put("Authorization", Utils.get_jwtToken());
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
    public void updateCall(CallIdModel callIdModel, Context context, String operationFlag) {
        if(_requestQueue == null)
        {
            _requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }

        String url = APIConstant.UPDATE_CALL_HISTORY+callIdModel.getCallId();

        JSONObject call_Idmodel = new JSONObject();
        try {
            call_Idmodel.put(APIConstant.doctorId, callIdModel.getDoctor_Id());
            call_Idmodel.put(APIConstant.startTime, callIdModel.getStartTime());
            call_Idmodel.put(APIConstant.individualId, callIdModel.getIndividual_Id());
            call_Idmodel.put(APIConstant.file, callIdModel.getFile());
            call_Idmodel.put(APIConstant.endTime, callIdModel.getEndTime());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.PUT, url, call_Idmodel,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("HttpController | updateCall | response"+response);
                        CallIdModel resultCallID = new CallIdModel();

                        try {
                            if(response.getString("error").equalsIgnoreCase("false")){
                                response=response.getJSONObject("result");
                                resultCallID.setCallId(response.getString(APPConstant.CALL_ID));
                                System.out.println("HttpController | CALL_HISTORY | callId :"+response.getString(APPConstant.CALL_ID));
                            }



                        } catch (JSONException e) {
                            System.out.println("HttpController | JSONException :"+e);
                            e.printStackTrace();
                        }



                        if ( _resultHandler!= null)
                            _resultHandler.onCallId(resultCallID, APPConstant.CALL_HISTORY_UPDATE_CALL);
                    }

                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(this.toString(), "Error: " + error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        // Add the request to the RequestQueue.

        //30 seconds timeout
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsObjRequest.setRetryPolicy(policy);

        _requestQueue.add(jsObjRequest);
    }

    public void set_resultHandler(iHttpResultHandler _resultHandler) {
        this._resultHandler = _resultHandler;
    }


}
