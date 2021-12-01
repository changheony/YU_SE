package com.example.nevi_menu.ui.exercise;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.nevi_menu.R;

import java.util.ArrayList;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder> {

    private ArrayList<Exercise> arrayList;
    private Context context;
    private OnItemClick mCallback;
    private OnItemViewClick mCallback2;

    private boolean visibility = false;

    public ExerciseAdapter(ArrayList<Exercise> arrayList, Context context,OnItemClick listener, OnItemViewClick listener2) {
        this.arrayList = arrayList;
        this.context = context;
        this.mCallback = listener;
        this.mCallback2= listener2;
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_exercise_item,parent, false);
        ExerciseViewHolder holder = new ExerciseViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        Glide.with(holder.itemView)
                .load(arrayList.get(position).getIv_profile())
                .into(holder.iv_profile);
        holder.tv_name.setText(arrayList.get(position).getTv_name());
        holder.tv_time.setText(arrayList.get(position).getTv_time());

        if(visibility){
            holder.tv_erase.setVisibility(View.VISIBLE);
        }
        else{
            holder.tv_erase.setVisibility(View.INVISIBLE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback2.onItemClick(holder.tv_name.getText().toString());
            }
        });

        holder.tv_erase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                remove(holder.getAdapterPosition());
                mCallback.onClick(holder.tv_name.getText().toString());
            }
        });

    }



    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public void remove(int position){
        try{
            arrayList.remove(position);
            notifyItemRemoved(position);
        }catch(IndexOutOfBoundsException ex){
            ex.printStackTrace();
        }
    }


    public void setVisibility(){
        if(this.visibility == true){
            this.visibility = false;
        }else{
            this.visibility = true;
        }
        notifyDataSetChanged();
    }


    public class ExerciseViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_profile;
        TextView tv_name;
        TextView tv_time;
        ImageView tv_erase;

        public ExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            this.iv_profile = itemView.findViewById(R.id.iv_profile);
            this.tv_name = itemView.findViewById(R.id.tv_name);
            this.tv_time = itemView.findViewById(R.id.tv_time);
            this.tv_erase = itemView.findViewById(R.id.tv_erase);
        }
    }
}
