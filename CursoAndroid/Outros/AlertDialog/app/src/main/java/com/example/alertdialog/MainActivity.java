package com.example.alertdialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
    public void abrirDialog(View view){
        //instanciar AlertDialog
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        //Configurar titulo e mensagem
        dialog.setTitle("Titulo da dialog");
        dialog.setMessage("Mensagem da Dialog");

        //Configurar cancelamento
        dialog.setCancelable(false);

        //Configurar icone
        dialog.setIcon(android.R.drawable.ic_btn_speak_now);

        //Configura ações para sim e não
        dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "Executar ação ao clicar no botão Sim", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "Executar ação ao clicar no botão Não", Toast.LENGTH_SHORT).show();
            }
        });
        //Criar e exibir AlertDialog
        dialog.create();
        dialog.show();
    }

}