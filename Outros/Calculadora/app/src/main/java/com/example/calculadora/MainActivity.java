package com.example.calculadora;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    Button btAc, btParenteses, btPorcentagem, btDivisao, btSoma, btSubtracao, btMultiplicacao, btIgual, btDel,
    btUm, btDois, btTres, btQuatro, btCinco, btSeis, btSete, btOito, btNove, btZero;
    TextView tvExibeConta, tvExibeResultado;
    int valor = 0;
    String operacao = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        init();
        configBotoes();

    }

    private void init() {
        btAc            = findViewById(R.id.bt_ac);
        btParenteses    = findViewById(R.id.bt_parenteses);
        btPorcentagem   = findViewById(R.id.bt_porcentagem);
        btDivisao       = findViewById(R.id.bt_div);
        btSoma          = findViewById(R.id.bt_soma);
        btSubtracao     = findViewById(R.id.bt_sub);
        btMultiplicacao = findViewById(R.id.bt_mult);
        btIgual         = findViewById(R.id.bt_igual);
        btDel           = findViewById(R.id.bt_del);

        btUm     = findViewById(R.id.bt_um);
        btDois   = findViewById(R.id.bt_dois);
        btTres   = findViewById(R.id.bt_tres);
        btQuatro = findViewById(R.id.bt_quatro);
        btCinco  = findViewById(R.id.bt_cinco);
        btSeis   = findViewById(R.id.bt_seis);
        btSete   = findViewById(R.id.bt_sete);
        btOito   = findViewById(R.id.bt_oito);
        btNove   = findViewById(R.id.bt_nove);
        btZero   = findViewById(R.id.bt_zero);
    }
    private void configBotoes(){
        btUm.setOnClickListener(v ->     valor = 1);
        btDois.setOnClickListener(v ->   valor = 2);
        btTres.setOnClickListener(v ->   valor = 3);
        btQuatro.setOnClickListener(v -> valor = 4);
        btCinco.setOnClickListener(v ->  valor = 5);
        btSeis.setOnClickListener(v ->   valor = 6);
        btSete.setOnClickListener(v ->   valor = 7);
        btOito.setOnClickListener(v ->   valor = 8);
        btNove.setOnClickListener(v ->   valor = 9);
        btZero.setOnClickListener(v ->   valor = 0);


        btDivisao.setOnClickListener(v ->       operacao = "÷");
        btSoma.setOnClickListener(v ->          operacao = "+");
        btSubtracao.setOnClickListener(v ->     operacao = "-");
        btMultiplicacao.setOnClickListener(v -> operacao = "x");
        btIgual.setOnClickListener(v ->         operacao = "=");
    }

}