package com.example.myplaces.EditMyPlaceComponent;

import android.app.Activity;
import android.os.Bundle;

import com.example.myplaces.Models.MyPlace;
import com.example.myplaces.Models.MyPlacesData;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.myplaces.R;

public class EditMyPlaceActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_my_place);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button finishBtn = (Button) findViewById(R.id.editmyplace_finish_btn);
        finishBtn.setOnClickListener(this);
        Button cancelBtn = (Button) findViewById(R.id.editmyplace_cancel_btn);
        cancelBtn.setOnClickListener(this);
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

                MyPlace place = new MyPlace(newName, newDesc);
                MyPlacesData.getInstance().addNewPlace(place);

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
        }
    }
}