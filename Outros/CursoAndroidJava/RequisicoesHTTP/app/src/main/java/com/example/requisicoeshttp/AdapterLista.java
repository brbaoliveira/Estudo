package com.example.requisicoeshttp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.requisicoeshttp.model.Foto;

import java.util.List;

public class AdapterLista extends RecyclerView.Adapter<AdapterLista.MyViewHolder>{
    private List<Foto> lista;

    public AdapterLista(List<Foto> lista) {
        this.lista = lista;
    }

    @NonNull
    @Override
    public AdapterLista.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_lista, parent, false);
        return new AdapterLista.MyViewHolder(itemLista);    }

    @Override
    public void onBindViewHolder(@NonNull AdapterLista.MyViewHolder holder, int position) {
        Foto foto = lista.get(position);
        holder.item.setText(foto.retornaFoto());
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView item;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.tvItem);

        }
    }
}
