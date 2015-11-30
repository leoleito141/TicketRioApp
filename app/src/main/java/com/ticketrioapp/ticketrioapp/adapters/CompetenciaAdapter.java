package com.ticketrioapp.ticketrioapp.adapters;

import com.ticketrioapp.ticketrioapp.wrapper.WrapperCompetencia;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.ArrayList;
import java.util.TimeZone;

import com.ticketrioapp.ticketrioapp.clases.R;



/**
 * Created by Juanma on 01/11/2015.
 */
public class CompetenciaAdapter extends ArrayAdapter<WrapperCompetencia>{


    public CompetenciaAdapter(Context context, ArrayList<WrapperCompetencia> competencias) {
        super(context,0,competencias);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        WrapperCompetencia wc = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_competecia, parent, false);
        }

        TextView fechaCompetencia = (TextView) convertView.findViewById(R.id.fechaCompetencia);
        TextView precio = (TextView) convertView.findViewById(R.id.precio);
        TextView estadio = (TextView) convertView.findViewById(R.id.estadio);
        TextView cantEntradas = (TextView) convertView.findViewById(R.id.cantEntradas);
        TextView cantentradasVendidas = (TextView) convertView.findViewById(R.id.cantentradasVendidas);


        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm a");
        String reportDate = df.format(wc.getFecha());


        fechaCompetencia.setText(reportDate);
        precio.setText(String.valueOf(wc.getPrecioEntrada()));
        estadio.setText(wc.getEstadio());
        cantEntradas.setText(String.valueOf(wc.getCantEntradas()));
        cantentradasVendidas.setText(String.valueOf(wc.getEntradasVendidas()));

        return convertView;
    }

}