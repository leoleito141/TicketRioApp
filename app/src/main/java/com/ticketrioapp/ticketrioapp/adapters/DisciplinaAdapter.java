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
 * Created by Juanma on 28/10/2015.
 */
public class DisciplinaAdapter extends ArrayAdapter<String> {


    public DisciplinaAdapter(Context context, ArrayList<String> disciplinas) {
        super(context,0,disciplinas);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        String d = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_disciplinas, parent, false);
        }

        TextView nombreDisciplina = (TextView) convertView.findViewById(R.id.nombreDisciplina);


        nombreDisciplina.setText(d.toString());

        return convertView;
    }

}