package com.nexxez.icsm;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nexxez.icsm.Constructores.Objetos;

import java.util.List;

public class AdapterClass extends ArrayAdapter<Objetos> {

    private Objetos objetos;

    public AdapterClass(@NonNull Context context, @NonNull List<Objetos> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        objetos = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_list, parent, false);
        }

        TextView txt_Matricula = (TextView) convertView.findViewById(R.id.txt_Matricula);
        TextView txt_Hora = (TextView) convertView.findViewById(R.id.txt_Hora);
        TextView txt_Direccion = (TextView) convertView.findViewById(R.id.txt_Direccion);

        //String[] horaFinal = objetos.getHora().split(".");

        txt_Matricula.setText(objetos.getMatricula());
        txt_Hora.setText(objetos.getHora());
        txt_Direccion.setText(objetos.getDireccion());

        return convertView;
    }

}
