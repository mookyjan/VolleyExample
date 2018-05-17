package com.example.mudassirkhan.volleyexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mudassirkhan.volleyexample.Adapter.UserAdapter;
import com.example.mudassirkhan.volleyexample.DataClasses.User;
import com.example.mudassirkhan.volleyexample.api.FetchDataListener;
import com.example.mudassirkhan.volleyexample.api.GETAPIRequest;
import com.example.mudassirkhan.volleyexample.api.POSTAPIRequest;
import com.example.mudassirkhan.volleyexample.api.RequestQueueService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView resultTextView;
    private Button btnGetApi,btnPostApi;
    RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private List<User> userList;
    private UserAdapter userAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resultTextView=(TextView)findViewById(R.id.txtResult);
        btnGetApi=(Button)findViewById(R.id.getApiButton);
        btnPostApi=(Button)findViewById(R.id.postApiButton);
        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        //Attaching OnClickListener with Buttons
        btnGetApi.setOnClickListener(getApiListener);
        btnPostApi.setOnClickListener(postApiListener);
        linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
    }

    View.OnClickListener getApiListener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //Call getApiCall() method
            getApiCall();
        }
    };

    View.OnClickListener postApiListener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //Call postApiCall() method
            postApiCall();
        }
    };

    private void getApiCall(){
        try{
            //Create Instance of GETAPIRequest and call it's
            //request() method
            GETAPIRequest getapiRequest=new GETAPIRequest();
            //Attaching only part of URL as base URL is given
            //in our GETAPIRequest(of course that need to be same for all case)
            String url="webapi.php?userId=1";
            getapiRequest.request(MainActivity.this,fetchGetResultListener,url);
            Toast.makeText(MainActivity.this,"GET API called",Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //Implementing interfaces of FetchDataListener for GET api request
    FetchDataListener fetchGetResultListener=new FetchDataListener() {
        @Override
        public void onFetchComplete(JSONObject data) {
            //Fetch Complete. Now stop progress bar  or loader
            //you started in onFetchStart
            RequestQueueService.cancelProgressDialog();
            try {
                //Now check result sent by our GETAPIRequest class
                if (data != null) {
                    if (data.has("success")) {
                        int success = data.getInt("success");
                        if(success==1){
                            JSONObject response=data.getJSONObject("response");
                            if(response!=null) {
                                //Display the result
                                //Or, You can do whatever you need to
                                //do with the JSONObject
                                //resultTextView.setText(response.toString(4));
                                userList=new ArrayList<>();
                                userAdapter=new UserAdapter(MainActivity.this);;
                                recyclerView.setAdapter(userAdapter);
                                for (int i=0;i<5;i++) {
                                    User user = new User();
                                    user.setFirstName(response.getString("firstName"));
                                    user.setLastName(response.getString("lastName"));
                                    user.setAge(response.getInt("age"));
                                    userList.add(user);
                                }
                                //set items to adapter
                                userAdapter.setUserList(userList);
                                userAdapter.notifyDataSetChanged();

                            }
                        }else{
                            RequestQueueService.showAlert("Error! No data fetched", MainActivity.this);
                        }
                    }
                } else {
                    RequestQueueService.showAlert("Error! No data fetched", MainActivity.this);
                }
            }catch (Exception e){
                RequestQueueService.showAlert("Something went wrong", MainActivity.this);
                e.printStackTrace();
            }
        }

        @Override
        public void onFetchFailure(String msg) {
            RequestQueueService.cancelProgressDialog();
            //Show if any error message is there called from GETAPIRequest class
            RequestQueueService.showAlert(msg,MainActivity.this);
        }

        @Override
        public void onFetchStart() {
            //Start showing progressbar or any loader you have
            RequestQueueService.showProgressDialog(MainActivity.this);
        }
    };

    private void postApiCall(){
        try{
            //Create Instance of POSTAPIRequest and call it's
            //request() method
            POSTAPIRequest postapiRequest=new POSTAPIRequest();
            //Attaching only part of URL as base URL is given
            //in our POSTAPIRequest(of course that need to be same for all case)
            String url="webapi.php";
            JSONObject params=new JSONObject();
            try {
                //Creating POST body in JSON format
                //to send in POST request
                params.put("userId",2);
            }catch (Exception e){
                e.printStackTrace();
            }
            postapiRequest.request(MainActivity.this,fetchPostResultListener,params,url);
            Toast.makeText(MainActivity.this,"POST API called",Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //Implementing interfaces of FetchDataListener for POST api request
    FetchDataListener fetchPostResultListener=new FetchDataListener() {
        @Override
        public void onFetchComplete(JSONObject data) {
            //Fetch Complete. Now stop progress bar  or loader
            //you started in onFetchStart
            RequestQueueService.cancelProgressDialog();
            try {
                //Now check result sent by our POSTAPIRequest class
                if (data != null) {
                    if (data.has("success")) {
                        int success = data.getInt("success");
                        if(success==1){
                            JSONObject response=data.getJSONObject("response");
                            if(response!=null) {
                                //Display the result
                                //Or, You can do whatever you need to
                                //do with the JSONObject
                                resultTextView.setText(response.toString(4));
                            }
                        }else{
                            RequestQueueService.showAlert("Error! No data fetched", MainActivity.this);
                        }
                    }
                } else {
                    RequestQueueService.showAlert("Error! No data fetched", MainActivity.this);
                }
            }catch (Exception e){
                RequestQueueService.showAlert("Something went wrong", MainActivity.this);
                e.printStackTrace();
            }
        }

        @Override
        public void onFetchFailure(String msg) {
            RequestQueueService.cancelProgressDialog();
            //Show if any error message is there called from POSTAPIRequest class
            RequestQueueService.showAlert(msg,MainActivity.this);
        }

        @Override
        public void onFetchStart() {
            //Start showing progressbar or any loader you have
            RequestQueueService.showProgressDialog(MainActivity.this);
        }
    };
}
