package com.example.myplaces.ListComponent;

import android.content.Intent;
import android.os.Bundle;

import com.example.myplaces.AboutComponent.About;
import com.example.myplaces.Models.MyPlace;
import com.example.myplaces.Models.MyPlacesData;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_places_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // To enable return to caller:
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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
                MyPlace place = (MyPlace)adapterView.getAdapter().getItem(position);
                Toast.makeText(getApplicationContext(), place.getName() + " Selected", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(this, "Show map!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            if(id == R.id.new_place_item)
            {
                Toast.makeText(this, "New place!", Toast.LENGTH_SHORT).show();
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
}