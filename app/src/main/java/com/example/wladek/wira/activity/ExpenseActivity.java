package com.example.wladek.wira.activity;

import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wladek.wira.R;
import com.example.wladek.wira.pojo.ExpenseClaim;
import com.example.wladek.wira.pojo.ExpenseItem;
import com.example.wladek.wira.utils.DatabaseHelper;
import com.example.wladek.wira.utils.DateDialog;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

public class ExpenseActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getName();

    int year_x, month_x , day_x;

    static final int DIALOG_ID = 0;

    EditText editTextExpenseName;
    EditText editTextExpenseAmount;
    ExpenseItem expenseItem;
    ImageView imgExpensePic;
    TextView txtExpenseDate;
    Spinner spnClaims;

    SpinnerAdapter spinnerAdapter;

    ArrayList<ExpenseClaim> expenseClaims = new ArrayList<>();

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

        dbHelper = new DatabaseHelper(this);

        getClaims();

        editTextExpenseName = (EditText) findViewById(R.id.editTextDescription);
        editTextExpenseAmount = (EditText) findViewById(R.id.editTextAmount);
        imgExpensePic = (ImageView) findViewById(R.id.imgExpensePic);
        txtExpenseDate = (TextView) findViewById(R.id.txtExpenseDate);

        spnClaims = (Spinner) findViewById(R.id.spnClaims);
        spinnerAdapter = new SpinnerAdapter(this , R.layout.claims_spinner_layout , R.id.txtClaimName , expenseClaims);
        spnClaims.setAdapter(spinnerAdapter);


        expenseItem = (ExpenseItem) getIntent().getSerializableExtra("expenseItem");

        mPicasso = Picasso.with(this);

        ;

        setValues();

        txtExpenseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog(v);
            }
        });

        if(expenseItem.getClaimId() != null){
            spnClaims.setSelection(expenseItem.getClaimId().intValue());
        }

        spnClaims.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ExpenseClaim claim = expenseClaims.get(position);
                if (claim.getId() == null){
                    return;
                }else {
                    expenseItem.setClaimId(claim.getId());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
        dialog.show(ft , "DatePicker");
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

        Toast.makeText(this, "Updated Date : " +txtExpenseDate.getText().toString(),
                Toast.LENGTH_LONG).show();
    }

    public void setValues() {

        if (expenseItem != null) {

            editTextExpenseName.setText(expenseItem.getExpenseName());
            editTextExpenseAmount.setText(expenseItem.getExpenseAmount() + "");

            if (expenseItem.getExpenseDate() != null) {
                txtExpenseDate.setText(expenseItem.getExpenseDate());
            }

            mPicasso
                    .load(new File(expenseItem.getImagePath()))
                    .placeholder(R.drawable.ic_launcher)
                    .error(R.drawable.error_circle)
                    .resize(250, 250)
                    .into(imgExpensePic);
        }
    }

    class SpinnerAdapter extends ArrayAdapter<ExpenseClaim>{

        private LayoutInflater layoutInflater;
        private Context context;
        private ArrayList<ExpenseClaim> spinnerClaims = new ArrayList<>();
        int groupid;

        public SpinnerAdapter(Context context, int layout , int resource, ArrayList<ExpenseClaim> spinnerClaims) {
            super(context, resource, spinnerClaims);

            this.spinnerClaims=spinnerClaims;
            layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.groupid=layout;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View itemView=layoutInflater.inflate(groupid,parent,false);
            TextView textView=(TextView)itemView.findViewById(R.id.txtClaimName);
            textView.setText(spinnerClaims.get(position).getTitle());
            return itemView;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return getView(position, convertView, parent);
        }
    }

}
