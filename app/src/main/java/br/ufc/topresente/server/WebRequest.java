package br.ufc.topresente.server;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.webkit.CookieManager;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Thiago on 19/06/2017.
 */
public class WebRequest {




    public static String httpPost(String link, String json) {



        try{
            String charset = "UTF-8";
            URL url = new URL(link);

            // perform connection and read content
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setDoOutput(true); // Triggers POST.
            connection.setRequestProperty("Accept-Charset", charset);
            connection.setRequestProperty("Cookie", "__test=b5848b849e37ef08823a8d74df01ffa8; expires=quinta-feira, 31 de dezembro de 2037 21:55:55; path=/");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);

            //name=value do POST
            String query = "presencaJson=" + json;

            //try
            OutputStream output = connection.getOutputStream();
            output.write(query.getBytes(charset));


            InputStream is = connection.getInputStream();
            BufferedReader reader;
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String data = null;
            String content = "";
            while ((data = reader.readLine()) != null) {
                content += data + "\n";
            }



            connection.disconnect();
            return content;

        } catch (Exception e) {
            e.printStackTrace();
            return new String("Exception: " + e.getMessage());
        }

    }


    public static String httpGet(String link){

        try {
            //String link = (String) params[0];
            URL url = new URL(link);

            // perform connection and read content
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept-Charset", "UTF-8");
            conn.setRequestProperty("Cookie", "__test=e6f79ce2df9d17dc1f521a272c7421a1; expires=quinta-feira, 31 de dezembro de 2037 21:55:55; path=/");
            conn.setReadTimeout(100000);
            conn.setConnectTimeout(150000);

            conn.setDoInput(true);
            conn.connect();

            //Log.d("Servidor response code", String.valueOf(conn.getResponseCode()));
            // Get the response code
            /*
            int statusCode = conn.getResponseCode();
            InputStream is = null;

            if (statusCode >= 200 && statusCode < 400) {
                // Create an InputStream in order to extract the response object
                is = conn.getInputStream();
            }
            else {
                is = conn.getErrorStream();
            }
            */

            InputStream is = conn.getInputStream();
            BufferedReader reader;
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String data = null;
            String content = "";
            while ((data = reader.readLine()) != null) {
                content += data + "\n";
            }



            conn.disconnect();
            return content;
        } catch (Exception e) {
            e.printStackTrace();
            return new String("Exception: " + e.getMessage());
        }
    }

    public static boolean checkConnection(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected())
        {
            Toast.makeText(context, "Conectado a Internet", Toast.LENGTH_SHORT).show();
            return true;
        }
        else
        {
            Toast.makeText(context, "Não foi possível conectar a internet", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

}
