package com.example.where2eat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.where2eat.databinding.FragmentBookingsBinding;
import com.example.where2eat.domain.modal.Booking;

import java.util.ArrayList;
import java.util.List;

public class BookingFragment extends Fragment {


    FragmentBookingsBinding binding = null;

    private BookingViewModel bookingViewModel;

    private List<Booking> bookingList = new ArrayList<>();

    private AdapterBookingCard adapterBookingCard;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBookingsBinding.inflate(inflater, container, false);
        binding.recyclerViewBookings.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapterBookingCard = new AdapterBookingCard(requireContext(), bookingList);
        binding.recyclerViewBookings.setAdapter(adapterBookingCard);

        bookingViewModel = new ViewModelProvider(requireActivity()).get(BookingViewModel.class);

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        bookingList.clear();
        bookingViewModel.getBookingList().observe(getViewLifecycleOwner(), bookings -> {
            bookingList.addAll(bookings);
            adapterBookingCard.notifyDataSetChanged();
        });
    }

}
