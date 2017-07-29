package cloud.tyler.victoriameals;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


public class TabMeals extends Fragment {
    private TextView homeText;

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
        View v = inflater.inflate(R.layout.nav_home, container,false);

        // Set the Text to try this out
        try
        {
            homeText = (TextView) v.findViewById(R.id.homeText);
            homeText.setText("I'm the meals tab...");
        }
        catch(Exception e)
        {
            Toast.makeText(getContext(), "There was a problem updating.", Toast.LENGTH_SHORT);
        }

        return v;
    }
}
