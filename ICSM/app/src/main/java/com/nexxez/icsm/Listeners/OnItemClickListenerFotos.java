package com.nexxez.icsm.Listeners;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.nexxez.icsm.Constructores.Objetos;
import com.nexxez.icsm.FotoActivity;
import com.nexxez.icsm.R;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;

public class OnItemClickListenerFotos implements AdapterView.OnItemClickListener {

    private Context context;
    private String dominio;
    final String directorio = "ICSM_iLPR1";

    private SharedPreferences preferences;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Objetos objetos = (Objetos)parent.getItemAtPosition(position);

        context = parent.getContext();
        preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        dominio = preferences.getString("Dominio", "");

        String [] fecha = objetos.getFecha().split("-");
        int reg = objetos.getRegistro();

        try {

            NtlmPasswordAuthentication auth = NtlmPasswordAuthentication.ANONYMOUS;
            String url = "smb://" + dominio + "/" + directorio + "/" + fecha[2] + fecha[1] + fecha[0] + "/FOTO/LPR" + reg + ".jpg";
            SmbFile file = new SmbFile(url); //Busca la imagen en el directorio
            File file2 = new File(Environment.getExternalStorageDirectory() + "/ICSM/" + fecha[2] + fecha[1] + fecha[0] + "/FOTO"); //Drectorio de almacenamiento interno
            File file3 = new File(file2.getPath() + "/" + file.getName());

            file2.mkdirs(); //Creación de carpetas (Si no existen)

            if (!file.exists()){

                Toast.makeText(context, R.string.to_try_in_seconds, Toast.LENGTH_SHORT).show();

            }else if (!file3.exists()){

                InputStream is = file.getInputStream();
                OutputStream os = new FileOutputStream(file3);

                IOUtils.copy(is, os);

                Intent intent = new Intent(context, FotoActivity.class);
                intent.putExtra("ruta", file3.getAbsolutePath());
                context.startActivity(intent);

            }else{

                Intent intent = new Intent(context, FotoActivity.class);
                intent.putExtra("ruta", file3.getAbsolutePath());
                context.startActivity(intent);

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
