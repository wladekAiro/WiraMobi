package com.example.wladek.wira.activity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wladek.wira.R;
import com.example.wladek.wira.pojo.Item;
import com.kosalgeek.android.photoutil.ImageLoader;

import java.io.FileNotFoundException;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ClaimActivity extends AppCompatActivity {

    TextView expenseTitle;
    EditText expenseAmount;
    EditText expenseVenue;
    Item expenseItem;
    ImageView imgExpensePic;
    Button btnSubmitExpense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claim);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Wira");

        expenseTitle = (TextView) findViewById(R.id.txtExpenseTitle);
        expenseAmount = (EditText) findViewById(R.id.editTextAmount);
        expenseVenue = (EditText) findViewById(R.id.editTextVenue);
        imgExpensePic = (ImageView) findViewById(R.id.imgExpensePic);
        btnSubmitExpense = (Button) findViewById(R.id.btnSubmitExpense);


        expenseItem = (Item) getIntent().getSerializableExtra("item");

        setValues();


        btnSubmitExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SweetAlertDialog pDialog = new SweetAlertDialog(ClaimActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("Submitting ...");
                pDialog.setCancelable(false);
                pDialog.show();
            }
        });
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    public void setValues(){
        if(expenseItem != null){
            expenseTitle.setText(expenseItem.getClaimTitle());

            try {
                Bitmap bitmap = ImageLoader.init().from(expenseItem.getImagePath()).requestSize(100, 100).getBitmap();
                imgExpensePic.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

}
