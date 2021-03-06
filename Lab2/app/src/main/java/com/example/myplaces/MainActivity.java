package com.example.myplaces;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.myplaces.AboutComponent.About;
import com.example.myplaces.EditMyPlaceComponent.EditMyPlaceActivity;
import com.example.myplaces.ListComponent.MyPlacesList;
import com.example.myplaces.MyPlacesMapsActivity.MyPlacesMapsActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    static int newPlaces = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addingMyPlaceIntent = new Intent(MainActivity.this, EditMyPlaceActivity.class);
                startActivityForResult(addingMyPlaceIntent, newPlaces);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
                // Toast.makeText(this, "New place!", Toast.LENGTH_SHORT).show();
                Intent editMyPlaceIntent = new Intent(this, EditMyPlaceActivity.class);
                startActivityForResult(editMyPlaceIntent, newPlaces);
            }
            else
            {
                if(id == R.id.my_places_list_item)
                {
                    //Toast.makeText(this, "My places!", Toast.LENGTH_SHORT).show();
                    Intent myPlacesIntent = new Intent(this, MyPlacesList.class);
                    startActivity(myPlacesIntent);
                }
                else
                {
                    if(id == R.id.about_item)
                    {
                        //Toast.makeText(this, "Annout!", Toast.LENGTH_SHORT).show();
                        Intent aboutIntent = new Intent(this, About.class);
                        startActivity(aboutIntent);
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
        }
    }
}