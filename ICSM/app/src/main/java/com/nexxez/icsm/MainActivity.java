package com.nexxez.icsm;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.os.Environment;
import android.os.StrictMode;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.nexxez.icsm.Constructores.Objetos;
import com.nexxez.icsm.Listeners.OnItemClickListenerFotos;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import veg.mediaplayer.sdk.MediaPlayer;
import veg.mediaplayer.sdk.MediaPlayerConfig;

public class MainActivity extends AppCompatActivity implements MediaPlayer.MediaPlayerCallback {

    static SharedPreferences preferences;

    MediaPlayer player = null;
    MediaPlayer player2 = null;
    ListView listView;
    static ArrayAdapter<Objetos> adapter;
    static ArrayList<Objetos> arrayObjetos = new ArrayList<>();

    static String urlRTSP;
    static String urlRTSP2;
    int width;
    int height;

    private final int REQUEST_PERMISSION=1;

    Context context = this;

    @Override
    public int Status(int i) {
        return 0;
    }

    @Override
    public int OnReceiveData(ByteBuffer byteBuffer, int i, long l) {
        return 0;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;

            case R.id.clear:
                new AlertDialog.Builder(context, R.style.AlertDialogTheme)
                        .setTitle(R.string.atention)
                        .setMessage(R.string.atention_msg)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                File file = new File(Environment.getExternalStorageDirectory() + "/ICSM/");

                                if (file.exists()) {
                                    String deleteCmd = "rm -r " + file.getPath();
                                    Runtime runtime = Runtime.getRuntime();
                                    try {
                                        runtime.exec(deleteCmd);
                                        Toast.makeText(context, R.string.to_data_deleted, Toast.LENGTH_SHORT).show();
                                    } catch (IOException e) {

                                    }
                                }else{
                                    Toast.makeText(context, R.string.to_no_data, Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView);
        player = (MediaPlayer) findViewById(R.id.player);
        player2 = (MediaPlayer) findViewById(R.id.player2);

        preferences = getSharedPreferences("settings", MODE_PRIVATE);

        //urlRTSP = "rtsp://mpv.cdn3.bigCDN.com:554/bigCDN/definst/mp4:bigbuckbunnyiphone_400.mp4";
        urlRTSP = preferences.getString("Url1", "");
        urlRTSP2 = preferences.getString("Ambiental", "");

        //Solicita permiso de almacenamiento
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
        }

        //Estas lineas permiten ejecutar el Timer
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //Timer per a actualitzar el adapter cada 0,5s
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                        listView.smoothScrollToPosition(0);
                    }
                });
            }
        }, 0, 500);

        new SocketThread().start(); //Inicia el Thread del socket

        playRTSP(urlRTSP, urlRTSP2); //Llamada al metodo para reproducir
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (arrayObjetos != null) {
            adapter = new AdapterClass(this, arrayObjetos);
            listView.setAdapter(adapter);
        }

        //urlRTSP = "rtsp://mpv.cdn3.bigCDN.com:554/bigCDN/definst/mp4:bigbuckbunnyiphone_400.mp4";
        urlRTSP = preferences.getString("Url1", "");
        urlRTSP2 = preferences.getString("Ambiental", "");

        if (urlRTSP2.equals("")) { //Si no existe direccion para la camara 2 aumenta el tamaño del reproductor para la camara 1
            height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 260, getResources().getDisplayMetrics());
            player2.setVisibility(View.INVISIBLE);
            player.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
        }else{
            height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 140, getResources().getDisplayMetrics());
            width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 180, getResources().getDisplayMetrics());
            player2.setVisibility(View.VISIBLE);
            player.setLayoutParams(new ConstraintLayout.LayoutParams(width, height));
        }

        listView.setOnItemClickListener(new OnItemClickListenerFotos());

        playRTSP(urlRTSP, urlRTSP2);
    }

    public void playRTSP(String url, String url2) {

        player.getSurfaceView().setZOrderOnTop(true);    // necessary
        SurfaceHolder sfhTrackHolder = player.getSurfaceView().getHolder();
        sfhTrackHolder.setFormat(PixelFormat.TRANSPARENT);

        player2.getSurfaceView().setZOrderOnTop(true);    // necessary
        SurfaceHolder sfhTrackHolder2 = player2.getSurfaceView().getHolder();
        sfhTrackHolder2.setFormat(PixelFormat.TRANSPARENT);

        player.getConfig().setConnectionUrl(url);
        player2.getConfig().setConnectionUrl(url2);

        if (player != null) {

            if (player.getConfig().getConnectionUrl().isEmpty())
                return;

            player.Close();

            MediaPlayerConfig conf = new MediaPlayerConfig();

            player.setVisibility(View.INVISIBLE);

            conf.setConnectionUrl(player.getConfig().getConnectionUrl());

            player.Open(conf, this);
        }

        if (player2 != null) {

            if (player2.getConfig().getConnectionUrl().isEmpty())
                return;

            player2.Close();

            MediaPlayerConfig conf = new MediaPlayerConfig();

            player2.setVisibility(View.INVISIBLE);

            conf.setConnectionUrl(player2.getConfig().getConnectionUrl());

            player2.Open(conf, this);
        }
    }
}