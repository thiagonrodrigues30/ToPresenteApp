package br.ufc.topresente;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import br.ufc.topresente.server.GetLoginServer;
import br.ufc.topresente.server.PostPresencaServer;

public class MainActivity extends AppCompatActivity {

    private PresencaDAO presencaDAO;
    int num = 0;
    TextView tvList;

    Integer userId;
    String userName;
    String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.presencaDAO = new PresencaDAO(this);

        this.tvList = (TextView) findViewById(R.id.textview_pendentes);
        mostrarAulasPendentes();

        setarDadosUsuario();
    }

    private void salvarPresenca(String codAula){


        Integer idUsuario = this.userId;
        String date = getAtualDateString();
        Log.d("MainActivity", date);

        Presenca p = new Presenca(idUsuario, codAula, date);

        if(presencaDAO.retrieve(codAula) != null)
        {
            Toast.makeText(this, "Aula já inserida", Toast.LENGTH_LONG).show();
        }
        else
        {
            long res = presencaDAO.create(p);

            if(res == -1)
            {
                Toast.makeText(this, "Não foi possivel salvar a presença", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(this, "Presença salva com sucesso", Toast.LENGTH_LONG).show();
            }
        }

        mostrarAulasPendentes();
    }

    public String getAtualDateString(){


        Calendar cal = Calendar.getInstance();

        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // Output "Wed Sep 26 14:23:28 EST 2012"

        String formatted = format1.format(cal.getTime());
        // Output "2012-09-26"


        return formatted;
    }

    public void onClickLerQrCode(View v){

        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scan");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(false);
        integrator.initiateScan();

    }

    private void mostrarAulasPendentes(){
        StringBuffer listText = new StringBuffer();

        List<Presenca> list = presencaDAO.listOffline();

        listText.append("( ");

        if(list == null)
        {
            listText.append("0");
        }
        else
        {
            listText.append(list.size());
        }

        listText.append(" )");

        this.tvList.setText(listText.toString());
    }

    public void onClickSalvarServidor(View v){

        List<Presenca> list = presencaDAO.listOffline();

        if(list == null)
        {
            Toast.makeText(this, "Nenhuma Presença Pendente", Toast.LENGTH_LONG).show();
        }
        else
        {
            new PostPresencaServer(this).execute();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Log.d("MainActivity", "Cancelled scan");
                Toast.makeText(this, "Cancelado", Toast.LENGTH_LONG).show();
            } else {
                String codAula = result.getContents();
                Log.d("MainActivity", "Scanned");
                //Toast.makeText(this, "Scanned: " + codAula, Toast.LENGTH_LONG).show();
                salvarPresenca(codAula);
            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void resultPostPresencas(int op){
        if(op == 1)
        {
            presencaDAO.updateOnline();
            mostrarAulasPendentes();
            Toast.makeText(this, "Todas Presenças Foram Salvas no Servidor", Toast.LENGTH_LONG).show();
        }
        else
        {
            // TODO mudar mensagem para erros vindos do servidor
            Toast.makeText(this, "Não foi possivel salvar as presenças no servidor, tente novamente mais tarde", Toast.LENGTH_LONG).show();
        }
    }

    public void setarDadosUsuario(){
        SharedPreferences sharedPreferences = getSharedPreferences(Statics.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        this.userId = sharedPreferences.getInt("USER_ID", 0);
        this.userName = sharedPreferences.getString("USER_NAME", "");
        this.userEmail = sharedPreferences.getString("USER_EMAIL", "");
    }
}
