package com.ka8eem.market24.ui.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Path;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.maps.android.SphericalUtil;
import com.ka8eem.market24.R;
import com.ka8eem.market24.databinding.MapFragmentBinding;
import com.ka8eem.market24.databinding.MapRadiusFragmentBinding;
import com.ka8eem.market24.util.Constants;
import com.ka8eem.market24.util.Keys;

import java.util.List;
import java.util.Locale;


enum   Direction {
    NORTH, SOUTH, WEST, EAST
}
public class MapRadiusFragment extends Fragment implements OnMapReadyCallback {

    private static final String TAG = "Map Fragment";
    private int PERMISSION_REQUEST= 10 ;
    private LocationManager service ;
    private  Boolean enabled = false ;
    private GoogleMap googleMap ;
    String   address ;
    private LatLng latLong , homeLatLong ;
    double raduis = 0.0 ;
    LinearLayout toolbar ;
    private MapRadiusFragmentBinding binding;
    private SupportMapFragment mapDetail  ;
    private FusedLocationProviderClient fusedLocationClient ;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    NavController navController ;
    private final Double zoneAdjustStep = 100.0;
    private final Double minus_cicle = 500.0 ;
    private final Double max_cicle = 5000.0 ;
    private final Double step = 100.0 ;
    private Circle circle = null ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = MapRadiusFragmentBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mapDetail =  (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map) ;

        toolbar = getActivity().findViewById(R.id.relative1);
        toolbar.setVisibility(View.GONE);
        navController = Navigation.findNavController(getActivity(),R.id.fragment_container);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext()) ;
        preferences = getContext().getSharedPreferences(Constants.SHARED, Context.MODE_PRIVATE);
        editor = preferences.edit();

        if (checkPermission()) {
            mapDetail.getMapAsync(this);
        }

        binding.pinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { getMap(); }
        });

        binding.buttonEast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                latLong = moveCircle(homeLatLong, latLong, googleMap, zoneAdjustStep, Direction.EAST, raduis);
        }});

        binding.buttonWest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    latLong = moveCircle(homeLatLong, latLong, googleMap, zoneAdjustStep, Direction.WEST, raduis);
        }});
        binding.buttonNourth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                latLong = moveCircle(homeLatLong, latLong, googleMap, zoneAdjustStep, Direction.NORTH, raduis);
        }});
        binding.buttonSouth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            latLong = moveCircle(homeLatLong, latLong, googleMap, zoneAdjustStep, Direction.SOUTH, raduis);
        }});

        binding.minusCicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(raduis > minus_cicle)
                    raduis = raduis - step ;

                drawCircleResize(latLong,googleMap,raduis) ;
            }
        });

        binding.maxCicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if(raduis < max_cicle)
                raduis = raduis + step;

            drawCircleResize(latLong,googleMap,raduis);
        }   });
        binding.buttonLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (latLong != null) {
                    /*editor.putString(Keys.latitude, latLong.latitude + "");
                    editor.putString(Keys.longtitude, latLong.longitude + "");
                    editor.putString(Keys.Address, address);
                    editor.commit();
                    editor.apply();
                     */
                    Bundle arguments = new Bundle();
                    arguments.putString("latitude", latLong.latitude+"");
                    arguments.putString("longtitude", latLong.longitude+"");
                    arguments.putString("radius", raduis+"");

                    navController.navigate(R.id.SearchFragment,arguments);
                } else
                   getMap();
            }
        });

        //You need to add the following line for this solution to work; thanks skayred
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener( new View.OnKeyListener()
        {
            @Override
            public boolean onKey( View v, int keyCode, KeyEvent event )
            {
                if( keyCode == KeyEvent.KEYCODE_BACK )
                {
                    binding.buttonLocation.setVisibility(View.GONE);
                    navController.navigate(R.id.HomeFragment);
                    return true;
                }
                return false;
            }
        } );



    }



    public Boolean checkPermission(){
        if(ContextCompat.checkSelfPermission(
                getContext().getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED){
            service = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
            enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (!enabled) {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
                enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);
            }
            return true;

        }else{
            ActivityCompat.requestPermissions(
                    getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_REQUEST
            );
            return false;
        }

    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {

        this.googleMap = googleMap ;
        fusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(),location -> {
            if (location != null) { // Set the map's camera position to the current location of the device.
                double lat = location.getLatitude();
                double longitude = location.getLongitude();


                latLong = new LatLng(lat, longitude);
                homeLatLong = latLong;
                 address = getCompleteAddressString(location.getLatitude(), location.getLongitude());
                Log.i("MapFragment.TAG", lat + " -- " + longitude +"--"+address );

             //   Toast.makeText(getContext(),lat+"---"+longitude,Toast.LENGTH_LONG);

                googleMap.addMarker(new MarkerOptions().position(new LatLng(lat, longitude))
                        .draggable(true));
              //  googleMap.setOnMarkerDragListener((GoogleMap.OnMarkerDragListener) this);
              //  googleMap.setOnMarkerClickListener((GoogleMap.OnMarkerClickListener) this);

                googleMap.moveCamera(
                        CameraUpdateFactory.newLatLngZoom(new
                                        LatLng(location.getLatitude(), location.getLongitude()),
                                15f
                        ));
                Log.i("MapFragment.TAG", Math.round(lat * 1000.0) / 1000.0 + " -- " + Math.round(longitude * 1000.0) / 1000.0 );


                drawCircle(latLong,googleMap);

            }

        });
        }

        void getMap(){
            mapDetail.getMapAsync( (OnMapReadyCallback) this );
        }

    void drawCircle(LatLng point,GoogleMap googleMap) {

        // Instantiating CircleOptions to draw a circle around the marker
        CircleOptions circleOptions = new CircleOptions();

        // Specifying the center of the circle
        circleOptions.center(point);

        raduis = 900.0 ;

        // Radius of the circle
        circleOptions.radius(900.0);

       // Fill color of the circle
        circleOptions.fillColor(R.color.colorCircle);

        // Border width of the circle
        circleOptions.strokeWidth(2f);

        if(circle != null)
        circle.remove(); // Remove old circle.
        // Adding the circle to the GoogleMap
        circle = googleMap.addCircle(circleOptions);
    }

    void drawCircleResize(LatLng point,GoogleMap googleMap , Double rad) {

        // Instantiating CircleOptions to draw a circle around the marker
        CircleOptions circleOptions = new CircleOptions();

        // Specifying the center of the circle
        circleOptions.center(point);

        raduis =rad ;

        // Radius of the circle
        circleOptions.radius(raduis);

        // Fill color of the circle
        circleOptions.fillColor(R.color.colorCircle);

        // Border width of the circle
        circleOptions.strokeWidth(2f);

        circle.remove(); // Remove old circle.
        // Adding the circle to the GoogleMap
        circle = googleMap.addCircle(circleOptions);
     //   Toast.makeText(getContext(),raduis+"",Toast.LENGTH_LONG).show();

    }

    LatLng moveCircle(LatLng homePoint, LatLng point, GoogleMap googleMap, Double step, Direction direction, Double radius) {

        Double hed = 0.0 ;
        switch(direction) {
            case EAST:
                hed = 90.0;
                break;
            case WEST:
                 hed = 270.0;
                 break;
            case NORTH:
                hed = 0.0;
                break;
            case SOUTH:
                hed = 180.0;
                break;
            default: { // Note the block
                Toast.makeText(getContext(),"Error in direction",Toast.LENGTH_LONG).show();
            }
        }
        LatLng newPoint =  SphericalUtil.computeOffset(point, step, hed);
        double distanceFromOrigin = SphericalUtil.computeDistanceBetween(homePoint, newPoint);
     //   Toast.makeText(getContext(),"distance from origin:",Toast.LENGTH_LONG).show();
      //  Toast.makeText(getContext(),distanceFromOrigin+"",Toast.LENGTH_LONG).show();
      //  Toast.makeText(getContext(),radius+"",Toast.LENGTH_LONG).show();

        if (distanceFromOrigin < radius) {
            drawCircleResize(newPoint,googleMap, radius);
            return  newPoint;
        }
//        Log.i("msg", "just test logging with tags")
     //   Toast.makeText(getContext(),newPoint.latitude+"--"+newPoint.longitude,Toast.LENGTH_LONG).show();
        return  point;
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST) {
            if (  grantResults[0] !=
                    PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(),
                        "Unable to show location - permission required",
                        Toast.LENGTH_LONG).show();
            }
            service =(LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
            enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (!enabled) {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
                enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);
                mapDetail.getMapAsync(this);
            }

        }

    }

    private String getCompleteAddressString(Double LATITUDE , Double LONGITUDE){
        String strAdd = "" ;
        Geocoder geocoder =new  Geocoder(getContext(), Locale.getDefault());
        try {
            List<Address> addresses =
                    geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                strAdd = returnedAddress.getAddressLine(0).toString();

            } else {
                Log.w(TAG, "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w(TAG, "Canont get Address!");
        }
        return strAdd;
    }

}
