package com.example.wladek.wira.activity;

import android.app.DatePickerDialog;
import android.app.FragmentTransaction;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.wladek.wira.R;
import com.example.wladek.wira.pojo.ExpenseItem;
import com.example.wladek.wira.utils.DatabaseHelper;
import com.example.wladek.wira.utils.DateDialog;
import com.squareup.picasso.Picasso;

import java.io.File;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ExpenseActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getName();

    int year_x, month_x , day_x;

    static final int DIALOG_ID = 0;

    EditText editTextExpenseName;
    EditText editTextExpenseAmount;
    ExpenseItem expenseExpenseItem;
    ImageView imgExpensePic;
    Button btnSubmitExpense;
    TextView txtExpenseDate;

    ActionBar actionBar;

    Picasso mPicasso;

    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Wira");

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        editTextExpenseName = (EditText) findViewById(R.id.editTextDescription);
        editTextExpenseAmount = (EditText) findViewById(R.id.editTextAmount);
        imgExpensePic = (ImageView) findViewById(R.id.imgExpensePic);
        btnSubmitExpense = (Button) findViewById(R.id.btnSubmitExpense);
        txtExpenseDate = (TextView) findViewById(R.id.txtExpenseDate);


        expenseExpenseItem = (ExpenseItem) getIntent().getSerializableExtra("expenseItem");

        mPicasso = Picasso.with(this);

        dbHelper = new DatabaseHelper(this);

        setValues();


        btnSubmitExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SweetAlertDialog pDialog = new SweetAlertDialog(ExpenseActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("Submitting ...");
                pDialog.setCancelable(false);
                pDialog.show();

                updateExpenseItem();

                pDialog.dismiss();
            }
        });

        txtExpenseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog(v);
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

    private void showDateDialog(View v) {
        DateDialog dialog = new DateDialog(v);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        dialog.show(ft , "DatePicker");
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    private void updateExpenseItem() {
        expenseExpenseItem.setExpenseName(editTextExpenseName.getText().toString());
        expenseExpenseItem.setExpenseAmount(new Double(editTextExpenseAmount.getText().toString()));
        expenseExpenseItem.setExpenseDate(txtExpenseDate.getText().toString());

        dbHelper.save(expenseExpenseItem);

        Toast.makeText(this, "Updated Date : " +txtExpenseDate.getText().toString(),
                Toast.LENGTH_LONG).show();
    }

    public void setValues() {

        if (expenseExpenseItem != null) {

            editTextExpenseName.setText(expenseExpenseItem.getExpenseName());
            editTextExpenseAmount.setText(expenseExpenseItem.getExpenseAmount() + "");

            if (expenseExpenseItem.getExpenseDate() != null) {
                txtExpenseDate.setText(expenseExpenseItem.getExpenseDate());
            }

            mPicasso
                    .load(new File(expenseExpenseItem.getImagePath()))
                    .placeholder(R.drawable.ic_launcher)
                    .error(R.drawable.error_circle)
                    .resize(250, 250)
                    .into(imgExpensePic);
        }
    }

}
