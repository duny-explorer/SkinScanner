package com.example.daria.scinscanner;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.util.HashMap;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ResultActivity extends AppCompatActivity {

    private String filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        filePath = getIntent().getStringExtra("filepath");
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        getReport();
    }

    public void getReport() {
        try {
            final Gson gson = new GsonBuilder().create();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://dunyexplorer.pythonanywhere.com")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();


            File file = new File(filePath);
            RequestAnswer service = retrofit.create(RequestAnswer.class);
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
            Call<Object> call = service.result(body);
            call.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    try {
                        HashMap<String, Double> map = gson.fromJson(response.body().toString(), HashMap.class);
                        Double data = map.get("res");
                        assert data != null;
                        Log.d("1234567", data.toString());
                        switch (data.toString()) {
                            case "0.0":
                                setContentView(R.layout.result_acne);
                                break;
                            case "1.0":
                                setContentView(R.layout.result_eczema);
                                break;
                            case "2.0":
                                setContentView(R.layout.result_health);
                                break;
                            case "3.0":
                                setContentView(R.layout.result_herpes);
                                break;

                        }
                    } catch (Exception e) {
                        Log.d("1234567", e.toString());
                    }

                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {
                    Error(t.toString());
                }
            });
        }catch (Exception e) {
            Error(e.toString());
        }
    }

    void Error(String a) {
        Log.d("1234567", a);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

