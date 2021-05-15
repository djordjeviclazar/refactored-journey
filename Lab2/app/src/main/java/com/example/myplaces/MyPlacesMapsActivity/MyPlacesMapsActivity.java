package com.example.myplaces.MyPlacesMapsActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;

import com.example.myplaces.AboutComponent.About;
import com.example.myplaces.EditMyPlaceComponent.EditMyPlaceActivity;
import com.example.myplaces.ListComponent.MyPlacesList;
import com.example.myplaces.Models.MyPlace;
import com.example.myplaces.Models.MyPlacesData;
import com.example.myplaces.ViewMyPlacesComponent.ViewMyPlacesActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.myplaces.R;

import org.osmdroid.api.IMapController;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import android.preference.*;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

public class MyPlacesMapsActivity extends AppCompatActivity {

    MapView map = null;
    IMapController mapController = null;

    static int NEW_PLACE = 1;
    final double latNis = 43.3209, lonNis = 21.8958;
    static final int PERMISSION_ACCESS_FINE_LOCATION = 1;

    public static final int SHOW_MAP = 0;
    public static final int CENTER_PLACE_ON_MAP = 1;
    public static final int SELECT_COORDINATES = 2;

    private int state = 0;
    private boolean selCoorsEnabled = false;
    private GeoPoint placeLoc;

    MyLocationNewOverlay myLocationNewOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            Intent mapIntent = getIntent();
            Bundle mapBundle = mapIntent.getExtras();
            if (mapBundle != null)
            {
                state = mapBundle.getInt("state");
                if (state == CENTER_PLACE_ON_MAP)
                {
                    String placeLat = mapBundle.getString("lat");
                    String placeLon = mapBundle.getString("lon");
                    placeLoc = new GeoPoint(Double.parseDouble(placeLat), Double.parseDouble(placeLon));
                }
            }
        }
        catch (Exception e)
        {
            Log.d("Error", "ErrorReadingState");
        }

        setContentView(R.layout.activity_my_places_maps);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        if (state != SELECT_COORDINATES)
        {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent addLocationIntent = new Intent(MyPlacesMapsActivity.this, EditMyPlaceActivity.class);
                    startActivityForResult(addLocationIntent, NEW_PLACE);
                }
            });
        }
        else
        {
            ViewGroup layout = (ViewGroup)fab.getParent();
            if (layout != null)
            {
                layout.removeView(fab);
            }
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Context context = getApplicationContext();
        org.osmdroid.config.Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context));
        map = (MapView) findViewById(R.id.Map);
        map.setMultiTouchControls(true);
        mapController = map.getController();

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ACCESS_FINE_LOCATION);
        }
        else
        {
            setupMap();
        }

        if(mapController != null)
        {
            mapController.setZoom(15.0);
            GeoPoint startPoint = new GeoPoint(latNis, lonNis);
            mapController.setCenter(startPoint);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
        {

        }
    }

    private void showMyPlaces()
    {
        if (myLocationNewOverlay != null)
        {
            this.map.getOverlays().remove(myLocationNewOverlay);
        }

        final ArrayList<OverlayItem> items = new ArrayList<>();
        ArrayList<MyPlace> myPlacesList = MyPlacesData.getInstance().getMyPlaces();
        for(MyPlace currentPlace : myPlacesList)
        {
            String latString = currentPlace.getLatitude(), lonString = currentPlace.getLongitude();
            if (latString == null || latString.equals("") || lonString == null || lonString.equals(""))
            {
                continue;
            }

            double lat = Double.parseDouble(latString), lon = Double.parseDouble(lonString);
            OverlayItem item = new OverlayItem(currentPlace.getName(), currentPlace.getDescription(),
                                                new GeoPoint(lat, lon));
            item.setMarker(this.getResources().getDrawable(R.drawable.marker_default_focused_base));
            items.add(item);
        }

        ItemizedIconOverlay overlay = new ItemizedIconOverlay<>(items,
                new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                    @Override
                    public boolean onItemSingleTapUp(int index, OverlayItem item) {
                        Intent intent = new Intent(MyPlacesMapsActivity.this, ViewMyPlacesActivity.class);
                        intent.putExtra("position", index);
                        startActivity(intent);
                        return true;
                    }

                    @Override
                    public boolean onItemLongPress(int index, OverlayItem item) {
                        Intent intent = new Intent(MyPlacesMapsActivity.this, EditMyPlaceActivity.class);
                        intent.putExtra("position", index);
                        startActivity(intent);
                        return true;
                    }
                }, getApplicationContext());
        this.map.getOverlays().add(overlay);
    }

    private void setCenterPlaceOnMap()
    {
        mapController = map.getController();
        if (mapController != null)
        {
            mapController.setZoom(15.0);
            mapController.animateTo(placeLoc);
        }
    }

    private void setupMap()
    {
        showMyPlaces();
        switch (state)
        {
            case SHOW_MAP:
            {
                setMyLocationOverlay();

                break;
            }
            case SELECT_COORDINATES:
            {
                mapController = map.getController();
                if (mapController != null)
                {
                    mapController.setZoom(15.0);
                    mapController.setCenter(new GeoPoint(latNis, lonNis));
                }

                setOnMapClickOverlay();
                break;
            }
            case CENTER_PLACE_ON_MAP:
            default:
            {
                setCenterPlaceOnMap();
                break;
            }
        }

    }

    private void setMyLocationOverlay()
    {
        myLocationNewOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(this), map);
        myLocationNewOverlay.enableMyLocation();
        map.getOverlays().add(this.myLocationNewOverlay);
        mapController = map.getController();
        if (mapController != null)
        {
            mapController.setZoom(15.0);
            myLocationNewOverlay.enableFollowLocation();
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        switch (requestCode)
        {
            case PERMISSION_ACCESS_FINE_LOCATION:
            {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    if (state == SHOW_MAP)
                    {
                        //map.setActivated(true);
                    }
                    else
                    {
                        if (state == SELECT_COORDINATES)
                        {
                            //setOnMapClickOverlay();
                        }
                    }
                    setupMap();
                }

                return;
            }
        }
    }

    public void onResume()
    {
        super.onResume();
        map.onResume();
    }

    public void onPause()
    {
        super.onPause();
        map.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if (state == SELECT_COORDINATES && !selCoorsEnabled)
        {
            menu.add(0, 1, 1, "Select Coordinates");
            menu.add(0, 2, 2, "Cancel");
            return super.onCreateOptionsMenu(menu);
        }
        else
        {
            getMenuInflater().inflate(R.menu.menu_my_places_maps, menu);
            return true;
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (state == SELECT_COORDINATES && !selCoorsEnabled)
        {
            if(id == 1)
            {
                selCoorsEnabled = true;
                Toast.makeText(this, "Select Coordinates", Toast.LENGTH_SHORT).show();
                item.setEnabled(false);
            }
            else
            {
                if (id == 2)
                {
                    setResult(Activity.RESULT_CANCELED);
                    finish();
                }
            }
        }
        else
        {
            if(id == R.id.new_place_item)
            {
                // Toast.makeText(this, "New place!", Toast.LENGTH_SHORT).show();
                Intent editMyPlaceIntent = new Intent(this, EditMyPlaceActivity.class);
                startActivityForResult(editMyPlaceIntent, 1);
            }
            else
            {
                if(id == R.id.about_item)
                {
                    //Toast.makeText(this, "Annout!", Toast.LENGTH_SHORT).show();
                    Intent aboutIntent = new Intent(this, About.class);
                    startActivity(aboutIntent);
                }
                else
                {
                    if(id == android.R.id.home)
                    {
                        finish();
                    }
                }
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void setOnMapClickOverlay()
    {
        MapEventsReceiver mapEventsReceiver = new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {
                if (state == SELECT_COORDINATES && selCoorsEnabled)
                {
                    String lon = Double.toString(p.getLongitude());
                    String lat = Double.toString(p.getLatitude());

                    Intent locationIntent = new Intent();
                    locationIntent.putExtra("lon", lon);
                    locationIntent.putExtra("lat", lat);
                    setResult(Activity.RESULT_OK, locationIntent);
                    finish();
                }

                return false;
            }

            @Override
            public boolean longPressHelper(GeoPoint p) {
                return false;
            }
        };

        MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(mapEventsReceiver);
        map.getOverlays().add(mapEventsOverlay);
    }
}