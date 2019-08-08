package dev.malikkurosaki.easyspeak;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.location.LocationEngineRequest;
import com.mapbox.android.core.location.LocationEngineResult;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.LocationComponentOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;

import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, PermissionsListener{
    private MapView mapView;
    private MapboxMap map;
    private LocationEngine locationEngine;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(getApplicationContext(), "pk.eyJ1IjoibWFsaWtrdXJvc2FraSIsImEiOiJjancwcWNuMGYwZHVxNDhtdGRuNXBqdTYzIn0._FSW8xQn8U5-VORm722x3Q");
        setContentView(R.layout.activity_main);


        int RECORD_AUDIO = ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        int FINE_LOCATION = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (RECORD_AUDIO != PackageManager.PERMISSION_GRANTED || FINE_LOCATION != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.ACCESS_FINE_LOCATION}, 500);
        }


        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @SuppressLint("MissingPermission")
            @Override
            public void onMapReady(@NonNull MapboxMap mapboxMap) {
                map = mapboxMap;



                map.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        if (PermissionsManager.areLocationPermissionsGranted(MainActivity.this)) {
                            LocationComponent locationComponent = map.getLocationComponent();
                            locationComponent.activateLocationComponent(
                                    LocationComponentActivationOptions.builder(MainActivity.this,style).build());
                            locationComponent.setLocationComponentEnabled(true);
                            locationComponent.setCameraMode(CameraMode.TRACKING);
                            locationComponent.setRenderMode(RenderMode.COMPASS);

                        } else {
                            PermissionsManager permissionsManager = new PermissionsManager(new PermissionsListener() {
                                @Override
                                public void onExplanationNeeded(List<String> permissionsToExplain) {

                                }

                                @Override
                                public void onPermissionResult(boolean granted) {

                                }
                            });
                            permissionsManager.requestLocationPermissions(MainActivity.this);
                        }

                    }
                });

            }
        });

    }


    @SuppressWarnings( {"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
// Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {

// Get an instance of the component
            LocationComponent locationComponent = map.getLocationComponent();

// Activate with options
            locationComponent.activateLocationComponent(
                    LocationComponentActivationOptions.builder(this, loadedMapStyle).build());

// Enable to make component visible
            locationComponent.setLocationComponentEnabled(true);

// Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING);

// Set the component's render mode
            locationComponent.setRenderMode(RenderMode.COMPASS);
        } else {
            PermissionsManager permissionsManager = new PermissionsManager(new PermissionsListener() {
                @Override
                public void onExplanationNeeded(List<String> permissionsToExplain) {

                }

                @Override
                public void onPermissionResult(boolean granted) {

                }
            });
            permissionsManager.requestLocationPermissions(this);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 500){
            if (grantResults[0] == RESULT_OK){
                Toast.makeText(getApplicationContext(),"ijin sudah diberikan , silahkan melanjutkan",Toast.LENGTH_LONG).show();
            }
        }

    }


    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {

    }

    @Override
    public void onPermissionResult(boolean granted) {

    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {

    }
}
