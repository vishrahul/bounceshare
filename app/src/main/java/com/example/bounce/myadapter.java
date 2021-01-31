package com.example.bounce;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class myadapter extends BaseAdapter {

    private List<lat_lang_class> viewlist;

    private LayoutInflater lf=null;
    // Context ctx=null;


    public myadapter(Activity activity, List<lat_lang_class>viewlist)
    {
        //ctx= activity.getApplicationContext();
        this.viewlist=viewlist;
        lf=LayoutInflater.from(activity);

    }



    @Override
    public int getCount() {
        return viewlist.size();
    }

    @Override
    public Object getItem(int position) {
        return viewlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Toast.makeText(ctx,"view created",Toast.LENGTH_LONG).show();

        if(convertView==null)
        {
            convertView = lf.inflate(R.layout.view_data,parent,false);
        }


        TextView lat=convertView.findViewById(R.id.lat);
        TextView lang=convertView.findViewById(R.id.lang);
        TextView time=convertView.findViewById(R.id.time);


        final lat_lang_class d=viewlist.get(position);
        lat.setText("Latitude : "+d.getLat());
        lang.setText("Longitude : "+d.getLang());
        time.setText("Time : "+d.getTime());



        return convertView;

    }


}