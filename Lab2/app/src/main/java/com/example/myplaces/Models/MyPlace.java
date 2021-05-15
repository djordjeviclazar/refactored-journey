package com.example.myplaces.Models;

public class MyPlace {

    private String name;
    private String description;
    private String longitude;
    private String latitude;

    int id;

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
    public int getId() { return id; }


    // seters:
    public void setName(String name) { this.name = name; }
    public void setDescription(String desc) { this.description = desc; }
    public void setLongitude(String longitude) { this.longitude = longitude; }
    public void setLatitude(String latitude) { this.latitude = latitude; }
    public void setId(int id) { this.id = id; }

}
