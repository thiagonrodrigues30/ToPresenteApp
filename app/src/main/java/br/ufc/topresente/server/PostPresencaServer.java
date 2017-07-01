package br.ufc.topresente.server;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import br.ufc.topresente.LoginActivity;
import br.ufc.topresente.MainActivity;
import br.ufc.topresente.Presenca;
import br.ufc.topresente.PresencaDAO;
import br.ufc.topresente.Statics;

/**
 * Created by Thiago on 30/06/2017.
 */
public class PostPresencaServer  extends AsyncTask<String, Void, Void> {


    Context context;
    int status;

    PresencaDAO presencaDAO;

    public PostPresencaServer(Context context){
        this.context = context;

        this.presencaDAO = new PresencaDAO(context);
    }

    @Override
    protected Void doInBackground(String... params) {

        List<Presenca> list = presencaDAO.listOffline();

        String jsonParam = createJsonParam(list);

        Log.d("json: ", jsonParam);


        // Inserindo no servidor
        String jsonStr = null;

        // Fazendo requisição para o web service pelo metodo estatico httpPost
        jsonStr = WebRequest.httpPost(Statics.POST_PRESENCA_SERVER_URL, jsonParam);

        Log.d("Response: ", "Servidor Presenca: " + jsonStr);

        // Transforma a string JSON em objeto e salva o status
        parseJson(jsonStr);

        return null;
    }

    public void parseJson(String json){
        try {
            JSONObject jsonObj = new JSONObject(json);

            if(jsonObj.getInt("status") == 1)
            {
                this.status = 1;
            }
            else
            {
                this.status = 0;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public String createJsonParam(List<Presenca> presencasList) {
        JSONArray json = new JSONArray();

        try {

            for(int i = 0; i < presencasList.size(); i++)
            {
                Presenca presenca = presencasList.get(i);

                JSONObject jObj = new JSONObject();
                jObj.put("id_usuario", presenca.getIdUsuario());
                jObj.put("codaula", presenca.getCodAula());
                jObj.put("date", presenca.getHora());

                json.put(jObj);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json.toString();
    }

    protected void onPostExecute(Void result) {

        ((MainActivity)context).resultPostPresencas(this.status);
    }


}

