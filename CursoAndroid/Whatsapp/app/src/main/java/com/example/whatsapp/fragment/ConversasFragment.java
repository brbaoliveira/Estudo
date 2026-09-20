package com.example.whatsapp.fragment;

import android.content.Intent;
import android.os.Bundle;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsapp.R;
import com.example.whatsapp.activity.ChatActivity;
import com.example.whatsapp.adapter.ConversasAdapter;
import com.example.whatsapp.config.ConfiguracaoFirebase;
import com.example.whatsapp.helper.Base64Custom;
import com.example.whatsapp.helper.RecyclerItemClickListener;
import com.example.whatsapp.helper.UsuarioFirebase;
import com.example.whatsapp.model.Conversa;
import com.example.whatsapp.model.Usuario;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConversasFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConversasFragment extends Fragment {

    private RecyclerView recyclerViewConversas;
    private List<Conversa> listaConversas = new ArrayList<>();
    private ConversasAdapter adapter;
    private DatabaseReference database, conversasRef;
    private ChildEventListener childEventListener;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ConversasFragment() {}
    public static ConversasFragment newInstance(String param1, String param2) {
        ConversasFragment fragment = new ConversasFragment();
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
        View view = inflater.inflate(R.layout.fragment_conversas, container, false);
        recyclerViewConversas = view.findViewById(R.id.recyclerListaConversas);
        //confiurando adapter
        adapter = new ConversasAdapter(listaConversas, getActivity());
        //configurando recyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewConversas.setLayoutManager(layoutManager);
        recyclerViewConversas.setHasFixedSize(true);
        recyclerViewConversas.setAdapter(adapter);

        recyclerViewConversas.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerViewConversas, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                List<Conversa> listaConversasAtualizada = adapter.getConversas();
                Conversa conversaSelecionada = listaConversasAtualizada.get(position);
                try {
                   if(conversaSelecionada.getIsGroup().equals("true")){
                        Intent i = new Intent(getActivity(), ChatActivity.class);
                        i.putExtra("idGrupo", conversaSelecionada.getGrupo().getId());
                        i.putExtra("nomeGrupo", conversaSelecionada.getGrupo().getNome());
                        startActivity(i);
                   }else {
                       String idDestinatario = Base64Custom.codificarBase64(conversaSelecionada.getUsuarioExibicao().getEmail());
                       Intent i = new Intent(getActivity(), ChatActivity.class);
                        i.putExtra("idDestinatario", idDestinatario);
                        i.putExtra("nome", conversaSelecionada.getUsuarioExibicao().getNome());
                        i.putExtra("email", conversaSelecionada.getUsuarioExibicao().getEmail());
                        startActivity(i);
                   }
               } catch (Exception e) {
                    Log.e("CHATINFO", "ERRO AO ABRIR CHAT", e);
               }
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        }));
        String identificadorUsuario = UsuarioFirebase.getIdentificadorUsuario();
        database = ConfiguracaoFirebase.getFirebaseDatabase();
        conversasRef = database.child("conversas").child(identificadorUsuario);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        recuperarConversas();
    }

    @Override
    public void onStop() {
        super.onStop();
        conversasRef.removeEventListener(childEventListener);
    }
    public void pesquisarConversas(String texto){
        Log.d("EVENTO", texto);
        List<Conversa> listaConversaBusca = new ArrayList<>();
        for(Conversa conversa : listaConversas){
            if(conversa.getUsuarioExibicao() != null){
                String nome = conversa.getUsuarioExibicao().getNome().toLowerCase();
                String ultimaMsg = conversa.getUltimaMensagem().toLowerCase();
                if(nome.contains(texto) || ultimaMsg.contains(texto)){
                    listaConversaBusca.add(conversa);
                }
            }else{
                String nome = conversa.getGrupo().getNome().toLowerCase();
                String ultimaMsg = conversa.getUltimaMensagem().toLowerCase();
                if(nome.contains(texto) || ultimaMsg.contains(texto)){
                    listaConversaBusca.add(conversa);
                }
            }
        }
        adapter = new ConversasAdapter(listaConversaBusca, getActivity());
        recyclerViewConversas.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
    public void recarregarConversas(){
        adapter = new ConversasAdapter(listaConversas, getActivity());
        recyclerViewConversas.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
    public void recuperarConversas(){
        listaConversas.clear();
        childEventListener = conversasRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Conversa conversa = snapshot.getValue(Conversa.class);
                    listaConversas.add(conversa);
                    adapter.notifyDataSetChanged();
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {}
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}