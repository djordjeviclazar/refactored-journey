package com.example.myplaces.Models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class MyPlace {

    public String name;
    public String description;
    public String longitude;
    public String latitude;

    @Exclude
    public String key;
    public MyPlace() {}

    public MyPlace(String name, String desc)
    {
        this.description = desc;
        this.name = name;
    }

    public MyPlace(String name)
    {
        this(name, "");
    }

    // Overrides

    @Override
    public String toString()
    {
        return this.name;
    }

    // geters:
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getLongitude() { return longitude; }
    public String getLatitude() { return latitude; }

    // seters:
    public void setName(String name) { this.name = name; }
    public void setDescription(String desc) { this.description = desc; }
    public void setLongitude(String longitude) { this.longitude = longitude; }
    public void setLatitude(String latitude) { this.latitude = latitude; }
}
