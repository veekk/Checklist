package com.albiorix.veek.keepsolidchecklist;


import android.os.Bundle;
import android.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.albiorix.veek.keepsolidchecklist.util.KeepSolidFragmentsManager;
import com.albiorix.veek.keepsolidchecklist.util.KeepSolidPreferenceManager;


public class RegistrationFragment extends Fragment {

    Button buttonReg;
    TextView etLogReg;
    TextView etMail;
    TextView etPassReg;
    TextView etRePass;

    KeepSolidFragmentsManager fragmentManager = KeepSolidFragmentsManager.getInstance();
    KeepSolidPreferenceManager preferenceManager = KeepSolidPreferenceManager.getInstance();
 //   Fragment regFragrment = new RegistrationFragment();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_registration, container, false);


        buttonReg = (Button) rootView.findViewById(R.id.button8);
        etLogReg = (EditText) rootView.findViewById(R.id.textLogReg);
        etMail = (EditText) rootView.findViewById(R.id.textMail);
        etPassReg = (EditText) rootView.findViewById(R.id.textPassReg);
        etRePass = (EditText) rootView.findViewById(R.id.textRePass);

        View.OnClickListener oclbuttonReg = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etLogReg.getText().toString().equals("") || etLogReg.getText().toString().equals(" ")) {
                    Toast.makeText(getActivity(), "Enter your login", Toast.LENGTH_SHORT).show();
                } else if (etMail.getText().toString().equals("") || etMail.getText().toString().equals(" ")) {
                    Toast.makeText(getActivity(), "Enter your mail", Toast.LENGTH_SHORT).show();
                } else if (etPassReg.getText().toString().equals("") || etPassReg.getText().toString().equals(" ")) {
                    Toast.makeText(getActivity(), "Enter your password", Toast.LENGTH_SHORT).show();
                } else {
                    if (etRePass.getText().toString().equals("") || etRePass.getText().toString().equals(" ")) {
                        Toast.makeText(getActivity(), "Repeat your password", Toast.LENGTH_SHORT).show();
                    } else {
                        if (!etPassReg.getText().toString().equals(etRePass.getText().toString())) {
                            Toast.makeText(getActivity(), "Password and RePassword not equals", Toast.LENGTH_SHORT).show();
                        } else {
                            preferenceManager.init(getActivity().getApplicationContext(), etLogReg.getText().toString());
                            if (!preferenceManager.getString("Password").equals("")) {
                                Toast.makeText(getActivity(), "User already exist", Toast.LENGTH_SHORT).show();
                            } else {
                                preferenceManager.putString("Password", etPassReg.getText().toString());
                                preferenceManager.init(getActivity().getApplicationContext(), "GlobalPreferences");
                                preferenceManager.putString("LastLogin", etLogReg.getText().toString());
                                fragmentManager.init(getActivity());
                                fragmentManager.setFragment(R.id.fr_lay2, new MainFragment(), true);
                            }

                        }
                    }
                }
            }
        };

        buttonReg.setOnClickListener(oclbuttonReg);





        return rootView;
    }



    @Override
    public void onResume() {
        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    fragmentManager.init(getActivity());
                    fragmentManager.setFragment(R.id.fr_lay2, new AuthorizationFragment(), true);
                    return true;
                }
                return false;
            }
        });
    }

}
