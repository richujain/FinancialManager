package com.webandrios.financialmanager;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

public class AboutActivity extends AppCompatActivity {

    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        setToolBar();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(AboutActivity.this,ProfileActivity.class));
        finish();
    }

    private void setToolBar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setHomeAsUpIndicator(R.drawable.back);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle("About Us");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AboutActivity.this,ProfileActivity.class));
                finish();
            }
        });
    }
}
