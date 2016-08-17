package com.example.wladek.wira.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.wladek.wira.R;
import com.example.wladek.wira.pojo.ExpenseItem;
import com.kosalgeek.android.photoutil.ImageLoader;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ExpenseActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getName();

    int year_x, month_x , day_x;

    static final int DIALOG_ID = 0;

    EditText expenseName;
    EditText expenseAmount;
    ExpenseItem expenseExpenseItem;
    ImageView imgExpensePic;
    Button btnSubmitExpense;
    EditText datePicker;;

    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Wira");

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        expenseName = (EditText) findViewById(R.id.editTextDescription);
        expenseAmount = (EditText) findViewById(R.id.editTextAmount);
        imgExpensePic = (ImageView) findViewById(R.id.imgExpensePic);
        btnSubmitExpense = (Button) findViewById(R.id.btnSubmitExpense);
        datePicker = (EditText) findViewById(R.id.editTextDate);


        expenseExpenseItem = (ExpenseItem) getIntent().getSerializableExtra("expenseItem");

        setValues();


        btnSubmitExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SweetAlertDialog pDialog = new SweetAlertDialog(ExpenseActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("Submitting ...");
                pDialog.setCancelable(false);
                pDialog.show();
            }
        });

        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_ID);
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

    @Override
    protected Dialog onCreateDialog(int id) {

        if(id == DIALOG_ID){
            return new DatePickerDialog(this , dateSetListener , year_x , month_x , day_x);
        }

        return null;
    }

    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            datePicker.setText(dayOfMonth+"/"+""+monthOfYear+"/"+year);
        }
    };

    public void setValues() {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/mm/yyyy");

        Date date = null;

        try {

            date = simpleDateFormat.parse(expenseExpenseItem.getExpenseDate());

        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (expenseExpenseItem != null) {

            expenseName.setText(expenseExpenseItem.getExpenseName());
            expenseAmount.setText(expenseExpenseItem.getExpenseAmount() + "");
            datePicker.setText(expenseExpenseItem.getExpenseDate());

            try {
                Bitmap bitmap = ImageLoader.init().from(expenseExpenseItem.getImagePath()).requestSize(250, 250).getBitmap();
                imgExpensePic.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

}
