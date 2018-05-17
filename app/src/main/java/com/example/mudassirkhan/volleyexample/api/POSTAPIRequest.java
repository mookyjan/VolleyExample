package com.example.mudassirkhan.volleyexample.api;

import android.content.Context;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class POSTAPIRequest {
    public void request(final Context context, final FetchDataListener listener, JSONObject params,final String ApiUrl) throws JSONException{

        if (listener!=null){
            //indicates calling start
            listener.onFetchStart();
        }
        //base url
        String baseUrl="http://studypeek.com/test/";
        //add extention recieved from caller
        String url=baseUrl+ApiUrl;
        //Requesting with post request as Params
        JsonObjectRequest postRequest=new JsonObjectRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (listener != null) {
                        if (response.has("response")) {
                            //recieved response
                            //call on fetchComplete
                            listener.onFetchComplete(response);
                        } else if (response.has("error")) {
                            //has error in response
                            //call on fetech Failure
                            listener.onFetchFailure(response.getString("error"));
                        } else {
                            listener.onFetchComplete(null);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NoConnectionError) {
                    listener.onFetchFailure("Network Connectivity Problem");
                } else if (error.networkResponse != null && error.networkResponse.data != null) {
                    VolleyError volley_error = new VolleyError(new String(error.networkResponse.data));
                    String errorMessage      = "";
                    try {
                        JSONObject errorJson = new JSONObject(volley_error.getMessage().toString());
                        if(errorJson.has("error")) errorMessage = errorJson.getString("error");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (errorMessage.isEmpty()) {
                        errorMessage = volley_error.getMessage();
                    }

                    if (listener != null) listener.onFetchFailure(errorMessage);
                } else {
                    listener.onFetchFailure("Something went wrong. Please try again later");
                }
            }
        });
        RequestQueueService.getInstance(context).addToRequestQueue(postRequest.setShouldCache(false));
    }
}
