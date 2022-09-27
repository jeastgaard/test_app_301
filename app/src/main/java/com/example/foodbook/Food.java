package com.example.foodbook;

import java.io.Serializable;
import java.util.Date;

/**
 * @class Food
 * @impliments Serializable
 * @description Used to store data about food. This class encapsulates it's data.
 * The name does not have a setter function as this is mean to be final.
 */
public class Food implements Serializable {

    private String foodName, description, location;
    private Date bestBeforeDate;
    private int count, locationPosition, cost;

    /**
     * Constructor
     * @param foodName The name of the food
     * @param description A short (30 char) descirption of the food
     * @param count The count of how many items this entry consists of.
     * @param bestBeforeDate The Date which the food is suppost to expire.
     * @param location The location in the house which the food is store.
     * @param locationPosition The position of each location relevant to the string array in the resources.
     *                         0 = Fridge
     *                         1 = Pantry
     *                         2 = Freezer
     * @param cost The cost per item of the food.
     */
    public Food( String foodName, String description, int count, Date bestBeforeDate, String location, int locationPosition, int cost ) {
        this.foodName = foodName;
        this.count = count;
        this.bestBeforeDate = bestBeforeDate;
        this.description = description;
        this.location = location;
        this.locationPosition = locationPosition;
        this.cost = cost;
    }

    public String getName() { return foodName; }
    public String getDescription() { return description; }
    public Integer getCount() { return count;}
    public Long getBestBeforeDate() { return bestBeforeDate.getTime(); }
    public String getLocation() { return location; }
    public int getLocationPosition() { return locationPosition; }
    public Integer getCost() { return cost; };


    public void setDescription( String description ) { this.description = description; }
    public void setCount( int count ) { this.count = count; }
    public void setBestBeforeDate( Date bestBeforeDate ) { this.bestBeforeDate = bestBeforeDate; }
    public void setLocation( String location ) { this.location = location; }
    public void setLocationPosition( int locationPosition ) { this.locationPosition = locationPosition; }
    public void setCost( int cost ) { this.cost = cost; }
}
