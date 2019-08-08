package dev.malikkurosaki.bestclean;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.mapbox.android.gestures.MoveGestureDetector;
import com.mapbox.api.geocoding.v5.GeocodingCriteria;
import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.snapshotter.MapSnapshot;
import com.mapbox.mapboxsdk.snapshotter.MapSnapshotter;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity{

    private String TOKENNYA = "pk.eyJ1IjoibWFsaWtrdXJvc2FraSIsImEiOiJjancwcWNuMGYwZHVxNDhtdGRuNXBqdTYzIn0._FSW8xQn8U5-VORm722x3Q";
    /*private MapView mapView;
    private MapboxMap mapboxMap1;
    private FusedLocationProviderClient client;
    private LatLng latLng;
    private Style style1;

    private ImageView targetGps;
    private boolean angkat = false;

    private ImageView marker1;
    private ImageView marker2;

    private int tunggu = 0;
    private ProgressBar loadingAlamat;

    private FrameLayout alamatContainer;
    private TextView alamatKet;
    private Button alamatPilih;

    private Map<String,Object> kirimAlamat;
    private MapSnapshot sanapshot1;

    private Button logonLogin;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this,TOKENNYA);
        setContentView(R.layout.activity_main);


        /*int FINE_LOCATION = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int EXTERNAL = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (FINE_LOCATION != PackageManager.PERMISSION_GRANTED || EXTERNAL != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.READ_EXTERNAL_STORAGE},55);
        }

        mapView = findViewById(R.id.mapView);
        targetGps = findViewById(R.id.targetGps);
        marker1 = findViewById(R.id.marker1);
        marker2 = findViewById(R.id.marker2);
        loadingAlamat = findViewById(R.id.loadingAlamat);
        alamatContainer = findViewById(R.id.alamatContainer);
        alamatKet = findViewById(R.id.alamatKet);
        alamatPilih = findViewById(R.id.alamatPilih);

        marker1.setVisibility(View.GONE);
        marker2.setVisibility(View.GONE);
        targetGps.setVisibility(View.GONE);
        alamatContainer.setVisibility(View.GONE);
        logonLogin = findViewById(R.id.loginLogin);

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull MapboxMap mapboxMap) {
                mapboxMap1 = mapboxMap;
                mapboxMap1.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        style1 = style;
                        client = LocationServices.getFusedLocationProviderClient(MainActivity.this);

                        mapView.addOnDidFinishLoadingMapListener(new MapView.OnDidFinishLoadingMapListener() {
                            @SuppressLint("MissingPermission")
                            @Override
                            public void onDidFinishLoadingMap() {
                                client.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                                    @Override
                                    public void onSuccess(Location location) {
                                        latLng = new LatLng(location.getLatitude(),location.getLongitude());
                                        CameraPosition position = new CameraPosition.Builder()
                                                .target(new LatLng(location.getLatitude(), location.getLongitude())) // Sets the new camera position
                                                .zoom(15) // Sets the zoom
                                                .bearing(180) // Rotate the camera
                                                .tilt(30) // Set the camera tilt
                                                .build(); // Creates a CameraPosition from the builder

                                        mapboxMap1.animateCamera(CameraUpdateFactory.newCameraPosition(position), 7000);
                                    }
                                });
                            }
                        });
                    }
                });


                //camera bergerak
                mapboxMap.addOnMoveListener(new MapboxMap.OnMoveListener() {
                    @Override
                    public void onMoveBegin(@NonNull MoveGestureDetector detector) {
                       Timber.i("onMoveBegin: %s", +detector.getMoveThreshold());
                       marker1.setVisibility(View.VISIBLE);
                       marker2.setVisibility(View.GONE);
                       YoYo.with(Techniques.Pulse).duration(1000).repeat(5).playOn(marker1);
                       alamatContainer.setVisibility(View.GONE);
                       angkat = true;
                    }

                    @Override
                    public void onMove(@NonNull MoveGestureDetector detector) {

                    }

                    @Override
                    public void onMoveEnd(@NonNull MoveGestureDetector detector) {

                    }
                });

                // camera diam
                mapboxMap1.addOnCameraIdleListener(new MapboxMap.OnCameraIdleListener() {
                    @Override
                    public void onCameraIdle() {
                        marker1.setVisibility(View.GONE);
                        marker2.setVisibility(View.VISIBLE);
                        targetGps.setVisibility(View.VISIBLE);
                        YoYo.with(Techniques.ZoomInUp).duration(500).playOn(marker2);

                        if (angkat){
                            tunggu += 2000;
                            alamatContainer.setVisibility(View.VISIBLE);
                            loadingAlamat.setVisibility(View.VISIBLE);
                            alamatKet.setText("");

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    cariLokasi();
                                }
                            },tunggu);
                        }

                    }
                });

            }

        });

        logonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,Object> keHalaman = new HashMap<>();
                keHalaman.put("halaman","login");
                Intent intent = new Intent(MainActivity.this,Main2Activity.class);
                intent.putExtra("halaman", (Serializable) keHalaman);
                startActivity(intent);

            }
        });

        mapView.addOnDidFinishLoadingMapListener(new MapView.OnDidFinishLoadingMapListener() {
            @Override
            public void onDidFinishLoadingMap() {
                Toast.makeText(getApplicationContext(),"selesai",Toast.LENGTH_LONG).show();
            }
        });


        targetGps.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View v) {
                LocationComponent locationComponent = mapboxMap1.getLocationComponent();
                locationComponent.activateLocationComponent(LocationComponentActivationOptions.builder(MainActivity.this,style1).build());
                locationComponent.setLocationComponentEnabled(true);
                locationComponent.setCameraMode(CameraMode.TRACKING);
                locationComponent.setRenderMode(RenderMode.COMPASS);
            }
        });*/
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        int id = R.id.frameCon1;
        HelperNgeFragment helperNgeFragment = new HelperNgeFragment(manager,transaction,id);

        helperNgeFragment.ngeFragment(new LayoutFragment1());

    }

    /*void cariLokasi(){
        LatLng lokasi = mapboxMap1.getCameraPosition().target;
        double lat = lokasi.getLatitude();
        double lang = lokasi.getLongitude();

        try {
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
            addresses = geocoder.getFromLocation(lat, lang, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL

            loadingAlamat.setVisibility(View.GONE);
            alamatKet.setText(address);
            angkat = false;
            tunggu = 0;

            kirimAlamat = new HashMap<>();
            kirimAlamat.put("halaman","jemput");
            kirimAlamat.put("address",address);
            kirimAlamat.put("city",city);
            kirimAlamat.put("state",state);
            kirimAlamat.put("country",country);
            kirimAlamat.put("postalCode",postalCode);

            alamatPilih.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadingAlamat.setVisibility(View.VISIBLE);
                    MapSnapshotter.Options snapShotOptions = new MapSnapshotter.Options(500, 500);
                    snapShotOptions.withRegion(mapboxMap1.getProjection().getVisibleRegion().latLngBounds);
                    snapShotOptions.withStyle(mapboxMap1.getStyle().getUrl());
                    MapSnapshotter mapSnapshotter = new MapSnapshotter(MainActivity.this, snapShotOptions);
                    mapSnapshotter.start(new MapSnapshotter.SnapshotReadyCallback() {
                        @Override
                        public void onSnapshotReady(MapSnapshot snapshot) {
                            sanapshot1 = snapshot;

                            if (sanapshot1.getBitmap() == null){
                                Toast.makeText(getApplicationContext(),"belum siap , ulangi",Toast.LENGTH_LONG).show();
                                return;
                            }

                            Bitmap bitmapImage = sanapshot1.getBitmap();
                            ByteArrayOutputStream bStream = new ByteArrayOutputStream();
                            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, bStream);
                            byte[] byteArray = bStream.toByteArray();
                            kirimAlamat.put("gambar",byteArray);
                            Intent intent = new Intent(MainActivity.this,Main2Activity.class);
                            intent.putExtra("halaman", (Serializable) kirimAlamat);
                            loadingAlamat.setVisibility(View.GONE);
                            startActivity(intent);

                        }
                    });
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 55){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(getApplicationContext(),"ijin telah diberikan",Toast.LENGTH_LONG).show();
            }
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
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
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        mapView.onSaveInstanceState(outState);
    }*/



}
