package com.example.frasesdodia;

import static kotlin.random.RandomKt.nextInt;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void gerarNovaFrase(View view){

        String[] frases = {
                "Você é capaz de muito mais do que imagina — só precisa dar o primeiro passo.",
                "Cada erro te aproxima do acerto. Não pare.",
                "Grandes conquistas começam com pequenas atitudes diárias.",
                "Você já superou dias difíceis antes — e vai continuar vencendo.",
                "Quando bater a dúvida, lembre-se do motivo que te fez começar.",
                "O esforço que você coloca hoje constrói o futuro que você merece.",
                "Você não precisa ser perfeita, só precisa continuar evoluindo.",
                "Mesmo devagar, avance. Movimento é vitória.",
                "A versão do futuro vai agradecer pela força que você está colocando agora.",
                "Acredite: você tem tudo para chegar onde sonha."
        };
        int numero = new Random().nextInt(10);// 0 1 2 3

        TextView texto = findViewById(R.id.textResultado);
        texto.setText(frases[numero]);

    }
}