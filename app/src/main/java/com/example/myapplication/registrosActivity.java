package com.example.myapplication;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.configuracion.AdaptadorLista;
import com.example.myapplication.configuracion.ObjetoFirmas;

import java.util.ArrayList;
import java.util.List;

public class registrosActivity extends AppCompatActivity {

    ListView listView;
    List<ObjetoFirmas> mData = new ArrayList<>();
    AdaptadorLista mAdapter;
    DatabaseHelper conexion;
    private Button btnRegresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registros);

        conexion = new DatabaseHelper(this);

        btnRegresar = findViewById(R.id.btnRegresar);
        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Vuelve a la actividad anterior al presionar el botón
            }
        });


        listView = (ListView) findViewById(R.id.listView);
        obtenerTabla();
        mAdapter = new AdaptadorLista(this,mData);
        listView.setAdapter(mAdapter);

    }

    private void obtenerTabla() {

        SQLiteDatabase db = conexion.getReadableDatabase();
        ObjetoFirmas firmas = null;
        //Cursor de base de datos
        Cursor cursor = db.rawQuery(DatabaseHelper.SelectTable,null);

        //Recorremos el cursor
        while (cursor.moveToNext()){
            firmas = new ObjetoFirmas();
            firmas.setId(cursor.getString(0));
            firmas.setNombre(cursor.getString(2));
            mData.add(firmas);
        }
        cursor.close();
    }


}