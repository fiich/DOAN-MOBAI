package com.example.hoangvancook.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hoangvancook.Models.Ingredient;
import com.example.hoangvancook.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class InstructionsIngredientsAdapter extends RecyclerView.Adapter<InstructionIngredientsViewHolder> {
    Context context;
    List<Ingredient> list;

    public InstructionsIngredientsAdapter(Context context, List<Ingredient> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public InstructionIngredientsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InstructionIngredientsViewHolder(LayoutInflater.from(context).inflate(R.layout.list_instructions_step_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull InstructionIngredientsViewHolder holder, int position) {

        Picasso.get().load(list.get(position).image)
                .placeholder(R.drawable.image_error)
                .error(R.drawable.image_error)
                .into(holder.imageView_instructions_step_item);
        holder.textView_instructions_step_item.setText(list.get(position).name);
        holder.textView_instructions_step_item.setSelected(true);
    }
    @Override
    public int getItemCount() {
        return list.size();
    }
}

class InstructionIngredientsViewHolder extends RecyclerView.ViewHolder {
    ImageView imageView_instructions_step_item;
    TextView textView_instructions_step_item;
    public InstructionIngredientsViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView_instructions_step_item = itemView.findViewById(R.id.imageView_instructions_step_item);
        textView_instructions_step_item = itemView.findViewById(R.id.textView_instructions_step_item);
    }
}