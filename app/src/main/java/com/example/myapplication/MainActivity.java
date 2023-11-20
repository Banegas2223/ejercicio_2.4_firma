package com.example.myapplication;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.databinding.ActivityMainBinding;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity {


    ActivityMainBinding mainBinding;

    private DatabaseHelper dbHelper;
    EditText txtnombre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());
        txtnombre=findViewById(R.id.txtnombre);

        mainBinding.btnLimpiarPantalla.setOnClickListener(view ->{
            mainBinding.firmaView.clearCanvas();
            txtnombre.setText("");
        });

        dbHelper = new DatabaseHelper(this);

        mainBinding.btnGuardarFirmas.setOnClickListener(view -> {
            Bitmap signBitmap = mainBinding.firmaView.getSignatureBitmap();
            String nombre = txtnombre.getText().toString().trim(); // Obtener el texto y eliminar espacios en blanco al inicio y al final

            if (signBitmap != null && !nombre.isEmpty()) { // Verificar si la firma no es nula y el nombre no está vacío
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                signBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                // Aquí debes insertar el byte array en la base de datos utilizando SQLiteOpenHelper o algún otro método de acceso a la base de datos
                // Por ejemplo:
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("signature", byteArray);
                values.put("nombre", nombre);
                long result = db.insert(DatabaseHelper.tableName, null, values);

                if (result != -1) {
                    Toast.makeText(MainActivity.this, "Firma guardada correctamente", Toast.LENGTH_SHORT).show();
                    mainBinding.firmaView.clearCanvas();
                    txtnombre.setText("");
                } else {
                    Toast.makeText(MainActivity.this, "Error al guardar la firma", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Mostrar un mensaje si la firma es nula o el nombre está vacío
                Toast.makeText(MainActivity.this, "Por favor, completa la firma y el nombre", Toast.LENGTH_SHORT).show();
            }
        });

        Button btnVerRegistros = findViewById(R.id.btnRegistros);
        btnVerRegistros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, registrosActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }


}
