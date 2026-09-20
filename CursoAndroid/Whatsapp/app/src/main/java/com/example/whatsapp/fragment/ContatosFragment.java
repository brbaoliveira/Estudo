package com.example.whatsapp.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.example.whatsapp.R;
import com.example.whatsapp.activity.ChatActivity;
import com.example.whatsapp.activity.GrupoActivity;
import com.example.whatsapp.adapter.ContatosAdapter;
import com.example.whatsapp.adapter.ConversasAdapter;
import com.example.whatsapp.config.ConfiguracaoFirebase;
import com.example.whatsapp.helper.Base64Custom;
import com.example.whatsapp.helper.RecyclerItemClickListener;
import com.example.whatsapp.helper.UsuarioFirebase;
import com.example.whatsapp.model.Conversa;
import com.example.whatsapp.model.Usuario;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ContatosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContatosFragment extends Fragment {
    private RecyclerView recyclerViewContatos;
    private ContatosAdapter adapter;
    private ArrayList<Usuario> listaContatos = new ArrayList<>();
    private DatabaseReference usuariosRef = ConfiguracaoFirebase.getFirebaseDatabase().child("usuarios");
    private FirebaseUser usuarioAtual = UsuarioFirebase.getUsuarioAtual();

    public ValueEventListener valueEventListenerContatos;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ContatosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ContatosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ContatosFragment newInstance(String param1, String param2) {
        ContatosFragment fragment = new ContatosFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contatos, container, false);
        //configuracoes iniciais
        recyclerViewContatos = view.findViewById(R.id.recyclerViewListaContatos);
        //configurar adapter
        //listaContatos.clear();
        adapter = new ContatosAdapter(listaContatos, getActivity());

        //configurar recyclerview
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager( getActivity());
        recyclerViewContatos.setLayoutManager(layoutManager);
        recyclerViewContatos.setHasFixedSize(true);
        recyclerViewContatos.setAdapter(adapter);

        //configurar evento de click no recyclerView
        recyclerViewContatos.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerViewContatos, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                List<Usuario> listaContatosAtualizada = adapter.getContatos();
                Usuario usuarioSelecionado = listaContatosAtualizada.get(position);
                boolean cabecalho = usuarioSelecionado.getEmail().isEmpty();
                Log.i("CHATINFO","CLICK: " + usuarioSelecionado.getNome() + " | " + usuarioSelecionado.getEmail());
                if(cabecalho){
                    Intent i = new Intent(getActivity(), GrupoActivity.class);
                    startActivity(i);
                    Log.i("GRUPOACRIVITY", "StartActivity ok");
                }else {
                    String idDestinatario = Base64Custom.codificarBase64(usuarioSelecionado.getEmail());
                    try {
                        Intent i = new Intent(getActivity(), ChatActivity.class);

                        i.putExtra("idDestinatario", idDestinatario);
                        i.putExtra("nome", usuarioSelecionado.getNome());
                        i.putExtra("email", usuarioSelecionado.getEmail());
                        //i.putExtra("idUsuario", usuarioSelecionado.getIdUsuario());
                        Log.i("CHATINFO","Intent: " + usuarioSelecionado + i + idDestinatario);

                        startActivity(i);
                    } catch (Exception e) {
                        Log.e("CHATINFO", "ERRO AO ABRIR CHAT", e);
                    }
                }
            }
            @Override
            public void onLongItemClick(View view, int position) {}
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {}
        }));


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        recuperarContatos();
    }

    @Override
    public void onStop() {
        super.onStop();
        usuariosRef.removeEventListener(valueEventListenerContatos);
    }
    public void pesquisarContatos(String texto){
        Log.d("EVENTO", texto);
        List<Usuario> listaContatosBusca = new ArrayList<>();
        for(Usuario usuario : listaContatos){
            String nome = usuario.getNome().toLowerCase();
            if(nome.contains(texto)){
                listaContatosBusca.add(usuario);
            }
        }
        adapter = new ContatosAdapter(listaContatosBusca, getActivity());
        recyclerViewContatos.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
    public void recarregarContatos(){
        adapter = new ContatosAdapter(listaContatos, getActivity());
        recyclerViewContatos.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
    public void recuperarContatos(){
        valueEventListenerContatos = usuariosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                limparListaContatos();
                for(DataSnapshot dados: snapshot.getChildren()){
                    Usuario usuario = dados.getValue(Usuario.class);
                    String emailUsuarioAtual = usuarioAtual.getEmail();
                    if(!emailUsuarioAtual.equals(usuario.getEmail())){
                        //listaContatos.clear();
                        listaContatos.add(usuario);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void limparListaContatos(){
        listaContatos.clear();
        adicionarMenuNovoGrupo();
    }
    public void adicionarMenuNovoGrupo(){
        Usuario itemGrupo = new Usuario();
        itemGrupo.setNome("Novo Grupo");
        itemGrupo.setEmail("");
        listaContatos.add( itemGrupo);
    }
}