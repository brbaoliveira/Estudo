package com.example.login;

import android.os.*;
import android.view.*;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btLogin = (Button) findViewById(R.id.btLogin);
        btLogin.setOnClickListener(onClickLogin());
    }
    private View.OnClickListener onClickLogin(){
        return new View.OnClickListener(){
            public void onClick(View v){
                TextView tLogin = (TextView) findViewById(R.id.tLogin);
                TextView tSenha = (TextView) findViewById(R.id.tSenha);
                String login = tLogin.getText().toString();
                String senha = tSenha.getText().toString();
                if ("ricardo".equals(login) && "123".equals(senha)){
                    alert("Bem-vindo, login realizado com sucesso.");
                }else{
                    alert("Login e/ou senha invalidos.");
                }
            }
        };
    }
    private void alert(String s) {
        Toast.makeText(this, s,Toast.LENGTH_SHORT).show();
    }

}