package com.example.organize.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.example.organize.adapter.AdapterMovimentacao;
import com.example.organize.config.ConfiguracaoFirebase;
import com.example.organize.helper.Base64Custom;
import com.example.organize.helper.Movimentacao;
import com.example.organize.model.Usuario;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.organize.databinding.ActivityPrincipalBinding;

import com.example.organize.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PrincipalActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityPrincipalBinding binding;

    private DatabaseReference usuarioRef;
    private DatabaseReference movimentacaoRef = ConfiguracaoFirebase.getFirebaseDatabase();
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private CalendarView  calendarView;
    private TextView textoSaudacao, textoSaldo;
    private Double despesaTotal = 0.0;
    private Double receitaTotal = 0.0;
    private Double resumoUsuario = 0.0;
    private ValueEventListener valueEventListenerUsuario;
    private RecyclerView recyclerView;
    private AdapterMovimentacao adapterMovimentacao;
    private List<Movimentacao> movimentacoes = new ArrayList<>();
    private Movimentacao movimentacao;
    private String mesAnoSelecionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPrincipalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("Organizze");

        calendarView = findViewById(R.id.calendarView);
        textoSaldo = findViewById(R.id.textSaldo);
        textoSaudacao = findViewById(R.id.textSaudacao);
        recyclerView = findViewById(R.id.recycleMovimentos);

        //Configurar Adapter
        adapterMovimentacao = new AdapterMovimentacao(movimentacoes, this);

        //Configurar RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapterMovimentacao);
        configuraCalendarView();
        swipe();


        binding.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        binding.menuDespesa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PrincipalActivity.this, DespesaActivity.class));

            }
        });
        binding.menuReceita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PrincipalActivity.this, ReceitaActivity.class));

            }
        });
    }
    public void swipe(){
        ItemTouchHelper.Callback itemTouch = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                int dragFlags = ItemTouchHelper.ACTION_STATE_IDLE;
                int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                excluirMovimentacao(viewHolder);
            }
        };
        new ItemTouchHelper(itemTouch).attachToRecyclerView(recyclerView);
    }

    public void excluirMovimentacao(RecyclerView.ViewHolder viewHolder){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Excluir movimentação da conta");
        alertDialog.setMessage("Tem certeza que deseja excluir permanentemente a movimentação da sua conta?");
        alertDialog.setCancelable(false);

        alertDialog.setPositiveButton("Concluir", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int position = viewHolder.getAbsoluteAdapterPosition();
                movimentacao = movimentacoes.get(position);
                String emailUsuario = autenticacao.getCurrentUser().getEmail();
                String idUsuario = Base64Custom.codificarBase64(emailUsuario);
                DatabaseReference ref = movimentacaoRef
                        .child("movimentacao")
                        .child(idUsuario)
                        .child(mesAnoSelecionado);
                ref.child(movimentacao.getKey()).removeValue();
                adapterMovimentacao.notifyItemRemoved(position);
                atualizarSaldo();
            }
        });
        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(PrincipalActivity.this, "Cancelado", Toast.LENGTH_SHORT).show();
                adapterMovimentacao.notifyDataSetChanged();
            }
        });
        AlertDialog alert = alertDialog.create();
        alert.show();
    }
    public void atualizarSaldo(){
        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);
        usuarioRef = firebaseRef.child("usuarios").child(idUsuario);
        if(movimentacao.getTipo().equals("r")){
            receitaTotal = receitaTotal - movimentacao.getValor();
            usuarioRef.child("receitaTotal").setValue(receitaTotal);
        }
        if(movimentacao.getTipo().equals("d")){
            despesaTotal = despesaTotal - movimentacao.getValor();
            usuarioRef.child("despesaTotal").setValue(despesaTotal);
        }
    }

    public void recuperarMovimentacoes(){
            String emailUsuario = autenticacao.getCurrentUser().getEmail();
            String idUsuario = Base64Custom.codificarBase64(emailUsuario);

/*
        movimentacaoRef.child("movimentacao").child(idUsuario).child(mesAnoSelecionado).addValueEventListener(new ValueEventListener() {
*/
        DatabaseReference ref = movimentacaoRef
                .child("movimentacao")
                .child(idUsuario)
                .child(mesAnoSelecionado);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                movimentacoes.clear();
                for (DataSnapshot dados : snapshot.getChildren()) {
                    Movimentacao movimentacao = dados.getValue(Movimentacao.class);
                    movimentacao.setKey(dados.getKey());
                    movimentacoes.add(movimentacao);
                }
                adapterMovimentacao.notifyDataSetChanged();
                Log.i("MOVIMENTACOES-MES",
                        "Mês/Ano: " + mesAnoSelecionado +
                                " | Qtd: " + movimentacoes.size());            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ERRO", "Erro ao buscar movimentações");
            }
        });
    }


    public void recuperarResumo(){
        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);
        usuarioRef = firebaseRef.child("usuarios").child(idUsuario);

        Log.i("Evento", "Evento foi adicionado!");
        valueEventListenerUsuario = usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Usuario usuario = snapshot.getValue(Usuario.class);
                despesaTotal = usuario.getDespesaTotal();
                receitaTotal = usuario.getReceitaTotal();
                resumoUsuario = receitaTotal - despesaTotal;

                DecimalFormat decimalFormat = new DecimalFormat("0.##");
                String resultadoFormatado = decimalFormat.format(resumoUsuario);

                textoSaudacao.setText("Olá, " + usuario.getNome());
                textoSaldo.setText("R$ " + resultadoFormatado);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void configuraCalendarView(){

            // 1️⃣ Pega a data atual visível no calendário
            long dataEmMillis = calendarView.getDate();

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(dataEmMillis);

        int dia = calendar.get(Calendar.DAY_OF_MONTH);
        int mes = calendar.get(Calendar.MONTH) + 1; // começa em 0
        int ano = calendar.get(Calendar.YEAR);

            mesAnoSelecionado = mes + "" + ano;

            Log.i("CALENDARIO", "Mês inicial: " + mesAnoSelecionado);

            // 2️⃣ Listener para qualquer mudança no calendário

            calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                @Override
                public void onSelectedDayChange( CalendarView view, int year, int month, int dayOfMonth) {

                    int mesSelecionado = month + 1;
                    mesAnoSelecionado = mesSelecionado + "" + year;

                    Log.i("CALENDARIO", "Mês alterado: " + mesAnoSelecionado);

                    // Aqui você pode chamar seu filtro
                    recuperarMovimentacoes();
                }
            });


        /*long dataEmMillis = calendarView.getDate();

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dataEmMillis);

        int mes = calendar.get(Calendar.MONTH) + 1; // começa em 0
        int ano = calendar.get(Calendar.YEAR);

        mesAnoSelecionado = mes + "" + ano;
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                // Mudar o texto para o mês e ano selecionado
                mesAnoSelecionado = String.format("%02d%d", (month + 1), year);
                recuperarMovimentacoes();
                Log.i("MES", "Mes: " + mesAnoSelecionado);

            }
        });*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sair, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.itemSair){
            AlertDialog.Builder dialog = new AlertDialog.Builder(PrincipalActivity.this);
            dialog.setTitle("Deseja sair?");
            dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    autenticacao.signOut();
                    startActivity(new Intent(PrincipalActivity.this, MainActivity.class));
                    finish();
                }
            });
            dialog.setNegativeButton("Não", null);
            dialog.show();

        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onStart() {
        super.onStart();
        recuperarResumo();
        recuperarMovimentacoes();
    }
    @Override
    protected void onStop(){
        super.onStop();
        usuarioRef.removeEventListener(valueEventListenerUsuario);
        Log.i("Evento", "Evento foi removido!");

    }

}