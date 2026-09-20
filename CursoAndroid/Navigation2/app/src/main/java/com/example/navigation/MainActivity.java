package com.example.navigation;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener((view) -> {
            enviarEmail();
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_principal, R.id.nav_servicos, R.id.nav_clientes,
                R.id.nav_sobre, R.id.nav_contato)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    public void enviarEmail(){
        String celular = "tel:19997650917";
        String imagem = "https://th.bing.com/th/id/R.6641156fbf18f16fe575dfa1d0c1350a?rik=bZ%2bzyCX7X2udFA&pid=ImgRaw&r=0";
        String endereco = "https://www.google.com/maps/place/Parque+Ibirapuera+-+Port%C3%B5es+1+e+2/@-23.5864413,-46.6575548,17z/data=!4m14!1m7!3m6!1s0x94ce59f1069d11d1:0xcb936109af9ce541!2sParque+Ibirapuera!8m2!3d-23.5859595!4d-46.6584346!16zL20vMDk3X3E2!3m5!1s0x94ce596942c1df0b:0xc715fbb0a1ea2faa!8m2!3d-23.5857896!4d-46.6555813!16s%2Fg%2F11pkbwc118?entry=ttu&g_ep=EgoyMDI1MTIwMS4wIKXMDSoASAFQAw%3D%3D";
        //abre o telefone ja com o numero digitado para fazer ligação
        //Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(celular));
        // Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(imagem));
        //Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(endereco));
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"atendimento@atmconsultoria.com.br"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Contato pelo App");
        intent.putExtra(Intent.EXTRA_TEXT, "Mensagem automática");

        intent.setType("message/rfc822");//email
        //intent.setType("text/pain");//apps de email
        startActivity(Intent.createChooser(intent, "Escolha um app de e-mail"));
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
