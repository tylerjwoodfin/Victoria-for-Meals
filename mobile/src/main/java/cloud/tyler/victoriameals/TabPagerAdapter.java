package cloud.tyler.victoriameals;

/**
 * Created by Tyler on 7/28/2017.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabPagerAdapter extends FragmentPagerAdapter {

    int tabCount;

    public TabPagerAdapter(FragmentManager fm, int numberOfTabs) {
        super(fm);
        this.tabCount = numberOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                TabMeals tab1 = new TabMeals();
                return tab1;
            case 1:
                TabIngredients tab2 = new TabIngredients();
                return tab2;
            case 2:
                TabStores tab3 = new TabStores();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}