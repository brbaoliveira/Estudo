package com.example.entendendothreads;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private TextView tvContador;
    private int numero;
    private Handler handler = new Handler();
    private boolean pararExecucao = false;

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

        tvContador = findViewById(R.id.tvContador);
    }
    public void iniciarThread(View v){
        pararExecucao = false;
        //MyThread myThread = new MyThread();
        //myThread.start();

        MyRunnable runnable =  new MyRunnable();
        new Thread(runnable).start();
    }

    public void pararThread(View v){
        pararExecucao = true;
    }

    class MyRunnable implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i <= 15; i++) {
                if (pararExecucao)
                    return;

                numero = i;
                Log.d("Thread", "contador: " + i);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvContador.setText("Contador: " + numero);
                    }
                });

                /*handler.post(new Runnable() {
                    @Override
                    public void run() {
                        btIniciarThread.setText("contador: " + numero);
                    }
                });*/

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
    class MyThread extends Thread {
        @Override
        public void run() {
            for (int i = 0; i <= 15; i++) {
                Log.d("Thread", "contador: " + i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

}