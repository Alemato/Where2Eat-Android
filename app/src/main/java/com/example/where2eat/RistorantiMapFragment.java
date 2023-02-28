package com.example.where2eat;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.where2eat.domain.modal.Restaurant;
import com.example.where2eat.tools.LocationTools;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class RistorantiMapFragment extends Fragment implements OnMapReadyCallback, LocationListener {
    private LocationTools locationTools;

    private Marker marker;

    private GoogleMap map;
    private RestaurantViewModal restaurantViewModal;

    private ActivityResultLauncher<String> launcher = registerForActivityResult(new ActivityResultContracts.RequestPermission(),
            new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean result) {

                    if(result) {
                        locationTools.start(RistorantiMapFragment.this);
                    } else {
                        Toast.makeText(requireContext(), "No permission", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ristorantimap, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        restaurantViewModal = new ViewModelProvider(requireActivity()).get(RestaurantViewModal.class);
        locationTools = new LocationTools(requireContext(), launcher);
        SupportMapFragment fragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapFragmentContainer);
        fragment.getMapAsync(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        locationTools.start(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        locationTools.stop(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        map.setOnMarkerClickListener(m -> {

            Object obj = m.getTag();
            if(obj != null && obj instanceof Restaurant) {
                Restaurant restaurant = (Restaurant) obj;

                restaurantViewModal.setRestaurant(restaurant);
                Navigation.findNavController(requireView())
                        .navigate(R.id.action_ristorantiMapFragment_to_restaurantDetailsFragment);
            }
            return false;
        });
        setupMarkers(googleMap);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        if(map != null) {

            if(marker == null) {
                MarkerOptions options = new MarkerOptions();
                options.position(new LatLng(location.getLatitude(), location.getLongitude()));
                options.title("MyLocation");
                marker = map.addMarker(options);
            } else {
                marker.setPosition(new LatLng(location.getLatitude(), location.getLongitude()));
            }
        }
    }

    private void setupMarkers(GoogleMap map) {
        restaurantViewModal.getRestaurantList().observe(getViewLifecycleOwner(), list->{
            new Thread(() -> {
                LatLngBounds.Builder bounds = new LatLngBounds.Builder();
                for (Restaurant r : list){
                    LatLng position = new LatLng(Double.parseDouble(r.getLatitudine()), Double.parseDouble(r.getLongitudine()));
                    bounds.include(position);
                    MarkerOptions options = new MarkerOptions();
                    options.position(position);
                    options.title(r.getRagioneSociale());
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                    requireView().post(() -> {
                        Marker m = map.addMarker(options);
                        m.setTag(r);
                    });
                }

                requireView().post(() -> {
                    map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 16));
                });
            }).start();
        });
    }
}
