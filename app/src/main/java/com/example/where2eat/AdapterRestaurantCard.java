package com.example.where2eat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.where2eat.databinding.AdapterRestaurantCardBinding;
import com.example.where2eat.domain.modal.Restaurant;
import com.example.where2eat.tools.VolleyRequests;

import java.util.ArrayList;
import java.util.List;

public class AdapterRestaurantCard extends RecyclerView.Adapter<AdapterRestaurantCard.ViewHolder> {

    AdapterRestaurantCardBinding binding = null;

    private Context context;
    private List<Restaurant> restaurantList;

    private RestaurantViewModal restaurantViewModal;

    NavController navController;

    public AdapterRestaurantCard(Context context, List<Restaurant> restaurantList) {
        this.context = context;
        this.restaurantList = restaurantList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = AdapterRestaurantCardBinding.inflate(LayoutInflater.from(context), parent, false);
        restaurantViewModal = new ViewModelProvider(((MainActivity) context)).get(RestaurantViewModal.class);
        navController = Navigation.findNavController(parent);
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

    public void updateRestaurants(List<Restaurant> list) {
        this.restaurantList.clear();
        this.restaurantList = list;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {


        public ViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(v -> {
                Restaurant restaurant = restaurantList.get(getAdapterPosition());
                restaurantViewModal.setRestaurant(restaurant);

                navController.navigate(R.id.restaurantDetailsFragment);
            });

        }

        public void onBind(int position) {

            Restaurant restaurant = restaurantList.get(position);
            binding.setRestaurant(restaurant);
            binding.imageView.setImageUrl(restaurant.getImmagine(), VolleyRequests.getInstance(binding.imageView.getContext()).getImageLoader());
        }
    }
}
