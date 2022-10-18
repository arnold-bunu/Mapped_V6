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
import android.graphics.Color;
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
import android.widget.Toast;

import com.example.mapped_v6.JsonParser;
import com.example.mapped_v6.R;
import com.example.mapped_v6.databinding.FragmentMapsBinding;
import com.example.mapped_v6.navActivity;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
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
    Boolean locationPermissionGranted = true;
    Button btnDirections, btnFavourites;
    TextView placeName, ETA, placeInfoDistance;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference mDatabase = database.getReference("Users");
    private String userId;
    private String distMeasureSystem;
    String LandmarkType;
    private final LatLng campus = new LatLng(-33.9728, 18.4695);
    AutocompleteSupportFragment autocomplete;
    String ETAA, distance;
    Polyline mPolyline;

    private double lattt;
    private double longg;

    LatLng starting;
    LatLng going;


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
//            LatLng sydney = new LatLng(-34, 151);
//            googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//            googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

            if (locationPermissionGranted) {
                if (ActivityCompat.checkSelfPermission(getContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                fetchlocation();

                search();


                lastlocation();
                System.out.println("\n" + lastlocation + "\n");


            }
            map.setOnMarkerClickListener(MapsFragment.this::onMarkerClick);
            map.setOnMapClickListener(MapsFragment.this::onMapClick);

        }
    };

    private void search() {
        autocomplete = (AutocompleteSupportFragment) getChildFragmentManager().findFragmentById(R.id.autoComplete);

        autocomplete.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME,
                Place.Field.LAT_LNG, Place.Field.TYPES, Place.Field.WEBSITE_URI));

        autocomplete.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onError(@NonNull Status status) {

            }

            @Override
            public void onPlaceSelected(@NonNull Place place) {
                Log.i("MapsFragment", "Place: " + place.getName() + ", " + place.getId() +
                        ", " + place.getLatLng() + ", " + place.getTypes() +
                        ", " + place.getWebsiteUri() + ", " + place.getPhotoMetadatas());
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 15));

                placeIds.put(place.getName(), place.getId());


                Marker marker = map.addMarker(
                        new MarkerOptions()
                                .position(place.getLatLng())
                                .title(place.getName())
                );
                marker.showInfoWindow();
            }
        });
    }

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
                                Toast.makeText(getActivity(), "xxxxx", Toast.LENGTH_SHORT).show();
                                map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(lastlocation.getLatitude(),
                                                lastlocation.getLongitude()), 15));
                                //   fetchSettings();
                            }
                        } else {

                            map.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(campus, 15));
                            map.getUiSettings().setMyLocationButtonEnabled(false);

                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }

    private void fetchSettings() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();
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
        }
        try {
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
        } catch (SecurityException e) {
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

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();

        Places.initialize(getActivity().getApplicationContext(), getString(R.string.google_maps_key));
        placesClient = Places.createClient(getContext());

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        fragmentContainerView = (FragmentContainerView) getView().findViewById(R.id.fragmentContainerViewPlaceInformation);

        placeInformation = (ConstraintLayout) getView().findViewById(R.id.placeInformation);

        placeName = (TextView) getView().findViewById(R.id.placeName);
        btnDirections = (Button) getView().findViewById(R.id.btnDirections);
        btnFavourites = (Button) getView().findViewById(R.id.btnFavourites);
        placeInfoDistance = (TextView) getView().findViewById(R.id.placeInfoDistance);
        ETA = (TextView) getView().findViewById(R.id.ETA);


        mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
            mMarkerPoints = new ArrayList<>();
        }
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        placeInformation.setVisibility(View.GONE);
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        placeInformation.setVisibility(View.VISIBLE);
        placeName.setVisibility(View.VISIBLE);
        btnDirections.setVisibility(View.VISIBLE);

        String placeID = "";
        ETA.setText("");
        placeInfoDistance.setText("");

        for (Map.Entry<String, String> entry : placeIds.entrySet()) {
            if (entry.getKey().equals(marker.getTitle())) {
                placeID = entry.getValue();
            }
        }
        if (placeID.equals("")) {
            Toast.makeText(getContext(), "Place not found", Toast.LENGTH_SHORT).show();
        } else {
            placeDetails(placeID);
        }

        placeName.setText(marker.getTitle());

        btnDirections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                starting = new LatLng(lastlocation.getLatitude(), lastlocation.getLongitude());
                going = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);

                lattt = marker.getPosition().latitude;
                longg = marker.getPosition().longitude;

                System.out.println(lattt );
                System.out.println(longg);
                showMeDaWay();
            }
        });
        return false;
    }

    private void showMeDaWay() {
        String url = PolyLinesss(starting, going);
        DownloadTask downloadTask = new DownloadTask();
        downloadTask.execute(url);
    }

    private String PolyLinesss(LatLng starting, LatLng going) {
        String start = "origin=" + starting.latitude + "," + starting.longitude;
         String end = "destination=" + lattt + "," + longg;
       // String end = "destination=-33.969182,18.4731455";
        String key = "AIzaSyC1oUlRGEsbyJu-nUOWzLFwprHh4W41mac";
        String output = "json";
        // String units = "units="+

        String parameters = start + "&" + end + "&"  + "&key=" + key;

        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        System.out.println(url);

        return url;
    }

    private String downloadUrl1(String strUrl) throws IOException {

        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception on download", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;

    }

    private void placeDetails(String placeID) {
    }

    private class PlaceTask extends AsyncTask<String, Integer, String> {


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

    private String downloadUrl(String string) throws IOException {

        URL url = new URL(string);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.connect();

        InputStream stream = connection.getInputStream();

        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder builder = new StringBuilder();
        String line = "";
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }
        String data = builder.toString();
        reader.close();

        return data;

    }


    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String, String>>> {
        @Override
        protected List<HashMap<String, String>> doInBackground(String... strings) {
            JsonParser jsonParser = new JsonParser();
            List<HashMap<String, String>> mapList = null;

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

    private class DownloadTask extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... url) {

            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl1(url[0]);
                Log.d("DownloadTask", "DownloadTask : " + data);
            } catch (Exception e) {
                Log.d("err", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            new ParserTask1().execute(result);
        }

    }

    // USING JSON PARSER FOR DIRECTIONS
    private class ParserTask1 extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... strings) {
            JSONObject jsonObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jsonObject = new JSONObject(strings[0]);
                DJSONParser parser = new DJSONParser();
                routes = parser.parse(jsonObject);

                System.out.println(routes);

            } catch (Exception e) {
                e.printStackTrace();

            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> lists) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;

            // Traversing through all the routes
            for (int i = 0; i < lists.size(); i++) {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = lists.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(8);
                lineOptions.color(Color.RED);
            }
            for (int i = lists.size() - 1; i < lists.size(); i++) {
                List<HashMap<String, String>> path = lists.get(i);
                for (int j = 0; j < path.size(); j++) {
                    placeInfoDistance.setVisibility(View.VISIBLE);
                    ETA.setVisibility(View.VISIBLE);
                    placeInfoDistance.setText("Distance: " + distance);
                    ETA.setText("Duration: " + ETAA);
                }

                // Drawing polyline in the Google Map for the i-th route
                if (lineOptions != null) {
                    if (mPolyline != null) {
                        mPolyline.remove();
                    }
                    mPolyline = map.addPolyline(lineOptions);

                } else
                    Toast.makeText(getContext().getApplicationContext(), "Route is to far, No specific Route available", Toast.LENGTH_LONG).show();
            }
        }
    }
}













