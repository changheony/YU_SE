package com.example.nevi_menu.ui.exercise;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nevi_menu.R;

import java.util.ArrayList;

public class ExerciseSelectItemAdapter extends RecyclerView.Adapter<ExerciseSelectItemAdapter.ExerciseSelectItemViewHolder> {

    private ArrayList<ExerciseSelectItem> arrayList;
    private Context context;
    private OnItemClick mCallback;


    public ExerciseSelectItemAdapter(ArrayList<ExerciseSelectItem> arrayList, Context context,OnItemClick listener) {
        this.arrayList = arrayList;
        this.context = context;
        this.mCallback = listener;

    }

    @NonNull
    @Override
    public ExerciseSelectItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_exercise_select_item, parent, false);
        ExerciseSelectItemViewHolder holder = new ExerciseSelectItemViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseSelectItemViewHolder holder,final int position) {
        holder.exercise_select_item_name.setText(arrayList.get(position).getTv_name());

        holder.exercise_select_item_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    mCallback.onClick(holder.exercise_select_item_name.getText().toString());
            }
        });

    }


    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class ExerciseSelectItemViewHolder extends RecyclerView.ViewHolder {
        Button exercise_select_item_name;

        public ExerciseSelectItemViewHolder(@NonNull View itemView) {
            super(itemView);
            this.exercise_select_item_name = itemView.findViewById(R.id.exercise_select_item_name);

        }
    }
}
