package com.example.where2eat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.where2eat.databinding.FragmentHomeBinding;
import com.example.where2eat.domain.modal.Restaurant;
import com.example.where2eat.roomdatabase.DBHelper;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    FragmentHomeBinding binding = null;

    List<Restaurant> restaurantList = new ArrayList<>();
    private AdapterRestaurantCard adapterRestaurantCard;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        adapterRestaurantCard = new AdapterRestaurantCard(restaurantList);
        binding.recyclerViewHome.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerViewHome.setAdapter(adapterRestaurantCard);
        return binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();

        new Thread(new Runnable() {
            @Override
            public void run() {
                restaurantList.clear();


                // RoomDatabase
                restaurantList.addAll(DBHelper.getInstance(getContext()).getRestaurantDao().findAll());

                binding.textTitleHome.post(new Runnable() {
                    @Override
                    public void run() {
                        adapterRestaurantCard.notifyDataSetChanged();
                    }
                });
            }
        }).start();
    }


}
