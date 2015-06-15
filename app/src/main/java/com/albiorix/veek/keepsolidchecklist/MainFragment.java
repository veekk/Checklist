package com.albiorix.veek.keepsolidchecklist;






import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.albiorix.veek.keepsolidchecklist.service.NotificationService;
import com.albiorix.veek.keepsolidchecklist.util.ElementListAdapter;
import com.albiorix.veek.keepsolidchecklist.util.ElementListModel;
import com.albiorix.veek.keepsolidchecklist.util.KeepSolidFragmentsManager;
import com.albiorix.veek.keepsolidchecklist.util.KeepSolidPreferenceManager;

import java.io.FileNotFoundException;
import java.io.InputStream;
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
    Button loadImageButton;
    ImageView imagePreview;

    BitmapFactory.Options options = new BitmapFactory.Options();

    Bitmap bmp;
    TextView tv_name;
    TextView tv_desc;

    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;

    LayoutInflater lainflater;

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy HH:mm");
    SimpleDateFormat currentDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    Calendar calendar = Calendar.getInstance();

    String login;
    String name;
    String filePath;
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
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;

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
                        preferenceManager.getString("listview_desc_" + i),
                        preferenceManager.getString("listview_img_" + i)));
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
                                            preferenceManager.putString("listview_img_" + i, bmp.toString());


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

        if (id == R.id.action_add) {
            context = getActivity();
            String title = "Add task";
            String message = "Task preferences";
            String button1String = "Add";
            String button2String = "Cancel";
            final EditText input = new EditText(context);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            input.setLayoutParams(lp);
            input.setSingleLine(true);
            builder.setTitle(title)
                    .setMessage(message)
                    .setView(input)
                    .setCancelable(false)
                    .setPositiveButton(button1String,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    name = input.getText().toString();
                                    showDatePickerDialog();
                                    Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
                                    photoPickerIntent.setType("image/*");
                                    startActivityForResult(photoPickerIntent, 1);
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
        }

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
                                        preferenceManager.putString("listview_img_" + i, ell.bmp_path);

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
        loadImageButton = (Button) dialogView.findViewById(R.id.loadImageButton);
        imagePreview = (ImageView) dialogView.findViewById(R.id.imageAttached);
        final Date currentDate = new Date();
        final String currentDateText = currentDateFormat.format(currentDate);
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
        loadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 1);
            }
        });

        final EditText taskName = (EditText) dialogView.findViewById(R.id.taskName);
        builder.setView(dialogView)
                .setPositiveButton("Add task", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        setAlarm(taskName.getText().toString(), dateFormat.format(calendar.getTime()));
                        tasks.add(new ElementListModel(taskName.getText().toString(), dateFormat.format(calendar.getTime()) , description.getText().toString(), filePath));
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
                            preferenceManager.putString("listview_img_" + i, ell.bmp_path);
                        }


                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        builder.show();
    }

    public void createInfoDialog(String inf_name, String inf_desc, Uri inf_uri) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        dialogView = lainflater.inflate(R.layout.info_dialog, null);
        tv_name = (TextView) dialogView.findViewById(R.id.tvTaskName);
        tv_desc = (TextView) dialogView.findViewById(R.id.tvTaskDesc);
        imagePreview = (ImageView) dialogView.findViewById(R.id.imageAttached);

        tv_name.setText(inf_name);
        tv_desc.setText(inf_desc);
       // imagePreview.setImageURI(inf_uri);
        builder.setView(dialogView)
                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
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


  @Override
  //  public void onActivityResult(int requestCode, int resultCode, Intent data)
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        {

       /* super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode)
        {
            case 1:
            {
                if (resultCode == getActivity().RESULT_OK)
                {
                    final Uri chosenImageUri = data.getData();
                    final Cursor cursor = getActivity().getContentResolver().query(chosenImageUri, null, null, null, null);
                    cursor.moveToFirst();
                    filePath = cursor.getString(0);
                    cursor.close();
                    uriri = chosenImageUri;
                    imagePreview.setImageURI(uriri);

                }
                break;
            }
        }*/
            super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

            switch (requestCode) {
                case 0:
                    if (resultCode == getActivity().RESULT_OK) {
                        try {
                            final Uri imageUri = imageReturnedIntent.getData();
                            final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                            final Cursor cursor = getActivity().getContentResolver().query(imageUri, null, null, null, null);
                            cursor.moveToFirst();
                            filePath = cursor.getString(0);
                            cursor.close();
                            bmp = selectedImage;
                            imagePreview.setImageBitmap(bmp);
                            imagePreview.setImageURI(imageUri);;
                            cursor.moveToFirst();
                            String filePath = cursor.getString();
                            cursor.close();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
            }

        }
    }




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
