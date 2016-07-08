package com.gaojunhui.adapter;

import android.content.ContentResolver;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.gaojunhui.bean.MusicBean;
import com.gaojunhui.day7_1audiorecycler.R;

import java.util.List;

/**
 * Created by Administrator on 2016/7/1.
 */
public class MyRecylerAdapter extends RecyclerView.Adapter<MyRecylerAdapter.MyViewHolder> {
    private List<MusicBean> list;
    private Context context;
    public interface OnItemLisener{
        void onMusicNameClick(View view,int position);
        void onPathClick(View view,int position);
    }
    private OnItemLisener lisener;
    public void setLisener(OnItemLisener lisener){
        this.lisener=lisener;
    }
    public MyRecylerAdapter(List<MusicBean> list,Context context){
        this.context=context;
        this.list=list;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.receycler_item,null);
        MyViewHolder holder=new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
//        holder.tv_path.setText(list.get(position).getPath());
        holder.tv_name.setText(list.get(position).getMusicName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tv_name,tv_path;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv_name= (TextView) itemView.findViewById(R.id.tv_name);
//            tv_path= (TextView) itemView.findViewById(R.id.tv_path);
            tv_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lisener.onMusicNameClick(v,getLayoutPosition());
                    Toast.makeText(context, list.get(getLayoutPosition()).getMusicName()+"歌曲名被点击", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
