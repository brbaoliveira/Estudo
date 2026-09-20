package com.example.whatsapp.activity;

import android.content.Intent;
import android.os.Bundle;

import com.example.whatsapp.adapter.ContatosAdapter;
import com.example.whatsapp.adapter.GrupoSelecionadoAdapter;
import com.example.whatsapp.config.ConfiguracaoFirebase;
import com.example.whatsapp.helper.RecyclerItemClickListener;
import com.example.whatsapp.helper.UsuarioFirebase;
import com.example.whatsapp.model.Usuario;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsapp.R;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GrupoActivity extends AppCompatActivity {

    private RecyclerView recyclerMembrosSelecionados, recyclerMembros;
    private ContatosAdapter contatosAdapter;
    private GrupoSelecionadoAdapter grupoSelecionadoAdapter;
    private List<Usuario> listaMembros = new ArrayList<>();
    private List<Usuario> listaMembrosSelecionados = new ArrayList<>();
    private ArrayList<String> idsMembros = new ArrayList<>();
    private ValueEventListener valueEventListenerMembros;
    private DatabaseReference usuariosRef = ConfiguracaoFirebase.getFirebaseDatabase().child("usuarios");
    private FirebaseUser usuarioAtual = UsuarioFirebase.getUsuarioAtual();
    private Toolbar toolbar;
    private FloatingActionButton fabAvancarCadastro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("GRUPOACRIVITY","ACTIVITY ABERTA");

        setContentView(R.layout.activity_grupo);
        Log.i("GRUPOACRIVITY","Layout aberto");

        // Toolbar
        toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Novo Grupo");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Log.i("GRUPOACRIVITY","Toolbar desenhada");

        recyclerMembrosSelecionados = findViewById(R.id.recyclerMembrosSelecionados);
        recyclerMembros = findViewById(R.id.recyclerMembros);

        //configurar Adapter
        contatosAdapter = new ContatosAdapter(listaMembros, getApplicationContext());
        //configurar recyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerMembros.setLayoutManager(layoutManager);
        recyclerMembros.setHasFixedSize(true);
        recyclerMembros.setAdapter(contatosAdapter);

        recyclerMembros.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), recyclerMembros, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Usuario usuarioSelecionado = listaMembros.get(position);

                //remover usuario selecionado da lista
                listaMembros.remove(usuarioSelecionado);
                contatosAdapter.notifyDataSetChanged();

                //adiciona usuario na nova lista de selecionados
                listaMembrosSelecionados.add(usuarioSelecionado);
                grupoSelecionadoAdapter.notifyDataSetChanged();
                atualizarMembrosToolbar();

            }

            @Override
            public void onLongItemClick(View view, int position) {}

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {}
        }));

        fabAvancarCadastro = findViewById(R.id.fabAvancarCadastro);
        fabAvancarCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (Usuario usuario : listaMembrosSelecionados) {
                    idsMembros.add(usuario.getIdUsuario());
                }

                Intent i = new Intent(GrupoActivity.this, CadastroGrupoActivity.class);
                i.putStringArrayListExtra("idsMembros", idsMembros);

                startActivity(i);
            }
        });
        //configurar Adapter
        grupoSelecionadoAdapter = new GrupoSelecionadoAdapter(listaMembrosSelecionados, getApplicationContext());
        //configurar recyclerView
        RecyclerView.LayoutManager layoutManagerHorizontal = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerMembrosSelecionados.setLayoutManager(layoutManagerHorizontal);
        recyclerMembrosSelecionados.setHasFixedSize(true);
        recyclerMembrosSelecionados.setAdapter(grupoSelecionadoAdapter);
        recyclerMembrosSelecionados.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), recyclerMembrosSelecionados, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
               Usuario usuarioSelecionado = listaMembrosSelecionados.get(position);

               //remover da lista de membros selecionados
                listaMembrosSelecionados.remove(usuarioSelecionado);
                grupoSelecionadoAdapter.notifyDataSetChanged();

                //adicionar na lista de membros
                listaMembros.add(usuarioSelecionado);
                contatosAdapter.notifyDataSetChanged();
                atualizarMembrosToolbar();
            }

            @Override
            public void onLongItemClick(View view, int position) {}
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {}
        }));
    }

    @Override
    public void onStart() {
        super.onStart();
        recuperarMembros();
    }

    @Override
    public void onStop() {
        super.onStop();
        usuariosRef.removeEventListener(valueEventListenerMembros);
    }
    public void atualizarMembrosToolbar(){
        int totalSelecionados = listaMembrosSelecionados.size();
        int total = listaMembros.size() + totalSelecionados;

        toolbar.setSubtitle(totalSelecionados + " de " + total + " selecionados");
    }
    public void recuperarMembros(){
        valueEventListenerMembros = usuariosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //listaContatos.clear();
                for(DataSnapshot dados: snapshot.getChildren()){
                    Usuario usuario = dados.getValue(Usuario.class);
                    String emailUsuarioAtual = usuarioAtual.getEmail();
                    if(!emailUsuarioAtual.equals(usuario.getEmail())){
                        //listaContatos.clear();
                        listaMembros.add(usuario);
                    }
                    //listaContatos.add(usuario);
                }
                contatosAdapter.notifyDataSetChanged();
                atualizarMembrosToolbar();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}