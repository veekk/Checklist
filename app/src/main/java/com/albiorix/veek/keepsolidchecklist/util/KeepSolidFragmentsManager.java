package com.albiorix.veek.keepsolidchecklist.util;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

/**
 * Created by veek on 02.06.15.
 */
public class KeepSolidFragmentsManager {
    
    private static KeepSolidFragmentsManager instance;

    private Activity activity;
    private FragmentManager fragmentManager;


    private KeepSolidFragmentsManager() {

    }



    public static KeepSolidFragmentsManager getInstance() {

        if (instance == null) {
            instance = new KeepSolidFragmentsManager();
        }

        return instance;
    }


    public void init(Activity activity){
        this.activity = activity;
        fragmentManager = activity.getFragmentManager();
    }


    public void setFragment(int containerViewId, Fragment fragment, boolean addToBackStack) {

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(containerViewId, fragment);

        if (addToBackStack) {
            fragmentTransaction.addToBackStack(null);
        }

        fragmentTransaction.commit();
    }


    public FragmentManager getFragmentManager() {
        return fragmentManager;
    }
}