package com.example.myplaces.Models;

public class MyPlace {

    private String name;
    private String description;

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

    // seters:
    public void setName(String name) { this.name = name; }
    public void setDescription(String desc) { this.description = desc; }
}
