package com.albiorix.veek.keepsolidchecklist;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.albiorix.veek.keepsolidchecklist.service.NotificationService;
import com.albiorix.veek.keepsolidchecklist.util.ElementListAdapter;
import com.albiorix.veek.keepsolidchecklist.util.ElementListModel;
import com.albiorix.veek.keepsolidchecklist.util.KeepSolidFragmentsManager;
import com.albiorix.veek.keepsolidchecklist.util.KeepSolidPreferenceManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class MainFragment extends Fragment {

    ArrayList<ElementListModel> tasks = new ArrayList<ElementListModel>();
    int del;
    ElementListAdapter adapter;
    ListView listView;
    ImageButton FAB;
    EditText date;
    EditText time;
    EditText description;

    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;

    LayoutInflater lainflater;

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy HH:mm");

    Calendar calendar = Calendar.getInstance();

    String login;
    String name;
    View rootView;
    View dialogView;

    Context context;

    KeepSolidFragmentsManager fragmentManager = KeepSolidFragmentsManager.getInstance();
    KeepSolidPreferenceManager preferenceManager = KeepSolidPreferenceManager.getInstance();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        lainflater = getActivity().getLayoutInflater();


    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        adapter = new ElementListAdapter(getActivity().getApplicationContext(), tasks);

        ImageButton FAB = (ImageButton) rootView.findViewById(R.id.fab_add);

        FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              createAddDialog();
            }
        });

        preferenceManager = KeepSolidPreferenceManager.getInstance();
        preferenceManager.init(getActivity().getApplicationContext(), "GlobalPreferences");
        login = preferenceManager.getString("LastLogin");

        preferenceManager.init(getActivity().getApplicationContext(), login);
        preferenceManager.init(getActivity().getApplicationContext(), "GlobalPreferences");
            login = preferenceManager.getString("LastLogin");

            preferenceManager.init(getActivity().getApplicationContext(), login);


            final ListView listView = (ListView) rootView.findViewById(R.id.listView);
            listView.setAdapter(adapter);


            preferenceManager.init(getActivity().getApplicationContext(), login);

            tasks.clear();
            for(int i = 0; i<preferenceManager.getInt("els"); ++i){
                tasks.add(new ElementListModel(
                        preferenceManager.getString("listview_" + i),
                        preferenceManager.getString("listview_date_" + i),
                        preferenceManager.getString("listview_desc_" + i)));
                adapter.notifyDataSetChanged();
            }


            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()

            {
                @Override
                public boolean onItemLongClick (AdapterView < ? > parent, View view,int position,
                long id){
                del = position;
                context = getActivity();
                String title = "Are you sure?";
                String message = "Delete this tasks?";
                String button1String = "Yes";
                String button2String = "No";


                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(title)
                        .setMessage(message)
                        .setCancelable(false)
                        .setPositiveButton(button1String,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                        tasks.remove(del);
                                        adapter.notifyDataSetChanged();
                                        Toast.makeText(context, "Task deleted", Toast.LENGTH_SHORT).show();
                                        preferenceManager.init(getActivity().getApplicationContext(), login);
                                        preferenceManager.putInt("els", adapter.getCount());
                                        for (int i = 0; i < adapter.getCount(); ++i) {
                                            ElementListModel ell = (ElementListModel) adapter.getItem(i);
                                            preferenceManager.putString("listview_" + i, ell.name);
                                            preferenceManager.putString("listview_date_" + i, ell.date);
                                            preferenceManager.putString("listview_desc_" + i, ell.desc);


                                        }
                                    }
                                })
                        .setNegativeButton(button2String,
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });

                AlertDialog alert = builder.create();
                alert.show();
                return true;
                }
            });






            return rootView;

        }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        if (id == R.id.action_clear){
            context = getActivity();
            String title = "Are you sure?";
            String message = "Delete all tasks?";
            String button1String = "Yes";
            String button2String = "No";


            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(title)
                    .setMessage(message)
                    .setCancelable(false)
                    .setPositiveButton(button1String,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    preferenceManager.init(getActivity().getApplicationContext(), "GlobalPreferences");
                                    login = preferenceManager.getString("LastLogin");
                                    preferenceManager.init(getActivity().getApplicationContext(), login);
                                    tasks.clear();
                                    preferenceManager.putInt("els", adapter.getCount());
                                    for (int i = 0; i < adapter.getCount(); ++i) {
                                        ElementListModel ell = (ElementListModel) adapter.getItem(i);
                                        preferenceManager.putString("listview_" + i, ell.name);
                                        preferenceManager.putString("listview_date_" + i, ell.date);
                                        preferenceManager.putString("listview_desc_" + i, ell.desc);

                                    }
                                    adapter.notifyDataSetChanged();
                                }
                            }

                    )
                    .

                            setNegativeButton(button2String,
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick (DialogInterface dialog,int which){
                                            dialog.cancel();
                                        }
                                    }

                            );

            AlertDialog alert = builder.create();
            alert.show();
        }

        if (id == R.id.action_logout){
            preferenceManager.init(getActivity().getApplicationContext(), "GlobalPreferences");
            preferenceManager.putState("Auto-Login", false);
            fragmentManager.init(getActivity());
            fragmentManager.setFragment(R.id.fr_lay2, new AuthorizationFragment(), true);
        }

        return super.onOptionsItemSelected(item);
    }


    public void createAddDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        dialogView = lainflater.inflate(R.layout.add_dialog, null);
        date = (EditText) dialogView.findViewById(R.id.date);
        time = (EditText) dialogView.findViewById(R.id.time);
        description = (EditText) dialogView.findViewById(R.id.taskDesc);
       // loadImageButton = (Button) dialogView.findViewById(R.id.loadImageButton);
       // imagePreview = (ImageView) dialogView.findViewById(R.id.imageAttached);
       // final Date currentDate = new Date();
       // final String currentDateText = currentDateFormat.format(currentDate);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });

        final EditText taskName = (EditText) dialogView.findViewById(R.id.taskName);
        builder.setView(dialogView)
                .setPositiveButton("Add task", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        setAlarm(taskName.getText().toString(), dateFormat.format(calendar.getTime()));
                        tasks.add(new ElementListModel(taskName.getText().toString(), dateFormat.format(calendar.getTime()), description.getText().toString()));
                        adapter.notifyDataSetChanged();
                        preferenceManager.init(getActivity().getApplicationContext(), "GlobalPreferences");
                        login = preferenceManager.getString("LastLogin");
                        preferenceManager.init(getActivity().getApplicationContext(), login);
                        preferenceManager.putInt("els", adapter.getCount());
                        for (int i = 0; i < adapter.getCount(); ++i) {
                            ElementListModel ell = (ElementListModel) adapter.getItem(i);
                            preferenceManager.putString("listview_" + i, ell.name);
                            preferenceManager.putString("listview_date_" + i, ell.date);
                            preferenceManager.putString("listview_desc_" + i, ell.desc);
                        }
                        Toast.makeText(getActivity(), "Task added", Toast.LENGTH_SHORT).show();


                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        builder.show();
    }



    public void showTimePickerDialog() {

        TimePickerDialog.OnTimeSetListener timeListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                time.setText(timeFormat.format(calendar.getTime()));
            }
        };
        timePickerDialog = new TimePickerDialog(getActivity(),
                timeListener, calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE), true);
        timePickerDialog.show();
    }

    public void showDatePickerDialog() {
        DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
                date.setText(dateFormat.format(calendar.getTime()));
            }
        };
        datePickerDialog = new DatePickerDialog(getActivity(),
                dateListener, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }


/*@Override
public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (resultCode == getActivity().RESULT_OK) {

        //imagePreview.setImageBitmap(null);

        //Uri return from external activity
        orgUri = data.getData();

        //path converted from Uri
        filePath = orgUri.toString();

        imagePreview.setImageURI(Uri.parse(filePath));

    }
}*/


    public void setAlarm(String nTitle, String nDate) {
        Date date_notif = null;
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy HH:mm");
        try {
            date_notif = simpleDateFormat.parse(nDate);

        } catch (Exception e) {
            e.printStackTrace();
        }
        calendar.setTimeInMillis(date_notif.getTime());

        Intent intent = new Intent(getActivity(),
                NotificationService.class);

        intent.putExtra("task_title", nTitle);

        PendingIntent pendingIntent = PendingIntent.getService(getActivity(), 0,
                intent, 0);

        AlarmManager manager = (AlarmManager) getActivity().getSystemService(
                Context.ALARM_SERVICE);

        manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

    }

}
