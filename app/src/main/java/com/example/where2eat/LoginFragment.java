package com.example.where2eat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.where2eat.databinding.FragmentLoginBinding;
import com.example.where2eat.domain.model.Booking;
import com.example.where2eat.domain.model.Restaurant;
import com.example.where2eat.domain.model.User;
import com.example.where2eat.domain.model.UserNamePassword;
import com.example.where2eat.domain.viewmodel.BookingViewModel;
import com.example.where2eat.domain.viewmodel.RestaurantViewModal;
import com.example.where2eat.domain.viewmodel.UserViewModel;
import com.example.where2eat.roomdatabase.DBHelper;
import com.example.where2eat.service.AuthService;
import com.example.where2eat.service.BookingService;
import com.example.where2eat.service.RestaurantService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class LoginFragment extends Fragment {

    FragmentLoginBinding binding = null;
    private boolean isProgressVisible = false;
    private UserViewModel userViewModel;
    private RestaurantViewModal restaurantViewModal;
    private BookingViewModel bookingViewModel;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                String action = intent.getAction();
                switch (action) {
                    case AuthService.LOGIN_SUCCESSFUL:
                        new Thread(() -> {
                            User user = DBHelper.getInstance(requireContext()).getUserDao().getUser();
                            if (user != null && !Objects.equals(user, new User())) {
                                userViewModel.setUser(user);
                            }
                            downloadRestauransts();
                        }).start();
                        return;
                    case AuthService.LOGIN_ERROR:
                        unlockLogin();
                        Toast.makeText(requireContext(), "Errore di Autenticazione", Toast.LENGTH_SHORT).show();
                        return;
                    case AuthService.INTERNET_LOGIN_ERROR:
                    case BookingService.INTERNET_BOOKING_ERROR:
                    case RestaurantService.INTERNET_RESTAURANTS_ERROR:
                        unlockLogin();
                        logout();
                        Toast.makeText(requireContext(), "Errore, Non sei connesso ad internet!", Toast.LENGTH_SHORT).show();
                        return;
                    case RestaurantService.DOWNLOAD_RESTAURANTS_COMPLETED:
                        new Thread(() -> {
                            List<Restaurant> restaurantList = DBHelper.getInstance(requireContext()).getRestaurantDao().findAll();
                            if (restaurantList != null && !restaurantList.equals(new ArrayList<>()) && restaurantList.size() > 0) {
                                restaurantViewModal.setRestaurantList(restaurantList);
                            }
                            downloadBookings();
                        }).start();
                        return;
                    case RestaurantService.DOWNLOAD_RESTAURANTS_ERROR:
                        unlockLogin();
                        Toast.makeText(requireContext(), "Errore nella ripresa dei ristoranti", Toast.LENGTH_SHORT).show();
                        logout();
                        return;
                    case BookingService.DOWNLOAD_BOOKINGS_SUCCESSFUL:
                        new Thread(() -> {
                            List<Booking> bookingList = DBHelper.getInstance(requireContext()).getBookingDao().findAll();
                            if (bookingList != null && !bookingList.equals(new ArrayList<>()) && bookingList.size() > 0) {
                                bookingViewModel.setBookingList(bookingList);
                            }
                            binding.textUserNameLogin.post(() -> {
                                unlockLogin();
                                final NavController navController = Navigation.findNavController(requireView());
                                navController.navigate(R.id.homeFragment);
                            });
                        }).start();
                        return;
                    case BookingService.DOWNLOAD_BOOKINGS_ERROR:
                        unlockLogin();
                        Toast.makeText(requireContext(), "Errore nella ripresa delle Prenotazioni", Toast.LENGTH_SHORT).show();
                        logout();
                        return;
                }
            }
        }
    };


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        restaurantViewModal = new ViewModelProvider(requireActivity()).get(RestaurantViewModal.class);
        bookingViewModel = new ViewModelProvider(requireActivity()).get(BookingViewModel.class);
        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!binding.textUserNameLogin.getText().toString().equals("") && !binding.textPasswordLogin.getText().toString().equals("")) {
                    if (isProgressVisible) {
                        unlockLogin();
                    } else {
                        UserNamePassword userNamePassword = new UserNamePassword(binding.textUserNameLogin.getText().toString(), binding.textPasswordLogin.getText().toString());
                        Intent intent = new Intent(requireContext(), AuthService.class);
                        intent.putExtra(AuthService.KEY_ACTION, AuthService.ACTION_LOGIN);
                        intent.putExtra(AuthService.KEY_USER_PASSWORD, userNamePassword);
                        requireActivity().startService(intent);
                        blockLogin();
                    }
                } else {
                    Toast.makeText(requireContext(), "Riempi i campi prima di effettaure la login!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        AppCompatActivity act = (AppCompatActivity) getActivity();
        if (act != null) {
            ActionBar a = act.getSupportActionBar();
            if (a != null) {
                a.hide();
            }
        }
    }

    private void blockLogin() {
        isProgressVisible = true;
        binding.progressBarLogin.setVisibility(View.VISIBLE);
        binding.textPasswordLogin.setEnabled(false);
        binding.textUserNameLogin.setEnabled(false);
    }

    private void unlockLogin() {
        binding.progressBarLogin.setVisibility(View.GONE);
        binding.textPasswordLogin.setEnabled(true);
        binding.textUserNameLogin.setEnabled(true);
        isProgressVisible = false;
    }

    @Override
    public void onResume() {
        System.out.println("Resume Login");
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(AuthService.LOGIN_ERROR);
        filter.addAction(AuthService.LOGIN_SUCCESSFUL);
        filter.addAction(RestaurantService.DOWNLOAD_RESTAURANTS_COMPLETED);
        filter.addAction(RestaurantService.DOWNLOAD_RESTAURANTS_ERROR);
        filter.addAction(BookingService.DOWNLOAD_BOOKINGS_SUCCESSFUL);
        filter.addAction(BookingService.DOWNLOAD_BOOKINGS_ERROR);
        filter.addAction(AuthService.INTERNET_LOGIN_ERROR);
        filter.addAction(RestaurantService.INTERNET_RESTAURANTS_ERROR);
        filter.addAction(BookingService.INTERNET_BOOKING_ERROR);
        LocalBroadcastManager.getInstance(requireContext())
                .registerReceiver(receiver, filter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        AppCompatActivity act = (AppCompatActivity) getActivity();
        if (act != null) {
            ActionBar a = act.getSupportActionBar();
            if (a != null) {
                a.show();
            }
        }
        LocalBroadcastManager.getInstance(requireContext())
                .unregisterReceiver(receiver);
    }

    private void downloadRestauransts() {
        Intent intent = new Intent(requireContext(), RestaurantService.class);
        intent.putExtra(RestaurantService.KEY_DOWNLOAD_RESTAURANTS, RestaurantService.DOWNLOAD_RESTAURANTS);
        requireActivity().startService(intent);
    }

    private void downloadBookings() {
        Intent intent = new Intent(requireContext(), BookingService.class);
        intent.putExtra(BookingService.KEY_BOOKING_ACTION, BookingService.ACTION_DOWNLOAD_BOOKINGS);
        requireActivity().startService(intent);
    }

    private void logout() {
        new Thread(() -> {
            DBHelper.getInstance(requireContext()).getUserDao().deleteAll();
            DBHelper.getInstance(requireContext()).getRestaurantDao().deleteAll();
            DBHelper.getInstance(requireContext()).getBookingDao().deleteAll();
            userViewModel.setUser(null);
            restaurantViewModal.setRestaurantList(new ArrayList<>());
            restaurantViewModal.setRestaurant(null);
            bookingViewModel.setBookingList(new ArrayList<>());
        }).start();
    }
}
