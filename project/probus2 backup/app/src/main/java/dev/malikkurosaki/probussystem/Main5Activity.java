package dev.malikkurosaki.probussystem;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.mapbox.api.geocoding.v5.GeocodingCriteria;
import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Main5Activity extends AppCompatActivity {

    private FusedLocationProviderClient client;
    private MapView mapView;
    private MapboxMap mapboxMap1;
    private LatLng latLng;
    private int batas = 0;
    private Location location1;
    private Button okLokasi;
    private LatLng lokasi;

    private String TOKENNYA = "pk.eyJ1IjoibWFsaWtrdXJvc2FraSIsImEiOiJjancwcWNuMGYwZHVxNDhtdGRuNXBqdTYzIn0._FSW8xQn8U5-VORm722x3Q";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        Mapbox.getInstance(this,TOKENNYA);
        setContentView(R.layout.activity_main5);

        client = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},499);

            Toast.makeText(getApplicationContext(),"minta ijin lokasi",Toast.LENGTH_LONG).show();
            return;
        }

        mapView = findViewById(R.id.mapView);
        okLokasi = findViewById(R.id.okLokasi);

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull MapboxMap mapboxMap) {
                mapboxMap1 = mapboxMap;
                mapboxMap1.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onStyleLoaded(@NonNull final Style style) {

                        // munculkan lokasi gps
                        /*LocationComponent locationComponent = mapboxMap1.getLocationComponent();
                        locationComponent.activateLocationComponent(LocationComponentActivationOptions.builder(Main5Activity.this,style).build());
                        locationComponent.setLocationComponentEnabled(true);
                        locationComponent.setCameraMode(CameraMode.TRACKING);
                        locationComponent.setRenderMode(RenderMode.COMPASS);*/

                        mapView.addOnDidFinishLoadingMapListener(new MapView.OnDidFinishLoadingMapListener() {
                            @Override
                            public void onDidFinishLoadingMap() {
                                client.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                                    @Override
                                    public void onSuccess(Location location) {
                                        location1 = location;
                                        latLng = new LatLng(location.getLatitude(),location.getLongitude());

                                        CameraPosition position = new CameraPosition.Builder()
                                                .target(new LatLng(location.getLatitude(), location.getLongitude())) // Sets the new camera position
                                                .zoom(15) // Sets the zoom
                                                .bearing(180) // Rotate the camera
                                                .tilt(30) // Set the camera tilt
                                                .build(); // Creates a CameraPosition from the builder

                                        mapboxMap1.animateCamera(CameraUpdateFactory
                                                .newCameraPosition(position), 7000);
                                        Toast.makeText(getApplicationContext(),"zoomm",Toast.LENGTH_LONG).show();

                                        //style.addImage("marker-icon-id",getResources().getDrawable(R.drawable.menu_icon1));

                                        GeoJsonSource geoJsonSource = new GeoJsonSource("source-id", Feature.fromGeometry(Point.fromLngLat(location.getLongitude(),location.getLatitude())));
                                        style.addSource(geoJsonSource);

                                        SymbolLayer symbolLayer = new SymbolLayer("layer-id","source-id");
                                        symbolLayer.withProperties(PropertyFactory.iconImage("marker-icon-id"));
                                        style.addLayer(symbolLayer);

                                    }
                                });
                            }
                        });

                    }
                });


            }
        });


        mapView.addOnCameraDidChangeListener(new MapView.OnCameraDidChangeListener() {
            @Override
            public void onCameraDidChange(boolean animated) {


            }
        });

        okLokasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               lokasi = mapboxMap1.getCameraPosition().target;

                MapboxGeocoding client = MapboxGeocoding.builder()
                        .accessToken(TOKENNYA)
                        .query(Point.fromLngLat(lokasi.getLongitude(), lokasi.getLatitude()))
                        .geocodingTypes(GeocodingCriteria.TYPE_ADDRESS)
                        .build();

                client.enqueueBatchCall(new Callback<List<GeocodingResponse>>() {
                    @Override
                    public void onResponse(Call<List<GeocodingResponse>> call, Response<List<GeocodingResponse>> response) {
                        if (response.body() != null) {
                            List<GeocodingResponse> results =  response.body();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<GeocodingResponse>> call, Throwable t) {

                    }
                });

               Toast.makeText(getApplicationContext(),String.valueOf(lokasi.getLatitude()),Toast.LENGTH_LONG).show();
            }
        });



       /* final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                batas ++;

                if (latLng == null || mapboxMap1 == null){
                    handler.postDelayed(this,1000);
                }
                if (batas > 10){
                    handler.removeCallbacks(this);
                    Toast.makeText(getApplicationContext(),"batas "+String.valueOf(batas),Toast.LENGTH_LONG).show();
                }


            }
        },1000);*/

      /* mapView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if (location1 != null){
                   Intent intent = new PlacePicker.IntentBuilder()
                           .accessToken(Mapbox.getAccessToken())
                           .placeOptions(
                                   PlacePickerOptions.builder()
                                           .statingCameraPosition(
                                                   new CameraPosition.Builder()
                                                           .target(new LatLng(location1.getLatitude(), location1.getLongitude()))
                                                           .zoom(16)
                                                           .build())
                                           .build())
                           .build(Main5Activity.this);
                   startActivityForResult(intent,345);
               }


           }
       });*/


    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 499){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                recreate();
            }
        }


    }

    /*@Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }*/
}
