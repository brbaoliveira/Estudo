package com.example.listadetarefas.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.listadetarefas.R;
import com.example.listadetarefas.databinding.ActivityAdicionarTarefaBinding;
import com.example.listadetarefas.databinding.ActivityMainBinding;
import com.example.listadetarefas.helper.TarefaDAO;
import com.example.listadetarefas.model.Tarefa;
import com.google.android.material.textfield.TextInputEditText;

public class AdicionarTarefaActivity extends AppCompatActivity {
    private TextInputEditText editText;
    private ActivityAdicionarTarefaBinding binding;
    private Tarefa tarefaAtual;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdicionarTarefaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Ativa a Toolbar
        setSupportActionBar(binding.toolbar);
        //setContentView(R.layout.activity_adicionar_tarefa);
        editText = findViewById(R.id.textTarefa);

        //recuperar tarefa, caso seja edição
        tarefaAtual = (Tarefa) getIntent().getSerializableExtra("tarefaSelecionada");

        //configurar tarefa na caixa de texto
        if( tarefaAtual != null){
            editText.setText(tarefaAtual.getNomeTarefa());
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       getMenuInflater().inflate(R.menu.menu_adicionar_tarefa, menu);

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.itemSalvar){
            //Toast.makeText(AdicionarTarefaActivity.this, "Tarefa salva", Toast.LENGTH_SHORT).show();
            if (item.getItemId() == R.id.itemSalvar){
                //execta açao para o item salvar
                TarefaDAO tarefaDAO = new TarefaDAO(getApplicationContext());

                if (tarefaAtual != null){//edicao
                    String nomeTarefa = editText.getText().toString();
                    if (!nomeTarefa.isEmpty()) {
                        Tarefa tarefa = new Tarefa();
                        tarefa.setNomeTarefa(nomeTarefa);
                        tarefa.setId(tarefaAtual.getId());

                        //atualizar no banco de dados
                        if (tarefaDAO.atualizar(tarefa)){
                            finish();
                            Toast.makeText(getApplicationContext(), "Sucesso ao atualizar tarefa!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Erro ao atualizar tarefa!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }else{//salvar
                    String nomeTarefa = editText.getText().toString();
                    if (!nomeTarefa.isEmpty()){
                        Tarefa tarefa = new Tarefa();
                        tarefa.setNomeTarefa(nomeTarefa);
                        if(tarefaDAO.salvar(tarefa)){
                            finish();
                            Toast.makeText(getApplicationContext(), "Sucesso ao salvar tarefa!", Toast.LENGTH_SHORT).show();
                        } else{
                            Toast.makeText(getApplicationContext(), "Erro ao salvar tarefa!", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }
}