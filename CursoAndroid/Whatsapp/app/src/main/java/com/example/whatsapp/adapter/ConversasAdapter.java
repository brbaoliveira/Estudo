package com.example.whatsapp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsapp.R;
import com.example.whatsapp.helper.Base64Custom;
import com.example.whatsapp.model.Conversa;
import com.example.whatsapp.model.Grupo;
import com.example.whatsapp.model.Usuario;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ConversasAdapter extends RecyclerView.Adapter<ConversasAdapter.MyViewHolder> {
    private List<Conversa> conversas;
    private Context context;
    public ConversasAdapter(List<Conversa> lista, Context c) {
        this.conversas = lista;
        this.context = c;
    }
    public List<Conversa> getConversas(){
        return this.conversas;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_contatos,parent,false);

        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Conversa conversa = conversas.get( position );
        holder.ultimaMensagem.setText( conversa.getUltimaMensagem() );
        if(conversa.getIsGroup().equals("true")){
            Grupo grupo = conversa.getGrupo();
            holder.nome.setText( grupo.getNome() );

            if(grupo.getFotoPerfil() != null && !grupo.getFotoPerfil().isEmpty()){
                Bitmap bitmap = Base64Custom.decodificarBase64Imagem(grupo.getFotoPerfil());
                holder.foto.setImageBitmap(bitmap);
                Log.i("FOTO_DEBUG", "foto depois de carregar= " + bitmap);

            }else{
                holder.foto.setImageResource(R.drawable.padrao);
                Log.i("FOTO_DEBUG", "Erro ao carregar foto de perfil");

            }
        }else{
            Usuario usuario = conversa.getUsuarioExibicao();
            if (usuario != null){
                String fotoPerfil = usuario.getFotoPerfil();
                holder.nome.setText( usuario.getNome() );
                Log.i("FOTO_DEBUG", "foto antes= " + fotoPerfil);

                if(fotoPerfil != null && !fotoPerfil.isEmpty()){
                    Bitmap bitmap = Base64Custom.decodificarBase64Imagem(fotoPerfil);
                    holder.foto.setImageBitmap(bitmap);
                    Log.i("FOTO_DEBUG", "foto depois de carregar= " + bitmap);

                }else{
                    holder.foto.setImageResource(R.drawable.padrao);
                    Log.i("FOTO_DEBUG", "Erro ao carregar foto de perfil");

                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return conversas.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        CircleImageView foto;
        TextView nome, ultimaMensagem;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            foto = itemView.findViewById(R.id.imageViewFotoContato);
            nome = itemView.findViewById(R.id.textNomeContato);
            ultimaMensagem = itemView.findViewById(R.id.textEmailContato);
        }
    }
}
