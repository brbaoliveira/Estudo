package com.example.whatsapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;


import com.example.whatsapp.R;
import com.example.whatsapp.adapter.ConversasAdapter;
import com.example.whatsapp.config.ConfiguracaoFirebase;
import com.example.whatsapp.fragment.ContatosFragment;
import com.example.whatsapp.fragment.ConversasFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private SearchView searchView;
    private ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("MAINActivity", "Activity criada");

        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("WhatsApp");
        setSupportActionBar(toolbar);
        Log.i("MAINActivity", "TOOLBAR criada");

        //configurar abas
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(getSupportFragmentManager(), FragmentPagerItems.with(this).add("Conversas", ConversasFragment.class).add("Contatos", ContatosFragment.class).create());
        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
        SmartTabLayout viewPagerTab = findViewById(R.id.viewPagerTab);
        viewPagerTab.setViewPager(viewPager);
        Log.i("MAINActivity", "Abas criadas");
        //searchView = findViewById(R.id.searchPrincipal);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        Log.i("MAINActivity", "Menu criado");

        //configurar botao de pesquisa
        //MenuItem item = menu.findItem(R.id.menuPesquisa);
        //searchView.setMenuItem(item);
        // Pegando o item de pesquisa

        MenuItem item = menu.findItem(R.id.menuPesquisa);
        Log.i("MAINActivity", "Menu pesquisa criado");
        // Certifique-se de que item.getActionView() não é null
        if (item.getActionView() instanceof SearchView) {
            // Inicializando o SearchView
            searchView = (SearchView) item.getActionView();
            searchView.setQueryHint("Pesquisar...");
            // Listener para o searchView
            searchView.setOnSearchClickListener(v -> {
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                }
            });
            searchView.setOnCloseListener(() -> {
                Log.d("SEARCH", "SearchView FECHADO");
                ConversasFragment fragment = (ConversasFragment)getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewPager + ":0");
                fragment.recarregarConversas();
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                return false;
            });
            // Listener de digitação
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false; // não precisa fazer nada ao submeter
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    //verifica se esta pesquisando Conversas ou Contatos a partir da tab que esta ativa
                    switch (viewPager.getCurrentItem()) {
                        case 0:
                            ConversasFragment conversasFragment = (ConversasFragment) getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewPager + ":0");
                            if (conversasFragment != null && newText != null && !newText.isEmpty()) {
                                conversasFragment.pesquisarConversas(newText.toLowerCase());
                            }else if (newText == null || newText.isEmpty()) {
                                // TEXTO LIMPO (X clicado)
                                conversasFragment.recarregarConversas();
                                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                            }
                            break;
                        case 1:
                            ContatosFragment contatosFragment = (ContatosFragment) getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewPager + ":1");
                            if (contatosFragment != null && newText != null && !newText.isEmpty()) {
                                contatosFragment.pesquisarContatos(newText.toLowerCase());
                            }else if (newText == null || newText.isEmpty()) {
                                // TEXTO LIMPO (X clicado)
                                contatosFragment.recarregarContatos();
                                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                            }
                            break;
                    }


                    return true;
                }
            });
            Log.i("MAINActivity", "Search criado");
        }
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menuSair){
            deslogarUsuario();
        }if(item.getItemId() == R.id.menuConfiguracoes){
            startActivity(new Intent(MainActivity.this, ConfiguracoesActivity.class));
            finish();
        } if (item.getItemId() == android.R.id.home) {

            if (searchView != null) {
                searchView.setQuery("", false);
                searchView.clearFocus();
                searchView.onActionViewCollapsed();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void deslogarUsuario(){
        try {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
            alertDialog.setTitle("Deseja sair?");
            alertDialog.setPositiveButton("Sair", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    autenticacao.signOut();
                    finish();
                }
            });
            alertDialog.setNegativeButton("Cancelar", null);
            alertDialog.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}