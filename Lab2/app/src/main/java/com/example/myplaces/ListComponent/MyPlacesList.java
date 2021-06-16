package com.example.myplaces.ListComponent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.myplaces.AboutComponent.About;
import com.example.myplaces.EditMyPlaceComponent.EditMyPlaceActivity;
import com.example.myplaces.Models.MyPlace;
import com.example.myplaces.Models.MyPlacesData;
import com.example.myplaces.MyPlacesMapsActivity.MyPlacesMapsActivity;
import com.example.myplaces.ViewMyPlacesComponent.ViewMyPlacesActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.myplaces.R;

import java.util.ArrayList;

public class MyPlacesList extends AppCompatActivity {

    //ArrayList<String> places;
    static int newPlaces = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_places_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // To enable return to caller:
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addingMyPlaceIntent = new Intent(MyPlacesList.this, EditMyPlaceActivity.class);
                startActivityForResult(addingMyPlaceIntent, newPlaces);
            }
        });

        /*
        places = new ArrayList<String>();
        places.add("Trg Kralja Milana");
        places.add("Čair");
        places.add("Čardak");
        places.add("Sövronnüe");
        */

        ListView myPlacesList = (ListView)findViewById(R.id.my_places_list);
        myPlacesList.setAdapter(new ArrayAdapter<MyPlace>(this,
                                                                  android.R.layout.simple_list_item_1,
                                                                  MyPlacesData.getInstance().getMyPlaces()));
        myPlacesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Bundle positionBundle = new Bundle();
                positionBundle.putInt("position", position);

                Intent viewIntent = new Intent(MyPlacesList.this, ViewMyPlacesActivity.class);
                viewIntent.putExtras(positionBundle);
                startActivity(viewIntent);
            }
        });
        myPlacesList.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
                MyPlace place = MyPlacesData.getInstance().getPlace(info.position);

                menu.setHeaderTitle(place.getName());
                menu.add(0, 1, 1, "View Place");
                menu.add(0, 2, 2, "Edit Place");
                menu.add(0, 3, 3, "Delete Place");
                menu.add(0, 4, 4, "Show on map");
            }
        });

        MyPlacesData.getInstance().setEventListener(new MyPlacesData.ListUpdatedEventListener() {
            @Override
            public void onListUpdated() {
                Toast.makeText(MyPlacesList.this, "Refresh activity to see new places", Toast.LENGTH_SHORT).show();
                
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_my_places_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.show_map_item)
        {
            //Toast.makeText(this, "Show map!", Toast.LENGTH_SHORT).show();
            Intent mapIntent = new Intent(this, MyPlacesMapsActivity.class);
            startActivity(mapIntent);
        }
        else
        {
            if(id == R.id.new_place_item)
            {
                //Toast.makeText(this, "New place!", Toast.LENGTH_SHORT).show();
                Intent editMyPlaceIntent = new Intent(this, EditMyPlaceActivity.class);
                startActivityForResult(editMyPlaceIntent, newPlaces);
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
                    if (id == android.R.id.home)
                    {
                        finish();
                    }
                }
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK)
        {
            Toast.makeText(this, "New place added", Toast.LENGTH_SHORT).show();

            ListView myPlacesList = (ListView) findViewById(R.id.my_places_list);
            myPlacesList.setAdapter(new ArrayAdapter<MyPlace>(this,
                                                                        android.R.layout.simple_list_item_1,
                                                                        MyPlacesData.getInstance().getMyPlaces()));
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Bundle positionBundle = new Bundle();
        positionBundle.putInt("position", info.position);

        Intent contexItemIntent = null;
        if (item.getItemId() == 1)
        {
            contexItemIntent = new Intent(this, ViewMyPlacesActivity.class);
            contexItemIntent.putExtras(positionBundle);
            startActivity(contexItemIntent);
        }
        else
        {
            if (item.getItemId() == 2)
            {
                contexItemIntent = new Intent(this, EditMyPlaceActivity.class);
                contexItemIntent.putExtras(positionBundle);
                startActivityForResult(contexItemIntent, 1);
            }
            else
            {
                if(item.getItemId() == 3)
                {
                    MyPlacesData.getInstance().deletePlace(info.position);
                    setList();
                }
                else
                {
                    if (item.getItemId() == 4)
                    {
                        Intent showItemIntent = new Intent(this, MyPlacesMapsActivity.class);
                        showItemIntent.putExtra("state", MyPlacesMapsActivity.CENTER_PLACE_ON_MAP);

                        MyPlace place = MyPlacesData.getInstance().getPlace(info.position);
                        showItemIntent.putExtra("lat", place.getLatitude());
                        showItemIntent.putExtra("lon", place.getLongitude());

                        startActivityForResult(showItemIntent, 2);
                    }
                }
            }
        }

        return super.onContextItemSelected(item);
    }

    private void setList()
    {
        ListView myPlacesList = (ListView) findViewById(R.id.my_places_list);
        myPlacesList.setAdapter(new ArrayAdapter<MyPlace>(this, android.R.layout.simple_list_item_1, MyPlacesData.getInstance().getMyPlaces()));
    }
}