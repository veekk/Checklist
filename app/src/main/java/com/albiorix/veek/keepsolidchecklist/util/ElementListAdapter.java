package com.albiorix.veek.keepsolidchecklist.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;

import com.albiorix.veek.keepsolidchecklist.R;

import java.io.File;
import java.util.ArrayList;

public class ElementListAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<ElementListModel> objects;


    public ElementListAdapter(Context context, ArrayList<ElementListModel> tasks){
        ctx = context;
        objects = tasks;
        lInflater = (LayoutInflater) ctx
                 .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    //кол-во эл-тов
    @Override
    public int getCount(){
        return objects.size();
    }

    //элемент по позиции
    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    //id по позиции
    @Override
    public long getItemId(int position) {
        return position;
    }

    //пункт списка
    @Override

    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.element_list, parent, false);
        }



        ElementListModel em = getElementListModel(position);

        //Uri uriFromPath;
        //uriFromPath = Uri.parse(em.bmp_path);

        ((CheckBox) view.findViewById(R.id.cbTask)).setText(em.name);
        ((TextView) view.findViewById(R.id.tvTaskDate)).setText(em.date);
        ((TextView) view.findViewById(R.id.tvTaskDesc)).setText(em.desc);
        //((ImageView) view.findViewById(R.id.imgView)).setImageURI(uriFromPath);;

        return  view;
    }

    ElementListModel getElementListModel(int position){
        return ((ElementListModel) getItem(position));
    }
    
}
