package eu.lucazanini.ariadne.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import eu.lucazanini.ariadne.R;
import eu.lucazanini.ariadne.fragment.MapFragment;
import eu.lucazanini.ariadne.fragment.PathFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Fragment fragment;
        if (savedInstanceState == null) {
            Intent intent = getIntent();
            String action = intent.getAction();
            FragmentManager fragmentManager = getSupportFragmentManager();
            if (Intent.ACTION_SEND.equals(action)) {
                fragment = fragmentManager.findFragmentByTag(MapFragment.TAG);
                if (fragment == null) {
                    fragment = new MapFragment();
                }
            } else {
                fragment = fragmentManager.findFragmentByTag(PathFragment.TAG);
                if (fragment == null) {
                    fragment = new PathFragment();
                }
            }
            fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
        }
    }
}
