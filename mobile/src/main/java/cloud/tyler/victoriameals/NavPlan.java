package cloud.tyler.victoriameals;

/**
 * Created by Tyler on 7/27/2017.
 */

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class NavPlan extends Fragment
{
    File dir = new File(Environment.getExternalStorageDirectory(), "/WeeklyShopping");
    ListView ingredientsList;
    DatabaseHandler db;

    public static NavPlan newInstance()
    {
        NavPlan fragment = new NavPlan();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.nav_plan, container,false);

        //View Pager
        final ViewPager viewPager = (ViewPager) v.findViewById(R.id.viewPager);
        TabLayout tabLayout = (TabLayout) v.findViewById(R.id.tabLayout);

        viewPager.setAdapter(new PagerAdapter
                (this.getChildFragmentManager(), 3));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        //Database Handler
        db = new DatabaseHandler(getContext());

        //Tab Listener
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                viewPager.setCurrentItem(tab.getPosition());
                updateIngredients();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //viewPager.setCurrentItem(tab.getPosition());
            }
        });

        return v;
    }

    //Easily make toasts
    public void toast(String s)
    {
        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
    }

    public void alert(String title, String message)
    {
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        String[] PERMISSIONS = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(PERMISSIONS, 112 );
                    }
                });
        alertDialog.show();
    }

    //Display Ingredients Add Dialog
    public void addIngredientAlert()
    {
        //Build Dialog
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle("Add Ingredient");

        //overall layout
        GridLayout layout = new GridLayout(getContext());
        layout.setColumnCount(1);
        layout.setRowCount(2);

        GridLayout everyLayout = new GridLayout(getContext());
        everyLayout.setColumnCount(4);
        everyLayout.setRowCount(1);

        // Create EditText for ingredient name
        final EditText input = new EditText(getActivity());
        input.setWidth(getView().getWidth() - 20);
        layout.addView(input, 0);

        //Frequency Checkbox
        final CheckBox frequencyButton = new CheckBox(getContext());
        everyLayout.addView(frequencyButton, 0);

        //Frequency Text (every)
        final TextView frequencyText = new TextView(getContext());
        frequencyText.setText("  I need this every");
        everyLayout.addView(frequencyText, 1);

        //Frequency Input
        int frequencyInputWidthDbl = (int)(getView().getWidth() * .1);
        final EditText frequencyInput = new EditText(getActivity());
        frequencyInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        frequencyInput.setWidth(frequencyInputWidthDbl);
        everyLayout.addView(frequencyInput, 2);

        //Frequency Text (days)
        final TextView frequencyTextDays = new TextView(getContext());
        frequencyTextDays.setText("days");
        everyLayout.addView(frequencyTextDays, 3);

        layout.addView(everyLayout, 1);

        //Set Alert View
        alert.setView(layout);

        // Make an "OK" button to save the name
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int whichButton) {

                // Grab the EditText's input
                String ingredientToAdd = input.getText().toString();
                ingredientToAdd.replaceAll("\"", "");   //remove quotes for safety

                boolean isIngredientAdded = false;

                if(frequencyButton.isChecked() && frequencyInput.length() > 0)
                {
                    isIngredientAdded = db.addIngredient(new Ingredient(ingredientToAdd,
                            Integer.parseInt(frequencyInput.getText().toString())));

                    //addIngredient(ingredientToAdd + "," + frequencyInput.getText().toString());
                }
                else
                {
                    isIngredientAdded = db.addIngredient(new Ingredient(ingredientToAdd, 0));
                }

                if(!isIngredientAdded)
                {
                    alert("Heads up!", "This ingredient already exists.");
                }

                updateIngredients();
            }
        });

        frequencyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frequencyInput.requestFocus();
            }
        });

        frequencyInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    frequencyButton.setChecked(true);

                }
            }});

        // Make a "Cancel" button
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int whichButton) {}
        });

        alert.show();

    }

    public void updateIngredients()
    {
        //get ingredients
        SimpleAdapter adapter =
                new SimpleAdapter
                        (getContext(), getAllIngredients(), android.R.layout.simple_list_item_2, new String[]{"name", "frequency"}, new int[]{android.R.id.text1, android.R.id.text2});
        ingredientsList = (ListView) getView().findViewById(R.id.ingredientsList);
        ingredientsList.setAdapter(adapter);
    }


    //Returns the ingredients from the database
    public ArrayList<HashMap<String,String>> getAllIngredients()
    {
        ArrayList<HashMap<String,String>> ingredients = new ArrayList<>();

        for(int i=0; i< db.getIngredientCount(); i++)
        {
            HashMap<String,String> hashMap = new HashMap<>();
            String name = db.getAllIngredients().get(i).getName();
            String frequency = Integer.toString(db.getAllIngredients().get(i).getFrequency());

            //toast("Getting all ingredients. Name: " + name + ", Frequency: " + frequency);
            hashMap.put("name", name);

            //If the user entered a frequency, display this as a subitem
            if(!frequency.matches("0"))
            {
                String days = "days";
                if(frequency.matches("1"))
                {
                    days = "day";
                }
                hashMap.put("frequency", "Every " + frequency + " " + days);
            }
            else
            {
                hashMap.put("frequency", "");
            }

            ingredients.add(hashMap);
        }

        return ingredients;
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        //get external storage
        dir.mkdir();

        //Check Permissions
        if(ContextCompat.checkSelfPermission(
                getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE )
                != PackageManager.PERMISSION_GRANTED)
        {
            alert("Heads up!", "You'll need to allow storage permissions to update the list.");
            return false;
        }
        else
        {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        // If request is cancelled, the result arrays are empty.
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            // Granted
        }
        else
        {
            // Denied
        }
        return;
    }
}