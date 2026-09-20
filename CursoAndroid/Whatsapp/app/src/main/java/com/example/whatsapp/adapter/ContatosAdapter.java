package com.example.whatsapp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsapp.R;
import com.example.whatsapp.helper.Base64Custom;
import com.example.whatsapp.helper.UsuarioFirebase;
import com.example.whatsapp.model.Conversa;
import com.example.whatsapp.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContatosAdapter extends RecyclerView.Adapter<ContatosAdapter.MyViewHolder> {
    private List<Usuario> contatos;
    private Context context;
    public ContatosAdapter(List<Usuario> listaContatos, Context c) {
        this.contatos = listaContatos;
        this.context = c;
    }
    public List<Usuario> getContatos(){
        return this.contatos;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_contatos, parent, false);

        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Usuario usuario = contatos.get(position);
        boolean cabecalho = usuario.getEmail().isEmpty();
        holder.nome.setText(usuario.getNome());
        holder.email.setText(usuario.getEmail());
        Log.d("FOTO", "Foto: " + usuario.getFotoPerfil());

        if (usuario.getFotoPerfil() != null && !usuario.getFotoPerfil().isEmpty()) {
            Bitmap bitmap = Base64Custom.decodificarBase64Imagem(usuario.getFotoPerfil());
            holder.foto.setImageBitmap(bitmap);
            Log.d("FOTO", "Foto: " + usuario.getFotoPerfil());

        } else {
            if(cabecalho){
                holder.foto.setImageResource(R.drawable.icone_grupo);
                holder.email.setVisibility(View.GONE);
            }else{
                holder.foto.setImageResource(R.drawable.padrao);
            }
        }
    }

    @Override
    public int getItemCount() {
        return contatos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        CircleImageView foto;
        TextView nome, email;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            foto = itemView.findViewById(R.id.imageViewFotoContato);
            nome = itemView.findViewById(R.id.textNomeContato);
            email = itemView.findViewById(R.id.textEmailContato);
        }
    }
}
