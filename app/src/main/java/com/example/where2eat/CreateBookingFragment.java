package com.example.where2eat;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.where2eat.databinding.FragmentCreateBookingBinding;
import com.example.where2eat.domain.model.Booking;
import com.example.where2eat.domain.model.CreateBooking;
import com.example.where2eat.domain.model.Restaurant;
import com.example.where2eat.domain.viewmodel.BookingViewModel;
import com.example.where2eat.domain.viewmodel.RestaurantViewModal;
import com.example.where2eat.domain.viewmodel.UserViewModel;
import com.example.where2eat.roomdatabase.DBHelper;
import com.example.where2eat.service.BookingService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CreateBookingFragment extends Fragment {

    private boolean isProgressVisible = false;
    final Calendar dateCalendar = Calendar.getInstance();
    final Calendar timeCalendar = Calendar.getInstance();
    FragmentCreateBookingBinding binding = null;
    Restaurant restaurant;
    private UserViewModel userViewModel;
    private RestaurantViewModal restaurantViewModal;
    private BookingViewModel bookingViewModel;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                String action = intent.getAction();
                switch (action) {
                    case BookingService.BOOKING_CREATE_SUCCESSFUL:
                        downloadBookings();
                        return;
                    case BookingService.BOOKING_CREATE_ERROR:
                        unlockCreateBooking();
                        Toast.makeText(requireContext(), getString(R.string.errore_creazione_prenotazione), Toast.LENGTH_SHORT).show();
                        return;
                    case BookingService.DOWNLOAD_BOOKINGS_SUCCESSFUL:
                        new Thread(() -> {
                            List<Booking> bookingList = DBHelper.getInstance(requireContext()).getBookingDao().findAll();
                            if (bookingList != null && !bookingList.equals(new ArrayList<>()) && bookingList.size() > 0) {
                                bookingViewModel.setBookingList(bookingList);
                            }
                            binding.textViewEffettuaPrenotazioneCreateBooking.post(() -> {
                                unlockCreateBooking();
                                final NavController navController = Navigation.findNavController(requireView());
                                navController.navigate(R.id.bookingFragment);
                            });
                        }).start();
                        return;
                    case BookingService.DOWNLOAD_BOOKINGS_ERROR:
                        unlockCreateBooking();
                        Toast.makeText(requireContext(), getString(R.string.errore_ripresa_prenotazione_riesegui_login), Toast.LENGTH_SHORT).show();
                        logout();
                        return;
                    case BookingService.INTERNET_CREATE_BOOKING_ERROR:
                        unlockCreateBooking();
                        Toast.makeText(requireContext(), getString(R.string.errore_non_sei_connesso_internet), Toast.LENGTH_SHORT).show();
                        return;
                    case BookingService.INTERNET_BOOKING_ERROR:
                        unlockCreateBooking();
                        Toast.makeText(requireContext(), getString(R.string.errore_non_connesso_riesegui_login), Toast.LENGTH_SHORT).show();
                        logout();
                        return;
                }
            }
        }
    };

    private void downloadBookings() {
        Intent intent = new Intent(requireContext(), BookingService.class);
        intent.putExtra(BookingService.KEY_BOOKING_ACTION, BookingService.ACTION_DOWNLOAD_BOOKINGS);
        requireActivity().startService(intent);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCreateBookingBinding.inflate(inflater, container, false);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        restaurantViewModal = new ViewModelProvider(requireActivity()).get(RestaurantViewModal.class);
        bookingViewModel = new ViewModelProvider(requireActivity()).get(BookingViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        restaurantViewModal.getRestaurant().observe(getViewLifecycleOwner(), r -> {
            restaurant = r;
        });

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                dateCalendar.set(Calendar.YEAR, year);
                dateCalendar.set(Calendar.MONTH, month);
                dateCalendar.set(Calendar.DAY_OF_MONTH, day);
                updateDateLabel();
            }
        };

        binding.textDateCreateBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getContext(), date, dateCalendar.get(Calendar.YEAR), dateCalendar.get(Calendar.MONTH), dateCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                timeCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                timeCalendar.set(Calendar.MINUTE, minute);
                updateTimeLabel();
            }
        };

        binding.textTimeCreateBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(getContext(), time, timeCalendar.get(Calendar.HOUR_OF_DAY), timeCalendar.get(Calendar.MINUTE), true).show();
            }
        });

        binding.buttonCreateBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isProgressVisible) {
                    unlockCreateBooking();
                } else {
                    CreateBooking createBooking = new CreateBooking();
                    createBooking.setIdRistorante(restaurant.getId());
                    createBooking.setDataPrenotazione(binding.textDateCreateBooking.getText().toString());
                    createBooking.setOraPrenotazione(binding.textTimeCreateBooking.getText().toString());
                    createBooking.setNumeroPosti(Integer.valueOf(binding.textNumberDecimalNumeroPostiCreateBooking.getText().toString()));

                    Intent intent = new Intent(requireContext(), BookingService.class);
                    intent.putExtra(BookingService.KEY_BOOKING_ACTION, BookingService.ACTION_BOOKING_CREATE);
                    intent.putExtra(BookingService.KEY_CREATE_BOOKING_OBJ, createBooking);
                    requireActivity().startService(intent);

                    blockCreateBooking();
                }
            }
        });

    }

    private void blockCreateBooking() {
        isProgressVisible = true;
        binding.textNumberDecimalNumeroPostiCreateBooking.setEnabled(false);
        binding.progressBarCreateBooking.setVisibility(View.VISIBLE);
    }

    private void unlockCreateBooking() {
        binding.progressBarCreateBooking.setVisibility(View.GONE);
        binding.textNumberDecimalNumeroPostiCreateBooking.setEnabled(true);
        isProgressVisible = false;
    }

    private void updateDateLabel() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        binding.textDateCreateBooking.setText(dateFormat.format(dateCalendar.getTime()));
    }

    private void updateTimeLabel() {
        String myFormat = "HH:mm";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        binding.textTimeCreateBooking.setText(dateFormat.format(timeCalendar.getTime()));
    }


    @Override
    public void onResume() {
        super.onResume();
        restaurantViewModal.getRestaurant().observe(getViewLifecycleOwner(), r -> {
            binding.textViewNomeRistoranteCreateBooking.setText(r.getRagioneSociale());
            restaurant = r;
        });
        IntentFilter filter = new IntentFilter();
        filter.addAction(BookingService.BOOKING_CREATE_SUCCESSFUL);
        filter.addAction(BookingService.BOOKING_CREATE_ERROR);
        filter.addAction(BookingService.DOWNLOAD_BOOKINGS_SUCCESSFUL);
        filter.addAction(BookingService.DOWNLOAD_BOOKINGS_ERROR);
        filter.addAction(BookingService.INTERNET_BOOKING_ERROR);
        filter.addAction(BookingService.INTERNET_CREATE_BOOKING_ERROR);
        LocalBroadcastManager.getInstance(requireContext())
                .registerReceiver(receiver, filter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LocalBroadcastManager.getInstance(requireContext())
                .unregisterReceiver(receiver);
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
            binding.textViewEffettuaPrenotazioneCreateBooking.post(()->{
                final NavController navController = Navigation.findNavController(requireView());
                navController.navigate(R.id.loginFragment);
            });
        }).start();
    }
}
