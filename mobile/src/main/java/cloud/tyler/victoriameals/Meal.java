package cloud.tyler.victoriameals;

import java.util.List;

/**
 * Created by Tyler on 1/16/2018.
 */

public class Meal
{
    //private variables here
    private String _name;       //unique identifier
    private int _frequency;     //frequency in days
    private int _mealType;      // 0, 1, 2 for breakfast/lunch/dinner
    private List<Ingredient> _ingredients;  //ingredients used in the meal

    //Empty Constructor
    public Meal()
    {

    }

    //Full Constructor
    public Meal(String name, int frequency)
    {
        // capitalize first letter
        this._name = name.substring(0, 1).toUpperCase()
                + name.substring(1);
    }

    //Getters, Setters

    public void setName(String name)
    {
        this._name = name;
    }

    public void setFrequency(int frequency)
    {
        this._frequency = frequency;
    }

    public void setIngredients(List<Ingredient> ingredients)
    {
        this._ingredients = ingredients;
    }

    public void setMealType(int mealType)
    {
        this._mealType = mealType;
    }

    public String getName()
    {
        return this._name;
    }

    public int getFrequency()
    {
        return this._frequency;
    }

    public List<Ingredient> getIngredients()
    {
        return _ingredients;
    }

    public int getMealType()
    {
        return this._mealType;
    }
}
