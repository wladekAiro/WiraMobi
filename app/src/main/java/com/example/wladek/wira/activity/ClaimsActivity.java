package com.example.wladek.wira.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.wladek.wira.R;
import com.example.wladek.wira.pojo.ExpenseClaim;
import com.example.wladek.wira.utils.DatabaseHelper;

public class ClaimsActivity extends AppCompatActivity {

    ActionBar actionBar;
    Toolbar toolbar;
    DatabaseHelper myDb;

    EditText editTextClaimTitle;
    EditText editTextDescription;
    Button btnSaveClaim;
    ExpenseClaim expenseClaim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claims);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myDb = new DatabaseHelper(this);

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("New Claim");

        editTextClaimTitle = (EditText) findViewById(R.id.editTextClaimTitle);
        editTextDescription = (EditText) findViewById(R.id.editTextDescription);
        btnSaveClaim = (Button) findViewById(R.id.btnSaveClaim);

        btnSaveClaim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveClaim();
            }
        });



//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabClaim);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    private void saveClaim() {
        String title = editTextClaimTitle.getText().toString();
        String description = editTextDescription.getText().toString();

        boolean invalid = false;
        View view = null;

        if (TextUtils.isEmpty(description)){
            editTextDescription.setError("This field is required");
            invalid = true;
            view = editTextDescription;
        }

        if(TextUtils.isEmpty(title)){
            editTextClaimTitle.setError("This field is required");
            invalid = true;
            view  = editTextClaimTitle;
        }


        if (invalid){
            view.requestFocus();
        }else {
            saveClaim(title,description);
        }

    }

    private void saveClaim(String title, String description) {
        expenseClaim = new ExpenseClaim();
        expenseClaim.setTitle(title);
        expenseClaim.setDescription(description);
        expenseClaim.setTotalAmount(new Double(0));

        expenseClaim = myDb.createClaim(expenseClaim);

        if (expenseClaim == null){
            Toast.makeText(this, "Record not saved" , Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(this, "Saved" , Toast.LENGTH_LONG).show();
        }
    }
}
