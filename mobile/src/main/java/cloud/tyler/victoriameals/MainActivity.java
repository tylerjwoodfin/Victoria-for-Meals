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

public class MainActivity extends AppCompatActivity
{
    private FrameLayout frame;
    private Fragment f;
    private NavHome navHome;
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
                    manager.beginTransaction().replace(R.id.content, navHome).commit();
                    return true;
                case R.id.title_planner:
                    manager.beginTransaction().replace(R.id.content, new NavPlan().newInstance()).commit();
                    return true;
                case R.id.title_glance:
                    manager.beginTransaction().replace(R.id.content, navGlance).commit();
                    return true;
                case R.id.title_list:
                    manager.beginTransaction().replace(R.id.content, navList).commit();
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize fragments
        navHome = new NavHome().newInstance();
        navGlance = new NavGlance().newInstance();
        navList = new NavList().newInstance();


        FragmentManager manager = getSupportFragmentManager();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //initialize Home tab
        manager.beginTransaction().replace(R.id.content, navHome).commit();
    }

}