package com.example.pedrapapeloutesoura;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    ImageView btnPedra, btnPapel, btnTesoura, btnResetar;
    int pontosApp = 0, pontosUsuario = 0;


    TextView exibeResultado, exibePontosApp,exibePontosUsuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
/*
        EdgeToEdge.enable(this);
*/
        setContentView(R.layout.activity_main);
       /* ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });*/
        btnPedra = findViewById(R.id.selecionadoPedra);
        btnPapel = findViewById(R.id.selecionadoPapel);
        btnTesoura = findViewById(R.id.selecionadoTesoura);


        exibeResultado = findViewById(R.id.exibeResultado);
        exibePontosApp = findViewById(R.id.exibePontosApp);
        exibePontosUsuario = findViewById(R.id.exibePontosUsuario);

    }
    public void selecionadoPedra(View view){
        this.opcaoSelecionada("pedra");
        ImageView imagemSelecionada = findViewById(R.id.exibeSuaEscolha);
        imagemSelecionada.setBackgroundResource(R.drawable.botao_normal);
        imagemSelecionada.setImageResource(R.drawable.pedra2);
        btnPedra.setBackgroundResource(R.drawable.botao_selecionado);
        btnPapel.setBackgroundResource(R.drawable.botao_normal);
        btnTesoura.setBackgroundResource(R.drawable.botao_normal);
    }
    public void selecionadoPapel(View view){
        this.opcaoSelecionada("papel");
        ImageView imagemSelecionada = findViewById(R.id.exibeSuaEscolha);
        imagemSelecionada.setBackgroundResource(R.drawable.botao_normal);
        imagemSelecionada.setImageResource(R.drawable.papel2);
        btnPedra.setBackgroundResource(R.drawable.botao_normal);
        btnPapel.setBackgroundResource(R.drawable.botao_selecionado);
        btnTesoura.setBackgroundResource(R.drawable.botao_normal);
    }
    public void selecionadoTesoura(View view){
        this.opcaoSelecionada("tesoura");
        ImageView imagemSelecionada = findViewById(R.id.exibeSuaEscolha);
        imagemSelecionada.setBackgroundResource(R.drawable.botao_normal);
        imagemSelecionada.setImageResource(R.drawable.tesoura2);
        btnPedra.setBackgroundResource(R.drawable.botao_normal);
        btnPapel.setBackgroundResource(R.drawable.botao_normal);
        btnTesoura.setBackgroundResource(R.drawable.botao_selecionado);
    }

    public void resetar(View view){
        pontosUsuario = 0;
        pontosApp = 0;
        exibePontosApp.setText("");
        exibePontosUsuario.setText("");
        exibeResultado.setText("");
        ImageView imagemResultado = findViewById(R.id.imageResultado);
        imagemResultado.setImageResource(R.drawable.botao_normal);
        ImageView imagemUsuario = findViewById(R.id.exibeSuaEscolha);
        imagemUsuario.setImageResource(R.drawable.botao_normal);

        btnPedra.setBackgroundResource(R.drawable.botao_normal);
        btnPapel.setBackgroundResource(R.drawable.botao_normal);
        btnTesoura.setBackgroundResource(R.drawable.botao_normal);
    }
    public void opcaoSelecionada(String opcaoSelecionada){
        ImageView imagemResultado = findViewById(R.id.imageResultado);
        int numero  = new Random().nextInt(3);
        String[] opcoes = {"pedra","papel","tesoura"};
        String opcoesApp = opcoes[numero];

        switch (opcoesApp){
            case "pedra":
                imagemResultado.setBackgroundResource(R.drawable.botao_normal);
                imagemResultado.setImageResource(R.drawable.pedra2);
                break;
            case "papel":
                imagemResultado.setBackgroundResource(R.drawable.botao_normal);
                imagemResultado.setImageResource(R.drawable.papel2);
                break;
            case "tesoura":
                imagemResultado.setBackgroundResource(R.drawable.botao_normal);
                imagemResultado.setImageResource(R.drawable.tesoura2);
                break;
        }
        if((opcoesApp == "pedra" && opcaoSelecionada == "tesoura") || (opcoesApp == "papel" && opcaoSelecionada == "pedra") || (opcoesApp == "tesoura" && opcaoSelecionada == "papel")){
            exibeResultado.setText("App ganhou!");
            pontosApp = pontosApp + 1;
            exibePontosApp.setText("Pontos: " + pontosApp);

        }else if ((opcaoSelecionada == "pedra" && opcoesApp == "tesoura") || (opcaoSelecionada == "papel" && opcoesApp == "pedra") || (opcaoSelecionada == "tesoura" && opcoesApp == "papel")){
            exibeResultado.setText("Você ganhou!");
            pontosUsuario = pontosUsuario + 1;
            exibePontosUsuario.setText("Pontos: " + pontosUsuario);
        }else{
            exibeResultado.setText("Empate!");
        }


    }
}