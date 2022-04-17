package com.example.shopapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shopapp.R;
import com.example.shopapp.activities.DetailedActivity;
import com.example.shopapp.models.NavCategoryModel;
import com.example.shopapp.models.ViewAllModel;

import java.util.List;

public class ViewAllAdapter extends RecyclerView.Adapter<ViewAllAdapter.ViewHolder> {
    Context context;
    List<ViewAllModel> viewAllModelList;

    public ViewAllAdapter(Context context, List<ViewAllModel> viewAllModelList) {
        this.context = context;
        this.viewAllModelList = viewAllModelList;
    }

    public ViewAllAdapter() {

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_all_item,parent,false));


    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,  int position) {
        Glide.with(context).load(viewAllModelList.get(position).getImg_url()).into(holder.imageView);
        holder.description.setText(viewAllModelList.get(position).getDescription());
        holder.name.setText(viewAllModelList.get(position).getName());
        holder.price.setText(String.valueOf(viewAllModelList.get(position).getPrice()));
        holder.rating.setText(viewAllModelList.get(position).getRating());
        if(viewAllModelList.get(position).getType().equals("egg")){
            holder.price.setText(String.valueOf(viewAllModelList.get(position).getPrice()+"/dozen"));

        }
        if(viewAllModelList.get(position).getType().equals("milk")){
            holder.price.setText(String.valueOf(viewAllModelList.get(position).getPrice()+"/litre"));

        }
///get data from viewmodel
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, DetailedActivity.class);
                intent.putExtra("detail", viewAllModelList.get(holder.getAdapterPosition()));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return viewAllModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView name,description,price,rating;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.view_img);
            name=itemView.findViewById(R.id.view_name);
            description=itemView.findViewById(R.id.view_description);
            rating=itemView.findViewById(R.id.view_rating);
            price=itemView.findViewById(R.id.view_price);

        }
    }
}
