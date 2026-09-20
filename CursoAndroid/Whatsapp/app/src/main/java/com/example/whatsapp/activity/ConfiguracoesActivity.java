package com.example.whatsapp.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.whatsapp.R;
import com.example.whatsapp.helper.Base64Custom;
import com.example.whatsapp.helper.Permissao;
import com.example.whatsapp.helper.UsuarioFirebase;
import com.example.whatsapp.model.Usuario;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class ConfiguracoesActivity extends AppCompatActivity {

    private String[] permissoesNecessarias = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA
    };
    private ImageButton imageButtonCamera, imageButtonGaleria, imageButtonExcluir;
    private static final int SELECAO_CAMERA = 100;
    private static final int SELECAO_GALERIA = 200;
    private static final int SELECAO_EXCLUIR = 300;
    private CircleImageView circleImageView;
    private EditText editPerfilNome;
    private ImageView imageAtualizarNome;
    private Usuario usuarioLogado = UsuarioFirebase.getDadosUsuarioLogado();
    private String identificadorUsuario = UsuarioFirebase.getIdentificadorUsuario();;
    //private StorageReference storageReference = ConfiguracaoFirebase.getFirebaseStorage();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes);

        //Validar permissões
        Permissao.validarPermissoes(permissoesNecessarias, this,1);

        imageButtonCamera = findViewById(R.id.imageButtonCamera);
        imageButtonGaleria = findViewById(R.id.imageButtonGaleria);
        imageButtonExcluir = findViewById(R.id.imageButtonExcluir);
        editPerfilNome = findViewById(R.id.editPerfilNome);
        imageAtualizarNome = findViewById(R.id.imageAtualizarNome);

        circleImageView = findViewById(R.id.circleImageViewFotoPerfil);
        // Carregar foto de perfil salva no banco ao abrir a tela
        DatabaseReference usuarioRef = FirebaseDatabase.getInstance().getReference("usuarios").child(identificadorUsuario);
        usuarioRef.child("fotoPerfil").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String imagemBase64 = snapshot.getValue(String.class);
                    if (imagemBase64 != null && !imagemBase64.isEmpty()) {
                        Bitmap bitmap = Base64Custom.decodificarBase64Imagem(imagemBase64);
                        circleImageView.setImageBitmap(bitmap);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                        // opcional: tratar erro
                    }
        });

        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Configurações");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //recuperar dados do usuario
        FirebaseUser usuario = UsuarioFirebase.getUsuarioAtual();
        /*Uri url = usuario.getphotoUrl();
        if(url != null){
        }else{
            circleImageView.setImageResource(R.drawable.padrao);
        }*/

        editPerfilNome.setText(usuario.getDisplayName());
        imageButtonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (i.resolveActivity(getPackageManager()) != null){
                    startActivityForResult(i,SELECAO_CAMERA);
                }
            }
        });
        imageButtonGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if (i.resolveActivity(getPackageManager()) != null){
                    startActivityForResult(i,SELECAO_GALERIA);
                }
            }
        });
        imageButtonExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(ConfiguracoesActivity.this);
                dialog.setMessage("Deseja excluir foto de perfil?");
                dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onActivityResult(SELECAO_EXCLUIR, RESULT_OK, null);
                    }
                });
                dialog.setNegativeButton("Cancelar", null);
                dialog.show();
            }
        });
        imageAtualizarNome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = editPerfilNome.getText().toString();
                boolean retorno = UsuarioFirebase.atualizarNomeUsuario(nome);
                if (retorno){
                    usuarioLogado.setNome(nome);
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("usuarios").child(identificadorUsuario);
                    ref.child("nome").setValue(nome);
                    Toast.makeText(ConfiguracoesActivity.this, "Nome alterado com sucesso!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            Bitmap imagem = null;
            try{
                switch (requestCode){
                    case SELECAO_CAMERA:
                        imagem = (Bitmap) data.getExtras().get("data");
                        break;
                    case SELECAO_GALERIA:
                        Uri localImagemSelecionada = data.getData();
                        imagem = MediaStore.Images.Media.getBitmap(getContentResolver(), localImagemSelecionada);
                        break;
                    case SELECAO_EXCLUIR:
                        DatabaseReference refExcluir = FirebaseDatabase.getInstance().getReference("usuarios").child(identificadorUsuario);
                        // Remove a foto do banco
                        refExcluir.child("fotoPerfil").removeValue();
                        // Exibe imagem padrão
                        circleImageView.setImageResource(R.drawable.padrao);
                        Toast.makeText(this, "Foto de perfil removida!", Toast.LENGTH_SHORT).show();
                        break;
                }
                if(imagem != null){
                    //STORAGE
                    /*circleImageView.setImageBitmap(imagem);
                    //recuperardados da imagem para o firebase
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.JPEG, 70,baos);
                    byte[] dadosImage = baos.toByteArray();
                    //Salvar imagem no firebase
                    StorageReference imagemRef = storageReference.child("imagens").child("perfil").child(identificadorUsuario + ".jpeg");
                    UploadTask uploadTask = imagemRef.putBytes(dadosImagem);

                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ConfiguracoesActivity.this, "Erro ao fazer upload da imagem", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccesListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(ConfiguracoesActivity.this, "Sucesso ao fazer upload da imagem", Toast.LENGTH_SHORT).show();
                        }
                    });

                    imagemRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>(){
                        public void onComplete(@android.annotation.NonNull Task<Uri> task){
                            Uri url = task.getResult();
                        }
                    });*/
                    //DATABASE
                    String imagemBase64 = Base64Custom.codificarBase64Imagem(imagem);
                    DatabaseReference ref = FirebaseDatabase.getInstance()
                            .getReference("usuarios")
                            .child(identificadorUsuario);

                    ref.child("fotoPerfil").setValue(imagemBase64);
                    usuarioLogado.setFotoPerfil(imagemBase64);
                    Toast.makeText(ConfiguracoesActivity.this,"Foto de perfil adicionada!",Toast.LENGTH_SHORT).show();

                    Bitmap bitmap = Base64Custom.decodificarBase64Imagem(imagemBase64);
                    circleImageView.setImageBitmap(bitmap);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for(int permissaoResultado : grantResults){
            if (permissaoResultado == PackageManager.PERMISSION_DENIED){
                alertaValidacaoPermissao();
            }
        }
    }
    private void alertaValidacaoPermissao(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissões Negadas");
        builder.setMessage("Para utilizar o app é necessário aceitar as permissões");
        builder.setCancelable(false);
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        //builder.create();
        builder.show();
    }
}