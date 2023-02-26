package com.example.where2eat;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.where2eat.databinding.ActivityMainBinding;
import com.example.where2eat.roomdatabase.DBHelper;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding = null;

    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        NavController navController = navHostFragment.getNavController();
        if (item.getItemId() == R.id.menuHome) {
            navController.navigate(R.id.homeFragment);
            return true;
        } else if (item.getItemId() == R.id.menuMappa) {
            navController.navigate(R.id.homeFragment);
            return true;
        } else if (item.getItemId() == R.id.menuPrenotazioni) {
            navController.navigate(R.id.homeFragment);
            return true;
        } else if (item.getItemId() == R.id.menuEsci) {
            logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        NavController navController = navHostFragment.getNavController();
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        new Thread(() -> {
            DBHelper.getInstance(getApplicationContext()).getUserDao().deleteAll();
            DBHelper.getInstance(getApplicationContext()).getRestaurantDao().deleteAll();
            userViewModel.setUserAnotherThread(null);
            binding.fragmentContainerView.post(() -> {
                Toast.makeText(getApplicationContext(), "Logout Effettuato con successo", Toast.LENGTH_SHORT).show();
                navController.navigate(R.id.loginFragment);
            });
        }).start();
    }
}