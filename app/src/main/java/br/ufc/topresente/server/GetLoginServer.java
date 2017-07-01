package br.ufc.topresente.server;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Vector;

import br.ufc.topresente.LoginActivity;
import br.ufc.topresente.Statics;

/**
 * Created by Thiago on 19/06/2017.
 */
public class GetLoginServer extends AsyncTask<String, Void, Void> {

    Context context;
    int status;

    public GetLoginServer(Context context){
        this.context = context;
    }

    @Override
    protected Void doInBackground(String... params) {
        String login = params[0];
        String senha = params[1];

        String jsonParam = createJsonParam(login, senha);

        Log.d("json: ", jsonParam);


        // Inserindo no servidor
        String jsonStr = null;

        String url = Statics.GET_LOGIN_SERVER_URL + "?loginJson=" + jsonParam ;

        // Fazendo requisição para o web service pelo metodo estatico httpGet
        jsonStr = WebRequest.httpGet(url);

        Log.d("Response: ", "Servidor Login: " + jsonStr);

        // Transforma a string JSON em objeto e salva no shared preferences
        parseJson(jsonStr);


        return null;
    }

    public void parseJson(String json){
        try {
            JSONObject jsonObj = new JSONObject(json);

            if(jsonObj.getInt("status") == 1)
            {
                this.status = 1;

                // Extrai o array results do objeto JSON
                JSONArray results = jsonObj.getJSONArray("results");
                JSONObject userJson = results.getJSONObject(0);

                Integer userId = userJson.getInt("user_id");
                String userName = userJson.getString("user_name");
                String userEmail = userJson.getString("user_email");

                salvarUsuario(userId,userName,userEmail);
            }
            else
            {
                this.status = 0;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void salvarUsuario(Integer userId, String userName, String userEmail){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Statics.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("USER_ID", userId);
        editor.putString("USER_NAME", userName);
        editor.putString("USER_EMAIL", userEmail);
        editor.commit();
    }

    public String createJsonParam(String login, String senha) {
        JSONObject json = new JSONObject();

        try {

            json.put("email" , login);
            json.put("senha" , senha);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    protected void onPostExecute(Void result) {

        ((LoginActivity)context).resultLogin(this.status);
    }


}
