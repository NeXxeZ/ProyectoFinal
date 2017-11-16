package com.nexxez.icsm;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class SettingsActivity extends AppCompatActivity {

    EditText et_Url1, et_Ambiental, et_Puerto, et_Dominio;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        et_Url1 = (EditText)findViewById(R.id.et_Url1);
        et_Ambiental = (EditText)findViewById(R.id.et_Ambiental);
        et_Puerto = (EditText)findViewById(R.id.et_Puerto);
        et_Dominio = (EditText)findViewById(R.id.et_Dominio);

        //Se declara el SharedPreferences y su editor
        preferences = getSharedPreferences("settings", MODE_PRIVATE);
        editor = preferences.edit();

        //Coloca en el editText el texto que habia guardado en el SharedPreferences
        et_Url1.setText(preferences.getString("Url1", ""));
        et_Ambiental.setText(preferences.getString("Ambiental", ""));
        et_Puerto.setText(preferences.getString("Puerto", ""));
        et_Dominio.setText(preferences.getString("Dominio", ""));

        et_Url1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                editor.putString("Url1", et_Url1.getText().toString().toLowerCase().trim()).apply();
            }
        });

        et_Ambiental.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                editor.putString("Ambiental", et_Ambiental.getText().toString().toLowerCase().trim()).apply();
            }
        });

        et_Puerto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                editor.putString("Puerto", et_Puerto.getText().toString().trim()).apply();
            }
        });

        et_Dominio.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                editor.putString("Dominio", et_Dominio.getText().toString().toLowerCase().trim()).apply();
            }
        });
    }
}
