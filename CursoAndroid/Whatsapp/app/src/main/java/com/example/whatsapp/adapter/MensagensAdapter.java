package com.example.whatsapp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsapp.R;
import com.example.whatsapp.helper.Base64Custom;
import com.example.whatsapp.helper.UsuarioFirebase;
import com.example.whatsapp.model.Conversa;
import com.example.whatsapp.model.Mensagem;

import java.time.Instant;
import java.util.List;

public class MensagensAdapter extends RecyclerView.Adapter<MensagensAdapter.MyViewHolder>{
    private List<Mensagem> mensagens;
    private Context context;
    private static final int TIPO_REMETENTE = 0;
    private static final int TIPO_DESTINATARIO = 1;
    public MensagensAdapter(List<Mensagem> lista, Context c) {
        this.mensagens = lista;
        this.context = c;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = null;
        Log.i("ESTANULO","item esta nulo");
        if (viewType == TIPO_REMETENTE){
            item = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_mensagens_remetente,parent,false);
        }else if(viewType == TIPO_DESTINATARIO){
            Conversa conversa = new Conversa();
            if(conversa.getIsGroup().equals("true")){
                item = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_mensagens_destinatario,parent,false);
            }else {
                item = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_mensagens_destinatario,parent,false);

            }
        }
        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Mensagem mensagem = mensagens.get(position);
        String msg =  mensagem.getMensagem();
        String imagem =  mensagem.getImagem();
        if(imagem != null && !imagem.isEmpty()){
            Bitmap bitmap = Base64Custom.decodificarBase64Imagem(mensagem.getImagem());
            holder.imagem.setImageBitmap(bitmap);
            String nome = mensagem.getNome();
            if(!nome.isEmpty()){
                holder.nome.setText(nome);
            }else{
                holder.nome.setVisibility(View.GONE);
            }
            holder.mensagem.setVisibility(View.GONE);
        }else {
            holder.mensagem.setText(msg);
            String nome = mensagem.getNome();
            if(!nome.isEmpty()){
                //holder.nome.setVisibility(View.VISIBLE);
                holder.nome.setText(nome);
            }else{
                holder.nome.setVisibility(View.GONE);
            }
            //holder.mensagem.setVisibility(View.VISIBLE);
            holder.imagem.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mensagens.size();
    }

    @Override
    public int getItemViewType(int position) {
        Mensagem mensagem = mensagens.get(position);
        String idUsuario = UsuarioFirebase.getIdentificadorUsuario();
        if (mensagem != null && mensagem.getIdUsuario() != null) {
            if(idUsuario.equals(mensagem.getIdUsuario())){
                return TIPO_REMETENTE;
            }
        }
        return TIPO_DESTINATARIO;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView mensagem, nome;
        ImageView imagem;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mensagem = itemView.findViewById(R.id.textMensagemTexto);
            nome = itemView.findViewById(R.id.textNomeExibicao);
            imagem   = itemView.findViewById(R.id.imageMensagemFoto);
        }
    }
}
