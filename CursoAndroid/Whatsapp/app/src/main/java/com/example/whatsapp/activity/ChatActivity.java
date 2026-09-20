package com.example.whatsapp.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsapp.R;
import com.example.whatsapp.adapter.MensagensAdapter;
import com.example.whatsapp.config.ConfiguracaoFirebase;
import com.example.whatsapp.helper.Base64Custom;
import com.example.whatsapp.helper.UsuarioFirebase;
import com.example.whatsapp.model.Conversa;
import com.example.whatsapp.model.Grupo;
import com.example.whatsapp.model.Mensagem;
import com.example.whatsapp.model.Usuario;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    private TextView textViewNome;
    private CircleImageView circleImageViewFoto;
    private Usuario usuarioDestinatarioConversas;
    private EditText editMensagem;
    private ImageView imageCamera;
    private String usuarioDestinatario;
    private String grupo;
    private Grupo grupos;
    //private Usuario usuarioDestinatario;
    private DatabaseReference database;
    private DatabaseReference mensagensRef;
    private ChildEventListener childEventListenerMensagens;

    //identificador usuarios remetente e destinatario
    private String idUsuarioRemetente;
    private String idUsuarioDestinatario;

    private RecyclerView recyclerMensagens;
    private MensagensAdapter adapter;
    private List<Mensagem> mensagens = new ArrayList<>();
    private static final int SELECAO_CAMERA = 100;
    String nome,email, foto;
    private boolean isGrupo;
    private Usuario usuarioRemetenteConversas = UsuarioFirebase.getDadosUsuarioLogado();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("CHATINFO","ACTIVITY ABERTA");

        setContentView(R.layout.activity_chat);
        Log.i("CHATINFO","Layout aberto");

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Views
        textViewNome = findViewById(R.id.textViewNomeChat);
        circleImageViewFoto = findViewById(R.id.circleImageFotoChat);
        editMensagem = findViewById(R.id.editMensagem);
        recyclerMensagens = findViewById(R.id.recyclerMensagens);
        imageCamera = findViewById(R.id.imageCamera);

        // Firebase
        database = ConfiguracaoFirebase.getFirebaseDatabase();
        idUsuarioRemetente = UsuarioFirebase.getIdentificadorUsuario();

        //Recuperar dados do usuário destinatario
        Bundle bundle = getIntent().getExtras();
        Log.i("CHATINFO","antes do bundle ok" + bundle);

        if ( bundle !=  null ) {
            if(bundle.containsKey("idGrupo")){
                /*grupo = (Grupo)bundle.getSerializable("chatGrupo");
                textViewNome.setText(grupo.getNome());*/
                isGrupo = true;
                bundle.setClassLoader(Grupo.class.getClassLoader());
                Log.i("CHATINFO", "bundle grupo ok");

                grupo = getIntent().getStringExtra("idGrupo");
                nome = getIntent().getStringExtra("nomeGrupo");
                idUsuarioDestinatario = grupo;

                Log.i("CHATINFO", "grupo antes do getIntent " + idUsuarioDestinatario);

                if (grupo != null) {
                    textViewNome.setText(nome);
                    DatabaseReference grupoRef = ConfiguracaoFirebase.getFirebaseDatabase().child("grupos").child(grupo);
                    grupoRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (!snapshot.exists()) {
                                finish();
                                return;
                            }
                            grupos = snapshot.getValue(Grupo.class);
                            if (grupos == null) return;
                            // Nome
                            textViewNome.setText(grupos.getNome());
                            foto = grupos.getFotoPerfil();
                            if (foto != null && !foto.isEmpty()) {
                                Bitmap bitmap = Base64Custom.decodificarBase64Imagem(foto);
                                circleImageViewFoto.setImageBitmap(bitmap);
                            } else {
                                circleImageViewFoto.setImageResource(R.drawable.padrao);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {}
                    });
                }
            }else{
                isGrupo = false;
                bundle.setClassLoader(Usuario.class.getClassLoader());
                Log.i("CHATINFO", "bundle ok");
                Log.i("CHATINFO", "usuario destinatario antes do getIntent " + usuarioDestinatario);

                usuarioDestinatario = getIntent().getStringExtra("idDestinatario");
                nome = getIntent().getStringExtra("nome");
                email = getIntent().getStringExtra("email");
                //usuarioDestinatario = (Usuario) bundle.getSerializable("chatContato");
                Log.i("CHATINFO", "usuario destinatario depois do getIntent " + usuarioDestinatario);


                if (usuarioDestinatario != null) {
                    textViewNome.setText(nome);
                    Log.i("CHATINFO","nome usuario " + textViewNome);

                    DatabaseReference usuarioRef = ConfiguracaoFirebase.getFirebaseDatabase().child("usuarios").child(usuarioDestinatario);
                    usuarioRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (!snapshot.exists()) {
                                finish();
                                return;
                            }
                            Usuario usuarioDest = snapshot.getValue(Usuario.class);
                            if (usuarioDest == null) return;
                            // Nome
                            textViewNome.setText(usuarioDest.getNome());
                            // Foto
                             foto = usuarioDest.getFotoPerfil();
                            Log.i("CHATINFO","foto usuario " + foto);

                            if (foto != null && !foto.isEmpty()) {
                                Bitmap bitmap = Base64Custom.decodificarBase64Imagem(foto);
                                circleImageViewFoto.setImageBitmap(bitmap);
                            } else {
                                circleImageViewFoto.setImageResource(R.drawable.padrao);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {}
                    });
                }/*
                //String foto = fotoP;
                Log.i("CHATINFO","foto usuario " + foto);
                if (foto != null) {
                    Bitmap bitmap = Base64Custom.decodificarBase64Imagem(foto);
                    circleImageViewFoto.setImageBitmap(bitmap);
                } else {
                    circleImageViewFoto.setImageResource(R.drawable.padrao);
                }*/
                //recuperar dados usuario destinatario
                idUsuarioDestinatario = Base64Custom.codificarBase64(email);


                Log.i("CHATINFO", "ID DEST: " + idUsuarioDestinatario);
            }

        } else {
            Toast.makeText(this, "Erro ao abrir conversa, mas usuario destinatario não é null", Toast.LENGTH_SHORT).show();
            Log.i("CHATINFO","Erro ao abrir conversa, mas usuario destinatario não é null");
           finish(); // segurança
        }


        //Configuração adapter
        adapter = new MensagensAdapter(mensagens, getApplicationContext() );

        //Configuração recyclerview
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerMensagens.setLayoutManager( layoutManager );
        recyclerMensagens.setHasFixedSize( true );
        recyclerMensagens.setAdapter( adapter );

        //database = ConfiguracaoFirebase.getFirebaseDatabase();
        if (idUsuarioDestinatario != null) {
            mensagensRef = database.child("mensagens").child(idUsuarioRemetente).child(idUsuarioDestinatario);
        }

        //mensagensRef = database.child("mensagens").child( idUsuarioRemetente ).child( idUsuarioDestinatario );
        //evento de clique na camera
        imageCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (i.resolveActivity(getPackageManager()) != null){
                    startActivityForResult(i,SELECAO_CAMERA);
                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bitmap imagem = null;
            try {
                switch (requestCode) {
                    case SELECAO_CAMERA:
                        imagem = (Bitmap) data.getExtras().get("data");
                        break;
                }
                if(imagem != null) {
                    String imagemBase64 = Base64Custom.codificarBase64Imagem(imagem);
                    //DatabaseReference ref = FirebaseDatabase.getInstance().getReference("mensagens").child(idUsuarioRemetente);
                    //ref.child("imagem").setValue(imagemBase64);
                    if (usuarioDestinatario != null){
                        Toast.makeText(ChatActivity.this,"Foto de perfil adicionada!",Toast.LENGTH_SHORT).show();
                        Mensagem mensagem = new Mensagem();
                        mensagem.setIdUsuario(idUsuarioRemetente);
                        mensagem.setMensagem("imagem.jpg");
                        mensagem.setImagem(imagemBase64);

                        //Bitmap bitmap = Base64Custom.decodificarBase64Imagem(imagemBase64);
                        //Salvar mensagem para remetente
                        salvarMensagem(idUsuarioRemetente, idUsuarioDestinatario,mensagem);
                        //Salvar mensagem para destinatario
                        salvarMensagem(idUsuarioDestinatario, idUsuarioRemetente,mensagem);
                    }else {
                        for (Usuario membro : grupos.getMembros()){
                            String idRemetenteGrupo = Base64Custom.codificarBase64(membro.getEmail());
                            String idUsuarioLogadoGrupo = UsuarioFirebase.getIdentificadorUsuario();
                            Mensagem mensagem = new Mensagem();
                            mensagem.setIdUsuario(idUsuarioLogadoGrupo);
                            mensagem.setMensagem("imagem.jpg");
                            mensagem.setImagem(imagemBase64);                            mensagem.setNome(usuarioRemetenteConversas.getNome());
                            //Salvar mensagem para o remetente
                            salvarMensagem(idRemetenteGrupo,idUsuarioDestinatario,mensagem);

                            //Salvar conversa
                            salvarConversa(idRemetenteGrupo, idUsuarioDestinatario, usuarioDestinatarioConversas, mensagem, true);
                        }
                    }

                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    //ENVIAR MENSAGEM
    public void enviarMensagem(View view){
        String textoMensagem = editMensagem.getText().toString();
        if (!textoMensagem.isEmpty()){
            if (usuarioDestinatario != null){
                Mensagem mensagem = new Mensagem();
                mensagem.setIdUsuario( idUsuarioRemetente );
                mensagem.setMensagem( textoMensagem );

                //Salvar mensagem para o remetente
                salvarMensagem(idUsuarioRemetente, idUsuarioDestinatario, mensagem);

                //Salvar mensagem para o destinatario
                salvarMensagem(idUsuarioDestinatario, idUsuarioRemetente, mensagem);

                //Salvar conversa remetente
                salvarConversa(idUsuarioRemetente, idUsuarioDestinatario, usuarioDestinatarioConversas, mensagem, false);
                //Salvar conversa remetente
                salvarConversa(idUsuarioDestinatario, idUsuarioRemetente, usuarioRemetenteConversas, mensagem, false);

            }else{
                for (Usuario membro : grupos.getMembros()){
                    String idRemetenteGrupo = Base64Custom.codificarBase64(membro.getEmail());
                    String idUsuarioLogadoGrupo = UsuarioFirebase.getIdentificadorUsuario();
                    Mensagem mensagem = new Mensagem();
                    mensagem.setIdUsuario(idUsuarioLogadoGrupo);
                    mensagem.setMensagem(textoMensagem);
                    mensagem.setNome(usuarioRemetenteConversas.getNome());
                    //Salvar mensagem para o remetente
                    salvarMensagem(idRemetenteGrupo,idUsuarioDestinatario,mensagem);

                    //Salvar conversa
                    salvarConversa(idRemetenteGrupo, idUsuarioDestinatario, usuarioDestinatarioConversas, mensagem, true);
                }
            }

        }else {
            Toast.makeText(ChatActivity.this, "Digite uma mensagem para enviar!", Toast.LENGTH_LONG).show();
        }
    }
    private void salvarConversa(String idRemetente, String idDestinatario, Usuario usuarioExibicao, Mensagem msg, boolean isGroup){
        Conversa conversaRemetente = new Conversa();
        conversaRemetente.setIdRemetente(idRemetente);
        conversaRemetente.setIdDestinatario(idDestinatario);
        conversaRemetente.setUltimaMensagem(msg.getMensagem());
        if(isGroup){
            conversaRemetente.setIsGroup("true");
            conversaRemetente.setGrupo(grupos);
        }else {
            usuarioDestinatarioConversas = new Usuario();
            usuarioDestinatarioConversas.setIdUsuario(usuarioDestinatario);
            usuarioDestinatarioConversas.setNome(nome);
            usuarioDestinatarioConversas.setEmail(email);
            usuarioDestinatarioConversas.setFotoPerfil(foto);

            conversaRemetente.setUsuarioExibicao(usuarioDestinatarioConversas);
            conversaRemetente.setIsGroup("false");

        }
        conversaRemetente.salvar();
    }
    // SALVAR MENSAGEM
    private void salvarMensagem(String idRemetente, String idDestinatario, Mensagem msg){
        //DatabaseReference database = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference mensagemRef = database.child("mensagens");

        mensagemRef.child(idRemetente).child(idDestinatario).push().setValue(msg);

        //Limpar texto
        editMensagem.setText("");

    }
    @Override
    protected void onStart() {
        super.onStart();
        recuperarMensagens();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mensagensRef != null && childEventListenerMensagens != null) {
            mensagensRef.removeEventListener(childEventListenerMensagens);
        }
    }
    // RECUPERAR MENSAGENS
    private void recuperarMensagens(){
        mensagens.clear();
        childEventListenerMensagens = mensagensRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Mensagem mensagem = dataSnapshot.getValue( Mensagem.class );
                mensagens.add( mensagem );
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }
}