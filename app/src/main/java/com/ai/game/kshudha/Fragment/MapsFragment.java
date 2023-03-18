package com.ai.game.kshudha.Fragment;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ai.game.kshudha.Model.Ngo;
import com.ai.game.kshudha.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.CancellationTokenSource;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MapsFragment extends Fragment{
    private final static int REQUEST_CODE = 100;
    private FusedLocationProviderClient fusedLocationClient;
    GoogleMap gmap;
    Location loc;
    Bitmap icon;

    private static final double r2d = 180.0D / 3.141592653589793D;
    private static final double d2r = 3.141592653589793D / 180.0D;
    private static final double d2km = 111189.57696D * r2d;
    public static double distance(double lt1, double ln1, double lt2, double ln2) {
        double x = lt1 * d2r;
        double y = lt2 * d2r;
        return Math.acos( Math.sin(x) * Math.sin(y) + Math.cos(x) * Math.cos(y) * Math.cos(d2r * (ln1 - ln2))) * d2km;
    }

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    func();
                } else {
                    getActivity().finish();
                }
            });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapView);
        if(mapFragment == null) Toast.makeText(getContext(), "adasdasd", Toast.LENGTH_SHORT).show();
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                gmap = googleMap;
            }
        });

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);

        return view;
    }

    public void func(){
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            icon = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.ngo);
            icon = Bitmap.createScaledBitmap(icon, 100, 100, false);
            CancellationTokenSource cancellationTokenSource = new CancellationTokenSource();
            fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, cancellationTokenSource.getToken())
                    .addOnSuccessListener((Activity) getContext(), new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                loc = location;
                                LatLng latLng = new LatLng(loc.getLatitude(), loc.getLongitude());
                                MarkerOptions markerOptions = new MarkerOptions();
                                markerOptions.position(latLng);
                                markerOptions.title(latLng.latitude+" : "+latLng.longitude);
                                if(gmap != null) {
                                    gmap.clear();
                                    gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
                                    gmap.addMarker(markerOptions);
                                }
                                else Toast.makeText(getContext(), "map = null", Toast.LENGTH_SHORT).show();



                                FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).child("lat").setValue(location.getLatitude());
                                FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).child("lon").setValue(location.getLongitude());
                                Toast.makeText(getContext(), Double.toString(location.getLatitude()) + " " + Double.toString(location.getLongitude()) , Toast.LENGTH_SHORT).show();
                                // 18.8932, 73.1764
                                FirebaseDatabase.getInstance().getReference().child("NGO").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot data : snapshot.getChildren()){
                                            Ngo snap = data.getValue(Ngo.class);
                                            MarkerOptions marker=new MarkerOptions();
                                            marker.position(new LatLng(snap.getLat(), snap.getLon())).icon(BitmapDescriptorFactory.fromBitmap(icon));
                                            gmap.addMarker(marker);
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        }
                    });

        }
    }
}
