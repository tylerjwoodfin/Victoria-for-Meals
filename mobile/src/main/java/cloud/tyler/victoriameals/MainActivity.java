package cloud.tyler.victoriameals;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
{
    private FrameLayout frame;
    private Fragment f;
    private NavHome navHome;
    private NavPlan navPlan;
    private NavGlance navGlance;
    private NavList navList;
    private TextView homeText;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager manager = getSupportFragmentManager();

            switch (item.getItemId()) {
                case R.id.title_home:
                    if(navHome.isAdded())
                        return true;
                    manager.beginTransaction().replace(R.id.content, navHome).addToBackStack(null).commit();
                    return true;
                case R.id.title_planner:
                    if(navPlan.isAdded())
                        return true;
                    manager.beginTransaction().replace(R.id.content, navPlan).addToBackStack(null).commit();
                    return true;
                case R.id.title_glance:
                    if(navGlance.isAdded())
                        return true;
                    manager.beginTransaction().replace(R.id.content, navGlance).addToBackStack(null).commit();
                    return true;
                case R.id.title_list:
                    if(navList.isAdded())
                        return true;
                    manager.beginTransaction().replace(R.id.content, navList).addToBackStack(null).commit();
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Toast.makeText(getApplicationContext(), "Creating", Toast.LENGTH_SHORT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize fragments
        navHome = new NavHome().newInstance();
        navPlan = new NavPlan().newInstance();
        navGlance = new NavGlance().newInstance();
        navList = new NavList().newInstance();


        FragmentManager manager = getSupportFragmentManager();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //initialize Home tab
        manager.beginTransaction().replace(R.id.content, navHome).commit();
    }

}