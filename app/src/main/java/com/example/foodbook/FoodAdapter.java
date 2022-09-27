package com.example.foodbook;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * @class FoodAdapter
 * @extends RecylerView.Adapter
 * @description  This adapter allows for a RecyclerView to present data using the food_item.xml sheet.
 * This class will be responsible for translating the data onto the view and sending the view to
 * whatever class instantiates it. This class will also hold the function for a long click popup menu
 * item selections.
 */
public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder> {

    /**
     * @class ViewHolder
     * @extends RecylerView.ViewHolder
     * @description Defines the views used in the recylerView and holds them for later use.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView nameTextView, countTextView, dateTextView, descriptionTextView, locationView, costView;
        public LinearLayout mainLayout;

        /**
         * @description Constructor. Binds all the view variables.
         * @param itemView The view which is being transled to the RecylerView
         */
        public ViewHolder( View itemView ) {
            super(itemView);

            mainLayout = (LinearLayout) itemView.findViewById(R.id.food_item_each);
            nameTextView = (TextView) itemView.findViewById(R.id.food_name);
            countTextView = (TextView) itemView.findViewById(R.id.food_count);
            dateTextView = (TextView) itemView.findViewById(R.id.food_date);
            descriptionTextView = (TextView) itemView.findViewById(R.id.food_description);
            locationView = (TextView) itemView.findViewById(R.id.food_location);
            costView = (TextView) itemView.findViewById(R.id.food_cost);
        }
    }

    /**
     * @description Overrides the onCreateViewHolder which will define what happens when the view
     * being held is created.
     * @param parent View that holds this View Holder
     * @param viewType integer represting which type of view is being used.
     * @return viewHolder: a instaitated ViewHolder object
     */
    @Override
    public FoodAdapter.ViewHolder onCreateViewHolder( @NonNull ViewGroup parent, int viewType ) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View foodView = inflater.inflate(R.layout.food_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(foodView);
        return viewHolder;
    }

    /**
     * @description Binds the viewHolder's view to data fields of the Food object.
     * This allows the view to show the correct data on view for the user.
     * When the binding happens a popup menu is also binded to the parent view which
     * allows a menu to apear for either editing or removal.
     * @param holder View holder of this class
     * @param position Position of the view on the RecylerView.
     */
    @Override
    public void onBindViewHolder( FoodAdapter.ViewHolder holder, int position) {
        Food food = mFoods.get(position);

        TextView nameView = holder.nameTextView;
        TextView countView = holder.countTextView;
        TextView dateView = holder.dateTextView;
        TextView descView = holder.descriptionTextView;
        TextView locationView = holder.locationView;
        TextView costView = holder.costView;

        nameView.setText(food.getName());
        countView.setText( food.getCount().toString() );
        dateView.setText( DateFormat.format("yyyy-MM-dd", food.getBestBeforeDate() ).toString() );
        descView.setText( food.getDescription().toString() );
        locationView.setText( food.getLocation().toString() );
        costView.setText( food.getCost().toString() );

        PopupMenu deleteMenu = new PopupMenu( holder.mainLayout.getContext(), holder.nameTextView );
        deleteMenu.inflate(R.menu.delete_menu);

        deleteMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch ( menuItem.getItemId() ) {
                    case R.id.delete:
                        mFoods.remove( holder.getAdapterPosition() );
                        editContext.refreshCostView();
                        notifyDataSetChanged();
                        break;
                    case R.id.edit:
                        editContext.editFood( holder.getAdapterPosition() );
                        break;
                    default:
                }
                return true;
            }
        });

        holder.mainLayout.setOnLongClickListener(view -> {
            deleteMenu.show();
            return true;
        });
    }

    /**
     * @description Gets the count of how many Food Objects are being viewed on the RecylerView.
     * @return The size of the data array being used to store Food objects.
     */
    @Override
    public int getItemCount() {
        return mFoods.size();
    }

    private List<Food> mFoods;
    private MainActivity editContext;

    /**
     * Constructor
     * @param foods List of Foods which will be used to fetch data and display on each binded view.
     * @param contextForEdit The context of the MainActivty so that a non-static method can be called
     *                       to edit one of the food items.
     *                       *NOTE*
     *                       This should later be changed out for a better
     *                       alternative as passing a scope through is prone to error.
     */
    public FoodAdapter( List<Food> foods, MainActivity contextForEdit ) {
        mFoods = foods;
        editContext = contextForEdit;
    }
}
