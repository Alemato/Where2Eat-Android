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
import com.example.where2eat.domain.modal.Booking;
import com.example.where2eat.domain.modal.Restaurant;
import com.example.where2eat.tools.VolleyRequests;

import java.util.List;


public class AdapterBookingCard extends RecyclerView.Adapter<AdapterBookingCard.ViewHolder> {


    AdapterBookingsCardBinding binding = null;
    private Context context;
    private List<Booking> bookingList;
    private BookingViewModel bookingViewModel;

    private RestaurantViewModal restaurantViewModal;

    NavController navController;

    public AdapterBookingCard(Context context, List<Booking> bookingList) {
        this.context = context;
        this.bookingList = bookingList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = AdapterBookingsCardBinding.inflate(LayoutInflater.from(context), parent, false);
        bookingViewModel = new ViewModelProvider(((MainActivity) context)).get(BookingViewModel.class);
        navController = Navigation.findNavController(parent);
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
            //Restaurant restaurant = bookingList.get(getAdapterPosition()).getRistoranteId();
            //restaurantViewModal.setRestaurant(restaurant);
            binding.textRagioneSocialeCardBooking.setOnClickListener(v -> {
                navController.navigate(R.id.restaurantDetailsFragment);
            });
        }

        public void onBind(int position) {
            Booking booking = bookingList.get(position);
            binding.setBooking(booking);
            binding.imageViewCardBooking.setImageUrl(booking.getImmagine(), VolleyRequests.getInstance(binding.imageViewCardBooking.getContext()).getImageLoader());
        }
    }


}
