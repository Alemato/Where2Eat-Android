package com.example.where2eat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.where2eat.databinding.FragmentHomeBinding;
import com.example.where2eat.domain.modal.Restaurant;
import com.example.where2eat.domain.modal.User;
import com.example.where2eat.roomdatabase.DBHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeFragment extends Fragment {

    FragmentHomeBinding binding = null;

    private UserViewModel userViewModel;

    List<Restaurant> restaurantList = new ArrayList<>();
    private AdapterRestaurantCard adapterRestaurantCard;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        adapterRestaurantCard = new AdapterRestaurantCard(restaurantList);
        binding.recyclerViewHome.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerViewHome.setAdapter(adapterRestaurantCard);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

        new Thread(new Runnable() {
            @Override
            public void run() {
                restaurantList.clear();

                // RoomDatabase
                //restaurantList.addAll(DBHelper.getInstance(getContext()).getRestaurantDao().findAll());

                binding.textTitleHome.post(new Runnable() {
                    @Override
                    public void run() {
                        adapterRestaurantCard.notifyDataSetChanged();
                    }
                });
            }
        }).start();
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
