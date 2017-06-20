package br.ufc.topresente;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import br.ufc.topresente.server.GetLoginServer;

public class MainActivity extends AppCompatActivity {

    private PresencaDAO presencaDAO;
    int num = 0;
    TextView tvList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.presencaDAO = new PresencaDAO(this);

        this.tvList = (TextView) findViewById(R.id.textview_pendentes);
        mostrarAulasPendentes();
    }

    private void salvarPresenca(String codAula){

        // TODO Mudar pro id salvo na sharedPreferencesx
        Integer idUsuario = 1;
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
        GregorianCalendar date = new GregorianCalendar();
        String formData = date.get(Calendar.YEAR) + "-" + (date.get(Calendar.MONTH) + 1) + "-" + date.get(Calendar.DAY_OF_MONTH)
                + " " + date.get(Calendar.HOUR_OF_DAY) + ":" + date.get(Calendar.MINUTE) + ":" + date.get(Calendar.SECOND);
        return formData;
    }

    public void onClickLerQrCode(View v){
        /*
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scan");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(false);
        integrator.initiateScan();
        */

        // TODO deletar depois
        String[] aulas = new String[5];
        aulas[0] = "abcd1234";
        aulas[1] = "aaa111";
        aulas[2] = "bbb222";
        aulas[3] = "ccc444";
        aulas[4] = "ddd555";
        salvarPresenca(aulas[this.num]);
        if(this.num < 4)
        {
            this.num++;
        }
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
        new GetLoginServer().execute("thiago@thiago", "123456");

        List<Presenca> list = presencaDAO.listOffline();

        if(list == null)
        {
            Toast.makeText(this, "Nenhuma Presença Pendente", Toast.LENGTH_LONG).show();
        }
        else
        {
            // TODO Subir presenças para o servidor
            presencaDAO.updateOnline();
            mostrarAulasPendentes();
            Toast.makeText(this, "Todas Presenças Foram Salvas no Servidor", Toast.LENGTH_LONG).show();
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
}
