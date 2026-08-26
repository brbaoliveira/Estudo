package com.example.requisicoeshttp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.requisicoeshttp.api.CEPService;
import com.example.requisicoeshttp.api.DataService;
import com.example.requisicoeshttp.model.CEP;
import com.example.requisicoeshttp.model.Foto;
import com.example.requisicoeshttp.model.Postagem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private TextView tvResultado;
    private EditText etDigitaCep;
    private Retrofit retrofit;
    private RecyclerView recyclerView;
    private List<Foto> listaFotos = new ArrayList<>();
    private ProgressBar progressBar;
    private DataService service;


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

        tvResultado = findViewById(R.id.tvResultado);
        etDigitaCep = findViewById(R.id.etDigitaCep);
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);

        retrofit = new Retrofit.Builder()
                //.baseUrl("https://viacep.com.br/ws/")
                .baseUrl("https://jsonplaceholder.typicode.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(DataService.class);
    }

    public void recuperarDados(View v) {

        /*String cep = String.valueOf(etDigitaCep.getText());
        if (cep.isEmpty()) {
            Toast.makeText(this, "CEP não informado!", Toast.LENGTH_SHORT).show();
            return;
        }*/

        //recuperarCEPRetrofit(cep);
        //recuperarListaRetrofit();
        //salvarPostagem();
        //atualizarPostagem();
        removerPostagem();

        /*MyTask task = new MyTask();
        String urlApi = "https://blockchain.info/latestblock";
        String cep = String.valueOf(etDigitaCep.getText())*//*"01001000"*//*;
        String urlCep = "https://viacep.com.br/ws/" + cep + "/json/";
        String urlTeste = "http://192.168.1.150:8080/book";
        task.execute(urlTeste);*/
    }

    public void removerPostagem() {
        Call<Void> call = service.removerPostagem(2);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    tvResultado.setText("Status: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {

            }
        });
    }

    public void atualizarPostagem() {
        Postagem postagem = new Postagem("1234", null, "Corpo postagem");
        //Call<Postagem> call = service.atualizarPostagem(2, postagem);
        Call<Postagem> call = service.atualizarPostagemPatch(2, postagem);
        call.enqueue(new Callback<Postagem>() {
            @Override
            public void onResponse(Call<Postagem> call, Response<Postagem> response) {
                if (response.isSuccessful()) {
                    Postagem postagemResposta = response.body();
                    tvResultado.setText("Código: " + response.code() +
                            "\nid: " + postagemResposta.getId() +
                            "\nuserId: " + postagemResposta.getUserId() +
                            "\ntitulo: " + postagemResposta.getTitle() +
                            "\nbody: " + postagemResposta.getBody());
                }
            }

            @Override
            public void onFailure(Call<Postagem> call, Throwable throwable) {

            }
        });
    }

    public void salvarPostagem() {
        Postagem postagem = new Postagem("1234", "Título Postagem!", "Corpo postagem");
        Call<Postagem> call = service.salvarPostagens(postagem);
        call.enqueue(new Callback<Postagem>() {
           @Override
           public void onResponse(Call<Postagem> call, Response<Postagem> response) {
               if (response.isSuccessful()) {
                   Postagem postagemResposta = response.body();
                   tvResultado.setText("Código: " + response.code() +
                           "\nid: " + postagemResposta.getId() +
                           "\ntitulo: " + postagemResposta.getTitle());
               }
           }

           @Override
           public void onFailure(Call<Postagem> call, Throwable throwable) {

           }
       });
    }

        public void recuperarListaRetrofit() {
        progressBar.setVisibility(View.VISIBLE);
        Call<List<Foto>> call = service.recuperarFotos();
        call.enqueue(new Callback<List<Foto>>() {
            @Override
            public void onResponse(Call<List<Foto>> call, Response<List<Foto>> response) {

                Log.d("RETROFIT", "onResponse chamado!");
                Log.d("RETROFIT", "Código HTTP: " + response.code());

                if (response.isSuccessful()) {

                    listaFotos = response.body();

                    if (listaFotos != null) {

                        Log.d("RETROFIT", "Quantidade: " + listaFotos.size());

                        /*for (Foto foto : listaFotos) {
                            Log.d("RESULTADO", foto.retornaFoto());
                        }*/

                        AdapterLista adapter = new AdapterLista(listaFotos);

                        recyclerView.setLayoutManager(
                                new LinearLayoutManager(MainActivity.this)
                        );

                        recyclerView.setAdapter(adapter);

                        recyclerView.setVisibility(View.VISIBLE);
                    }

                } else {

                    Log.e("RETROFIT", "Erro HTTP: " + response.code());
                    Log.e("RETROFIT", "Mensagem: " + response.message());
                }

                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<Foto>> call, Throwable throwable) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Não foi possivel exibir a lista!", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void recuperarCEPRetrofit(String cep) {
        CEPService cepService = retrofit.create(CEPService.class);
        Call<CEP> call = cepService.recuperarCEP(cep);
        call.enqueue(new Callback<CEP>() {
            @Override
            public void onResponse(Call<CEP> call, Response<CEP> response) {
                if(response.isSuccessful()) {
                    CEP cep = response.body();
                    tvResultado.setText(cep.retornaCEPCompleto());
                }
            }

            @Override
            public void onFailure(Call<CEP> call, Throwable throwable) {

            }
        });
    }

    class MyTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String stringUrl = strings[0];
            InputStream inputStream = null;
            InputStreamReader inputStreamReader = null;
            StringBuffer buffer = null;
            try {
                URL url = new URL(stringUrl);
                HttpURLConnection conexao = (HttpURLConnection) url.openConnection();

                // Recupera os dados em Bytes
                inputStream = conexao.getInputStream();

                // InputStreamReader lê os dadsos em Bytes e decodifica para caracteres
                inputStreamReader = new InputStreamReader(inputStream);

                // Objeto utilizado para leitura dos caracteres do InputStreamReader
                BufferedReader reader = new BufferedReader(inputStreamReader);

                buffer = new StringBuffer();
                String linha = "";
                while((linha = reader.readLine()) != null) {
                    buffer.append(linha);
                };
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return buffer.toString();
        }

        @Override
        protected void onPostExecute(String resultado) {
            super.onPostExecute(resultado);
            String cep = null;
            String logradouro = null;
            String complemento = null;
            String unidade = null;
            String bairro = null;
            String localidade = null;
            String uf = null;
            String estado = null;
            String regiao = null;
            String ibge = null;
            String gia = null;
            String ddd = null;
            String siafi= null;

            /*try {
                JSONObject jsonObject = new JSONObject(resultado);
                cep = jsonObject.getString("cep");
                logradouro = jsonObject.getString("logradouro");
                complemento = jsonObject.getString("complemento");
                unidade = jsonObject.getString("unidade");
                bairro = jsonObject.getString("bairro");
                localidade = jsonObject.getString("localidade");
                uf = jsonObject.getString("uf");
                estado = jsonObject.getString("estado");
                regiao = jsonObject.getString("regiao");;
                ibge = jsonObject.getString("ibge");;
                gia = jsonObject.getString("gia");;
                ddd = jsonObject.getString("ddd");;
                siafi = jsonObject.getString("siafi");
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }*/
            tvResultado.setText(resultado);
            /*tvResultado.setText(cep + " / " + logradouro + " / " + complemento + " / " + unidade + " / " + bairro + " / " + localidade + " / "
                    + uf + " / " + estado + " / " + regiao + " / " + ibge + " / " + gia + " / " + ddd + " / " + siafi);*/
        }
    }
}