package com.albiorix.veek.keepsolidchecklist;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.albiorix.veek.keepsolidchecklist.util.KeepSolidFragmentsManager;
import com.albiorix.veek.keepsolidchecklist.util.KeepSolidPreferenceManager;


public class AuthorizationFragment extends Fragment {

    Button buttonSignUp;
    Button buttonLogin;

    EditText etLog;
    EditText etPass;

    CheckBox chAutoLog;


    KeepSolidFragmentsManager fragmentManager = KeepSolidFragmentsManager.getInstance();
    KeepSolidPreferenceManager preferenceManager = KeepSolidPreferenceManager.getInstance();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);


        buttonSignUp = (Button) rootView.findViewById(R.id.button6);
        buttonLogin = (Button) rootView.findViewById(R.id.button7);

        etLog = (EditText) rootView.findViewById(R.id.textLog);
        etPass = (EditText) rootView.findViewById(R.id.textPass);

        chAutoLog = (CheckBox) rootView.findViewById(R.id.chAutoLog);


        preferenceManager.init(getActivity().getApplicationContext(), "GlobalPreferences");
        chAutoLog.setChecked(preferenceManager.getState("Auto-Login"));

        if (chAutoLog.isChecked()) {
            fragmentManager.init(getActivity());
            fragmentManager.setFragment(R.id.fr_lay2, new MainFragment(), true);

        }



        View.OnClickListener oclchAutoLog = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferenceManager.init(getActivity().getApplicationContext(), "GlobalPreferences");
                preferenceManager.putState("Auto-Login", chAutoLog.isChecked());
            }
        };

        View.OnClickListener oclbuttonSignUp = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.init(getActivity());
                fragmentManager.setFragment(R.id.fr_lay2, new RegistrationFragment(), true);

            }
        };

        View.OnClickListener oclbuttonLogin = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etLog.getText().toString().equals("") || etLog.getText().toString().equals(" ")){
                    Toast.makeText(getActivity(), "Enter your login", Toast.LENGTH_SHORT).show();
                } else preferenceManager.init(getActivity().getApplicationContext(), etLog.getText().toString());
                if (etPass.getText().toString().equals("") || etPass.getText().toString().equals(" ")){
                    Toast.makeText(getActivity(), "Enter your password", Toast.LENGTH_SHORT).show();
                } else if (preferenceManager.getString("Password").equals(etPass.getText().toString())){
                    fragmentManager.init(getActivity());
                    fragmentManager.setFragment(R.id.fr_lay2, new MainFragment(), true);
                    preferenceManager.init(getActivity().getApplicationContext(), "GlobalPreferences");
                    preferenceManager.putState("Auto-Login", chAutoLog.isChecked());
                    preferenceManager.putString("LastLogin", etLog.getText().toString());
                } else Toast.makeText(getActivity(), "Login or password is wrong", Toast.LENGTH_SHORT).show();
            }
        };

        chAutoLog.setOnClickListener(oclchAutoLog);
        buttonSignUp.setOnClickListener(oclbuttonSignUp);
        buttonLogin.setOnClickListener(oclbuttonLogin);

        return rootView;
    }




}
