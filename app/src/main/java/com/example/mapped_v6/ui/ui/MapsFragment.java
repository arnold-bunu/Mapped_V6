package com.example.mapped_v6.ui.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.lifecycle.ViewModelProvider;



import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.mapped_v6.R;
import com.example.mapped_v6.databinding.FragmentMapsBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MapsFragment extends Fragment implements GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener {
    SupportMapFragment mapFragment;
    GoogleMap map;
    PlacesClient placesClient;
    viewModel viewModel;
    Location lastKnownLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    FragmentContainerView fragmentContainerView;
    ConstraintLayout placeInformation;
   FragmentMapsBinding binding;
 Boolean locationPermissionGranted = false;
     Button directionButton, favouriteButton;
    TextView placeInfoTitle, placeInfoDuration, placeInfoDistance;

    // VARIABLES USED TO GET LANDMARKS NEAR THE USERS LOCATION
    Map<String, String> placeIds = new HashMap<String, String>();
    String currentPlaceId;
    String currentPlaceAddress;
    String currentPlaceName;
    Uri currentPlaceWebUri;
   // GeoApiContext mGeoApiContext;
   ArrayList<LatLng> mMarkerPoints;



    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) {
            map = googleMap;

            if (locationPermissionGranted) {
                if (ActivityCompat.checkSelfPermission(getContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                fetchlocation();
            }

        }
    };

    private void fetchlocation() {

        if (map == null) {
            return;
        }  try {
            map.getUiSettings().setMapToolbarEnabled(false);
            if (locationPermissionGranted) {
                map.setMyLocationEnabled(true);
                map.getUiSettings().setMyLocationButtonEnabled(true);
                map.getUiSettings().setCompassEnabled(true);
                map.getUiSettings().setZoomControlsEnabled(true);
            } else {
                map.setMyLocationEnabled(false);
                map.getUiSettings().setMyLocationButtonEnabled(false);
                map.getUiSettings().setCompassEnabled(false);
                map.getUiSettings().setZoomControlsEnabled(false);
                lastKnownLocation = null;
                //getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(viewModel.class);

        binding = FragmentMapsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Places.initialize(getActivity().getApplicationContext(), getString(R.string.google_maps_key));
        placesClient = Places.createClient(getContext());

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        fragmentContainerView = (FragmentContainerView) getView().findViewById(R.id.fragmentContainerViewPlaceInformation);

        placeInformation = (ConstraintLayout) getView().findViewById(R.id.placeInformation);

        placeInfoTitle = (TextView) getView().findViewById(R.id.placeInfoTitle);
        directionButton = (Button) getView().findViewById(R.id.directionButton);
        favouriteButton = (Button) getView().findViewById(R.id.addToFavouritesButton);
        placeInfoDistance = (TextView) getView().findViewById(R.id.placeInfoDistance);
        placeInfoDuration = (TextView) getView().findViewById(R.id.placeInfoDuration);


        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
            mMarkerPoints = new ArrayList<>();
        }
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {

    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        return false;
    }
}