package com.example.acer.sqlitetotxtfilewriter;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    EditText etFName, etLName, etCity, etSalary;
    Button btnSubmit, btnReport;

    DatabaseHelper databaseHelper;

    //path for create the directory in your storage for store .txt file
    public String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ShubhDir";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseHelper=new DatabaseHelper(this);
        //Initialization of attributes
        init();
    }

    private void init() {
        etFName = findViewById(R.id.etFName);
        etLName = findViewById(R.id.etLName);
        etCity = findViewById(R.id.etCity);
        etSalary = findViewById(R.id.etSalary);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnReport = findViewById(R.id.btnReport);

        //On submit Button Listener
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isInserted = databaseHelper.insertData(etFName.getText().toString(),
                        etLName.getText().toString(), etCity.getText().toString(),
                        etSalary.getText().toString());
                if (isInserted) {
                    Toast.makeText(MainActivity.this, "Inserted :-)", Toast.LENGTH_SHORT).show();
                    etFName.setText("");
                    etLName.setText("");
                    etCity.setText("");
                    etSalary.setText("");
                } else {
                    etFName.setText("");
                    etLName.setText("");
                    etCity.setText("");
                    etSalary.setText("");
                    Toast.makeText(MainActivity.this, "Error :-( ,Plz try Again..", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //On Report Button Listener
        btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                File file = new File(path + "/Report.txt");
                Cursor res = databaseHelper.getAllRecord();
                if (res.getCount() == 0) {
                    //Your Database is Empty!
                    Toast.makeText(MainActivity.this, "Sorry! Database is Empty.", Toast.LENGTH_SHORT).show();
                } else {
                    StringBuffer buffer = new StringBuffer();
                    buffer.append("\n----------------------------------------------------------------------\n");
                    buffer.append("ID\t First Name\t\t Last Name\t\t City\t\t Salary\t");
                    buffer.append("\n----------------------------------------------------------------------\n");
                    while (res.moveToNext()) {
                        buffer.append(res.getString(0) + "\t\t");
                        buffer.append(res.getString(1) + "\t\t\t");
                        buffer.append(res.getString(2) + "\t\t\t");
                        buffer.append(res.getString(3) + "\t\t\t");
                        buffer.append(res.getString(4) + "\t");
                        buffer.append("\n- -- - -- - -- - -- - -- - -- - -- - -- - -- - -- - -- - -- -\n");
                    }
                    buffer.append("\n------------------------------END--------------------------------------\n");
                    String saveRecord = buffer.toString();
                    //Call for File Generate Method
                    SaveRecord(file, saveRecord);
                    Toast.makeText(MainActivity.this, "File Created,See in Storage", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //Method for Writing data in txt files
    public static void SaveRecord(File file, String saveRecord) {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            try {
                fileOutputStream.write(saveRecord.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } finally {
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yoo! Do the
                    // storage-related task you need to do.
                    File dir = new File(path);
                    dir.mkdirs();
                } else {

                    // permission denied, Woo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(MainActivity.this, "Permission denied to read your External Storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
