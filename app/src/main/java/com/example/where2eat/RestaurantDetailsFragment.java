package com.example.where2eat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.where2eat.databinding.FragmentRestaurantDetailsBinding;
import com.example.where2eat.tools.VolleyRequests;

public class RestaurantDetailsFragment extends Fragment {

    FragmentRestaurantDetailsBinding binding = null;

    RestaurantViewModal restaurantViewModal;


    NavController navController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRestaurantDetailsBinding.inflate(inflater, container, false);
        restaurantViewModal = new ViewModelProvider(requireActivity()).get(RestaurantViewModal.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        restaurantViewModal.getRestaurant().observe(getViewLifecycleOwner(), rest -> {
            binding.setRestaurant(rest);

            binding.imageRestaurantDetails.setImageUrl(rest.getImmagine(), VolleyRequests.getInstance(binding.imageRestaurantDetails.getContext()).getImageLoader());
            binding.bottonePrenotaRestaurantDetails.setOnClickListener(v -> {
                navController.navigate(R.id.homeFragment);
            });
        });
    }
}
