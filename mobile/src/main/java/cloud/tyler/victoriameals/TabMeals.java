package cloud.tyler.victoriameals;

import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.GridLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class TabMeals extends Fragment {
    private DatabaseHandler db;
    private ExpandableListView list;
    ExpandableListAdapter listAdapter;
    List<String> mealTypeList;
    HashMap<String, List<String>> listDataChild;


    public static NavHome newInstance() {
        NavHome fragment = new NavHome();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_tab_meals, container,false);

        //Database Handler
        db = new DatabaseHandler(getContext());

        list = (ExpandableListView) v.findViewById(R.id.mealListView);

        mealTypeList = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Add Headers
        mealTypeList.add("Breakfast");
        mealTypeList.add("Lunch");
        mealTypeList.add("Dinner");

        // Add child data
        List<String> breakfastList = new ArrayList<String>();
        List<String> lunchList = new ArrayList<String>();
        List<String> dinnerList = new ArrayList<String>();

        // Add Breakfast
        breakfastList.add("Cereal");
        breakfastList.add("Toast");
        breakfastList.add("French Toast");

        // Add Lunch
        lunchList.add("Cheeseburger");
        lunchList.add("Hot Dogs");
        lunchList.add("Oatmeal");

        // Add to headers
        listDataChild.put(mealTypeList.get(0), breakfastList); // Header, Child data
        listDataChild.put(mealTypeList.get(1), lunchList);
        listDataChild.put(mealTypeList.get(2), dinnerList);


        // Put into ExpandableListView
        listAdapter = new ExpandableListAdapter(getContext(), mealTypeList, listDataChild);

        // setting list adapter
        list.setAdapter(listAdapter);

        //Floating Action button
        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Add Meal Alert
                mealAlert("Add Meal", "", -1, 0, -1);
            }
        });

        return v;
    }

    public void mealAlert(
            String alertTitle, final String mealName, int frequency, int mealType, final int mealToModifyIndex)
    {
        //Build Dialog
        final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle(alertTitle);

        //overall layout
        GridLayout layout = new GridLayout(getContext());
        layout.setColumnCount(1);
        layout.setRowCount(4);

        // "Every x Days" layout
        GridLayout everyLayout = new GridLayout(getContext());
        everyLayout.setColumnCount(4);
        everyLayout.setRowCount(1);

        // Meal Type spinner layout
        GridLayout mealTypeLayout = new GridLayout(getContext());
        mealTypeLayout.setColumnCount(2);
        mealTypeLayout.setRowCount(1);

        // Ingredient Spinner Layout
        GridLayout ingredientTypeLayout = new GridLayout(getContext());
        ingredientTypeLayout.setColumnCount(2);
        ingredientTypeLayout.setRowCount(1);

        // Create EditText for ingredient name
        final EditText mealNameInput = new EditText(getActivity());

        mealNameInput.setWidth(getView().getWidth() - 20);
        layout.addView(mealNameInput, 0);

        //Frequency Checkbox
        final CheckBox frequencyButton = new CheckBox(getContext());
        everyLayout.addView(frequencyButton, 0);

        //Frequency Text (every)
        final TextView frequencyText = new TextView(getContext());
        frequencyText.setText(getString(R.string.string_meal_frequency_every));
        everyLayout.addView(frequencyText, 1);

        //Frequency Input
        int frequencyInputWidthDbl = (int)(getView().getWidth() * .1);
        final EditText frequencyInput = new EditText(getActivity());
        frequencyInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        frequencyInput.setWidth(frequencyInputWidthDbl);
        everyLayout.addView(frequencyInput, 2);

        //Frequency Text (days)
        final TextView frequencyTextDays = new TextView(getContext());
        frequencyTextDays.setText(getString(R.string.string_meal_frequency_days));
        everyLayout.addView(frequencyTextDays, 3);

        // Add "I need this Every x Days"
        layout.addView(everyLayout, 1);

        // Meal Type Spinner Layout
        final TextView mealTypeTextView = new TextView(getContext());
        mealTypeTextView.setText(R.string.string_meal_type);
        mealTypeTextView.setPadding(0, 0, 60, 0);
        mealTypeLayout.addView(mealTypeTextView, 0);

        final Spinner mealTypeSpinner = new Spinner(getContext());
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (getContext(), android.R.layout.simple_spinner_dropdown_item, mealTypeList);
        mealTypeSpinner.setAdapter(spinnerArrayAdapter);
        mealTypeLayout.addView(mealTypeSpinner, 1);
        mealTypeLayout.setPadding(25, 0, 0, 0);

        // Ingredient Spinner Layout
        final TextView ingredientTextView = new TextView(getContext());
        ingredientTextView.setText(R.string.string_meal_ingredients);
        ingredientTypeLayout.addView(ingredientTextView, 0);

        Spinner ingredientSelectionSpinner = new Spinner(getContext());

        ArrayList<StateVO> listVOs = new ArrayList<>();

        Ingredient[] ingredientList = db.getAllIngredients().toArray(new Ingredient[0]);
        for (int i = 0; i < db.getIngredientCount()+1; i++)
        {
            StateVO stateVO = new StateVO();

            if(i==0)
            {
                stateVO.setTitle("Tap to Select");
            }
            else
            {
                stateVO.setTitle(ingredientList[i-1].getName());
            }

            stateVO.setSelected(false);
            listVOs.add(stateVO);
        }
        final CheckboxSpinnerAdapter myAdapter = new CheckboxSpinnerAdapter(getContext(), 0,
                listVOs);
        ingredientSelectionSpinner.setAdapter(myAdapter);
        ingredientTypeLayout.addView(ingredientSelectionSpinner, 1);
        ingredientTypeLayout.setPadding(25, 0, 0, 0);

        layout.addView(mealTypeLayout, 2);
        layout.addView(ingredientTypeLayout, 3);

        // Add Elements Above to Alert
        alert.setView(layout);

        //Populate Name
        mealNameInput.setText(mealName);

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
                // in case duplicate meal added. Otherwise this is a good place
                // to handle things.
            }
        });

        // If Modifying ingredient, add delete button
        if(mealToModifyIndex != -1)
        {
            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int whichButton) {

                    alert.setNeutralButton("Delete", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {

                            //Confirm Before Delete
                            AlertDialog.Builder alertDelete = new AlertDialog.Builder(getActivity());
                            alertDelete.setTitle(mealName);
                            alertDelete.setMessage("Are you sure you want to delete this ingredient?");

                            //Delete Confirmed
                            alertDelete.setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {
                                    // hide keyboard
                                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                                    //db.deleteIngredient(db.getAllIngredients().get(mealToModifyIndex));
                                    updateMeals();
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

                // temporary
                //toast(Integer.toString(myAdapter.getSelection().size()));
                toast(Integer.toString(myAdapter.getCount()));
                for(int i=0; i<myAdapter.getSelection().size(); i++)
                {
                    toast(myAdapter.getSelection().get(i).getTitle());
                }

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
                if(mealToModifyIndex == -1)   // Add Ingredient
                {
                    // Grab the EditText's input, replace quotes for safety
                    String ingredientToAdd = mealNameInput.getText()
                            .toString().replaceAll("\"", "");

                    boolean isMealAdded = false;

                    if(frequencyButton.isChecked() && frequencyInput.length() > 0)
                    {
//                        isIngredientAdded = db.addIngredient(new Ingredient(ingredientToAdd,
//                                Integer.parseInt(frequencyInput.getText().toString())));

                        //addIngredient(ingredientToAdd + "," + frequencyInput.getText().toString());
                    }
                    else
                    {
                        //isIngredientAdded = db.addIngredient(new Ingredient(ingredientToAdd, 0));
                    }

                    // hide keyboard
                    imm.toggleSoftInput(InputMethodManager.RESULT_HIDDEN, 0);

                    if(!isMealAdded)
                    {
                        alert("Heads up!", "This meal already exists.");

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

                    updateMeals();
                }
                else    //Modifying Ingredient, not adding
                {
                    // Replacement Ingredient Name, remove quotes for safety
                    String replacementIngredientName = mealNameInput.getText()
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

//                    db.updateIngredient(db.getAllIngredients().get(ingredientToModifyIndex).getName(),
//                            replacementIngredient);

                    dialog.dismiss();
                    updateMeals();
                }
            }
        });

        // Manage Keyboard
        Window w = getActivity().getWindow();
        w.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        // Show keyboard
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        mealNameInput.requestFocus();

    }

    // Update Meals
    public void updateMeals()
    {
        toast("Updating Meals");
    }

    // Show a basic "OK" Alert
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

    // Easily make toasts
    public void toast(String s)
    {
        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
    }
}
