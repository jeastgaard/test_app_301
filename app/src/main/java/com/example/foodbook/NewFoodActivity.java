package com.example.foodbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * @class
 * @description Represents the activity where users can enter data to create a new food item.
 * This activity is designed to be launched with a intent for results.
 */
public class NewFoodActivity extends AppCompatActivity {

    private Intent intent;
    private Button confirmButton;
    private Food alreadyStoredFood;
    private boolean isEditing = false;
    private CalendarView calendar;
    private long currentSelectedDate;

    /**
     * @class
     * @description The override of the onCreate function for this activity. Will initialize
     * everything we need to present data fields and save the data.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_food);

        intent = getIntent();

        confirmButton = (Button) findViewById(R.id.confirm_new_food);
        confirmButton.setEnabled( false );

        ArrayList< EditText > requiredFields =  new ArrayList<>();
        requiredFields.add( (EditText) findViewById(R.id.new_found_name) );
        requiredFields.add( (EditText) findViewById(R.id.new_food_count) );
        requiredFields.add( (EditText) findViewById(R.id.new_food_cost) );

        setUpRequiredFields( requiredFields );

        calendar = (CalendarView) findViewById(R.id.new_food_date);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                Calendar c = Calendar.getInstance();
                c.set( year, month, day );
                currentSelectedDate = c.getTimeInMillis();
                calendar.setDate( currentSelectedDate );
            }
        });

        Spinner locationChoices = (Spinner) findViewById(R.id.new_food_location_choice);
        ArrayAdapter<CharSequence>adapter= ArrayAdapter.createFromResource(this, R.array.locations, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        locationChoices.setAdapter( adapter );

        if( intent.getBooleanExtra(String.valueOf( R.string.edit_food ), false) ) {
            // We need to get the food object and prefill everything.
            isEditing = true;
            alreadyStoredFood = (Food) intent.getSerializableExtra( String.valueOf( R.string.food_object ));
            preFillFields();
        }

    }

    /**
     * @description Used if we have passed in a food object for editing through our intent.
     * It will use that food object to pre-fill all the fields presented on the
     * activity_new_food.xml layout.
     */
    private void preFillFields() {
        EditText foodName = (EditText) findViewById( R.id.new_found_name  );
        EditText foodCount = (EditText) findViewById(R.id.new_food_count);
        CalendarView foodExpDate = (CalendarView) findViewById(R.id.new_food_date);
        Spinner foodLocation = (Spinner) findViewById(R.id.new_food_location_choice);
        EditText foodDescription = (EditText) findViewById(R.id.new_food_description);
        EditText foodCost = (EditText) findViewById(R.id.new_food_cost);

        foodName.setText( alreadyStoredFood.getName() );
        // We don't want to allow the user to change the name of already save food
        foodName.setFocusable( false );
        foodName.setClickable( false );
        foodCount.setText( alreadyStoredFood.getCount().toString() );
        foodExpDate.setDate( alreadyStoredFood.getBestBeforeDate() );
        foodLocation.setSelection( alreadyStoredFood.getLocationPosition() );
        foodDescription.setText( alreadyStoredFood.getDescription() );
        foodCost.setText( alreadyStoredFood.getCost().toString() );
    }

    /**
     * @description Binds the defined fields to a function call when field has been changed.
     * This is so the confirm can be disabled if we do not have all the required information.
     * @param requiredFields
     */
    private void setUpRequiredFields( ArrayList< EditText > requiredFields ) {

        for( EditText field : requiredFields ) {
            field.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                @Override
                public void afterTextChanged(Editable editable) {
                    enableDisableButton( requiredFields );
                }
            });
        }
    }

    /**
     * @description Determines if we have all the required fields filled out.
     * If so we will enable the button, else we will disabeld it.
     * @param requiredFields List of views that are required to hold an input.
     */
    private void enableDisableButton( ArrayList< EditText > requiredFields ) {
        boolean isEnabled = true;

        for( EditText field : requiredFields ) {
            if( TextUtils.isEmpty( field.getText() ) ) isEnabled = false;
        }
        confirmButton.setEnabled( isEnabled );
    }

    /**
     * @description Tells the application what to when the confirm button is clicked. This should
     * only be bound to the confirm button on the activity_new_food.xml layout.
     * This will take all the data filled out a build a food object from that.
     * The intent will be returned with this food object to be either added to the list of food
     * or to replace an already existing food.
     * @param view View which calls this function.
     */
    public void onConfirmClick( View view ) {

        EditText foodName = (EditText) findViewById( R.id.new_found_name  );
        EditText foodCount = (EditText) findViewById(R.id.new_food_count);
        CalendarView foodExpDate = (CalendarView) findViewById(R.id.new_food_date);
        Spinner foodLocation = (Spinner) findViewById(R.id.new_food_location_choice);
        EditText foodDescription = (EditText) findViewById(R.id.new_food_description);
        EditText foodCost = (EditText) findViewById(R.id.new_food_cost);

        String name = foodName.getText().toString();
        Integer count = Integer.parseInt(foodCount.getText().toString());
        Date date = new Date( foodExpDate.getDate() );
        String location = foodLocation.getSelectedItem().toString();
        int locationPosition = foodLocation.getSelectedItemPosition();
        String description = foodDescription.getText().toString();
        int cost = (int)Double.parseDouble( foodCost.getText().toString() );

        Food newFood;

        if( !isEditing ) {
            newFood = new Food(name, description, count, date, location, locationPosition, cost);
            setResult( MainActivity.SUCCESS , intent );
        }
        else {
            alreadyStoredFood.setCount( count );
            alreadyStoredFood.setBestBeforeDate( date );
            alreadyStoredFood.setLocation( location );
            alreadyStoredFood.setLocationPosition( locationPosition );
            alreadyStoredFood.setDescription( description );
            alreadyStoredFood.setCost( cost );

            newFood = alreadyStoredFood;
            setResult( MainActivity.SAVED , intent );

        }
        intent.putExtra( String.valueOf(R.string.new_food), newFood);
        finish();
    }

    /**
     * @description Should be called by the cancel button on the activity_new_food.xml layout.
     * Will close the activity sending an empty intent back.
     * @param view The view that calls this method.
     */
    public void onCancelClick( View view ) {
        setResult( MainActivity.CANCEL );
        finish();
    }
}