package com.ybmt.sbssample;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

/**
 * Created by YBMT on 6/26/2018.
 */

public class UserAdapter extends RecyclerView.Adapter {private Context mContext;
    private List<UserData> usersList;

    public UserAdapter(Context context, List<UserData> list){
        mContext = context;
        usersList = list;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        return new UserViewHolder(LayoutInflater.from(mContext).inflate(R.layout.user_layout,null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position){
        UserData userData = usersList.get(position);
        ((UserViewHolder)holder).nameHolder.setText(userData.getFullName());
        userData.getImageFor(((UserViewHolder)holder).imgHolder);
    }

    @Override
    public int getItemCount(){
        return usersList.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder
    {
        TextView nameHolder;
        ImageView imgHolder;

        public UserViewHolder(View item){
            super(item);
            nameHolder = (TextView) item.findViewById(R.id.name);
            imgHolder = (ImageView) item.findViewById(R.id.img);
        }
    }


}
