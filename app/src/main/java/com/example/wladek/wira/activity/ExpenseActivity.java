package com.example.wladek.wira.activity;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.wladek.wira.R;
import com.example.wladek.wira.pojo.ExpenseClaim;
import com.example.wladek.wira.pojo.ExpenseItem;
import com.example.wladek.wira.utils.DatabaseHelper;
import com.example.wladek.wira.utils.DateDialog;
import com.example.wladek.wira.utils.ExpenseCategory;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.EnumSet;

public class ExpenseActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getName();

    int year_x, month_x , day_x;

    static final int DIALOG_ID = 0;

    EditText editTextExpenseName;
    EditText editTextExpenseAmount;
    ExpenseItem expenseItem;
    ImageView imgExpensePic;
    EditText txtExpenseDate;

    EditText editClaimTitle;
    EditText editCategory;
    ArrayList<ExpenseClaim> expenseClaims = new ArrayList<>();

    ArrayList<ExpenseCategory> expenseCategories =
            new ArrayList<ExpenseCategory>(EnumSet.allOf(ExpenseCategory.class));

    ActionBar actionBar;

    Picasso mPicasso;

    DatabaseHelper dbHelper;

    Typeface boldTf;

    static final int CLAIM_RESULT_CODE = 145;
    static final int CATEGORY_RESULT_CODE = 146;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Wira");

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        dbHelper = new DatabaseHelper(this);

        getClaims();

        editTextExpenseName = (EditText) findViewById(R.id.editTextDescription);
        editTextExpenseAmount = (EditText) findViewById(R.id.editTextAmount);
        imgExpensePic = (ImageView) findViewById(R.id.imgExpensePic);
        txtExpenseDate = (EditText) findViewById(R.id.txtExpenseDate);

        editClaimTitle = (EditText) findViewById(R.id.editClaimTitle);
        editCategory = (EditText) findViewById(R.id.editCategory);

        expenseItem = (ExpenseItem) getIntent().getSerializableExtra("expenseItem");

        mPicasso = Picasso.with(this);

        setValues();

        txtExpenseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog(v);
            }
        });

        boldTf = Typeface.createFromAsset(this.getAssets(), "fonts/regular_bold_droid_serif.ttf");
        editClaimTitle.setTypeface(boldTf);
        editCategory.setTypeface(boldTf);
        editTextExpenseName.setTypeface(boldTf);
        editTextExpenseAmount.setTypeface(boldTf);
        txtExpenseDate.setTypeface(boldTf);

        editClaimTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Intent intent = new Intent(ExpenseActivity.this, AttachClaimActivity.class);
                    intent.putExtra("expense", expenseItem);
                    startActivityForResult(intent, 1);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        editCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Intent intent = new Intent(ExpenseActivity.this, AttachCategoryActivity.class);
                    intent.putExtra("expense", expenseItem);
                    startActivityForResult(intent, 1);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void getClaims(){
        ExpenseClaim claim = new ExpenseClaim();
        claim.setTitle("Select claim");
        expenseClaims.clear();
        expenseClaims.add(claim);
        expenseClaims.addAll(dbHelper.getClaims());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.shared_save , menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.saveIcon:
                updateExpenseItem();
                return true;
            default:
                return false;
        }
    }

    private void showDateDialog(View v) {
        DateDialog dialog = new DateDialog(v);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        dialog.show(ft, "DatePicker");
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    private void updateExpenseItem() {
        expenseItem.setExpenseName(editTextExpenseName.getText().toString());
        expenseItem.setExpenseAmount(new Double(editTextExpenseAmount.getText().toString()));
        expenseItem.setExpenseDate(txtExpenseDate.getText().toString());

        dbHelper.save(expenseItem);
        onBackPressed();
    }

    public void setValues() {

        if (expenseItem != null) {

            editTextExpenseName.setText(expenseItem.getExpenseName());
            editTextExpenseAmount.setText(expenseItem.getExpenseAmount() + "");

            if (expenseItem.getExpenseDate() != null) {
                txtExpenseDate.setText(expenseItem.getExpenseDate());
            }

            if (expenseItem.getClaim() != null){
                editClaimTitle.setText(expenseItem.getClaim().getTitle());
            }

            if (expenseItem.getCategory() != null){
                editCategory.setText(expenseItem.getCategory().name());
            }else {
                editCategory.setText(ExpenseCategory.Fuel.name());
            }

            mPicasso
                    .load(new File(expenseItem.getImagePath()))
                    .placeholder(R.drawable.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .resize(250, 250)
                    .into(imgExpensePic);
        }

        editClaimTitle.setTypeface(boldTf);
        editCategory.setTypeface(boldTf);
        editTextExpenseName.setTypeface(boldTf);
        editTextExpenseAmount.setTypeface(boldTf);
        txtExpenseDate.setTypeface(boldTf);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == CLAIM_RESULT_CODE){
            Long claimId = data.getLongExtra("claimId", 0);
            String claimTitle = data.getStringExtra("claimTitle");
            expenseItem.setClaimId(claimId);
            editClaimTitle.setText(claimTitle);
        }else if (resultCode == CATEGORY_RESULT_CODE){
            //Logic for category

            try{
                String name = data.getStringExtra("claimCategory");
                expenseItem.setCategory(ExpenseCategory.valueOf(name));
                editCategory.setText(name);
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }
}
