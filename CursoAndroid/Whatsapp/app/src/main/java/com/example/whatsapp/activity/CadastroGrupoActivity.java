package com.example.whatsapp.activity;

import android.app.ComponentCaller;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import com.example.whatsapp.adapter.GrupoSelecionadoAdapter;
import com.example.whatsapp.config.ConfiguracaoFirebase;
import com.example.whatsapp.helper.Base64Custom;
import com.example.whatsapp.helper.UsuarioFirebase;
import com.example.whatsapp.model.Grupo;
import com.example.whatsapp.model.Usuario;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import kotlin.collections.UArraySortingKt;

public class CadastroGrupoActivity extends AppCompatActivity {

    private TextView textTotalParticipantes;
    private List<Usuario> listaMembrosSelecionados = new ArrayList<>();
    private GrupoSelecionadoAdapter  grupoSelecionadoAdapter;
    private RecyclerView recyclerMembrosSelecionados;
    private CircleImageView imageGrupo;
    private static final int SELECAO_GALERIA = 200;
    private Grupo grupo;
    private FloatingActionButton fabSalvarGrupo;
    private EditText editNomeGrupo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i("GRUPOACRIVITY","ACTIVITY ABERTA");

        setContentView(R.layout.activity_cadastro_grupo);
        Log.i("GRUPOACRIVITY","Layout aberto");

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Novo Grupo");
        toolbar.setSubtitle("Defina o nome");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Log.i("GRUPOACRIVITY","Toolbar desenhada");

        textTotalParticipantes = findViewById(R.id.textTotalParticipantes);
        recyclerMembrosSelecionados = findViewById(R.id.recyclerMembrosGrupo);
        imageGrupo = findViewById(R.id.imageGrupo);
        imageGrupo = findViewById(R.id.imageGrupo);
        fabSalvarGrupo = findViewById(R.id.fabSalvarGrupo);
        editNomeGrupo = findViewById(R.id.editNomeGrupo);
        grupo = new Grupo();
        imageGrupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if (i.resolveActivity(getPackageManager()) != null){
                    startActivityForResult(i,SELECAO_GALERIA);
                }
            }
        });

        //recuperar lista de membros passada
        if(getIntent().getExtras() != null){
            ArrayList<String> idsMembros = getIntent().getStringArrayListExtra("idsMembros");
            if (idsMembros != null) {
                buscarUsuariosPorId(idsMembros);
                Log.i("GRUPOACRIVITY","Membros: " + idsMembros);
            }
        }
        //configurar adapter
        grupoSelecionadoAdapter = new GrupoSelecionadoAdapter(listaMembrosSelecionados, getApplicationContext());
        //configurar recyclerView
        RecyclerView.LayoutManager layoutManagerHorizontal = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerMembrosSelecionados.setLayoutManager(layoutManagerHorizontal);
        recyclerMembrosSelecionados.setHasFixedSize(true);
        recyclerMembrosSelecionados.setAdapter(grupoSelecionadoAdapter);

        fabSalvarGrupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nomeGrupo = editNomeGrupo.getText().toString();
                listaMembrosSelecionados.add(UsuarioFirebase.getDadosUsuarioLogado());
                grupo.setMembros(listaMembrosSelecionados);
                grupo.setNome(nomeGrupo);
                grupo.salvar();
                Intent i = new Intent(CadastroGrupoActivity.this, ChatActivity.class);
                i.putExtra("idGrupo", grupo.getId());
                i.putExtra("nomeGrupo", grupo.getNome());
                startActivity(i);
            }
        });
    }

    private void buscarUsuariosPorId(List<String> idsMembros) {
        DatabaseReference usuariosRef = ConfiguracaoFirebase.getFirebaseDatabase().child("usuarios");
        for (String id : idsMembros) {
            usuariosRef.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Usuario usuario = snapshot.getValue(Usuario.class);
                    if (usuario != null) {
                        listaMembrosSelecionados.add(usuario);
                        Log.i("GRUPOACRIVITY","Lista: " + listaMembrosSelecionados);
                        grupoSelecionadoAdapter.notifyDataSetChanged();
                        int totalMembros = listaMembrosSelecionados.size();
                        textTotalParticipantes.setText("Participantes: " + totalMembros);
                        Log.i("GRUPOACRIVITY","Qtd membros: " + totalMembros);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {}
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            Bitmap imagem = null;
            try{
                switch (requestCode){
                    case SELECAO_GALERIA:
                        Uri localImagemSelecionada = data.getData();
                        imagem = MediaStore.Images.Media.getBitmap(getContentResolver(), localImagemSelecionada);
                        break;
                }
                if(imagem != null){
                    //DATABASE
                    String imagemBase64 = Base64Custom.codificarBase64Imagem(imagem);
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("grupos").child(grupo.getId());

                    ref.child("fotoPerfil").setValue(imagemBase64);
                    grupo.setFotoPerfil(imagemBase64);
                    Toast.makeText(CadastroGrupoActivity.this,"Foto de perfil adicionada!",Toast.LENGTH_SHORT).show();

                    Bitmap bitmap = Base64Custom.decodificarBase64Imagem(imagemBase64);
                    imageGrupo.setImageBitmap(bitmap);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}