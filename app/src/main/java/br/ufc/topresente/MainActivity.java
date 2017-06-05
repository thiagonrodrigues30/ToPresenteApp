package br.ufc.topresente;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity {

    //private GregorianCalendar date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private void salvarPresenca(String codAula){
        Presenca p = new Presenca();

        // TODO Mudar pro id salvo na sharedPreferencesx
        Integer idUsuario = 1;
        String date = getAtualDateString();
        //String date = "2017-05-18";
        Log.d("MainActivity", date);

        p.setCodAula(codAula);

    }

    public String getAtualDateString(){
        //String formData = date.get(Calendar.DAY_OF_MONTH) + "/" + date.get(Calendar.MONTH) + "/" + date.get(Calendar.YEAR);
        GregorianCalendar date = new GregorianCalendar();
        String formData = date.get(Calendar.YEAR) + "-" + (date.get(Calendar.MONTH) + 1) + "-" + date.get(Calendar.DAY_OF_MONTH)
                + " " + date.get(Calendar.HOUR_OF_DAY) + ":" + date.get(Calendar.MINUTE) + ":" + date.get(Calendar.SECOND);
        return formData;


        /*
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date date = null;
        try {
            date = sdf.parse("2015-01-24 17:39:50.000");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //System.out.println(date.getTime());
        return Long.toString(date.getTime());
        */

        //Date date = new Date();
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

        String d = getAtualDateString();
        Log.d("Date", d);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Log.d("MainActivity", "Cancelled scan");
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                String codAula = result.getContents();
                Log.d("MainActivity", "Scanned");
                Toast.makeText(this, "Scanned: " + codAula, Toast.LENGTH_LONG).show();
                salvarPresenca(codAula);
            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
