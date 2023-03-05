package com.example.where2eat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.where2eat.databinding.AdapterBookingsCardBinding;
import com.example.where2eat.domain.model.Booking;
import com.example.where2eat.domain.model.Restaurant;
import com.example.where2eat.domain.viewmodel.RestaurantViewModal;
import com.example.where2eat.roomdatabase.DBHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


public class AdapterBookingCard extends RecyclerView.Adapter<AdapterBookingCard.ViewHolder> {


    AdapterBookingsCardBinding binding = null;
    private Context context;
    private List<Booking> bookingList;

    private RestaurantViewModal restaurantViewModal;

    private List<Restaurant> restaurantList = new ArrayList<>();
    NavController navController;

    public AdapterBookingCard(Context context, List<Booking> bookingList) {
        this.context = context;
        this.bookingList = bookingList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = AdapterBookingsCardBinding.inflate(LayoutInflater.from(context), parent, false);
        navController = Navigation.findNavController(parent);
        restaurantViewModal = new ViewModelProvider(((MainActivity) context)).get(RestaurantViewModal.class);

        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return this.bookingList != null ? this.bookingList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            new Thread(() -> {
                List<Restaurant> list = DBHelper.getInstance((MainActivity) context).getRestaurantDao().findAll();
                restaurantList.clear();
                restaurantList.addAll(list);
                binding.textRagioneSocialeCardBooking.post(() -> {
                    itemView.setOnClickListener(v -> {
                        Restaurant restaurant = restaurantList.stream().filter(r -> Objects.equals(r.getId(), bookingList.get(getAdapterPosition()).getRistoranteId())).collect(Collectors.toList()).get(0);
                        restaurantViewModal.setRestaurant(restaurant);
                        navController.navigate(R.id.restaurantDetailsFragment);
                    });
                });
            }).start();

        }

        public void onBind(int position) {
            Booking booking = bookingList.get(position);
            binding.setBooking(booking);
        }
    }


}
