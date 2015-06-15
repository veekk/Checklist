package com.albiorix.veek.keepsolidchecklist;


import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import com.albiorix.veek.keepsolidchecklist.util.KeepSolidFragmentsManager;
import com.albiorix.veek.keepsolidchecklist.util.KeepSolidPreferenceManager;


public class MainActivity extends ActionBarActivity {

    KeepSolidFragmentsManager fragmentManager = KeepSolidFragmentsManager.getInstance();
    KeepSolidPreferenceManager preferenceManager = KeepSolidPreferenceManager.getInstance();

    AuthorizationFragment loginFragment = new AuthorizationFragment();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        fragmentManager.init(this);
        fragmentManager.setFragment(R.id.fr_lay2, loginFragment, true);

        preferenceManager.init(getApplicationContext(), "GlobalPreferences");
        preferenceManager.init(getApplicationContext(), preferenceManager.getString("LastLogin"));



    }

 /*   @Override
    public void onBackPressed() {

        int count = fragmentManager.getFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
        } else {
            fragmentManager.getFragmentManager().popBackStack();
        }

    }*/



}
