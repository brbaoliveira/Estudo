package com.example.organize.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.example.organize.R;
import com.example.organize.config.ConfiguracaoFirebase;
import com.example.organize.helper.Base64Custom;
import com.example.organize.helper.DateCustom;
import com.example.organize.helper.Movimentacao;
import com.example.organize.model.Usuario;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class ReceitaActivity extends AppCompatActivity {
    private TextInputEditText campoData, campoCategoria, campoDescricao;
    private EditText campoValor;
    private Movimentacao movimentacao;
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private Double receitaTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receita);

        campoData = findViewById(R.id.editDataR);
        campoCategoria = findViewById(R.id.editCategoriaR);
        campoDescricao = findViewById(R.id.editDescricaoR);
        campoValor= findViewById(R.id.editValorR);

        //preencha o campo data com a data atual
        campoData.setText(DateCustom.dataAtual());
        recuperarReceitaTotal();

    }
    public void salvarReceita(View view){
        if (validarCamposReceitas()){
            movimentacao = new Movimentacao();
            String data = campoData.getText().toString();
            Double valorRecuperado = Double.parseDouble(campoValor.getText().toString());
            movimentacao.setValor(valorRecuperado);
            movimentacao.setCategoria(campoCategoria.getText().toString());
            movimentacao.setDescricao(campoDescricao.getText().toString());
            movimentacao.setData(data);
            movimentacao.setTipo("r");

            Double receitaAtualizada = receitaTotal + valorRecuperado;
            atualizarReceita(receitaAtualizada);

            movimentacao.salvar(data);
            startActivity(new Intent(this, PrincipalActivity.class));

        }
    }
    public Boolean validarCamposReceitas(){
        String textValor = campoValor.getText().toString();
        String textData = campoData.getText().toString();
        String textCategoria = campoCategoria.getText().toString();
        String textDescricao = campoDescricao.getText().toString();
        if(!textValor.isEmpty()){
            if(!textData.isEmpty()){
                if(!textCategoria.isEmpty()){
                    if(!textDescricao.isEmpty()){
                        return true;
                    }else {
                        Toast.makeText(ReceitaActivity.this, "Preencha a descrição!", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }else {
                    Toast.makeText(ReceitaActivity.this,"Preencha a categoria!", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }else {
                Toast.makeText(ReceitaActivity.this,"Preencha a data!", Toast.LENGTH_SHORT).show();
                return false;
            }
        }else {
            Toast.makeText(ReceitaActivity.this,"Preencha o valor!", Toast.LENGTH_SHORT).show();
            return false;
        }

    }
    public void recuperarReceitaTotal(){
        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);
        DatabaseReference usuarioRef = firebaseRef.child("usuarios").child(idUsuario);

        usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Usuario usuario = snapshot.getValue(Usuario.class);
                receitaTotal = usuario.getReceitaTotal();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void atualizarReceita(Double receita){
        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);
        DatabaseReference usuarioRef = firebaseRef.child("usuarios").child(idUsuario);

        usuarioRef.child("receitaTotal").setValue(receita);
    }
}