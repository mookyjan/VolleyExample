package com.example.mudassirkhan.volleyexample.api;

import android.content.Context;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;

public class GETAPIRequest {
    public void request(final Context context,final FetchDataListener listener,final String apiURL) throws JSONException{
        if (listener!=null){
            listener.onFetchStart();
        }
        //base server APi url
        String baseUrl="http://studypeek.com/test/";
        //add extension api url recieved from caller
        //add make full api
        String url=baseUrl+apiURL;
        JsonObjectRequest getRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (listener != null) {
                        if (response.has("response")) {
                            //recieved response
                            //call onFetchComplete of the listener
                            listener.onFetchComplete(response);
                        } else if (response.has("error")) {
                            //has error in response
                            //call on FetchFailure of the listener
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
                if (error instanceof NoConnectionError){
                    listener.onFetchFailure("Network connectivity Problem");
                }else if (error.networkResponse!=null && error.networkResponse.data!=null){
                    VolleyError volley_error=new VolleyError(new String(error.networkResponse.data));
                    String errorMessage="";
                    try {
                        JSONObject errorJson=new JSONObject(volley_error.getMessage().toString());
                        if (errorJson.has("error")) errorMessage=errorJson.getString("error");
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                    if (errorMessage.isEmpty()){
                        errorMessage=volley_error.getMessage();
                    }
                    if (listener!=null){
                        listener.onFetchFailure(errorMessage);
                    }
                }else {
                    listener.onFetchFailure("Something went wrong please Try Again later");
                }
            }
        });

        RequestQueueService.getInstance(context).addToRequestQueue(getRequest.setShouldCache(false));
    }
}
