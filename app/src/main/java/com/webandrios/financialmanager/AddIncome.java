package com.webandrios.financialmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.github.chrisbanes.photoview.PhotoView;

import java.util.Locale;

public class AddIncome extends AppCompatActivity {

    final Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date;
    EditText dateBox,titleBox,amountBox;
    PhotoView photoView;
    Button scanButton,saveIncome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_income);
        init();
    }

    private void init() {
        titleBox = findViewById(R.id.incomeTitleEditText);
        amountBox = findViewById(R.id.incomeAmountEditText);
        photoView = findViewById(R.id.uploadedBill);
        dateBox = findViewById(R.id.incomeDateEditText);
        scanButton = findViewById(R.id.buttonScan);
        saveIncome = findViewById(R.id.buttonSaveIncome);
        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        dateBox.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(AddIncome.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, 0);
                /*
                * Intent pickPhoto = new Intent(Intent.ACTION_PICK,
           android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
startActivityForResult(pickPhoto , 1);//one can be replaced with any action code
                * */
            }
        });
        saveIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(titleBox.getText().toString().isEmpty()){
                    titleBox.setError("This Field Cannot be Empty");
                }
                else if(amountBox.getText().toString().isEmpty()){
                    amountBox.setError("This Field Cannot be Empty");
                }
                else if(dateBox.getText().toString().isEmpty()){
                    dateBox.setError("This Field Cannot be Empty");
                }
                else{
                    uploadIncome();
                }

            }
        });
    }
    private void uploadIncome(){
        String title = titleBox.getText().toString().trim();
        String date = dateBox.getText().toString().trim();
        String amount = amountBox.getText().toString().trim();
        if(photoView.getDrawable() == null){

        }else{

        }


    }
    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        dateBox.setText(sdf.format(myCalendar.getTime()));
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {
            case 0:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    photoView.setImageURI(selectedImage);
                }

                break;
            case 1:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    photoView.setImageURI(selectedImage);
                }
                break;
        }
    }
}
