package com.albiorix.veek.keepsolidchecklist.util;


import android.graphics.Bitmap;
import android.net.Uri;

public class ElementListModel {

    public String name;
    public String date;
    public String desc;
    public Bitmap bmp_path;


    public ElementListModel(String _name, String _date, String _desc, Bitmap _bmp){
        name = _name;
        date = _date;
        desc = _desc;
        bmp_path = _bmp;

    }
}
