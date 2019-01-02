package com.dam.jesus.practica_2;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.ArrayList;

public class AdapterMomentos2  extends RecyclerView.Adapter<AdapterMomentos2.ViewHolderDatos>
        implements View.OnClickListener {

    ArrayList<Momento2> listaMomentos;
    private View.OnClickListener listener;

    public AdapterMomentos2(ArrayList<Momento2> listaMomentos) {
        this.listaMomentos = listaMomentos;
    }

    @Override
    public AdapterMomentos2.ViewHolderDatos onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_momento,null,false);
        view.setOnClickListener(this);

        return new AdapterMomentos2.ViewHolderDatos(view);
    }

    @Override
    public void onBindViewHolder(AdapterMomentos2.ViewHolderDatos holder, int position) {
       holder.etiTitulo.setText(listaMomentos.get(position).getTitulo());
        holder.etiDescripcion.setText(listaMomentos.get(position).getDescripcion());
        holder.etiCancion.setText(listaMomentos.get(position).getCancion());

        holder.etiFecha.setText(dateFormat(listaMomentos.get(position).getFechaYhora()).toString());
    }

    @Override
    public int getItemCount() {
        return listaMomentos.size();
    }

    public void setOnClickListener(View.OnClickListener listener)
    {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if(listener != null){
            listener.onClick(view);
        }

    }

    public class ViewHolderDatos extends RecyclerView.ViewHolder {

        TextView etiTitulo,etiDescripcion,etiCancion,etiFecha;

        public ViewHolderDatos( View itemView) {
            super(itemView);
            etiTitulo = itemView.findViewById(R.id.idTitulo);
            etiDescripcion = itemView.findViewById(R.id.idDescripcion);
            etiCancion = itemView.findViewById(R.id.idCancion);
            etiFecha = itemView.findViewById(R.id.idFecha);
        }
    }

    public static String dateFormat(Date dateX)
    {
        SimpleDateFormat dt = new SimpleDateFormat("dd-mm-yy hh:mm");


        return dt.format(dateX);
    }
}
