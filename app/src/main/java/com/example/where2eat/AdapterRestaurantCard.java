package com.example.where2eat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.where2eat.databinding.AdapterRestaurantCardBinding;
import com.example.where2eat.domain.modal.Restaurant;

import java.util.ArrayList;
import java.util.List;

public class AdapterRestaurantCard extends RecyclerView.Adapter<AdapterRestaurantCard.ViewHolder> {

    AdapterRestaurantCardBinding binding = null;

    List<Restaurant> restaurantList;

    public AdapterRestaurantCard(List<Restaurant> restaurantList) {
        this.restaurantList = restaurantList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = AdapterRestaurantCardBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {

        return this.restaurantList != null ? this.restaurantList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        public ViewHolder(View itemView) {
            super(itemView);
        }

        public void onBind(int position) {

            Restaurant restaurant = restaurantList.get(position);
            binding.setRestaurant(restaurant);
        }
    }
}
