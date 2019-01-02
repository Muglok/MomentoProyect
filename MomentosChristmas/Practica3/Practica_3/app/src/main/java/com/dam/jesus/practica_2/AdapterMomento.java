package com.dam.jesus.practica_2;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterMomento extends RecyclerView.Adapter<AdapterMomento.ViewHolderDatos>
implements View.OnClickListener {

    ArrayList<Momento> listaPersonajes;
    private View.OnClickListener listener;

    public AdapterMomento(ArrayList<Momento> listaPersonajes) {
        this.listaPersonajes = listaPersonajes;
    }

    @Override
    public ViewHolderDatos onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list,null,false);
        view.setOnClickListener(this);

        return new ViewHolderDatos(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderDatos holder, int position) {
        holder.etiNombre.setText(listaPersonajes.get(position).getNombre());
        holder.etiInformacion.setText(listaPersonajes.get(position).getInfo());
        holder.foto.setImageResource(listaPersonajes.get(position).getFoto());
    }

    @Override
    public int getItemCount() {
        return listaPersonajes.size();
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

        TextView etiNombre,etiInformacion;
        ImageView foto;

        public ViewHolderDatos( View itemView) {
            super(itemView);
            etiNombre = itemView.findViewById(R.id.idNombre);
            etiInformacion = itemView.findViewById(R.id.idInfo);
            foto = itemView.findViewById(R.id.idImagen);
        }
    }
}
