package cloud.tyler.victoriameals;

/**
 * Created by Tyler on 1/16/2018.
 */

public class Ingredient
{
    //private variables here
    private String _name;       //unique identifier
    private int _frequency;     //how often ingredient is needed

    //Empty Constructor
    public Ingredient()
    {

    }

    //Full Constructor
    public Ingredient(String name, int frequency)
    {
        // capitalize first letter
        this._name = name.substring(0, 1).toUpperCase()
                + name.substring(1);

        this._frequency = frequency;
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

    public String getName()
    {
        return this._name;
    }

    public int getFrequency()
    {
        return this._frequency;
    }
}
