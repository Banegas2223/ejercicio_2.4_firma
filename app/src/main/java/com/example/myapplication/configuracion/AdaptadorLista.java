package com.example.myapplication.configuracion;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.DatabaseHelper;
import com.example.myapplication.R;

import java.util.List;

public class AdaptadorLista extends ArrayAdapter<ObjetoFirmas> implements View.OnClickListener {

    private List<ObjetoFirmas> Datalist;
    private Context contexto;

    DatabaseHelper conexion;
    public AdaptadorLista(@NonNull Context context, List<ObjetoFirmas> mData) {
        super(context, R.layout.activity_signaturess, mData);
        this.contexto = context;
        this.Datalist = mData;
    }

    public static class ViewHolder{
        ImageView iv_firma;
        TextView txt_name;
    }

    @Override
    public void onClick(View v) {

    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ObjetoFirmas firmas = Datalist.get(position);
        ViewHolder viewHolder;
        View view = convertView;

        if (view == null){
            view = LayoutInflater.from(contexto).inflate(R.layout.activity_signaturess,null);
        }
        ImageView imagen = view.findViewById(R.id.iv_firma);
        TextView txt_name = view.findViewById(R.id.txt_name);

        imagen.setImageBitmap(obtenerImagen(firmas.getId()));
        txt_name.setText(firmas.getNombre());

        return view;
    }

    private Bitmap obtenerImagen(String id) {
        conexion = new DatabaseHelper(contexto);
        SQLiteDatabase db = conexion.getReadableDatabase();
        Bitmap bitmap;
        String selectQuery = "SELECT signature FROM firmas WHERE id = ?";
        // Ejecuta la consulta
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(id)});
        // Verifica si se encontraron resultados
        if (cursor.moveToFirst()) {
            // Obtiene los datos de la imagen en forma de arreglo de bytes
            byte[] imageData = cursor.getBlob(cursor.getColumnIndexOrThrow("signature"));
            // Convierte los datos de la imagen en un objeto Bitmap
            bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
        }
        else{
            bitmap = BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.imageb);
        }
        // Cierra el cursor y la conexión a la base de datos
        cursor.close();
        db.close();
        return bitmap;
    }
}
