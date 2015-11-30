package com.ticketrioapp.ticketrioapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ticketrioapp.ticketrioapp.clases.R;

import java.util.ArrayList;

/**
 * Created by Juanma on 01/11/2015.
 */
public class RondaAdapter extends ArrayAdapter<Integer> {

    public RondaAdapter(Context context, ArrayList<Integer> rondas) {
        super(context,0,rondas);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        Integer r = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_rondas, parent, false);
        }

        TextView ronda = (TextView) convertView.findViewById(R.id.numRonda);


        ronda.setText(r.toString());

        return convertView;
    }

}
