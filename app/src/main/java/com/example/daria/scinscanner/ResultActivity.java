package com.example.daria.scinscanner;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;


public class ResultActivity extends AppCompatActivity {

    private String filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        filePath = getIntent().getStringExtra("filepath");
        new ServerResult().execute();
    }


    class ServerResult extends AsyncTask<Void, String, String> {

        private String answer;
        private String lineEnd = "\r\n";
        private String twoHyphens = "--";
        private String boundary =  "----WebKitFormBoundary9xFB2hiUhzqbBQ4M";
        private int bytesRead, bytesAvailable;
        private byte[] buffer;

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL uploadUrl = new URL("http://192.168.1.6:5000/result557578");
                HttpURLConnection connection = (HttpURLConnection) uploadUrl.openConnection();

                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setUseCaches(false);
                connection.setRequestMethod("POST");

                connection.setRequestProperty("Connection", "Keep-Alive");
                connection.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);

                DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());

                outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                outputStream.writeBytes("Content-Disposition: form-data; name=\"" +
                        "file" + "\"; filename=\"" + filePath + "\"" + lineEnd);

                outputStream.writeBytes("Content-Type: image/jpeg" + lineEnd);
                outputStream.writeBytes(lineEnd);

                FileInputStream fileInputStream = new FileInputStream(new File(filePath));

                bytesAvailable = fileInputStream.available();
                buffer = new byte[bytesAvailable];

                bytesRead = fileInputStream.read(buffer, 0, bytesAvailable);

                while (bytesRead > 0) {
                    outputStream.write(buffer, 0, bytesAvailable);
                    bytesAvailable = fileInputStream.available();
                    bytesRead = fileInputStream.read(buffer, 0, bytesAvailable);
                }

                outputStream.writeBytes(lineEnd);
                outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                int serverResponseCode = connection.getResponseCode();

                fileInputStream.close();
                outputStream.flush();
                outputStream.close();

                if(serverResponseCode == 200) {
                    answer = readStream(connection.getInputStream());
                    Log.d("1234567", "2");
                } else {
                    answer = "-1";
                }
            } catch (MalformedURLException e) {
                Log.d("1234567", e.toString());
            } catch (ProtocolException e) {
                Log.d("1234567", e.toString());
            } catch (FileNotFoundException e) {
                Log.d("1234567", e.toString());
            } catch (IOException e) {
                Log.d("1234567", e.toString());
            }

            return null;
        }

        public String readStream(InputStream inputStream) throws IOException {
            StringBuffer buffer = new StringBuffer();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }

            return buffer.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
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
            } catch (Exception e) {
                Log.d("1234567", e.toString());
            }

        }
    }
}
