package com.webandrios.financialmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class LoginScreen extends AppCompatActivity {

    private Spinner spinner;
    private EditText mobileEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        init();
    }
    private void init(){
        spinner = findViewById(R.id.spinnerCountries);
        spinner.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,CountryData.countryNames));
        spinner.setSelection(30, true);
        mobileEditText = findViewById(R.id.editTextMobile);
        findViewById(R.id.buttonContinue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = CountryData.countryAreaCodes[spinner.getSelectedItemPosition()];
                String number = mobileEditText.getText().toString().trim();
                if(number.isEmpty() || number.length() < 10){
                    mobileEditText.setText("Mobile Number is required.");
                    mobileEditText.requestFocus();
                    return;
                }
                String phoneNumber = "+" + code + number;
                Intent intent = new Intent(LoginScreen.this,VerifyPhoneActivity.class);
                intent.putExtra("phoneNumber",phoneNumber);
                startActivity(intent);

            }
        });
    }

}
