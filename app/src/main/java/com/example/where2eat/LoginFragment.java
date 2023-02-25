package com.example.where2eat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.method.KeyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.where2eat.databinding.FragmentLoginBinding;
import com.example.where2eat.domain.modal.User;
import com.example.where2eat.domain.modal.UserNamePassword;
import com.example.where2eat.roomdatabase.DBHelper;
import com.example.where2eat.service.AuthService;

import java.util.Objects;


public class LoginFragment extends Fragment {

    FragmentLoginBinding binding = null;
    private boolean isProgressVisible = false;
    private UserViewModel viewModel;

    private KeyListener pwd;
    private KeyListener usr;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                String action = intent.getAction();
                switch (action) {
                    case AuthService.LOGIN_SUCCESSFUL:
                        unlockLogin(pwd, usr);
                        new Thread(() -> {
                            User user = DBHelper.getInstance(requireContext()).getUserDao().getUser();
                            if (user != null && !Objects.equals(user, new User())) {
                                viewModel.setUserAnotherThread(user);
                            }
                            binding.textUserNameLogin.post(() -> {
                                final NavController navController = Navigation.findNavController(requireView());
                                navController.navigate(R.id.twoFragment);
                            });
                        }).start();
                        return;
                    case AuthService.LOGIN_ERROR:
                        unlockLogin(pwd, usr);
                        Toast.makeText(requireContext(), "Errore di Autenticazione", Toast.LENGTH_SHORT).show();
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
        viewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pwd = binding.textPasswordLogin.getKeyListener();
                usr = binding.textUserNameLogin.getKeyListener();
                if (isProgressVisible) {
                    unlockLogin(pwd, usr);
                } else {
                    UserNamePassword userNamePassword = new UserNamePassword(binding.textUserNameLogin.getText().toString(), binding.textPasswordLogin.getText().toString());
                    Intent intent = new Intent(requireContext(), AuthService.class);
                    intent.putExtra(AuthService.KEY_ACTION, AuthService.ACTION_LOGIN);
                    intent.putExtra(AuthService.KEY_USER_PASSWORD, userNamePassword);
                    requireActivity().startService(intent);
                    blockLogin();
                }
            }
        });
    }

    private void blockLogin() {
        isProgressVisible = true;
        binding.progressBarLogin.setVisibility(View.VISIBLE);
        binding.textPasswordLogin.setFocusable(false);
        binding.textPasswordLogin.setEnabled(false);
        binding.textPasswordLogin.setCursorVisible(false);
        binding.textPasswordLogin.setKeyListener(null);
        binding.textUserNameLogin.setFocusable(false);
        binding.textUserNameLogin.setEnabled(false);
        binding.textUserNameLogin.setCursorVisible(false);
        binding.textUserNameLogin.setKeyListener(null);
    }

    private void unlockLogin(KeyListener pwd, KeyListener usr) {
        binding.progressBarLogin.setVisibility(View.GONE);
        binding.textPasswordLogin.setFocusable(true);
        binding.textPasswordLogin.setEnabled(true);
        binding.textPasswordLogin.setCursorVisible(true);
        binding.textPasswordLogin.setKeyListener(pwd);
        binding.textUserNameLogin.setFocusable(true);
        binding.textUserNameLogin.setEnabled(true);
        binding.textUserNameLogin.setCursorVisible(true);
        binding.textUserNameLogin.setKeyListener(usr);
        isProgressVisible = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(AuthService.LOGIN_ERROR);
        filter.addAction(AuthService.LOGIN_SUCCESSFUL);
        LocalBroadcastManager.getInstance(requireContext())
                .registerReceiver(receiver, filter);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(requireContext())
                .unregisterReceiver(receiver);
    }
}
