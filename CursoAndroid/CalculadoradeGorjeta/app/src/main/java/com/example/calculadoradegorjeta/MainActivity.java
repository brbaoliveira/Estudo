package com.example.calculadoradegorjeta;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private EditText editvalor;
    private TextView textPorcentagem, textGorjeta, textTotal;
    private SeekBar seekBarGorjeta;
    private double porcentagem = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editvalor = findViewById(R.id.editValor);
        textGorjeta = findViewById(R.id.textGorjeta);
        textPorcentagem = findViewById(R.id.textPorcentagem);
        textTotal = findViewById(R.id.textTotal);
        seekBarGorjeta = findViewById(R.id.seekBarGorjeta);
        //Adicionar listener SeekBar
        seekBarGorjeta.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                porcentagem = progress;
                textPorcentagem.setText(Math.round(porcentagem) + "%");
                calcular();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }
    public void calcular(){
        String valorRecuperado = editvalor.getText().toString();
        if(valorRecuperado == null || valorRecuperado.equals("")){
            Toast.makeText(getApplicationContext(), "Digite um valor primeiro!", Toast.LENGTH_LONG).show();
        }else{
            //Converter string para double
            double valorDigitado = Double.parseDouble(valorRecuperado);
            //calcula a gorjeta total
            double gorjeta = valorDigitado * (porcentagem / 100);
            double total = valorDigitado + gorjeta;
            //exibe gorjeta e total
            //textGorjeta.setText("R$ " + Math.round(gorjeta));
            textGorjeta.setText("R$ " + gorjeta);
            textTotal.setText("R$ " + total);
        }
    }
}