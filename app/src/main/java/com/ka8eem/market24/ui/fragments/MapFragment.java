package com.ka8eem.market24.ui.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.navigation.NavigationView;
import com.ka8eem.market24.R;
import com.ka8eem.market24.databinding.MapFragmentBinding;
import com.ka8eem.market24.util.Constants;
import com.ka8eem.market24.util.Keys;

import java.util.List;
import java.util.Locale;


public class MapFragment extends Fragment implements OnMapReadyCallback {

    private static final String TAG = "Map Fragment";
    private int PERMISSION_REQUEST= 10 ;
    LinearLayout searchView;
    private LocationManager service ;
    private  Boolean enabled = false ;
    private GoogleMap googleMap ;
    String   address ;
    private LatLng latLong ;
    private MapFragmentBinding binding;
    private SupportMapFragment mapDetail  ;
    private FusedLocationProviderClient fusedLocationClient ;
    NavigationView navigationView;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = MapFragmentBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        searchView = (LinearLayout) getActivity().findViewById(R.id.relative1);
        searchView.setVisibility(View.GONE);

        mapDetail =  (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map) ;

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext()) ;
        preferences = getContext().getSharedPreferences(Constants.SHARED, Context.MODE_PRIVATE);
        editor = preferences.edit();

        if (checkPermission()) {
            mapDetail.getMapAsync(this);
        }

        binding.pinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMap();
            }
        });
        binding.buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (latLong != null) {
                    editor.putString(Keys.latitude, latLong.latitude + "");
                    editor.putString(Keys.longtitude, latLong.longitude + "");
                    editor.putString(Keys.Address, address);
                    editor.commit();
                    editor.apply();
                    navigationView = getActivity().findViewById(R.id.nav_view);
                    navigationView.setCheckedItem(R.id.nav_add);
                    getParentFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new AddProductFragment()).addToBackStack(null).commit();

                } else
                    mapDetail.getMapAsync((OnMapReadyCallback) getContext());
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
                    binding.buttonSave.setVisibility(View.GONE);
                    FragmentTransaction tx = getFragmentManager().beginTransaction();
                    tx.replace(R.id.fragment_container, new AddProductFragment() ).addToBackStack( "tag" ).commit();

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
        //    check_location = false;
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

                 address = getCompleteAddressString(location.getLatitude(), location.getLongitude());
                Log.i("MapFragment.TAG", lat + " -- " + longitude +"--"+address );

                Toast.makeText(getContext(),lat+"---"+longitude,Toast.LENGTH_LONG);

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



            }

        });


        }

    void getMap(){
        mapDetail.getMapAsync( this );
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
