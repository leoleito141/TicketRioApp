package com.ticketrioapp.ticketrioapp.adapters;

/**
 * Created by Juanma on 27/10/2015.
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import com.ticketrioapp.ticketrioapp.clases.R;



public class DeportesAdapter extends ArrayAdapter<String>{


    public DeportesAdapter(Context context, ArrayList<String> deportes) {
        super(context,0,deportes);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        String d = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_deportes, parent, false);
        }

        TextView nombreDeporte = (TextView) convertView.findViewById(R.id.nombreDeporteListDep);


        nombreDeporte.setText(d.toString());

        return convertView;
    }

}
