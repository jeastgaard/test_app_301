package com.example.foodbook;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

/*
Disclaimer:
Created by: jeastgaa
All Files are property of Joshua Eastgaard, Sept. 2022

All imports and overrides where found at https://developer.android.com/
If there are any other locations I resourced from they will be listed here:
    -https://www.tutorialspoint.com/how-do-i-put-a-border-around-an-android-textview (border.xml)
 */
/**
 * @class
 * @description The MainActivity which runs the android app. Launched when the application is first launched.
 * This will be the base of the app and where all data collects.
 */
public class MainActivity extends AppCompatActivity {

    // Used for status of intent results.
    public static int SUCCESS = 123;
    public static int SAVED = 321;
    public static int CANCEL = 1;
    public static int FAILURE = 0;

    ArrayList<Food> foods;
    FoodAdapter adapter;

    // Used in order to edit a specific food on the list instead of adding a new food to the bottom on edit.
    private int editingIndex;

    /**
     * @class
     * @description The override of the onCreate function for this activity. Will initialize everything we need to display our foods.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView rvFoods = (RecyclerView) findViewById(R.id.rv_food_list);

        foods = new ArrayList<>();
        adapter = new FoodAdapter(foods, this);

        rvFoods.setAdapter(adapter);
        rvFoods.setLayoutManager(new GridLayoutManager(this, 1));

    }

    ActivityResultLauncher<Intent> activityResultLaunch = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == this.SUCCESS ) {
                    Food newFood = (Food) result.getData()
                            .getSerializableExtra( String.valueOf(R.string.new_food) );
                    foods.add( newFood );
                    adapter.notifyDataSetChanged();
                    refreshCostView();
                } else if ( result.getResultCode() == this.SAVED ) {
                    Food savedFood = (Food) result.getData().
                            getSerializableExtra( String.valueOf( R.string.new_food ));
                    foods.set( editingIndex, savedFood );
                    adapter.notifyDataSetChanged();
                    refreshCostView();
                }
            });

    /**
     * @description Launches activity where user can define a new food to add to the list of foods.
     * @param view View calling this function.
     */
    public void addNewFood( View view ) {
        Intent intent = new Intent(this, NewFoodActivity.class );
        activityResultLaunch.launch( intent );
    }

    /**
     * @description This method will launch the add new food activity while passing in the data from
     * the food the user wants to edit.
     * @param position Positing on the list of stored foods which holds the food to edit.
     */
    public void editFood( int position ) {
        Intent intent = new Intent(this, NewFoodActivity.class );
        intent.putExtra( String.valueOf( R.string.edit_food ), true );
        intent.putExtra(String.valueOf( R.string.food_object ), foods.get( position ) );

        editingIndex = position;

        activityResultLaunch.launch( intent );
    }

    /**
     * @description Used to re-calculate the total cost of all the food. Defined as
     * sum( cost * count ) for all food.
     */
    public void refreshCostView() {
        int totalSum = 0;
        for( Food food : foods ) {
            totalSum += (food.getCost() * food.getCount() );
        }
        TextView totalCostView = (TextView) findViewById(R.id.total_cost_view);
        totalCostView.setText( String.valueOf( totalSum ) );
    }
}