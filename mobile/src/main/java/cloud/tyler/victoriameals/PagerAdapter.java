package cloud.tyler.victoriameals;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Tyler on 7/28/2017.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    TabMeals tabMeals;
    TabIngredients tabIngredients;
    TabStores tabStores;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;

        tabMeals = new TabMeals();
        tabIngredients = new TabIngredients();
        tabStores = new TabStores();
    }



    @Override
    public Fragment getItem(int position) {

        switch (position)
        {
            case 0:
                return tabMeals;
            case 1:
                return tabIngredients;
            case 2:
                return tabStores;
            default:
                return tabMeals;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
