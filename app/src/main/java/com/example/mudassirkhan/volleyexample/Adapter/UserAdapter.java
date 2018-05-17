package com.example.mudassirkhan.volleyexample.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mudassirkhan.volleyexample.DataClasses.User;
import com.example.mudassirkhan.volleyexample.R;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private List<User> userList=new ArrayList<>();
    private Context context;
    public UserAdapter(Context context){
        this.context=context;
    }
    public void setUserList(List<User> userList){
        this.userList=userList;
    }
    //invoked by layout Manager to create new views
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //attach layout for singel list
        int layout=R.layout.single_user_layout;
        View view= LayoutInflater.from(parent.getContext()).inflate(layout,parent,false);
        return new  ViewHolder(view);
    }

    //invoked by the layout manager to replace the view contents
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user=userList.get(position);
        holder.showUserDetail(user);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
     TextView txtUserId,txtFirstName,txtLastName,txtAge;

        public ViewHolder(View itemView) {
            super(itemView);
            txtFirstName=itemView.findViewById(R.id.txtFirstName);
            txtLastName=itemView.findViewById(R.id.txtLastName);
            txtAge=itemView.findViewById(R.id.txtAge);
        }
        public void showUserDetail(User user){
            String firstName=user.getFirstName();
            String lastName=user.getLastName();
            int age=user.getAge();
            txtFirstName.setText(firstName);
            txtLastName.setText(lastName);
            txtAge.setText(""+age);
        }
    }

}