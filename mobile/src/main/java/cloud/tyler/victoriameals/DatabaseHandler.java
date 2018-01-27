package cloud.tyler.victoriameals;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tyler on 1/16/2018.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "ingredientsManager";

    // Ingredients table name
    private static final String TABLE_INGREDIENT = "ingredients";

    // Ingredients Table Columns names
    private static final String KEY_NAME = "name";
    private static final String KEY_FREQUENCY = "frequency";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_INGREDIENT_TABLE = "CREATE TABLE " + TABLE_INGREDIENT + "("
                + KEY_NAME + " TEXT PRIMARY KEY," + KEY_FREQUENCY + " TEXT"
                + ")";
        db.execSQL(CREATE_INGREDIENT_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INGREDIENT);

        // Create tables again
        onCreate(db);
    }

    // Add a single ingredient
    public boolean addIngredient(Ingredient ingredient) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, ingredient.getName()); // Name
        values.put(KEY_FREQUENCY, ingredient.getFrequency()); // Frequency

        // Inserting Row
        try
        {
            db.insertOrThrow(TABLE_INGREDIENT, null, values);
        }
        catch(SQLException e)
        {
            Log.e("Unique Constraint", "Returning false now...");
            return false;
        }

        db.close(); // Closing database connection
        return true;
    }

    // Get a single ingredient
    public Ingredient getIngredient(String name)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_INGREDIENT, new String[] { KEY_NAME,
                        KEY_FREQUENCY }, KEY_NAME + "=?",
                new String[] { String.valueOf(name) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Ingredient ingredient = new Ingredient(cursor.getString(0),
                Integer.parseInt(cursor.getString(1)));

        // return ingredient
        return ingredient;
    }

    // Get all ingredients
    public List<Ingredient> getAllIngredients() {
        List<Ingredient> ingredientList = new ArrayList<Ingredient>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_INGREDIENT;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Ingredient ingredient = new Ingredient();
                ingredient.setName(cursor.getString(0));
                ingredient.setFrequency(cursor.getInt(1));

                // Adding ingredient to list
                ingredientList.add(ingredient);
            } while (cursor.moveToNext());
        }

        // return ingredient list
        return ingredientList;
    }

    // Get Ingredient Count
    public int getIngredientCount()
    {
        String countQuery = "SELECT  * FROM " + TABLE_INGREDIENT;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        //cursor.close();

        // return count
        return cursor.getCount();
    }

    // Update a single ingredient
    public void updateIngredient(String originalIngredientName, Ingredient ingredient) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, ingredient.getName());
        values.put(KEY_FREQUENCY, ingredient.getFrequency());

        // updating row
        try
        {
            db.update(TABLE_INGREDIENT, values, KEY_NAME + " = ?",
                    new String[] { String.valueOf(originalIngredientName) });
        }
        catch(SQLException e)
        {
            Log.e("Update Error", "The database was not updated.");
        }
    }

    // Delete a single ingredient
    public void deleteIngredient(Ingredient ingredient) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_INGREDIENT, KEY_NAME + " = ?",
                new String[] { String.valueOf(ingredient.getName()) });
        db.close();
    }
}
