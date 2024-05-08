
package com.example.hoangvancook.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hoangvancook.Listeners.Clickitem;
import com.example.hoangvancook.Models.Result;
import com.example.hoangvancook.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.myviewholder> {
    private Context context;
    private Clickitem<Result> onclickitem;
    public List<Result> results;

    public SearchAdapter(Context context, Clickitem<Result> onclickitem, List<Result> results) {
        this.context = context;
        this.onclickitem = onclickitem;
        this.results = results;
    }


    @NonNull
    @Override
    public SearchAdapter.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search,parent,false);
        return new myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.myviewholder holder, int position) {
        Result result = results.get(position);
        holder.name.setText(result.getTitle());
        Picasso.get().load(result.getImage()).into(holder.imageView_search_item);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onclickitem.onclick(result);
            }
        });
    }

    @Override
    public int getItemCount() {
        return results.size();
    }
    public static class myviewholder extends RecyclerView.ViewHolder{
        private TextView name;
        private ImageView imageView_search_item;
        public myviewholder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.search_item);
            imageView_search_item = itemView.findViewById(R.id.imageView_foods);
        }
    }
}