package com.example.srujansai.myapplication;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by srujan sai on 11-07-2017.
 */

public class FriendlistAdapter extends  RecyclerView.Adapter<FriendlistAdapter.MyViewHolder> {
    Context mContext;
    List<Friend> friendlist;
    public FriendlistAdapter(UserDashboard userDashboard, List<Friend> mfrnds) {

        this.mContext=userDashboard;
        this.friendlist=mfrnds;
    }

    @Override
    public FriendlistAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.frndrow,parent,false),mContext,friendlist);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Friend fr=friendlist.get(position);
        holder.fn.setText(fr.getFname());
        holder.fphno.setText(fr.getFphno());
        if(fr.getFstatus().equals("accept")){
            int id = mContext.getResources().getIdentifier("com.example.srujansai.myapplication:drawable/accept", null, null);
           holder.stat.setImageResource(id);
        }else if(fr.getFstatus().equals("pending")){
            int id = mContext.getResources().getIdentifier("com.example.srujansai.myapplication:drawable/pending", null, null);
            holder.stat.setImageResource(id);
        }else if(fr.getFstatus().equals("reject")){
            int id = mContext.getResources().getIdentifier("com.example.srujansai.myapplication:drawable/reject", null, null);
            holder.stat.setImageResource(id);
        }



    }

    @Override
    public int getItemCount() {
        return friendlist.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder{
         TextView fn,fphno;
        ImageView stat;
        LinearLayout ly;
        Context mContext;
        List<Friend> friendlist=new ArrayList<Friend>();


        public MyViewHolder(View inflate, final Context mContext, final List<Friend> friendlist) {
            super(inflate);
            this.mContext=mContext;
            this.friendlist=friendlist;
            fn=(TextView)inflate.findViewById(R.id.textView2);
            fphno=(TextView)inflate.findViewById(R.id.textView3);
            stat=(ImageView)inflate.findViewById(R.id.stat);
            ly=(LinearLayout)inflate.findViewById(R.id.lay1);
            ly.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, "U have clicked me", Toast.LENGTH_SHORT).show();
                    int position=getAdapterPosition();
                    Friend fr=friendlist.get(position);
                    if(fr.getFstatus().equals("accept")){
                    Intent in=new Intent(mContext,MapsActivity.class);
                    in.putExtra("name",fr.getFname());
                    in.putExtra("phno",fr.getFphno());
                    mContext.startActivity(in);
                    }else {
                        Toast.makeText(mContext, " Selected Friend is not connected to you", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }
}
