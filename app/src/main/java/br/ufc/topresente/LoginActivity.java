package br.ufc.topresente;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import br.ufc.topresente.server.GetLoginServer;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Mudando fonte do texto logo
        TextView tv = (TextView) findViewById(R.id.tv_logo);
        Typeface face = Typeface.createFromAsset(getAssets(),
                "fonts/Pacifico-Regular.ttf");
        tv.setTypeface(face);

       // goToMainActivity();
    }

    public void onClickLogar(View v){
        TextView tLogin = (TextView) findViewById(R.id.tLogin);
        TextView tSenha = (TextView) findViewById(R.id.tSenha);
        String login = tLogin.getText().toString();
        String senha = tSenha.getText().toString();

        new GetLoginServer(this).execute(login, senha);

    }

    public void goToMainActivity(){
        Intent intent = new Intent();
        intent.setAction("br.ufc.topresente.MAINLOGED");
        intent.setComponent(null);

        startActivity(intent);
    }

    public void resultLogin(int op){
        if(op == 1)
        {
            goToMainActivity();
        }
        else
        {
            // TODO mudar mensagem para erros vindos do servidor
            alert("Não foi possivel logar, tente novamente mais tarde");
        }
    }

    private void alert(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

}
