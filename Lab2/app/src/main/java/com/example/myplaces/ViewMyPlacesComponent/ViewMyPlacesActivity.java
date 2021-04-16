package com.example.myplaces.ViewMyPlacesComponent;

import android.content.Intent;
import android.os.Bundle;

import com.example.myplaces.AboutComponent.About;
import com.example.myplaces.EditMyPlaceComponent.EditMyPlaceActivity;
import com.example.myplaces.Models.MyPlace;
import com.example.myplaces.Models.MyPlacesData;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myplaces.R;

public class ViewMyPlacesActivity extends AppCompatActivity {

    static int newPlaces = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_my_places);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        int position = -1;
        try
        {
            Intent listIntent = getIntent();
            Bundle positionBundle = listIntent.getExtras();
            position = positionBundle.getInt("position");
        }
        catch (Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            finish();
        }
        if (position >= 0)
        {
            MyPlace place = MyPlacesData.getInstance().getPlace(position);
            TextView twName = (TextView) findViewById(R.id.viewmyplace_name_text);
            twName.setText(place.getName());
            TextView twDesc = (TextView) findViewById(R.id.viewmyplace_desc_text);
            twDesc.setText(place.getDescription());
        }

        final Button finishBtn = (Button) findViewById(R.id.viewmyplace_finish_btn);
        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_view_my_places, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.show_map_item)
        {
            Toast.makeText(this, "Show map", Toast.LENGTH_SHORT).show();
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
}