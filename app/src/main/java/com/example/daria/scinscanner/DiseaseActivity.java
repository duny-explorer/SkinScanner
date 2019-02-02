package com.example.daria.scinscanner;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class DiseaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String disease = getIntent().getStringExtra("disease");

        switch (disease) {
            case "Экзема":
                setContentView(R.layout.activity_eczema);
                break;
            case "Акне":
                setContentView(R.layout.activity_acne);
                break;
            case "Герпес":
                setContentView(R.layout.activity_herpes);
                break;
        }

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

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
