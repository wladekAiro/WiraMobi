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

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.wladek.wira.R;
import com.example.wladek.wira.pojo.ExpenseClaim;
import com.example.wladek.wira.pojo.ExpenseItem;
import com.example.wladek.wira.utils.DatabaseHelper;
import com.getbase.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ClaimsActivity extends AppCompatActivity {

    ActionBar actionBar;
    Toolbar toolbar;
    DatabaseHelper myDb;

    EditText editTextClaimTitle;
    EditText editTextDescription;
    Button btnSaveClaim;
    ExpenseClaim expenseClaim;

    MaterialDialog.Builder builder;
    MaterialDialog dialog;

    FloatingActionButton fabAttachExpense;

    ArrayList<ExpenseItem> expenseItems = new ArrayList<>();
    Set<ExpenseItem> claimExpenses = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claims);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myDb = new DatabaseHelper(this);

        loadExpenses();

        expenseClaim = new ExpenseClaim();

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


        fabAttachExpense = (FloatingActionButton) findViewById(R.id.fbAddExpense);
        fabAttachExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callDialog();
            }
        });
    }

    private void callDialog() {

        Integer[] selected = new Integer[]{};

        if (!claimExpenses.isEmpty()) {
            for (ExpenseItem i : claimExpenses) {
                for(ExpenseItem a : expenseItems){
                    if(i.equals(a)){
                        selected = addItem(selected, expenseItems.indexOf(a));
                    }
                }
            }
        }

        builder = new MaterialDialog.Builder(this);
        builder.title("Select expense");
        builder.items(expenseItems);
        builder.itemsCallbackMultiChoice(selected.length == 0 ? null : selected, new MaterialDialog.ListCallbackMultiChoice() {
            @Override
            public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {
                /**
                 * If you use alwaysCallMultiChoiceCallback(), which is discussed below,
                 * returning false here won't allow the newly selected check box to actually be selected.
                 * See the limited multi choice dialog example in the sample project for details.
                 **/
                dialog.getSelectedIndices();

                claimExpenses.clear();

                for (int i = 0 ; i < which.length ; i++){
                    claimExpenses.add(expenseItems.get(which[i]));
                }

                expenseClaim.getExpenses().clear();
                expenseClaim.getExpenses().addAll(claimExpenses);

                Toast.makeText(getApplicationContext() ,"Selected : "+claimExpenses.size() ,
                        Toast.LENGTH_LONG).show();

                return true;
            }
        });
        builder.positiveText("Ok");
        dialog = builder.build();
        dialog.show();
    }

    static Integer[] addItem(Integer[] array, Integer newElement) {
        array = Arrays.copyOf(array, array.length + 1);
        array[array.length - 1] = newElement;
        return array;
    }

    private void saveClaim() {
        String title = editTextClaimTitle.getText().toString();
        String description = editTextDescription.getText().toString();

        boolean invalid = false;
        View view = null;

        if (TextUtils.isEmpty(description)) {
            editTextDescription.setError("This field is required");
            invalid = true;
            view = editTextDescription;
        }

        if (TextUtils.isEmpty(title)) {
            editTextClaimTitle.setError("This field is required");
            invalid = true;
            view = editTextClaimTitle;
        }


        if (invalid) {
            view.requestFocus();
        } else {
            saveClaim(title, description);
        }

    }

    private void saveClaim(String title, String description) {
        expenseClaim.setTitle(title);
        expenseClaim.setDescription(description);
        expenseClaim.setTotalAmount(new Double(0));

        String response = myDb.createClaim(expenseClaim);

        if (response == null) {
            Toast.makeText(this, "Error : Response is null ", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, response, Toast.LENGTH_LONG).show();
        }
    }

    public void loadExpenses() {
        expenseItems.clear();
        expenseItems.addAll(myDb.getExpenseItems());
    }
}