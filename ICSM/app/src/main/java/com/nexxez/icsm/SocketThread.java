package com.nexxez.icsm;

import android.os.StrictMode;

import com.nexxez.icsm.Constructores.Objetos;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketThread extends Thread{

    @Override
    public void run() {
        String[] objeto;
        int puerto = 8081;

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        if (!MainActivity.preferences.getString("Puerto", "").equalsIgnoreCase("")) //Si el SharedPreferences es != "" actualiza el puerto
            puerto = Integer.parseInt(MainActivity.preferences.getString("Puerto", ""));

        try {

            ServerSocket sSocket = new ServerSocket(puerto);
            System.out.println("Servidor a la escucha...");

            while (true) {

                Socket cSocket = sSocket.accept();

                BufferedReader br = new BufferedReader(new InputStreamReader(cSocket.getInputStream()), 0x200);

                // Llegir arxiu

                String recibido;

                while ((recibido = br.readLine()) != null) {

                    Objetos objetos = new Objetos();

                    objeto = recibido.split(",");

                    System.out.println();
                    System.out.println(recibido);
                    System.out.println();
                    System.out.println("Registro: " + objeto[0]);
                    //System.out.println("?: " + objeto[1]);
                    //System.out.println("?: " + objeto[2]);
                    System.out.println("Matricula: " + objeto[3]);
                    System.out.println("Fecha: " + objeto[4]);
                    System.out.println("Hora: " + objeto[5]);
                    System.out.println("Velocidad: " + objeto[6]);
                    //System.out.println("?: " + objeto[7]);
                    System.out.println("Dirección: " + objeto[8]);
                    //System.out.println("?: " + objeto[9]);
                    System.out.println("-----------------------");

                    // Guarda el objeto en un array de objetos
                    objetos.setRegistro(Integer.parseInt(objeto[0]));
                    objetos.setMatricula(objeto[3]);
                    objetos.setFecha(objeto[4]);
                    objetos.setHora(objeto[5]);
                    objetos.setVelocidad(Double.parseDouble(objeto[6]));
                    objetos.setDireccion(objeto[8]);
                    MainActivity.arrayObjetos.add(0, objetos);
                }
                // Cerramos el socket cliente
                cSocket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
