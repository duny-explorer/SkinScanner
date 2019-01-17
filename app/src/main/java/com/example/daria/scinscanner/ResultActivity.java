package com.example.daria.scinscanner;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class ResultActivity extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new ServerResult().execute();
    }

    class ServerResult extends AsyncTask<Void, String, String> {

        String answer;

        @Override
        protected String doInBackground(Void... voids) {
            try {

                URL url = new URL("http://192.168.1.6:5000/result557578");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                if (conn.getResponseCode() != 200) {
                    throw new RuntimeException("Failed : HTTP error code : "
                            + conn.getResponseCode());
                }

                BufferedReader br = new BufferedReader(new InputStreamReader(
                        (conn.getInputStream())));

                answer = new String();
                for (String line; (line = br.readLine()) != null; answer += line) ;

                conn.disconnect();

            } catch (MalformedURLException e) {
                Log.d("1234567", e.toString());

            } catch (IOException e) {
                Log.d("1234567", e.toString());

            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("1234567", "1");
            switch (answer) {
                case "1":
                    setContentView(R.layout.result_acne);
                    break;
                case "2":
                    setContentView(R.layout.result_herpes);
                    break;
                case "3":
                    setContentView(R.layout.result_eczema);
                    break;
            }

        }
    }
}
