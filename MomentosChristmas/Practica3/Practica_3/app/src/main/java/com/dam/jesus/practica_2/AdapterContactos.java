package com.dam.jesus.practica_2;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterContactos extends RecyclerView.Adapter<AdapterContactos.ViewHolderDatos>
        implements View.OnClickListener  {

    ArrayList<Contacto> listaContactos;
    private View.OnClickListener listener;

    public AdapterContactos(ArrayList<Contacto> listaContactos) {
        this.listaContactos = listaContactos;
    }

    @Override
    public AdapterContactos.ViewHolderDatos onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_contacto,null,false);
        view.setOnClickListener(this);

        return new AdapterContactos.ViewHolderDatos(view);
    }



    @Override
    public void onBindViewHolder(AdapterContactos.ViewHolderDatos holder, int position) {
        holder.etiNombre.setText(listaContactos.get(position).getNombre());
        holder.etiTelefono.setText(listaContactos.get(position).getTelefono());
    }

    @Override
    public int getItemCount() {
        return listaContactos.size();
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

        TextView etiNombre, etiTelefono;

        public ViewHolderDatos(View itemView)
        {
            super(itemView);
            etiNombre = itemView.findViewById(R.id.idContactoNombre);
            etiTelefono = itemView.findViewById(R.id.idContactoTelefono);
        }
    }
}
