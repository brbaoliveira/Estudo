package com.example.youtube;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.youtube.api.DataService;
import com.example.youtube.model.VideoResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private Retrofit retrofit;

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

        retrofit = new Retrofit.Builder()
                .baseUrl("https://www.googleapis.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        recuperarVideo();
    }

    private void recuperarVideo() {

        DataService dataService = retrofit.create(DataService.class);

        String apiKey = "AIzaSyDvaBsUo9opLGRzqxi9pazyQT3uFJ6MRCc";
        String videoId = "dQw4w9WgXcQ";

        Call<VideoResponse> call = dataService.recuperarVideo(
                "snippet",
                videoId,
                apiKey
        );

        call.enqueue(new Callback<VideoResponse>() {

            @Override
            public void onResponse(
                    Call<VideoResponse> call,
                    Response<VideoResponse> response) {

                if (response.isSuccessful() && response.body() != null) {

                    VideoResponse resultado = response.body();

                    if (!resultado.getItems().isEmpty()) {

                        VideoResponse.Item video =
                                resultado.getItems().get(0);

                        Log.d("YOUTUBE", "ID: " + video.getId());
                        Log.d("YOUTUBE",
                                "Título: " + video.getSnippet().getTitle());

                        Log.d("YOUTUBE",
                                "Descrição: " +
                                        video.getSnippet().getDescription());
                    }

                } else {

                    Log.e("YOUTUBE",
                            "Erro HTTP: " + response.code());
                }
            }

            @Override
            public void onFailure(
                    Call<VideoResponse> call,
                    Throwable throwable) {

                Log.e("YOUTUBE",
                        "Erro: " + throwable.getMessage());
            }
        });
    }
}