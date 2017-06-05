package br.ufc.topresente;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void onClickLogar(View v){
        Intent intent = new Intent();
        intent.setAction("br.ufc.topresente.MAINLOGED");
        intent.setComponent(null);

        startActivity(intent);
    }

    /*
    private void chamarLogedActivity(String userName){
        Intent intent = new Intent();
        intent.setAction("br.ufc.dc.dspm.action.LOGINTWEETER");
        intent.setComponent(null);
        intent.addCategory("br.ufc.dc.dspm.category.CATEGORIA");
        intent.putExtra("USER", userName);

        startActivity(intent);
    }
    */
}
