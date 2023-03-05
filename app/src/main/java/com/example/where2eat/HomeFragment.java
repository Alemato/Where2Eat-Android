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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.where2eat.databinding.FragmentHomeBinding;
import com.example.where2eat.domain.model.Restaurant;
import com.example.where2eat.domain.model.User;
import com.example.where2eat.domain.viewmodel.RestaurantViewModal;
import com.example.where2eat.domain.viewmodel.UserViewModel;
import com.example.where2eat.roomdatabase.DBHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeFragment extends Fragment {

    FragmentHomeBinding binding = null;
    private RestaurantViewModal restaurantViewModal;


    private List<Restaurant> restaurantList = new ArrayList<>();
    private AdapterRestaurantCard adapterRestaurantCard;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        binding.recyclerViewHome.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapterRestaurantCard = new AdapterRestaurantCard(requireContext(), restaurantList);
        binding.recyclerViewHome.setAdapter(adapterRestaurantCard);
        restaurantViewModal = new ViewModelProvider(requireActivity()).get(RestaurantViewModal.class);
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        restaurantList.clear();
        restaurantViewModal.getRestaurantList().observe(getViewLifecycleOwner(), restaurants -> {
            restaurantList.addAll(restaurants);
            adapterRestaurantCard.notifyDataSetChanged();
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final NavController navController = Navigation.findNavController(view);
        new Thread(() -> {
            User user = DBHelper.getInstance(requireContext()).getUserDao().getUser();
            if (user == null || Objects.equals(user, new User())) {
                binding.textTitleHome.post(() -> {
                    navController.navigate(R.id.loginFragment);
                });
            }
        }).start();
    }

}
