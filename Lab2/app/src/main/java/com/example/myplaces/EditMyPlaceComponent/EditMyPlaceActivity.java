package com.example.myplaces.EditMyPlaceComponent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.myplaces.AboutComponent.About;
import com.example.myplaces.ListComponent.MyPlacesList;
import com.example.myplaces.Models.MyPlace;
import com.example.myplaces.Models.MyPlacesData;
import com.example.myplaces.MyPlacesMapsActivity.MyPlacesMapsActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myplaces.R;

public class EditMyPlaceActivity extends AppCompatActivity implements View.OnClickListener {

    boolean editMode = true;
    int position = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_my_place);

        // If positionBundle is null, user wants to add
        // If not null, user clicked on Edit in contextMenu in MyPlacesList activity
        try
        {
            Intent listIntent = getIntent();
            Bundle positionBundle = listIntent.getExtras();
            if (positionBundle != null)
            {
                // add
                position = positionBundle.getInt("position");
            }
            else
            {
                editMode = false;
            }
        }
        catch (Exception e)
        {
            editMode = false;
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Button finishBtn = (Button) findViewById(R.id.editmyplace_finish_btn);
        finishBtn.setOnClickListener(this);
        finishBtn.setEnabled(false);
        //finishBtn.setText("Add");
        Button cancelBtn = (Button) findViewById(R.id.editmyplace_cancel_btn);
        cancelBtn.setOnClickListener(this);

        EditText nameEditText = (EditText) findViewById(R.id.editmyplace_name_edit);
        EditText descEditText = (EditText) findViewById(R.id.editmyplace_desc_edit);

        if (!editMode)
        {
            finishBtn.setEnabled(false);
            finishBtn.setText("Add");
        }
        else
        {
            if (position >= 0)
            {
                finishBtn.setText("Save");
                MyPlace place = MyPlacesData.getInstance().getPlace(position);

                nameEditText.setText(place.getName());
                descEditText.setText(place.getDescription());
            }
        }

        nameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                finishBtn.setEnabled(s.length() > 0);
            }
        });

        descEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) { finishBtn.setEnabled(nameEditText.length() > 0); }
        });

        Button locationButton = (Button) findViewById(R.id.editmyplace_location_btn);
        locationButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.editmyplace_finish_btn:
            {
                EditText editName = (EditText) findViewById(R.id.editmyplace_name_edit);
                String newName = editName.getText().toString();
                EditText editDesc = (EditText) findViewById(R.id.editmyplace_desc_edit);
                String newDesc = editDesc.getText().toString();
                EditText latEdit = (EditText)findViewById(R.id.editmyplace_lat_edit);
                String lat = latEdit.getText().toString();
                EditText lonEdit = (EditText)findViewById(R.id.editmyplace_lon_edit);
                String lon = lonEdit.getText().toString();

                if (!editMode)
                {
                    MyPlace place = new MyPlace(newName, newDesc);
                    place.setLatitude(lat);
                    place.setLongitude(lon);
                    MyPlacesData.getInstance().addNewPlace(place);
                }
                else
                {
                    /*
                    MyPlace place = MyPlacesData.getInstance().getPlace(position);
                    place.setName(newName);
                    place.setDescription(newDesc);
                    place.setLatitude(lat);
                    place.setLongitude(lon);
                    */
                    MyPlacesData.getInstance().updatePlace(position, newName, newDesc, lon, lat);
                }

                setResult(Activity.RESULT_OK);
                finish();
                break;
            }

            case R.id.editmyplace_cancel_btn:
            {
                setResult(Activity.RESULT_CANCELED);
                finish();
                break;
            }

            case R.id.editmyplace_location_btn:
            {
                Intent locationIntent = new Intent(this, MyPlacesMapsActivity.class);
                locationIntent.putExtra("state", MyPlacesMapsActivity.SELECT_COORDINATES);
                startActivityForResult(locationIntent, 1);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try
        {
            if (resultCode == Activity.RESULT_OK)
            {
                String lon = data.getExtras().getString("lon");
                EditText lonText = (EditText)findViewById(R.id.editmyplace_lon_edit);
                lonText.setText(lon);

                String lat = data.getExtras().getString("lat");
                EditText latText = (EditText)findViewById(R.id.editmyplace_lat_edit);
                latText.setText(lat);

                ((Button)findViewById(R.id.editmyplace_finish_btn)).setEnabled(true);
            }
        }
        catch (Exception e)
        {

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_edit_my_place, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.show_map_item)
        {
            //Toast.makeText(this, "Show map", Toast.LENGTH_SHORT).show();
            Intent mapIntent = new Intent(this, MyPlacesMapsActivity.class);
            mapIntent.putExtra("state", MyPlacesMapsActivity.SHOW_MAP);
            startActivity(mapIntent);
        }
        else
        {
            if (id == R.id.my_places_list_item)
            {
                Intent myPlacesListItemIntent = new Intent(this, MyPlacesList.class);
                startActivity(myPlacesListItemIntent);
            }
            else
            {
                if (id == R.id.about_item)
                {
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