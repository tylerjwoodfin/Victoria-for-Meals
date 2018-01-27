package cloud.tyler.victoriameals;

import android.app.Service;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;


public class TabIngredients extends Fragment
{

    private DatabaseHandler db;
    private ListView ingredientsList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_tab_ingredients, container,false);

        final ListView ingredientsList = (ListView) v.findViewById(R.id.ingredientsList);
        //Ingredients List Listener
        ingredientsList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int i, long arg)
            {

                //Show Modify Ingredient Alert
                ingredientAlert("Modify Ingredient",
                        db.getAllIngredients().get(i).getName(), db.getAllIngredients().get(i).getFrequency(), i);

            }
        });

        //Database Handler
        db = new DatabaseHandler(getContext());

        //Floating Action button
        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Add Ingredient Alert
                ingredientAlert();
            }
        });

        return v;
    }

    //Display Ingredients Add Dialog
    public void ingredientAlert()
    {
        ingredientAlert("Add Ingredient", "", 0, -1);
    }

    /*Display Ingredients Add Dialog
    @param frequency: For modifying, 0
    @param ingredientIndex: The index of the ingredient to modify in the database. If adding, set to -1.
     */
    public void ingredientAlert(String alertTitle, final String ingredientName, int frequency, final int ingredientToModifyIndex)
    {
        //Build Dialog
        final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle(alertTitle);

        //overall layout
        GridLayout layout = new GridLayout(getContext());
        layout.setColumnCount(1);
        layout.setRowCount(2);

        GridLayout everyLayout = new GridLayout(getContext());
        everyLayout.setColumnCount(4);
        everyLayout.setRowCount(1);

        // Create EditText for ingredient name
        final EditText ingredientNameInput = new EditText(getActivity());

        ingredientNameInput.setWidth(getView().getWidth() - 20);
        layout.addView(ingredientNameInput, 0);

        //Frequency Checkbox
        final CheckBox frequencyButton = new CheckBox(getContext());
        everyLayout.addView(frequencyButton, 0);

        //Frequency Text (every)
        final TextView frequencyText = new TextView(getContext());
        frequencyText.setText(getString(R.string.string_ingredient_frequency_every));
        everyLayout.addView(frequencyText, 1);

        //Frequency Input
        int frequencyInputWidthDbl = (int)(getView().getWidth() * .1);
        final EditText frequencyInput = new EditText(getActivity());
        frequencyInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        frequencyInput.setWidth(frequencyInputWidthDbl);
        everyLayout.addView(frequencyInput, 2);

        //Frequency Text (days)
        final TextView frequencyTextDays = new TextView(getContext());
        frequencyTextDays.setText(getString(R.string.string_ingredient_frequency_days));
        everyLayout.addView(frequencyTextDays, 3);

        // Add "I need this Every x Days"
        layout.addView(everyLayout, 1);

        //Add Elements Above to Alert
        alert.setView(layout);

        //Populate Name
        ingredientNameInput.setText(ingredientName);

        //Populate Frequency
        if(frequency > 0)
        {
            frequencyButton.setChecked(true);
            frequencyInput.setText(Integer.toString(frequency));
        }

        // Manage keyboard
        final InputMethodManager imm =
                (InputMethodManager)getContext().getSystemService(Service.INPUT_METHOD_SERVICE);

        // Initialize OK Button
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int whichButton)
            {
                // do nothing, this is handled below so I can fine-tune dismissing
                // in case duplicate ingredient added. Otherwise this is a good place
                // to handle things.
            }
        });

        // If Modifying ingredient, add delete button
        if(ingredientToModifyIndex != -1)
        {
            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int whichButton) {

                    alert.setNeutralButton("Delete", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {

                            //Confirm Before Delete
                            AlertDialog.Builder alertDelete = new AlertDialog.Builder(getActivity());
                            alertDelete.setTitle(ingredientName);
                            alertDelete.setMessage("Are you sure you want to delete this ingredient?");

                            //Delete Confirmed
                            alertDelete.setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {
                                    // hide keyboard
                                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                                    db.deleteIngredient(db.getAllIngredients().get(ingredientToModifyIndex));
                                    updateIngredients();
                                }
                            });

                            //Delete Cancelled
                            alertDelete.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {
                                    // hide keyboard
                                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                                }
                            });

                            alertDelete.show();
                        }
                    });

                }
            });
        }

        frequencyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(frequencyButton.isChecked())
                {
                    frequencyInput.requestFocus();  //focus on frequency field when checked
                }
                else
                {
                    frequencyInput.setText(""); //clear frequency when unchecked
                }
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

            public void onClick(DialogInterface dialog, int whichButton) {
                // hide keyboard
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            }
        });

        // Close keyboard after window closed

        // Show alert
        final AlertDialog dialog = alert.create();
        dialog.show();

        // Alert OK Button Listeners
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(ingredientToModifyIndex == -1)   // Add Ingredient
                {
                    // Grab the EditText's input, replace quotes for safety
                    String ingredientToAdd = ingredientNameInput.getText()
                            .toString().replaceAll("\"", "");

                    boolean isIngredientAdded;

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

                    // hide keyboard
                    imm.toggleSoftInput(InputMethodManager.RESULT_HIDDEN, 0);

                    if(!isIngredientAdded)
                    {
                        alert("Heads up!", "This ingredient already exists.");

                        // hide keyboard after this dialog and add window both dismissed
                        Window w = getActivity().getWindow();
                        w.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                        imm.toggleSoftInput(InputMethodManager.RESULT_HIDDEN, 0);
                    }
                    else
                    {
                        // dismiss dialog and hide keyboard
                        dialog.dismiss();
                    }

                    updateIngredients();
                }
                else    //Modifying Ingredient, not adding
                {
                    // Replacement Ingredient Name, remove quotes for safety
                    String replacementIngredientName = ingredientNameInput.getText()
                            .toString().replaceAll("\"", "");

                    //Replacement Ingredient Frequency
                    int replacementIngredientFrequency = 0;
                    if(frequencyInput.getText().toString().length() > 0)
                    {
                        replacementIngredientFrequency = Integer.parseInt(frequencyInput.getText().toString());
                    }

                    Ingredient replacementIngredient =
                            new Ingredient(replacementIngredientName, replacementIngredientFrequency);

                    // hide keyboard
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                    db.updateIngredient(db.getAllIngredients().get(ingredientToModifyIndex).getName(),
                            replacementIngredient);

                    dialog.dismiss();
                    updateIngredients();
                }
            }
        });

        //Manage Keyboard
        Window w = getActivity().getWindow();
        w.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        // Show keyboard
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        ingredientNameInput.requestFocus();

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
}