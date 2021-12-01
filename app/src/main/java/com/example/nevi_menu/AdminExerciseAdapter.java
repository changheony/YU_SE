package com.example.nevi_menu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nevi_menu.ui.exercise.OnItemClick;

import java.util.ArrayList;

public class AdminExerciseAdapter extends RecyclerView.Adapter<AdminExerciseAdapter.CustomViewHolder> {

    private ArrayList<AdminExerciseItem> arrayList;
    private OnItemClick mCallback;

    public AdminExerciseAdapter(ArrayList<AdminExerciseItem> arrayList, OnItemClick listener) {
        this.arrayList = arrayList;
        this.mCallback = listener;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_admin_exercise_edit_item,parent,false);
        CustomViewHolder holder = new CustomViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder,int position) {
        holder.exercise_select_item_name.setText(arrayList.get(position).getTv_name());
        holder.exercise_select_item_erase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                remove(holder.getAdapterPosition());
                mCallback.onClick(holder.exercise_select_item_name.getText().toString());
            }
        });
    }

    @Override
    public int getItemCount() { return (arrayList != null ? arrayList.size() : 0); }

    public void remove(int position){
        try{
            arrayList.remove(position);
            notifyItemRemoved(position);
        }catch(IndexOutOfBoundsException ex){
            ex.printStackTrace();
        }
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        protected TextView exercise_select_item_name;
        protected ImageView exercise_select_item_erase;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            exercise_select_item_name = itemView.findViewById(R.id.exercise_select_item_name);
            exercise_select_item_erase = itemView.findViewById(R.id.exercise_select_item_erase);
        }
    }
}
