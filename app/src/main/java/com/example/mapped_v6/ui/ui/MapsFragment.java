package com.example.mapped_v6.ui.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.lifecycle.ViewModelProvider;



import android.Manifest;
import android.content.AsyncQueryHandler;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.mapped_v6.JsonParser;
import com.example.mapped_v6.R;
import com.example.mapped_v6.databinding.FragmentMapsBinding;
import com.example.mapped_v6.navActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MapsFragment extends Fragment implements GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener {
    SupportMapFragment mapFragment;
    GoogleMap map;
    PlacesClient placesClient;
    viewModel viewModel;
    Location lastlocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    FragmentContainerView fragmentContainerView;
    ConstraintLayout placeInformation;
   FragmentMapsBinding binding;
 Boolean locationPermissionGranted = false;
     Button directionButton, favouriteButton;
    TextView placeInfoTitle, placeInfoDuration, placeInfoDistance;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference mDatabase = database.getReference("Users");
    private String userId;
    private String distMeasureSystem;
    String LandmarkType;
    private final LatLng campus = new LatLng(-33.9728, 18.4695);

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


               lastlocation();
                System.out.println("\n" +lastlocation + "\n");


            }
            map.setOnMarkerClickListener(MapsFragment.this::onMarkerClick);
            map.setOnMapClickListener(MapsFragment.this::onMapClick);

        }
    };

    private void lastlocation() {
        try {
            if (locationPermissionGranted) {
                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(getActivity(), new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            lastlocation = task.getResult();
                            if (lastlocation != null) {
                                map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(lastlocation.getLatitude(),
                                                lastlocation.getLongitude()), 15));
                                fetchSettings();
                            }
                        } else {

                            map.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(campus, 15));
                            map.getUiSettings().setMyLocationButtonEnabled(false);

                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }

    private void fetchSettings() {
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        userId=user.getUid();
        mDatabase.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                distMeasureSystem = snapshot.child("distMeasureSystem").getValue(String.class);
                LandmarkType = snapshot.child("LandmarkType").getValue(String.class);
                nearbySearch();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void nearbySearch() {


        String radius = "5000";
        String type = LandmarkType;

        StringBuilder googlePlacesUrl =
                new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=").append(lastlocation.getLatitude())
                .append(",").append(lastlocation.getLongitude());
        googlePlacesUrl.append("&radius=").append(radius);
        googlePlacesUrl.append("&types=").append(type);
        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=" + (R.string.google_maps_key));

        String url = googlePlacesUrl.toString();

        new PlaceTask().execute(url);
    }
    
    


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
                lastlocation = null;

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

        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        userId=user.getUid();

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


         mapFragment =
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

    private class PlaceTask extends AsyncTask <String,Integer,String>  {


        @Override
        protected String doInBackground(String... strings) {
            String data = null;
            try {
                data = downloadUrl(strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            new ParserTask().execute(s);
        }
    }

    private String downloadUrl(String string) throws IOException{

        URL url = new URL(string);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.connect();

        InputStream stream = connection.getInputStream();

        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder builder = new StringBuilder();
        String line = "";
        while ((line = reader.readLine()) != null){
            builder.append(line);
        }
        String data = builder.toString();
        reader.close();

        return data;
        
    }


    private class ParserTask extends AsyncTask <String,Integer,List<HashMap<String,String>>> {
        @Override
        protected List<HashMap<String, String>> doInBackground(String... strings) {
            JsonParser jsonParser = new JsonParser();
            List<HashMap<String,String>> mapList = null;

            JSONObject object = null;
            try {
                object = new JSONObject(strings[0]);

                mapList = jsonParser.parseResult(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return mapList;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> hashMaps) {
            map.clear();

            for (int i = 0; i < hashMaps.size(); i++) {
                HashMap<String, String> hashMapList = hashMaps.get(i);

                double lat = Double.parseDouble(hashMapList.get("lat"));

                double lng = Double.parseDouble(hashMapList.get("lng"));

                String name = hashMapList.get("name");

                String id = hashMapList.get("place_id");

                LatLng latLng = new LatLng(lat, lng);

                MarkerOptions options = new MarkerOptions();

                placeIds.put(name, id);

                options.position(latLng);

                options.title(name);

                map.addMarker(options);
            }
        }
    }
}